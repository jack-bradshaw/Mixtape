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

import android.graphics.drawable.Drawable;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.matthewtamlin.mixtape.library.data.DisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;
import com.matthewtamlin.mixtape.library.databinders.ArtworkBinder;
import com.matthewtamlin.mixtape.library.databinders.TitleBinder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link ArtworkBinder} class.
 */
@RunWith(AndroidJUnit4.class)
public class TestArtworkBinder {
	/**
	 * The length of time to pause for when waiting for background tasks to finish.
	 */
	private static final int PAUSE_DURATION = 500;

	/**
	 * A mock artwork item.
	 */
	private Drawable artwork;

	/**
	 * A mock default artwork item. This should be returned by the defaults.
	 */
	private Drawable defaultArtwork;

	/**
	 * A mock artwork item. This should be inserted into the cache directly.
	 */
	private Drawable cachedArtwork;

	/**
	 * A mock LibraryItem which returns artwork but no title or subtitle.
	 */
	private LibraryItem libraryItem;

	/**
	 * A actual cache for use in testing, not a mock.
	 */
	private LruCache<LibraryItem, Drawable> cache;

	/**
	 * A mock DisplayableDefaults object which returns default artwork but no title or subtitle.
	 */
	private DisplayableDefaults displayableDefaults;

	/**
	 * A mock ImageView which data can be bound to.
	 */
	private ImageView imageView;

	/**
	 * Initialises the testing objects and assigns them to member variables.
	 */
	@Before
	public void setup() throws LibraryReadException {
		artwork = mock(Drawable.class);
		when(artwork.getIntrinsicWidth()).thenReturn(100);
		when(artwork.getIntrinsicHeight()).thenReturn(100);

		defaultArtwork = mock(Drawable.class);
		when(defaultArtwork.getIntrinsicWidth()).thenReturn(100);
		when(defaultArtwork.getIntrinsicHeight()).thenReturn(100);

		cachedArtwork = mock(Drawable.class);
		when(cachedArtwork.getIntrinsicWidth()).thenReturn(100);
		when(cachedArtwork.getIntrinsicHeight()).thenReturn(100);

		libraryItem = mock(LibraryItem.class);
		when(libraryItem.getArtwork(anyInt(), anyInt())).thenReturn(artwork);

		cache = new LruCache<>(10);

		displayableDefaults = mock(DisplayableDefaults.class);
		when(displayableDefaults.getArtwork()).thenReturn(defaultArtwork);

		imageView = mock(ImageView.class);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code cache} argument of {@link
	 * ArtworkBinder#ArtworkBinder(LruCache, DisplayableDefaults)} is null. The test will only pass
	 * if an IllegalArgumentException is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_invalidArgs_nullCache() {
		new ArtworkBinder(null, displayableDefaults);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code defaults} argument of
	 * {@link ArtworkBinder#ArtworkBinder(LruCache, DisplayableDefaults)} is null. The test will
	 * only pass if an IllegalArgumentException is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_invalidArgs_nullDefaults() {
		new ArtworkBinder(cache, null);
	}

	/**
	 * Test to verify that the {@link ArtworkBinder#ArtworkBinder(LruCache, DisplayableDefaults)}
	 * constructor functions correctly when provided with valid arguments. The test will only pass
	 * if the getters return the values passed to the constructor.
	 */
	public void testConstructor_validArgs() {
		final ArtworkBinder binder = new ArtworkBinder(cache, displayableDefaults);

		assertThat("Incorrect cache.", binder.getCache(), is(cache));
		assertThat("Incorrect defaults.", binder.getDefaults(), is(displayableDefaults));
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

		waitForAsyncEventsToFinish();

		verify(imageView, atLeastOnce()).setImageDrawable(null);
		verify(imageView, never()).setImageDrawable(artwork);
		verify(imageView, never()).setImageDrawable(defaultArtwork);
	}

	/**
	 * Test to verify that the {@link ArtworkBinder#bind(ImageView, LibraryItem)} method functions
	 * correctly when the {@code data} argument is not null, but it returns null artwork. The test
	 * will only pass if null is bound to the view.
	 */
	@Test
	public void testBind_nullArtwork() throws LibraryReadException {
		final ArtworkBinder binder = new ArtworkBinder(cache, displayableDefaults);

		when(libraryItem.getArtwork(anyInt(), anyInt())).thenReturn(null);

		binder.bind(imageView, libraryItem);

		waitForAsyncEventsToFinish();

		verify(imageView, atLeastOnce()).setImageDrawable(null);
		verify(imageView, never()).setImageDrawable(artwork);
		verify(imageView, never()).setImageDrawable(defaultArtwork);
	}

	/**
	 * Test to verify that the {@link ArtworkBinder#bind(ImageView, LibraryItem)} method functions
	 * correctly when the cache already contains artwork for the bound LibraryItem. The test will
	 * only pass if the cached artwork is bound to the view.
	 */
	@Test
	public void testBind_dataCached() {
		final ArtworkBinder binder = new ArtworkBinder(cache, displayableDefaults);
		cache.put(libraryItem, cachedArtwork);

		binder.bind(imageView, libraryItem);

		waitForAsyncEventsToFinish();

		verify(imageView).setImageDrawable(cachedArtwork);
		assertThat("Artwork was removed from the cache.", cache.get(libraryItem),
				is(cachedArtwork));
	}

	/**
	 * Test to verify that the {@link ArtworkBinder#bind(ImageView, LibraryItem)} method functions
	 * correctly when the cache does not contain artwork for the bound LibraryItem, and the
	 * LibraryItem provides access to artwork. The test will only pass if the item's artwork is
	 * bound to the view.
	 */
	@Test
	public void testBind_dataNotCached_dataAccessible() {
		final ArtworkBinder binder = new ArtworkBinder(cache, displayableDefaults);

		binder.bind(imageView, libraryItem);

		waitForAsyncEventsToFinish();

		verify(imageView).setImageDrawable(artwork);
		assertThat("Artwork was not added to the cache.", cache.get(libraryItem), is(artwork));
	}

	/**
	 * Test to verify that the {@link ArtworkBinder#bind(ImageView, LibraryItem)} method functions
	 * correctly when the cache does not contain artwork for the bound LibraryItem, and the
	 * LibraryItem fails to provide access to artwork. The test will only pass if the default
	 * artwork is bound to the view.
	 */
	@Test
	public void testBind_dataNotCached_dataInaccessible() throws LibraryReadException {
		final ArtworkBinder binder = new ArtworkBinder(cache, displayableDefaults);

		final LibraryItem inaccessibleItem = mock(LibraryItem.class);
		when(inaccessibleItem.getArtwork(anyInt(), anyInt())).thenThrow(new LibraryReadException());

		binder.bind(imageView, inaccessibleItem);

		waitForAsyncEventsToFinish();

		verify(imageView).setImageDrawable(defaultArtwork);
		assertThat("Something was added to the cache.", cache.get(libraryItem), is(nullValue()));
	}

	/**
	 * Suspends execution of the current thread. The duration is defined by the {@code
	 * PAUSE_DURATION} constant.
	 */
	private void waitForAsyncEventsToFinish() {
		try {
			Thread.sleep(PAUSE_DURATION);
		} catch (final InterruptedException e) {
			throw new RuntimeException("Wait interrupted, test aborted.");
		}
	}
}