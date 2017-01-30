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

import com.matthewtamlin.mixtape.library.data.LibraryItem;

/**
 * A cache for storing LibraryItem titles, subtitles and artwork.
 */
public interface LibraryItemCache {
	/**
	 * Stores the title of the supplied item in the cache. If the supplied item is null or its title
	 * cannot be accessed, then no caching occurs. If true is passed to {@code onlyIfNotCached} and
	 * the title has already been cached, then no caching occurs.
	 *
	 * @param item
	 * 		the item to cache the title of
	 * @param onlyIfNotCached
	 * 		true to skip caching if the title is already cached, false to cache regardless
	 */
	void cacheTitle(LibraryItem item, boolean onlyIfNotCached);

	/**
	 * Stores the subtitle of the supplied item in the cache. If the supplied item is null or its
	 * subtitle cannot be accessed, then no caching occurs. If true is passed to {@code
	 * onlyIfNotCached} and the subtitle has already been cached, then no caching occurs.
	 *
	 * @param item
	 * 		the item to cache the subtitle of
	 * @param onlyIfNotCached
	 * 		true to skip caching if the subtitle is already cached, false to cache regardless
	 */
	void cacheSubtitle(LibraryItem item, boolean onlyIfNotCached);

	/**
	 * Stores the artwork of the supplied item in the cache. If the supplied item is null or its
	 * artwork cannot be accessed, then no caching occurs. If true is passed to {@code
	 * onlyIfNotCached} and the artwork has already been cached, then no caching occurs. The width
	 * and height parameters are used to optimise the memory used by the artwork. If either value is
	 * less than or equal to zero, then the unoptimised artwork is returned. Furthermore the
	 * provided item must support memory optimisation for the parameters to have any effect.
	 *
	 * @param item
	 * 		the item to cache the artwork of
	 * @param onlyIfNotCached
	 * 		true to skip caching if the artwork is already cached, false to cache regardless
	 * @param preferredWidth
	 * 		the preferred width of the artwork, measured in pixels
	 * @param preferredHeight
	 * 		the preferred height of the artwork, measured in pixels
	 */
	void cacheArtwork(LibraryItem item, boolean onlyIfNotCached, int preferredWidth, int
			preferredHeight);

	/**
	 * Retrieves the title of the supplied item from the cache. If the supplied item is null or its
	 * title is not contained within the cache, then null is returned.
	 *
	 * @param item
	 * 		the item to retrieve the title of
	 * @return the cached title, null if none exists
	 */
	CharSequence getTitle(LibraryItem item);

	/**
	 * Retrieves the subtitle of the supplied item from the cache. If the supplied item is null or
	 * its subtitle is not contained within the cache, then null is returned.
	 *
	 * @param item
	 * 		the item to retrieve the subtitle of
	 * @return the cached subtitle, null if none exists
	 */
	CharSequence getSubtitle(LibraryItem item);

	/**
	 * Retrieves the artwork of the supplied item from the cache. If the supplied item is null or
	 * its artwork is not contained within the cache, then null is returned.
	 *
	 * @param item
	 * 		the item to retrieve the artwork of
	 * @return the cached artwork, null if none exists
	 */
	Bitmap getArtwork(LibraryItem item);

	/**
	 * Removes all titles from the cache without removing any subtitles or artwork.
	 */
	void clearTitles();

	/**
	 * Removes all subtitles from the cache without removing any titles or artwork.
	 */
	void clearSubtitles();

	/**
	 * Removes all artwork from the cache, without removing any titles or subtitles.
	 */
	void clearArtwork();

	/**
	 * Removes the title of the supplied item from the cache, without removing its subtitle or
	 * artwork. If the supplied item is null or its title is not contained within the cache, then
	 * the cache is not modified and this method exits normally.
	 *
	 * @param item
	 * 		the item to remove the title of, not null
	 */
	void removeTitle(LibraryItem item);

	/**
	 * Removes the subtitle of the supplied item from the cache, without removing its title or
	 * artwork. If the supplied item is null or its subtitle is not contained within the cache, then
	 * the cache is not modified and this method exits normally.
	 *
	 * @param item
	 * 		the item to remove the subtitle of, not null
	 */
	void removeSubtitle(LibraryItem item);

	/**
	 * Removes the artwork of the supplied item from the cache, without removing its title or
	 * subtitle. If the supplied item is null or its artwork is not contained within the cache, then
	 * the cache is not modified and this method exits normally.
	 *
	 * @param item
	 * 		the item to remove the artwork of, not null
	 */
	void removeArtwork(LibraryItem item);

	/**
	 * Checks if the title of an item is contained in this cache.
	 *
	 * @param item
	 * 		the item to check for
	 * @return true if the title of the supplied item exists in the cache, false otherwise
	 */
	boolean containsTitle(LibraryItem item);

	/**
	 * Checks if the subtitle of an item is contained in this cache.
	 *
	 * @param item
	 * 		the item to check for
	 * @return true if the subtitle of the supplied item exists in the cache, false otherwise
	 */
	boolean containsSubtitle(LibraryItem item);

	/**
	 * Checks if the artwork of an item is contained in this cache.
	 *
	 * @param item
	 * 		the item to check for
	 * @return true if the artwork of the supplied item exists in the cache, false otherwise
	 */
	boolean containsArtwork(LibraryItem item);
}