package com.matthewtamlin.mixtape.library.databinders;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.matthewtamlin.java_utilities.checkers.IntChecker;
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
 * Binds artwork from LibraryItems to ImageViews. A caching mechanism is implemented to allow for
 * faster and more efficient data binding. To optimise load times and minimise memory usage, artwork
 * size is matched to the ImageView when being loaded. In some cases the ImageView dimensions are
 * inaccessible, so default dimensions are used. If the artwork of an item is inaccessible, then the
 * default artwork is used. A fade-in effect is applied when binding artwork.
 */
@Tested(testMethod = "unit")
public final class ArtworkBinder implements DataBinder<LibraryItem, ImageView> {
	/**
	 * Identifies this class during logging.
	 */
	private static final String TAG = "[ArtworkBinder]";

	/**
	 * Caches artwork to increase efficiency and performance.
	 */
	private final LibraryItemCache cache;

	/**
	 * Supplies the default artwork.
	 */
	private final DisplayableDefaults defaults;

	/**
	 * The duration to use when fading-in artwork, measured in milliseconds.
	 */
	private final int fadeInDurationMs;

	/**
	 * A record of all bind tasks currently in progress, where each task is mapped to the ImageView
	 * it is updating.
	 */
	private final HashMap<ImageView, BinderTask> tasks = new HashMap<>();

	/**
	 * The width to use when loading artwork if the width of the receiving dimension is
	 * unavailable.
	 */
	private int defaultWidth = 300; // Default to 300 - Looks ok for small images

	/**
	 * The height to use when loading artwork if the height of the receiving dimension is
	 * unavailable.
	 */
	private int defaultHeight = 300; // Default to 300 - Looks ok for small images

	/**
	 * Constructs a new TitleBinder.
	 *
	 * @param cache
	 * 		a cache for storing artwork, may already contain data, not null
	 * @param defaults
	 * 		supplies the default artwork, not null
	 * @param fadeInDurationMs
	 * 		the duration to use when fading-in artwork, measured in milliseconds, not less than zero
	 * @throws IllegalArgumentException
	 * 		if {@code cache} is null
	 * @throws IllegalArgumentException
	 * 		if {@code defaults} is null
	 * @throws IllegalArgumentException
	 * 		if {@code fadeInDurationMs} is less than zero
	 */
	public ArtworkBinder(final LibraryItemCache cache, final DisplayableDefaults defaults,
			final int fadeInDurationMs) {
		this.cache = NullChecker.checkNotNull(cache, "cache cannot be null");
		this.defaults = NullChecker.checkNotNull(defaults, "defaults cannot be null");
		this.fadeInDurationMs = IntChecker.checkGreaterThan(fadeInDurationMs, -1);
	}

	@Override
	public void bind(final ImageView imageView, final LibraryItem data) {
		NullChecker.checkNotNull(imageView, "imageView cannot be null");

		// There should never be more than one task operating on the same ImageView concurrently
		cancel(imageView);

		// Create the task but don't execute it immediately
		final BinderTask task = new BinderTask(imageView, data);
		tasks.put(imageView, task);
		task.execute();
	}

	@Override
	public void cancel(final ImageView imageView) {
		final AsyncTask existingTask = tasks.get(imageView);

		if (existingTask != null) {
			existingTask.cancel(false);
			tasks.remove(imageView);
		}
	}

	@Override
	public void cancelAll() {
		final Iterator<ImageView> imageViewIterator = tasks.keySet().iterator();

		while (imageViewIterator.hasNext()) {
			final AsyncTask existingTask = tasks.get(imageViewIterator.next());

			if (existingTask != null) {
				existingTask.cancel(false);
				imageViewIterator.remove();
			}
		}
	}

	/**
	 * @return the cache used to store artwork, not null
	 */
	public final LibraryItemCache getCache() {
		return cache;
	}

	/**
	 * @return the source of the default artwork, not null
	 */
	public final DisplayableDefaults getDefaults() {
		return defaults;
	}

	/**
	 * Sets the width to use if an ImageView doesn't return its width.
	 */
	public final void setDefaultWidth(final int defaultWidth) {
		this.defaultWidth = defaultWidth;
	}

	/**
	 * Sets the height to use if an ImageView doesn't return its height.
	 */
	public final void setDefaultHeight(final int defaultHeight) {
		this.defaultHeight = defaultHeight;
	}

	/**
	 * Loads LibraryItem artwork in the background and binds the data to the UI when available. If
	 * data cannot be loaded for any reason, then the default artwork is used instead. Caching is
	 * used to increase performance.
	 */
	private class BinderTask extends AsyncTask<Void, Void, Bitmap> {
		/**
		 * The ImageView to bind data to.
		 */
		private final ImageView imageView;

		/**
		 * The LibraryItem to source the artwork from.
		 */
		private final LibraryItem data;

		/**
		 * The width of the ImageView, measured in pixels. This value must be saved as an instance
		 * variable in {@code onPreExecute()} because the UI cannot be queried from the background
		 * task.
		 */
		private int viewWidth;

		/**
		 * The height of the ImageView, measured in pixels. This value must be saved as an instance
		 * variable in {@code onPreExecute()} because the UI cannot be queried from the background
		 * task.
		 */
		private int viewHeight;

		/**
		 * Constructs a new BinderTask.
		 *
		 * @param imageView
		 * 		the ImageView to bind data to, not null
		 * @param data
		 * 		the LibraryItem to source the artwork from, not null
		 * @throws IllegalArgumentException
		 * 		if {@code imageView} is null
		 */
		public BinderTask(final ImageView imageView, final LibraryItem data) {
			this.imageView = NullChecker.checkNotNull(imageView, "imageView cannot be null");
			this.data = data;
		}

		@Override
		public void onPreExecute() {
			// If the task has been cancelled, it must not modify the UI
			if (!isCancelled()) {
				imageView.setImageBitmap(null);

				// Zero dimensions require the defaults
				viewWidth = imageView.getWidth() == 0 ? -1 : imageView.getWidth();
				viewHeight = imageView.getHeight() == 0 ? -1 : imageView.getHeight();
			}
		}

		@Override
		public Bitmap doInBackground(final Void... params) {
			final int width = viewWidth == -1 ? defaultWidth : viewWidth;
			final int height = viewHeight == -1 ? defaultHeight : viewHeight;

			if (isCancelled() || data == null) {
				return null;
			} else if (cache.containsArtwork(data)) {
				final Bitmap cachedArtwork = cache.getArtwork(data);

				// The cached artwork cannot not be used if the desired width or height are larger
				// then the cache artwork dimensions without looking blurry
				if (cachedArtwork.getWidth() < width || cachedArtwork.getHeight() <
						height) {
					cache.clearArtwork(); // Don't keep the data if it's not useful anymore
					return getArtworkDirectly(width, height);
				} else {
					return cachedArtwork;
				}
			} else {
				return getArtworkDirectly(width, height);
			}
		}

		@Override
		public void onPostExecute(final Bitmap artwork) {
			// Skip the animation if it isn't necessary
			if (fadeInDurationMs <= 0 || artwork == null) {
				imageView.setImageBitmap(null); // Resets the view to ensure the image changes
				imageView.setImageBitmap(artwork);
			} else {
				// Animation to fade in from fully invisible to fully visible
				final ValueAnimator fadeInAnimation = ValueAnimator.ofFloat(0, 1);

				// When the animations starts, bind the artwork but make it invisible
				fadeInAnimation.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationStart(final Animator animation) {
						// If the task has been cancelled, it must not modify the UI
						if (!isCancelled()) {
							imageView.setAlpha((float) 0);
							imageView.setImageBitmap(null); // Resets ensures image changes
							imageView.setImageBitmap(artwork);
						}
					}

					@Override
					public void onAnimationCancel(Animator animation) {
						// The UI part of the task has now completed, so remove it from the record
						tasks.remove(imageView);
					}
				});

				// As the animation progresses, fade-in the artwork by changing the transparency
				fadeInAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(final ValueAnimator animation) {
						// If the task has been cancelled, the animation must also be cancelled
						if (isCancelled()) {
							fadeInAnimation.cancel();
						}

						final Float value = (Float) animation.getAnimatedValue();
						imageView.setAlpha(value);
					}
				});

				fadeInAnimation.setDuration(fadeInDurationMs);
				fadeInAnimation.start();
			}

			// Cache the data in the background to optimise future performance
			final ExecutorService es = Executors.newSingleThreadExecutor();
			es.execute(new Runnable() {
				@Override
				public void run() {
					final int width = viewWidth == 0 ? defaultWidth : viewWidth;
					final int height = viewHeight == 0 ? defaultHeight : viewHeight;

					cache.cacheArtwork(data, true, width, height);
				}
			});
		}

		/**
		 * Queries the data directly to access the artwork. If a LibraryReadException occurs when
		 * reading the data then the default artwork is returned.
		 *
		 * @param width
		 * 		the desired width of the artwork, measured in pixels
		 * @param height
		 * 		the desired height of the artwork, measured in pixels
		 * @return the artwork directly from the source or the default
		 */
		private Bitmap getArtworkDirectly(final int width, final int height) {
			try {
				return data.getArtwork(width, height);
			} catch (final LibraryReadException e) {
				Log.e(TAG, "Artwork for item \"" + data + "\" could not be accessed.", e);
				return defaults.getArtwork();
			}
		}
	}
}