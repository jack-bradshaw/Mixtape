package com.matthewtamlin.mixtape.library.databinders;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
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
 * Binds titles from LibraryItems to TextViews. A caching mechanism is implemented to allow for
 * faster and more efficient data binding, and asynchronous processing is only used when if data is
 * not already cached. If the title of an item is inaccessible, then a default is used.
 */
@Tested(testMethod = "unit")
public final class TitleBinder implements DataBinder<LibraryItem, TextView> {
	/**
	 * Identifies this class during logging.
	 */
	private static final String TAG = "[TitleBinder]";

	/**
	 * Caches titles to increase efficiency and performance.
	 */
	private final LibraryItemCache cache;

	/**
	 * Supplies the default title.
	 */
	private final DisplayableDefaults defaults;

	/**
	 * A record of all bind tasks currently in progress, where each task is mapped to the TextView
	 * it is updating.
	 */
	private final HashMap<TextView, BinderTask> tasks = new HashMap<>();

	/**
	 * Constructs a new TitleBinder.
	 *
	 * @param cache
	 * 		a cache for storing titles, may already contain data, not null
	 * @param defaults
	 * 		supplies the default title, not null
	 * @throws IllegalArgumentException
	 * 		if {@code cache} is null
	 * @throws IllegalArgumentException
	 * 		if {@code defaults} is null
	 */
	public TitleBinder(final LibraryItemCache cache, final DisplayableDefaults defaults) {
		this.cache = NullChecker.checkNotNull(cache, "cache cannot be null");
		this.defaults = NullChecker.checkNotNull(defaults, "defaults cannot be null");
	}

	@Override
	public final void bind(final TextView view, final LibraryItem data) {
		NullChecker.checkNotNull(view, "textView cannot be null");

		// There should never be more than one task operating on the same TextView concurrently
		cancel(view);

		// Create the task but don't execute it immediately
		final BinderTask task = new BinderTask(view, data);
		tasks.put(view, task);

		// Asynchronous processing is unnecessary overhead if the title is already cached
		if (cache.containsTitle(data)) {
			task.onPreExecute();
			task.onPostExecute(cache.getTitle(data));
		} else {
			task.execute();
		}
	}

	@Override
	public final void cancel(final TextView view) {
		final AsyncTask task = tasks.get(view);

		if (task != null) {
			task.cancel(false);
			tasks.remove(view);
		}
	}

	@Override
	public final void cancelAll() {
		final Iterator<TextView> textViewIterator = tasks.keySet().iterator();

		while (textViewIterator.hasNext()) {
			final AsyncTask existingTask = tasks.get(textViewIterator.next());

			if (existingTask != null) {
				existingTask.cancel(false);
				textViewIterator.remove();
			}
		}
	}

	/**
	 * @return the cache used to store titles, not null
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
	 * Loads LibraryItem titles in the background and binds the data to the UI when available. If
	 * data cannot be loaded for any reason, then the default title is used instead. Caching is used
	 * to increase performance.
	 */
	private final class BinderTask extends AsyncTask<Void, Void, CharSequence> {
		/**
		 * The TextView to bind data to.
		 */
		private final TextView textView;

		/**
		 * The LibraryItem to source the title from.
		 */
		private final LibraryItem data;

		/**
		 * Constructs a new BinderTask.
		 *
		 * @param textView
		 * 		the TextView to bind data to, not null
		 * @param data
		 * 		the LibraryItem to source the title from, not null
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
			}

			cache.cacheTitle(data, true);

			return cache.getTitle(data) == null ? defaults.getTitle() : cache.getTitle(data);
		}

		@Override
		protected final void onPostExecute(final CharSequence title) {
			// If the task has been cancelled, it must not modify the UI
			if (!isCancelled()) {
				textView.setText(null); // Resets the view to ensure the text changes
				textView.setText(title);
			}
		}
	}
}