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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ImageView;

import com.matthewtamlin.mixtape.library.caching.LruLibraryItemCache;
import com.matthewtamlin.mixtape.library.data.DisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.ImmutableDisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;
import com.matthewtamlin.mixtape.library.databinders.ArtworkBinder;
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
public class TestArtworkBinder {
	/**
	 * The length of time to pause for when waiting for background tasks to finish.
	 */
	private static final int PAUSE_DURATION = 500;

	private static final int FADE_IN_DURATION_MS = 200;

	/**
	 * The resource ID for an image to use as the artwork and the default artwork.
	 */
	private static final int ARTWORK_RES_ID = R.raw.image1;

	/**
	 * The image to use as the artwork.
	 */
	private Drawable artwork;

	/**
	 * The image to use as the default artwork.
	 */
	private Drawable defaultArtwork;

	/**
	 * A read-only LibraryItem for use in testing, which uses {@code artwork} for the artwork and
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
	 * An ImageView for use in testing. This view should be mocked so that method invocations can be
	 * recorded and reviewed.
	 */
	private ImageView imageView;

	/**
	 * Initialises the testing objects and assigns them to member variables.
	 */
	@Before
	public void setup() throws LibraryReadException {
		final Resources res = InstrumentationRegistry.getTargetContext().getResources();

		artwork = new BitmapDrawable(res, BitmapFactory.decodeResource(res, ARTWORK_RES_ID));
		defaultArtwork = new BitmapDrawable(res, BitmapFactory.decodeResource(res, ARTWORK_RES_ID));
		assertThat("Precondition failed, artwork is null.", artwork, is(notNullValue()));
		assertThat("Precondition failed, default artwork is null.", defaultArtwork,
				is(notNullValue()));

		libraryItem = new NormalLibraryItem(res, null, null, R.raw.image1);
		cache = new LruLibraryItemCache(1, 1, 1000000); // Should be more than enough for the test
		displayableDefaults = new ImmutableDisplayableDefaults(null, null, defaultArtwork);
		imageView = mock(ImageView.class);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code cache} argument of {@link
	 * ArtworkBinder#bind(ImageView, LibraryItem)} is null. The test will only pass if an
	 * IllegalArgumentException is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_invalidArgs_nullCache() {
		new ArtworkBinder(null, displayableDefaults);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code cache} argument of {@link
	 * ArtworkBinder#bind(ImageView, LibraryItem)} is null. The test will only pass if an
	 * IllegalArgumentException is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_invalidArgs_nullDefaults() {
		new ArtworkBinder(cache, null);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code view} argument of {@link
	 * ArtworkBinder#bind(ImageView, LibraryItem)} is null. The test will only pass if an
	 * IllegalArgumentException is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBind_invalidArgs_nullView() {
		final ArtworkBinder binder = new ArtworkBinder(cache, displayableDefaults);
		binder.bind(null, libraryItem);
	}

	/**
	 * Test to verify that the {@link ArtworkBinder#bind(ImageView, LibraryItem)} method functions
	 * correctly when the {@code data} argument is null. The test will only pass if null is bound to
	 * the view.
	 */
	@Test
	public void testBind_nullData() {
		final ArtworkBinder binder = new ArtworkBinder(cache, displayableDefaults);

		binder.bind(imageView, null);

		pause(); // Allow time for async processing to complete
		verify(imageView, atLeastOnce()).setImageBitmap(null);
		verify(imageView, never()).setImageDrawable(artwork);
		verify(imageView, never()).setImageDrawable(defaultArtwork);
	}

	/**
	 * Test to verify that the {@link ArtworkBinder#bind(ImageView, LibraryItem)} method functions
	 * correctly when the cache already contains the artwork of the data passed to the {@code data}
	 * argument. The test will only pass if the artwork is bound to the view.
	 */
	@Test
	public void testBind_dataCached() {
		final ArtworkBinder binder = new ArtworkBinder(cache, displayableDefaults);
		cache.cacheTitle(libraryItem, false);

		binder.bind(imageView, libraryItem);

		pause(); // Allow time for async processing to complete
		verify(imageView).setImageBitmap(artwork); // Called once to clear and once to set
	}

	/**
	 * Test to verify that the {@link ArtworkBinder#bind(ImageView, LibraryItem)} method functions
	 * correctly when the cache does not contain the artwork of the data passed to the {@code data}
	 * argument. The test will only pass if the artwork is bound to the view.
	 */
	@Test
	public void testBind_dataAccessibleButNotCached() {
		final ArtworkBinder binder = new ArtworkBinder(cache, displayableDefaults);
		cache.removeTitle(libraryItem); // In case it was somehow cached previously

		binder.bind(imageView, libraryItem);

		pause(); // Allow time for async processing to complete
		verify(imageView).setImageBitmap(artwork);
	}

	/**
	 * Test to verify that the {@link ArtworkBinder#bind(ImageView, LibraryItem)} method functions
	 * correctly when the cache does not contain the artwork of the data passed to the {@code data}
	 * argument, and the data cannot be directly accessed. The test will only pass if the default
	 * artwork is bound to the view.
	 */
	@Test
	public void testBind_dataInaccessibleAndNotCached() {
		final ArtworkBinder binder = new ArtworkBinder(cache, displayableDefaults);
		final LibraryItem inaccessibleItem = new InaccessibleLibraryItem();

		binder.bind(imageView, inaccessibleItem);

		pause(); // Allow time for async processing to complete
		verify(imageView).setImageBitmap(defaultArtwork);
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