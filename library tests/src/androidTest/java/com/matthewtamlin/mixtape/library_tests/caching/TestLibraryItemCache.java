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

package com.matthewtamlin.mixtape.library_tests.caching;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;


import com.matthewtamlin.mixtape.library.caching.LibraryItemCache;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;
import com.matthewtamlin.mixtape.library_tests.test.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public abstract class TestLibraryItemCache {
	private static final CharSequence TITLE = "cake";
	private static final CharSequence SUBTITLE = "lie";
	private static final int ARTWORK_RES_ID = R.raw.image1;
	private static final int DIMENSION = 1;

	private Drawable artwork;

	private LibraryItemCache cache;

	private LibraryItem workingLibraryItem;

	private LibraryItem brokenLibraryItem;

	protected abstract LibraryItemCache createLibraryItemCache();

	@SuppressWarnings("unchecked") // Ignore weird mocking error
	@Before
	public void setup() throws LibraryReadException {
		// Context is now available so test image can be decoded
		final Context context = InstrumentationRegistry.getTargetContext();
		final Resources res = context.getResources();
		artwork = new BitmapDrawable(res, BitmapFactory.decodeResource(res, ARTWORK_RES_ID));
		assertThat("precondition failed: artwork is null", artwork, is(notNullValue()));

		// Source the cache to test from the subclass
		cache = createLibraryItemCache();
		assertThat("precondition failed: cache is null.", cache, is(notNullValue()));

		// Sanitise the cache by removing any existing contents
		cache.clearTitles();
		cache.clearSubtitles();
		cache.clearArtwork();

		// Create a LibraryItem which functions normally
		workingLibraryItem = mock(LibraryItem.class);
		when(workingLibraryItem.getTitle()).thenReturn(TITLE);
		when(workingLibraryItem.getSubtitle()).thenReturn(SUBTITLE);
		when(workingLibraryItem.getArtwork(anyInt(), anyInt())).thenReturn(artwork);

		// Create a LibraryItem which always throws exceptions
		brokenLibraryItem = mock(LibraryItem.class);
		when(brokenLibraryItem.getTitle()).thenThrow(LibraryReadException.class);
		when(brokenLibraryItem.getSubtitle()).thenThrow(LibraryReadException.class);
		when(brokenLibraryItem.getArtwork(anyInt(), anyInt())).thenThrow(LibraryReadException.class);
	}

	@Test
	public void testCacheTitle_usingNull_shouldNotCache() {
		cache.cacheTitle(null, false);

		assertThat("null was cached", !cache.containsTitle(null));
	}

	@Test
	public void testCacheTitle_usingWorkingItem_shouldCache() throws LibraryReadException {
		cache.cacheTitle(workingLibraryItem, false);

		assertThat("title was not cached", cache.containsTitle(workingLibraryItem));
		assertThat("subtitle was cached", !cache.containsSubtitle(workingLibraryItem));
		assertThat("artwork was cached", !cache.containsArtwork(workingLibraryItem));
	}

	@Test
	public void testCacheTitle_usingBrokenItem_shouldNotCache() throws LibraryReadException {
		cache.cacheTitle(brokenLibraryItem, false);

		assertThat("title was cached", !cache.containsTitle(workingLibraryItem));
		assertThat("subtitle was cached", !cache.containsSubtitle(workingLibraryItem));
		assertThat("artwork was cached", !cache.containsArtwork(workingLibraryItem));
	}

	@Test
	public void testCacheTitle_usingDoNotRecache_shouldNotCache() throws LibraryReadException {
		// The cache must contain the title or else the test will fail
		cache.cacheTitle(workingLibraryItem, false);
		assertThat("precondition failed: title was not cached.", cache.containsTitle
				(workingLibraryItem));

		// Request caching without recaching
		cache.cacheTitle(workingLibraryItem, true);

		// Verify the cache contents
		assertThat("title was not cached", cache.containsTitle(workingLibraryItem));
		assertThat("subtitle was cached", !cache.containsSubtitle(workingLibraryItem));
		assertThat("artwork was cached", !cache.containsArtwork(workingLibraryItem));

		// Check how many times the item was directly queried
		verify(workingLibraryItem, times(1)).getTitle();
		verify(workingLibraryItem, never()).getSubtitle();
		verify(workingLibraryItem, never()).getArtwork(anyInt(), anyInt());
	}

	@Test
	public void testGetTitle_shouldReturnCachedTitle() {
		// The cache must contain the title or else the test will fail
		cache.cacheTitle(workingLibraryItem, false);
		assertThat("precondition failed: title was not cached.", cache.containsTitle
				(workingLibraryItem));

		assertThat(cache.getTitle(workingLibraryItem), is(TITLE));
	}

	@Test
	public void testRemoveTitle_shouldRemoveTitleFromCache() {
		// The cache must contain the title or else the test will fail
		cache.cacheTitle(workingLibraryItem, false);
		assertThat("precondition failed: title was not cached.", cache.containsTitle
				(workingLibraryItem));

		cache.removeTitle(workingLibraryItem);

		assertThat("title was not removed", !cache.containsTitle(workingLibraryItem));
	}

	@Test
	public void testRemoveTitles_shouldRemoveAllTitlesFromCache() {
		// The cache must contain the title or else the test will fail
		cache.cacheTitle(workingLibraryItem, false);
		assertThat("precondition failed: title was not cached.", cache.containsTitle
				(workingLibraryItem));

		cache.clearTitles();

		assertThat("title cache was not cleared", !cache.containsTitle(workingLibraryItem));
	}

	@Test
	public void testCacheSubtitle_usingNull_shouldNotCache() {
		cache.cacheSubtitle(null, false);

		assertThat("null was cached", !cache.containsSubtitle(null));
	}

	@Test
	public void testCacheSubtitle_usingWorkingItem_shouldCache() throws
			LibraryReadException {
		cache.cacheSubtitle(workingLibraryItem, false);

		assertThat("title was cached", !cache.containsTitle(workingLibraryItem));
		assertThat("subtitle not was cached", cache.containsSubtitle(workingLibraryItem));
		assertThat("artwork was cached", !cache.containsArtwork(workingLibraryItem));
	}

	@Test
	public void testCacheSubtitle_usingBrokenItem_shouldNotCache() throws LibraryReadException {
		cache.cacheSubtitle(brokenLibraryItem, false);

		assertThat("title was cached", !cache.containsTitle(workingLibraryItem));
		assertThat("subtitle was cached", !cache.containsSubtitle(workingLibraryItem));
		assertThat("artwork was cached", !cache.containsArtwork(workingLibraryItem));
	}

	@Test
	public void testCacheSubtitle_usingDoNotRecache_shouldNotCache() throws LibraryReadException {
		// The cache must contain the subtitle or else the test will fail
		cache.cacheSubtitle(workingLibraryItem, false);
		assertThat("precondition failed: subtitle was not cached.", cache.containsSubtitle
				(workingLibraryItem));

		// Request caching without recaching
		cache.cacheSubtitle(workingLibraryItem, true);

		// Verify the cache contents
		assertThat("title was cached", !cache.containsTitle(workingLibraryItem));
		assertThat("subtitle was not cached", cache.containsSubtitle(workingLibraryItem));
		assertThat("artwork was cached", !cache.containsArtwork(workingLibraryItem));

		// Check how many times the item was directly queried
		verify(workingLibraryItem, never()).getTitle();
		verify(workingLibraryItem, times(1)).getSubtitle();
		verify(workingLibraryItem, never()).getArtwork(anyInt(), anyInt());
	}

	@Test
	public void testGetSubtitle_shouldReturnCachedSubtitle() {
		// The cache must contain the subtitle or else the test will fail
		cache.cacheSubtitle(workingLibraryItem, false);
		assertThat("precondition failed: subtitle was not cached.", cache.containsSubtitle
				(workingLibraryItem));

		assertThat(cache.getSubtitle(workingLibraryItem), is(SUBTITLE));
	}

	@Test
	public void testRemoveSubtitle_shouldRemoveSubtitleFromCache() {
		// The cache must contain the subtitle or else the test will fail
		cache.cacheSubtitle(workingLibraryItem, false);
		assertThat("precondition failed: subtitle was not cached.", cache.containsSubtitle
				(workingLibraryItem));

		cache.removeSubtitle(workingLibraryItem);

		assertThat("subtitle was not removed", !cache.containsSubtitle(workingLibraryItem));
	}

	@Test
	public void testRemoveSubtitles_shouldRemoveAllSubtitlesFromCache() {
		// The cache must contain the subtitle or else the test will fail
		cache.cacheSubtitle(workingLibraryItem, false);
		assertThat("precondition failed: subtitle was not cached.", cache.containsSubtitle
				(workingLibraryItem));

		cache.clearSubtitles();

		assertThat("subtitle cache was not cleared", !cache.containsSubtitle(workingLibraryItem));
	}

	@Test
	public void testCacheArtwork_usingNull_shouldNotCache() {
		cache.cacheArtwork(null, false, DIMENSION, DIMENSION);

		assertThat("null was cached", !cache.containsArtwork(null));
	}

	@Test
	public void testCacheArtwork_usingWorkingItem_shouldCache() throws
			LibraryReadException {
		cache.cacheArtwork(workingLibraryItem, false, DIMENSION, DIMENSION);

		assertThat("title was cached", !cache.containsTitle(workingLibraryItem));
		assertThat("subtitle was cached", !cache.containsSubtitle(workingLibraryItem));
		assertThat("artwork was not cached", cache.containsArtwork(workingLibraryItem));
	}

	@Test
	public void testCacheArtwork_usingBrokenItem_shouldNotCache() throws LibraryReadException {
		cache.cacheArtwork(brokenLibraryItem, false, DIMENSION, DIMENSION);

		assertThat("title was cached", !cache.containsTitle(workingLibraryItem));
		assertThat("subtitle was cached", !cache.containsSubtitle(workingLibraryItem));
		assertThat("artwork was cached", !cache.containsArtwork(workingLibraryItem));
	}

	@Test
	public void testCacheArtwork_usingDoNotRecache_shouldNotCache() throws LibraryReadException {
		// The cache must contain the subtitle or else the test will fail
		cache.cacheArtwork(workingLibraryItem, false, DIMENSION, DIMENSION);
		assertThat("precondition failed: artwork was not cached.", cache.containsArtwork
				(workingLibraryItem));

		// Request caching without recaching
		cache.cacheArtwork(workingLibraryItem, true, DIMENSION, DIMENSION);

		// Verify the cache contents
		assertThat("title was cached", !cache.containsTitle(workingLibraryItem));
		assertThat("subtitle was cached", !cache.containsSubtitle(workingLibraryItem));
		assertThat("artwork was not cached", cache.containsArtwork(workingLibraryItem));

		// Check how many times the item was directly queried
		verify(workingLibraryItem, never()).getTitle();
		verify(workingLibraryItem, never()).getSubtitle();
		verify(workingLibraryItem, times(1)).getArtwork(anyInt(), anyInt());
	}

	@Test
	public void testGetArtwork_shouldReturnCachedArtwork() {
		// The cache must contain the artwork or else the test will fail
		cache.cacheArtwork(workingLibraryItem, false, DIMENSION, DIMENSION);
		assertThat("precondition failed: artwork was not cached.", cache.containsArtwork
				(workingLibraryItem));

		assertThat(cache.getArtwork(workingLibraryItem), is(artwork));
	}

	@Test
	public void testRemoveArtwork_1_shouldRemoveArtworkFromCache() {
		// The cache must contain the artwork or else the test will fail
		cache.cacheArtwork(workingLibraryItem, false, DIMENSION, DIMENSION);
		assertThat("precondition failed: artwork was not cached.", cache.containsArtwork
				(workingLibraryItem));

		cache.removeArtwork(workingLibraryItem);

		assertThat("artwork was not removed", !cache.containsArtwork(workingLibraryItem));
	}

	@Test
	public void testRemoveArtwork_2_shouldRemoveAllArtworkFromCache() {
		// The cache must contain the artwork or else the test will fail
		cache.cacheArtwork(workingLibraryItem, false, DIMENSION, DIMENSION);
		assertThat("precondition failed: artwork was not cached.", cache.containsArtwork
				(workingLibraryItem));

		cache.clearArtwork();

		assertThat("artwork cache was not cleared", !cache.containsArtwork(workingLibraryItem));
	}
}