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

package com.matthewtamlin.mixtape.library_tests.databinders;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;


import com.matthewtamlin.mixtape.library.caching.LibraryItemCache;
import com.matthewtamlin.mixtape.library.caching.LruLibraryItemCache;
import com.matthewtamlin.mixtape.library.data.DisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.ImmutableDisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;
import com.matthewtamlin.mixtape.library.databinders.SubtitleBinder;
import com.matthewtamlin.mixtape.library_tests.stubs.InaccessibleLibraryItem;
import com.matthewtamlin.mixtape.library_tests.stubs.NormalLibraryItem;
import com.matthewtamlin.mixtape.library_tests.test.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class TestSubtitleBinder {
	/**
	 * The resource ID for an image to use as the artwork and the default artwork.
	 */
	private static final int ARTWORK_RES_ID = R.raw.image1;

	/**
	 * The length of time to pause for when waiting for background tasks to finish.
	 */
	private static final int PAUSE_DURATION = 500;

	/**
	 * The subtitle to use for the test LibraryItem.
	 */
	private static final String SUBTITLE = "a subtitle";

	/**
	 * The default subtitle to use in the test DisplayableDefaults.
	 */
	private static final String DEFAULT_SUBTITLE = "a default subtitle";

	/**
	 * A read-only LibraryItem for use in testing, which uses {@code SUBTITLE} for the subtitle and
	 * null for the other metadata.
	 */
	private LibraryItem libraryItem;

	/**
	 * A cache for use in testing.
	 */
	private LruLibraryItemCache cache;

	/**
	 * Defaults for use in testing.
	 */
	private DisplayableDefaults displayableDefaults;

	/**
	 * A TextView for use in testing. This view should be mocked so that method invocations can be
	 * recorded and reviewed.
	 */
	private TextView textView;

	/**
	 * Initialises the testing objects and assigns them to member variables.
	 */
	@Before
	public void setup() throws LibraryReadException {
		final Resources res = InstrumentationRegistry.getTargetContext().getResources();

		final Bitmap defaultArtwork = BitmapFactory.decodeResource(res, ARTWORK_RES_ID);
		assertThat("Precondition failed, default artwork is null.", defaultArtwork,
				is(notNullValue()));

		libraryItem = new NormalLibraryItem(res, null, SUBTITLE, -1);
		cache = new LruLibraryItemCache(1, 1000000, 1); // Should be more than enough for the test
		displayableDefaults = new ImmutableDisplayableDefaults(null, DEFAULT_SUBTITLE, null);
		textView = mock(TextView.class);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code cache} argument of {@link
	 * SubtitleBinder#SubtitleBinder(LibraryItemCache, DisplayableDefaults)} is null. The test will
	 * only pass if an IllegalArgumentException is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_invalidArgs_nullCache() {
		new SubtitleBinder(null, displayableDefaults);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code defaults} argument of
	 * {@link SubtitleBinder#SubtitleBinder(LibraryItemCache, DisplayableDefaults)} is null. The
	 * test will only pass if an IllegalArgumentException is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_invalidArgs_nullDefaults() {
		new SubtitleBinder(cache, null);
	}

	/**
	 * Test to verify that the {@link SubtitleBinder#SubtitleBinder(LibraryItemCache,
	 * DisplayableDefaults)} constructor functions correctly when the provided with valid
	 * arguments.
	 */
	@Test
	public void testConstructor_validArgs() {
		new SubtitleBinder(cache, displayableDefaults); // Shouldn't throw exception
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code view} argument of {@link
	 * SubtitleBinder#bind(TextView, LibraryItem)} is null. The test will only pass if an
	 * IllegalArgumentException is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBind_invalidArgs_nullView() {
		final SubtitleBinder binder = new SubtitleBinder(cache, displayableDefaults);
		binder.bind(null, libraryItem);
	}

	/**
	 * Test to verify that the {@link SubtitleBinder#bind(TextView, LibraryItem)} method functions
	 * correctly when the {@code data} argument is null. The test will only pass if null is bound to
	 * the view.
	 */
	@Test
	public void testBind_nullData() {
		final SubtitleBinder binder = new SubtitleBinder(cache, displayableDefaults);

		binder.bind(textView, null);

		pause(); // Allow time for async processing to complete
		verify(textView, atLeastOnce()).setText(null);
		verify(textView, never()).setText(SUBTITLE);
		verify(textView, never()).setText(DEFAULT_SUBTITLE);
	}

	/**
	 * Test to verify that the {@link SubtitleBinder#bind(TextView, LibraryItem)} method functions
	 * correctly when the cache already contains the subtitle of the data passed to the {@code data}
	 * argument. The test will only pass if the subtitle is bound to the view.
	 */
	@Test
	public void testBind_dataCached() {
		final SubtitleBinder binder = new SubtitleBinder(cache, displayableDefaults);
		cache.cacheSubtitle(libraryItem, false);

		binder.bind(textView, libraryItem);

		pause(); // Allow time for async processing to complete
		verify(textView).setText(SUBTITLE); // Called once to clear and once to set
	}

	/**
	 * Test to verify that the {@link SubtitleBinder#bind(TextView, LibraryItem)} method functions
	 * correctly when the cache does not contain the subtitle of the data passed to the {@code data}
	 * argument. The test will only pass if the subtitle is bound to the view.
	 */
	@Test
	public void testBind_dataAccessibleButNotCached() {
		final SubtitleBinder binder = new SubtitleBinder(cache, displayableDefaults);
		cache.removeSubtitle(libraryItem); // In case it was somehow cached previously

		binder.bind(textView, libraryItem);

		pause(); // Allow time for async processing to complete
		verify(textView).setText(SUBTITLE);
	}

	/**
	 * Test to verify that the {@link SubtitleBinder#bind(TextView, LibraryItem)} method functions
	 * correctly when the cache does not contain the subtitle of the data passed to the {@code data}
	 * argument, and the data cannot be directly accessed. The test will only pass if the default
	 * subtitle is bound to the view.
	 */
	@Test
	public void testBind_dataInaccessibleAndNotCached() {
		final SubtitleBinder binder = new SubtitleBinder(cache, displayableDefaults);
		final LibraryItem inaccessibleItem = new InaccessibleLibraryItem();

		binder.bind(textView, inaccessibleItem);

		pause(); // Allow time for async processing to complete
		verify(textView).setText(DEFAULT_SUBTITLE);
	}

	/**
	 * Suspends execution of the current thread. The duration is defined by the {@code
	 * PAUSE_DURATION} constant.
	 */
	private void pause() {
		try {
			Thread.sleep(PAUSE_DURATION);
		} catch (InterruptedException e) {
			throw new RuntimeException("wait interrupted, test aborted");
		}
	}
}