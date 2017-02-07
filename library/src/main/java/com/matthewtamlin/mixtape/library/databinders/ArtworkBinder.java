/*
 * Copyright 2017 Matthew Tamlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import com.matthewtamlin.mixtape.library.caching.LibraryItemCache;
import com.matthewtamlin.mixtape.library.data.DisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Binds artwork data from LibraryItems to ImageViews. Data is cached as it is loaded to improve
 * future performance, and asynchronous processing is only used if data is not already cached. By
 * default a fade-in effect is used when artwork is bound, but this can be disabled if desired.
 */
public class ArtworkBinder implements DataBinder<LibraryItem, ImageView> {
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
	 * A record of all bind tasks currently in progress. Each task is mapped to the target
	 * ImageView.
	 */
	private final HashMap<ImageView, BinderTask> tasks = new HashMap<>();

	/**
	 * The duration to use when transitioning artwork, measured in milliseconds.
	 */
	private int fadeInDurationMs;

	/**
	 * The default width to use when loading artwork. This value is used if the ImageView is not
	 * ready to return its dimensions.
	 */
	private int fallbackDecodingWidth = 300;

	/**
	 * The default height to use when loading artwork. This value is used if the ImageView is not
	 * ready to return its dimensions.
	 */
	private int fallbackDecodingHeight = 300;

	/**
	 * Constructs a new ArtworkBinder.
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
	 * @return the cache used to store artwork
	 */
	public final LibraryItemCache getCache() {
		return cache;
	}

	/**
	 * @return the defaults used when artwork cannot be accessed
	 */
	public final DisplayableDefaults getDefaults() {
		return defaults;
	}

	/**
	 * @return the duration used when fading in artwork
	 */
	public int getFadeInDurationMs() {
		return fadeInDurationMs;
	}

	/**
	 * Sets the duration to use when fading in artwork.
	 *
	 * @param durationMs
	 * 		the duration to use, measured in milliseconds, not less than zero
	 */
	public void setFadeInDurationMs(final int durationMs) {
		fadeInDurationMs = durationMs;
	}

	/**
	 * @return the width dimension to use when decoding artwork if the optimal dimension cannot be
	 * inferred from the target ImageView
	 */
	public int getFallbackDecodingWidth() {
		return fallbackDecodingWidth;
	}

	/**
	 * Sets the width to use when decoding artwork if the target ImageView cannot return its
	 * dimensions.
	 */
	public final void setFallbackDecodingWidth(final int defaultWidth) {
		this.fallbackDecodingWidth = defaultWidth;
	}

	/**
	 * @return the height dimension to use when decoding artwork if the optimal dimension cannot be
	 * inferred from the target ImageView
	 */
	public int getFallbackDecodingHeight() {
		return fallbackDecodingHeight;
	}

	/**
	 * Sets the height to use when decoding artwork if the target ImageView cannot return its
	 * dimensions.
	 */
	public final void setDefaultHeight(final int defaultHeight) {
		this.fallbackDecodingHeight = defaultHeight;
	}

	/**
	 * Task for asynchronously loading data and binding it to the UI when available.
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
		 * The width of the ImageView, measured in pixels. This value must be saved in {@code
		 * onPreExecute()} since the UI cannot be queried from the background task.
		 */
		private int viewWidth;

		/**
		 * The height of the ImageView, measured in pixels. This value must be saved in {@code
		 * onPreExecute()} since the UI cannot be queried from the background task.
		 */
		private int viewHeight;

		/**
		 * Constructs a new BinderTask.
		 *
		 * @param imageView
		 * 		the ImageView to bind data to, not null
		 * @param data
		 * 		the LibraryItem to source the artwork from
		 * @throws IllegalArgumentException
		 * 		if {@code imageView} is null
		 */
		public BinderTask(final ImageView imageView, final LibraryItem data) {
			this.imageView = NullChecker.checkNotNull(imageView, "imageView cannot be null");
			this.data = data;
		}

		@Override
		public void onPreExecute() {
			if (!isCancelled()) {
				imageView.setImageBitmap(null);

				// Zero dimensions require the defaults
				viewWidth = imageView.getWidth() == 0 ? -1 : imageView.getWidth();
				viewHeight = imageView.getHeight() == 0 ? -1 : imageView.getHeight();
			}
		}

		@Override
		public Bitmap doInBackground(final Void... params) {
			final int width = viewWidth == -1 ? fallbackDecodingWidth : viewWidth;
			final int height = viewHeight == -1 ? fallbackDecodingHeight : viewHeight;

			if (isCancelled() || data == null) {
				return null;
			} else if (cache.containsArtwork(data)) {
				final Bitmap cachedArtwork = cache.getArtwork(data);

				// Only use the cached artwork if it is an adequate size
				if (cachedArtwork.getWidth() < width || cachedArtwork.getHeight() < height) {
					cache.removeArtwork(data); // Don't keep the data if it's not useful anymore
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
				if (!isCancelled()) {
					imageView.setImageBitmap(null); // Resets view
					imageView.setImageBitmap(artwork);
				}
			} else {
				// Animation to fade in from fully invisible to fully visible
				final ValueAnimator fadeInAnimation = ValueAnimator.ofFloat(0, 1);

				// When the animations starts, bind the artwork but make it invisible
				fadeInAnimation.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationStart(final Animator animation) {
						// If the task has been cancelled, it must not modify the UI
						if (!isCancelled()) {
							imageView.setAlpha(0f);
							imageView.setImageBitmap(null); // Resets ensures image changes
							imageView.setImageBitmap(artwork);
						}
					}

					@Override
					public void onAnimationCancel(final Animator animation) {
						imageView.setAlpha(1f);
						imageView.setImageBitmap(null);
					}
				});

				// As the animation progresses, fade-in the artwork by changing the transparency
				fadeInAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(final ValueAnimator animation) {
						// If the task has been cancelled, the animation must also be cancelled
						if (isCancelled()) {
							fadeInAnimation.cancel();
						} else {
							final Float value = (Float) animation.getAnimatedValue();
							imageView.setAlpha(value);
						}
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
					final int width = viewWidth == 0 ? fallbackDecodingWidth : viewWidth;
					final int height = viewHeight == 0 ? fallbackDecodingHeight : viewHeight;

					cache.cacheArtwork(data, true, width, height);
				}
			});
		}

		/**
		 * Gets the artwork directly from the data. If a LibraryReadException occurs when reading
		 * the data, then the default artwork is returned. This method does not interact with the
		 * cache in anyway.
		 *
		 * @param width
		 * 		the desired width of the artwork, measured in pixels
		 * @param height
		 * 		the desired height of the artwork, measured in pixels
		 * @return the artwork
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