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
import android.util.LruCache;
import android.widget.TextView;

import com.matthewtamlin.mixtape.library.caching.LibraryItemCache;
import com.matthewtamlin.mixtape.library.caching.LruLibraryItemCache;
import com.matthewtamlin.mixtape.library.data.DisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.ImmutableDisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;
import com.matthewtamlin.mixtape.library.databinders.TitleBinder;
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
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class TestTitleBinder {
	/**
	 * The length of time to pause for when waiting for background tasks to finish.
	 */
	private static final int PAUSE_DURATION = 500;

	/**
	 * The title to use for the test LibraryItem.
	 */
	private CharSequence title;

	/**
	 * The default title to use in the test DisplayableDefaults.
	 */
	private CharSequence defaultTitle;

	/**
	 * A read-only LibraryItem for use in testing, which uses {@code TITLE} for the title and null
	 * for the other metadata.
	 */
	private LibraryItem libraryItem;

	/**
	 * A cache for use in testing.
	 */
	private LruCache<LibraryItem, CharSequence> cache;

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
		title = mock(CharSequence.class);
		defaultTitle = mock(CharSequence.class);

		libraryItem = mock(LibraryItem.class);
		when(libraryItem.getTitle()).thenReturn(title);

		cache = new LruCache<>(10);
		displayableDefaults = new ImmutableDisplayableDefaults(DEFAULT_TITLE, null, null);
		textView = mock(TextView.class);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code cache} argument of {@link
	 * TitleBinder#TitleBinder(LibraryItemCache, DisplayableDefaults)} is null. The test will only
	 * pass if an IllegalArgumentException is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_invalidArgs_nullCache() {
		new TitleBinder(null, displayableDefaults);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code defaults} argument of
	 * {@link TitleBinder#TitleBinder(LibraryItemCache, DisplayableDefaults)} is null. The test will
	 * only pass if an IllegalArgumentException is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_invalidArgs_nullDefaults() {
		new TitleBinder(cache, null);
	}

	/**
	 * Test to verify that the {@link TitleBinder#TitleBinder(LibraryItemCache,
	 * DisplayableDefaults)} constructor functions correctly when the provided with valid
	 * arguments.
	 */
	@Test
	public void testConstructor_validArgs() {
		new TitleBinder(cache, displayableDefaults); // Shouldn't throw exception
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code view} argument of {@link
	 * TitleBinder#bind(TextView, LibraryItem)} is null. The test will only pass if an
	 * IllegalArgumentException is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBind_invalidArgs_nullView() {
		final TitleBinder binder = new TitleBinder(cache, displayableDefaults);
		binder.bind(null, libraryItem);
	}

	/**
	 * Test to verify that the {@link TitleBinder#bind(TextView, LibraryItem)} method functions
	 * correctly when the {@code data} argument is null. The test will only pass if null is bound to
	 * the view.
	 */
	@Test
	public void testBind_nullData() {
		final TitleBinder binder = new TitleBinder(cache, displayableDefaults);

		binder.bind(textView, null);

		pause(); // Allow time for async processing to complete
		verify(textView, atLeastOnce()).setText(null);
		verify(textView, never()).setText(TITLE);
		verify(textView, never()).setText(DEFAULT_TITLE);
	}

	/**
	 * Test to verify that the {@link TitleBinder#bind(TextView, LibraryItem)} method functions
	 * correctly when the cache already contains the title of the data passed to the {@code data}
	 * argument. The test will only pass if the title is bound to the view.
	 */
	@Test
	public void testBind_dataCached() {
		final TitleBinder binder = new TitleBinder(cache, displayableDefaults);
		cache.cacheTitle(libraryItem, false);

		binder.bind(textView, libraryItem);

		pause(); // Allow time for async processing to complete
		verify(textView).setText(TITLE); // Called once to clear and once to set
	}

	/**
	 * Test to verify that the {@link TitleBinder#bind(TextView, LibraryItem)} method functions
	 * correctly when the cache does not contain the title of the data passed to the {@code data}
	 * argument. The test will only pass if the title is bound to the view.
	 */
	@Test
	public void testBind_dataAccessibleButNotCached() {
		final TitleBinder binder = new TitleBinder(cache, displayableDefaults);
		cache.removeTitle(libraryItem); // In case it was somehow cached previously

		binder.bind(textView, libraryItem);

		pause(); // Allow time for async processing to complete
		verify(textView).setText(TITLE);
	}

	/**
	 * Test to verify that the {@link TitleBinder#bind(TextView, LibraryItem)} method functions
	 * correctly when the cache does not contain the title of the data passed to the {@code data}
	 * argument, and the data cannot be directly accessed. The test will only pass if the default
	 * title is bound to the view.
	 */
	@Test
	public void testBind_dataInaccessibleAndNotCached() {
		final TitleBinder binder = new TitleBinder(cache, displayableDefaults);
		final LibraryItem inaccessibleItem = new InaccessibleLibraryItem();

		binder.bind(textView, inaccessibleItem);

		pause(); // Allow time for async processing to complete
		verify(textView).setText(DEFAULT_TITLE);
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