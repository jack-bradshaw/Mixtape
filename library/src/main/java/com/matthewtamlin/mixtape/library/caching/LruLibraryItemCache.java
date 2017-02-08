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

package com.matthewtamlin.mixtape.library.caching;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.matthewtamlin.java_utilities.checkers.IntChecker;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;

import timber.log.Timber;

/**
 * An in-memory LibraryItemCache which operates using the least-recently-used (LRU) principle. When
 * the cache is filled to capacity, new caching requests result in objects being evicted until there
 * is sufficient room. This allows a finite cache to accept new data indefinitely without running
 * out of memory. Titles, subtitles and artwork are stored independently, so that eviction of one
 * data type does not affect the others.
 */
public class LruLibraryItemCache implements LibraryItemCache {
	/**
	 * Used to identify this class during debugging.
	 */
	private static final String TAG = "LruLibraryItemCache";

	/**
	 * Contains the cached titles. Each title is mapped to the LibraryItem object it was sourced
	 * from.
	 */
	private final LruCache<LibraryItem, CharSequence> titleCache;

	/**
	 * Contains the cached subtitles. Each subtitle is mapped to the LibraryItem object it was
	 * sourced from.
	 */
	private final LruCache<LibraryItem, CharSequence> subtitleCache;

	/**
	 * Contains the cached artwork. Each item of artwork is mapped to the LibraryItem object it was
	 * sourced from.
	 */
	private final LruCache<LibraryItem, Bitmap> artworkCache;

	/**
	 * Constructs a new LruLibraryItemCache. The supplied capacities determine when to evict items
	 * to free up space.
	 *
	 * @param titleSizeBytes
	 * 		the capacity of the title cache, measured in bytes, greater than zero
	 * @param subtitleSizeBytes
	 * 		the capacity of the subtitle cache, measured in bytes, greater than zero
	 * @param artworkSizeBytes
	 * 		the capacity of the artwork cache, measured in bytes, greater than zero
	 * @throws IllegalArgumentException
	 * 		if titleSizeBytes is not greater than zero
	 * @throws IllegalArgumentException
	 * 		if subtitleSizeBytes is not greater than zero
	 * @throws IllegalArgumentException
	 * 		if artworkSizeBytes is not greater than zero
	 */
	public LruLibraryItemCache(final int titleSizeBytes, final int subtitleSizeBytes,
			final int artworkSizeBytes) {
		IntChecker.checkGreaterThan(titleSizeBytes, 0, "titleSizeBytes is less than one");
		IntChecker.checkGreaterThan(subtitleSizeBytes, 0, "subtitleSizeBytes is less than one");
		IntChecker.checkGreaterThan(artworkSizeBytes, 0, "artworkSizeBytes is less than one");

		titleCache = new LruCache<LibraryItem, CharSequence>(titleSizeBytes) {
			@Override
			protected int sizeOf(final LibraryItem key, final CharSequence value) {
				return value.toString().getBytes().length;
			}
		};

		subtitleCache = new LruCache<LibraryItem, CharSequence>(subtitleSizeBytes) {
			@Override
			protected int sizeOf(final LibraryItem key, final CharSequence value) {
				return value.toString().getBytes().length;
			}
		};

		artworkCache = new LruCache<LibraryItem, Bitmap>(artworkSizeBytes) {
			@Override
			protected int sizeOf(final LibraryItem key, final Bitmap value) {
				return value.getByteCount();
			}
		};
	}

	@Override
	public final void cacheTitle(final LibraryItem item, final boolean onlyIfNotCached) {
		// If the title is already cached and 'onlyIfNotCached' is true, don't re-cache
		if ((item != null) && !(onlyIfNotCached && containsTitle(item))) {
			try {
				final CharSequence title = item.getTitle(); // May throw access exception

				// Execution will only reach here if a read exception was not thrown
				if (title != null) {
					titleCache.put(item, title);
				}
			} catch (final LibraryReadException e) {
				// Log the error and perform no caching
				Timber.w("Title for item \"" + item + "\" could not be accessed.", e);
			}
		}
	}

	@Override
	public final void cacheSubtitle(final LibraryItem item, final boolean onlyIfNotCached) {
		// If the subtitle is already cached and 'onlyIfNotCached' is true, don't re-cache
		if ((item != null) && !(onlyIfNotCached && containsSubtitle(item))) {
			try {
				final CharSequence subtitle = item.getSubtitle(); // May throw access exception

				// Execution will only reach here if an exception was not thrown
				if (subtitle != null) {
					subtitleCache.put(item, subtitle);
				}
			} catch (final LibraryReadException e) {
				// Log the error and perform no caching
				Timber.w("Subtitle for item \"" + item + "\" could not be accessed.", e);
			}
		}
	}

	@Override
	public final void cacheArtwork(final LibraryItem item, final boolean onlyIfNotCached,
			final int preferredWidth, final int preferredHeight) {
		// If the artwork is already cached and 'onlyIfNotCached' is true, don't re-cache
		if ((item != null) && !(onlyIfNotCached && containsArtwork(item))) {
			try {
				// May throw access exception
				final Bitmap artwork = item.getArtwork(preferredWidth, preferredHeight);

				// Execution will only reach here if an exception was not thrown
				if (artwork != null) {
					artworkCache.put(item, artwork);
				}
			} catch (final LibraryReadException e) {
				// Log the error and perform no caching
				Timber.w("Artwork for item \"" + item + "\" could not be accessed.", e);
			}
		}
	}

	@Override
	public final CharSequence getTitle(final LibraryItem item) {
		return (item == null) ? null : titleCache.get(item);
	}

	@Override
	public final CharSequence getSubtitle(final LibraryItem item) {
		return (item == null) ? null : subtitleCache.get(item);
	}

	@Override
	public final Bitmap getArtwork(final LibraryItem item) {
		return (item == null) ? null : artworkCache.get(item);
	}

	@Override
	public final void clearTitles() {
		titleCache.evictAll();
	}

	@Override
	public final void clearSubtitles() {
		subtitleCache.evictAll();
	}

	@Override
	public final void clearArtwork() {
		artworkCache.evictAll();
	}

	@Override
	public final void removeTitle(final LibraryItem item) {
		titleCache.remove(item);
	}

	@Override
	public final void removeSubtitle(final LibraryItem item) {
		subtitleCache.remove(item);
	}

	@Override
	public final void removeArtwork(final LibraryItem item) {
		artworkCache.remove(item);
	}

	@Override
	public final boolean containsTitle(final LibraryItem item) {
		return getTitle(item) != null;
	}

	@Override
	public final boolean containsSubtitle(final LibraryItem item) {
		return getSubtitle(item) != null;
	}

	@Override
	public final boolean containsArtwork(final LibraryItem item) {
		return getArtwork(item) != null;
	}
}