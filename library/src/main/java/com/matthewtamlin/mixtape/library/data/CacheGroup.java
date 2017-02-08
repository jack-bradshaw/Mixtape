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

package com.matthewtamlin.mixtape.library.data;

import android.graphics.drawable.Drawable;
import android.util.LruCache;

/**
 * Provides access to three caches which can be used to store the titles, subtitles and artwork of
 * LibraryItems.
 */
public interface CacheGroup {
	/**
	 * @return a cache for storing titles, not null
	 */
	public LruCache<LibraryItem, CharSequence> getTitleCache();

	/**
	 * @return a cache for storing subtitles, not null
	 */
	public LruCache<LibraryItem, CharSequence> getSubtitleCache();

	/**
	 * @return a cache for storing artwork, not null
	 */
	public LruCache<LibraryItem, Drawable> getArtworkCache();
}
