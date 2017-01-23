package com.matthewtamlin.mixtape.library.databinders;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.matthewtamlin.java_utilities.checkers.NullChecker;
import com.matthewtamlin.java_utilities.testing.Tested;
import com.matthewtamlin.mixtape.library.caching.LibraryItemCache;
import com.matthewtamlin.mixtape.library.data.DisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Binds subtitles to TextViews. A caching mechanism is implemented to allow for faster
 * and more efficient data binding, and asynchronous processing is only used when if data is not
 * already cached. If the subtitle of an item is inaccessible, then a default is used.
 */
@Tested(testMethod = "unit")
public final class SubtitleBinder implements DataBinder<LibraryItem, TextView> {
	/**
	 * Identifies this class during logging.
	 */
	private static final String TAG = "[SubtitleBinder]";

	/**
	 * Caches subtitles to increase efficiency and performance.
	 */
	private final LibraryItemCache cache;

	/**
	 * Supplies the default subtitle.
	 */
	private final DisplayableDefaults defaults;

	/**
	 * A record of all bind tasks currently in progress, where each task is mapped to the TextView
	 * it is updating.
	 */
	private final HashMap<TextView, BinderTask> tasks = new HashMap<>();

	/**
	 * Constructs a new SubtitleBinder.
	 *
	 * @param cache
	 * 		a cache for storing subtitles, may already contain data, not null
	 * @param defaults
	 * 		supplies the default subtitle, not null
	 * @throws IllegalArgumentException
	 * 		if {@code cache} is null
	 * @throws IllegalArgumentException
	 * 		if {@code defaults} is null
	 */
	public SubtitleBinder(final LibraryItemCache cache, final DisplayableDefaults defaults) {
		this.cache = NullChecker.checkNotNull(cache, "cache cannot be null");
		this.defaults = NullChecker.checkNotNull(defaults, "defaults cannot be null");
	}

	@Override
	public final void bind(final TextView view, final LibraryItem data) {
		NullChecker.checkNotNull(view, "textView cannot be null");

		// There should never be more than one task operating on the same TextView concurrently
		cancel(view);
		tasks.remove(view);

		// Create the task but don't execute it immediately
		final BinderTask task = new BinderTask(view, data);
		tasks.put(view, task);

		// Asynchronous processing is unnecessary overhead if the subtitle is already cached
		if (cache.containsSubtitle(data)) {
			task.onPreExecute();
			task.onPostExecute(cache.getSubtitle(data));
		} else {
			task.execute();
		}
	}

	@Override
	public final void cancel(final TextView view) {
		final AsyncTask task = tasks.get(view);

		if (task != null) {
			task.cancel(false);
		}
	}

	@Override
	public final void cancelAll() {
		// Use an iterator to avoid concurrent modification exceptions
		final Iterator<TextView> i = tasks.keySet().iterator();

		while (i.hasNext()) {
			cancel(i.next());
			i.remove();
		}
	}

	/**
	 * @return the cache used to store subtitles, not null
	 */
	public final LibraryItemCache getCache() {
		return cache;
	}

	/**
	 * @return the source of the default titles, not null
	 */
	public final DisplayableDefaults getDefaults() {
		return defaults;
	}

	/**
	 * Loads LibraryItem subtitles in the background and binds the data to the UI when available. If
	 * data cannot be loaded for any reason, then the default subtitle is used instead. Caching is
	 * used to increase performance.
	 */
	private final class BinderTask extends AsyncTask<Void, Void, CharSequence> {
		/**
		 * The TextView to bind data to.
		 */
		private final TextView textView;

		/**
		 * The LibraryItem to source the subtitle from.
		 */
		private final LibraryItem data;

		/**
		 * Constructs a new BinderTask.
		 *
		 * @param textView
		 * 		the TextView to bind data to, not null
		 * @param data
		 * 		the LibraryItem to source the subtitle from, not null
		 * @throws IllegalArgumentException
		 * 		if {@code textView} is null
		 */
		public BinderTask(final TextView textView, final LibraryItem data) {
			this.textView = NullChecker.checkNotNull(textView, "textView cannot be null");
			this.data = data;
		}

		@Override
		public final void onPreExecute() {
			// If the task has been cancelled, it must not modify the UI
			if (!isCancelled()) {
				textView.setText(null);
			}
		}

		@Override
		public final CharSequence doInBackground(final Void... params) {
			if (isCancelled() || data == null) {
				return null;
			} else {
				try {
					return data.getSubtitle();
				} catch (final LibraryReadException e) {
					Log.e(TAG, "Subtitle for item \"" + data + "\" could not be accessed.", e);
					return defaults.getSubtitle();
				}
			}
		}

		@Override
		protected final void onPostExecute(final CharSequence subtitle) {
			// If the task has been cancelled, it must not modify the UI
			if (!isCancelled()) {
				textView.setText(null); // Resets the view to ensure the text changes
				textView.setText(subtitle);

				// The UI part of the task has now completed, so remove it from the record
				tasks.remove(textView);
			}

			// Cache the data in the background to optimise future performance
			final ExecutorService es = Executors.newSingleThreadExecutor();
			es.execute(new Runnable() {
				@Override
				public void run() {
					cache.cacheSubtitle(data, true);
				}
			});
		}
	}
}