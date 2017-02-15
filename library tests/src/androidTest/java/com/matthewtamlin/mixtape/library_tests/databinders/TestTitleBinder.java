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

import android.support.test.runner.AndroidJUnit4;
import android.util.LruCache;
import android.widget.TextView;

import com.matthewtamlin.mixtape.library.data.DisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;
import com.matthewtamlin.mixtape.library.databinders.TitleBinder;
import com.matthewtamlin.mixtape.library_tests.stubs.InaccessibleLibraryItem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
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
	 * A mock title.
	 */
	private CharSequence title;

	/**
	 * A mock default title.
	 */
	private CharSequence defaultTitle;

	/**
	 * A mock title. This should be inserted into the cache directly.
	 */
	private CharSequence cachedTitle;

	/**
	 * A mock LibraryItem which returns a title but no subtitle or artwork.
	 */
	private LibraryItem libraryItem;

	/**
	 * A cache for use in testing. This object is functional and is not a mock.
	 */
	private LruCache<LibraryItem, CharSequence> cache;

	/**
	 * A mock DisplayableDefaults object which returns a default title, but no subtitle or artwork.
	 */
	private DisplayableDefaults displayableDefaults;

	/**
	 * A mock TextView which data can be bound to.
	 */
	private TextView textView;

	/**
	 * Initialises the testing objects and assigns them to member variables.
	 */
	@Before
	public void setup() throws LibraryReadException {
		title = mock(CharSequence.class);
		defaultTitle = mock(CharSequence.class);
		cachedTitle = mock(CharSequence.class);

		libraryItem = mock(LibraryItem.class);
		when(libraryItem.getTitle()).thenReturn(title);

		cache = new LruCache<>(10);

		displayableDefaults = mock(DisplayableDefaults.class);
		when(displayableDefaults.getTitle()).thenReturn(defaultTitle);

		textView = mock(TextView.class);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code cache} argument of {@link
	 * TitleBinder#TitleBinder(LruCache, DisplayableDefaults)} is null. The test will only pass if
	 * an IllegalArgumentException is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_invalidArgs_nullCache() {
		new TitleBinder(null, displayableDefaults);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code defaults} argument of
	 * {@link TitleBinder#TitleBinder(LruCache, DisplayableDefaults)} is null. The test will only
	 * pass if an IllegalArgumentException is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_invalidArgs_nullDefaults() {
		new TitleBinder(cache, null);
	}

	/**
	 * Test to verify that the {@link TitleBinder#TitleBinder(LruCache, DisplayableDefaults)}
	 * constructor functions correctly when provided with valid arguments. The test will only pass
	 * if the getters return the values passed to the constructor.
	 */
	@Test
	public void testConstructor_validArgs() {
		final TitleBinder binder = new TitleBinder(cache, displayableDefaults);

		assertThat("Incorrect cache.", binder.getCache(), is(cache));
		assertThat("Incorrect defaults.", binder.getDefaults(), is(displayableDefaults));

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

		waitForAsyncEventsToFinish();

		verify(textView, atLeastOnce()).setText(null);
		verify(textView, never()).setText(title);
		verify(textView, never()).setText(defaultTitle);
	}

	/**
	 * Test to verify that the {@link TitleBinder#bind(TextView, LibraryItem)} method functions
	 * correctly when the cache already contains a title for the bound LibraryItem. The test will
	 * only pass if the cached title is bound to the view.
	 */
	@Test
	public void testBind_dataCached() {
		final TitleBinder binder = new TitleBinder(cache, displayableDefaults);
		cache.put(libraryItem, cachedTitle);

		binder.bind(textView, libraryItem);

		waitForAsyncEventsToFinish();

		verify(textView).setText(cachedTitle);
		assertThat("Title was removed from the cache.", cache.get(libraryItem), is(cachedTitle));
	}

	/**
	 * Test to verify that the {@link TitleBinder#bind(TextView, LibraryItem)} method functions
	 * correctly when the cache does not contain a title for the bound LibraryItem, and the
	 * LibraryItem provides access to a title. The test will only pass if the item's title is bound
	 * to the view.
	 */
	@Test
	public void testBind_dataNotCached_dataAccessible() {
		final TitleBinder binder = new TitleBinder(cache, displayableDefaults);
		cache.remove(libraryItem); // In case it was somehow cached previously

		binder.bind(textView, libraryItem);

		waitForAsyncEventsToFinish();

		verify(textView).setText(title);
		assertThat("Title was not added to the cache.", cache.get(libraryItem), is(title));
	}

	/**
	 * Test to verify that the {@link TitleBinder#bind(TextView, LibraryItem)} method functions
	 * correctly when the cache does not contain a title for the bound LibraryItem, and the
	 * LibraryItem fails to provide access to a title. The test will only pass if the default title
	 * is bound to the view.
	 */
	@Test
	public void testBind_dataNotCached_dataInaccessible() {
		final TitleBinder binder = new TitleBinder(cache, displayableDefaults);
		final LibraryItem inaccessibleItem = new InaccessibleLibraryItem();

		binder.bind(textView, inaccessibleItem);

		waitForAsyncEventsToFinish();

		verify(textView).setText(defaultTitle);

	}

	/**
	 * Suspends execution of the current thread. The duration is defined by the {@code
	 * PAUSE_DURATION} constant.
	 */
	private void waitForAsyncEventsToFinish() {
		try {
			Thread.sleep(PAUSE_DURATION);
		} catch (final InterruptedException e) {
			throw new RuntimeException("wait interrupted, test aborted");
		}
	}
}