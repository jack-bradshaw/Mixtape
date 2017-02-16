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
import android.support.v4.util.LruCache;
import android.widget.TextView;

import com.matthewtamlin.mixtape.library.data.DisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;
import com.matthewtamlin.mixtape.library.databinders.SubtitleBinder;
import com.matthewtamlin.mixtape.library_tests.stubs.InaccessibleLibraryItem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link SubtitleBinder} class.
 */
@RunWith(AndroidJUnit4.class)
public class TestSubtitleBinder {
	/**
	 * The length of time to pause for when waiting for background tasks to finish.
	 */
	private static final int PAUSE_DURATION = 500;

	/**
	 * A mock subtitle.
	 */
	private CharSequence subtitle;

	/**
	 * A mock default subtitle.
	 */
	private CharSequence defaultSubtitle;

	/**
	 * A mock subtitle. This should be inserted into the cache directly.
	 */
	private CharSequence cachedSubtitle;

	/**
	 * A mock LibraryItem which returns a subtitle but no title or artwork.
	 */
	private LibraryItem libraryItem;

	/**
	 * A cache for use in testing. This object is functional and is not a mock.
	 */
	private LruCache<LibraryItem, CharSequence> cache;

	/**
	 * A mock DisplayableDefaults object which returns a default subtitle, but no title or artwork.
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
		subtitle = mock(CharSequence.class);
		defaultSubtitle = mock(CharSequence.class);
		cachedSubtitle = mock(CharSequence.class);

		libraryItem = mock(LibraryItem.class);
		when(libraryItem.getSubtitle()).thenReturn(subtitle);

		cache = new LruCache<>(10);

		displayableDefaults = mock(DisplayableDefaults.class);
		when(displayableDefaults.getSubtitle()).thenReturn(defaultSubtitle);

		textView = mock(TextView.class);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code cache} argument of {@link
	 * SubtitleBinder#SubtitleBinder(LruCache, DisplayableDefaults)} is null. The test will only
	 * pass if an IllegalArgumentException is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_invalidArgs_nullCache() {
		new SubtitleBinder(null, displayableDefaults);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code defaults} argument of
	 * {@link SubtitleBinder#SubtitleBinder(LruCache, DisplayableDefaults)} is null. The test will
	 * only pass if an IllegalArgumentException is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_invalidArgs_nullDefaults() {
		new SubtitleBinder(cache, null);
	}

	/**
	 * Test to verify that the {@link SubtitleBinder#SubtitleBinder(LruCache, DisplayableDefaults)}
	 * constructor functions correctly when provided with valid arguments. The test will only pass
	 * if the getters return the values passed to the constructor.
	 */
	@Test
	public void testConstructor_validArgs() {
		final SubtitleBinder binder = new SubtitleBinder(cache, displayableDefaults);

		assertThat("Incorrect cache.", binder.getCache(), is(cache));
		assertThat("Incorrect defaults.", binder.getDefaults(), is(displayableDefaults));
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

		waitForAsyncEventsToFinish();

		verify(textView, atLeastOnce()).setText(null);
		verify(textView, never()).setText(subtitle);
		verify(textView, never()).setText(defaultSubtitle);
	}

	/**
	 * Test to verify that the {@link SubtitleBinder#bind(TextView, LibraryItem)} method functions
	 * correctly when the cache already contains a subtitle for the bound LibraryItem. The test will
	 * only pass if the cached subtitle is bound to the view.
	 */
	@Test
	public void testBind_dataCached() {
		final SubtitleBinder binder = new SubtitleBinder(cache, displayableDefaults);
		cache.put(libraryItem, cachedSubtitle);

		binder.bind(textView, libraryItem);

		waitForAsyncEventsToFinish();

		verify(textView).setText(cachedSubtitle);
		assertThat("Subtitle was removed from the cache.", cache.get(libraryItem),
				is(cachedSubtitle));
	}

	/**
	 * Test to verify that the {@link SubtitleBinder#bind(TextView, LibraryItem)} method functions
	 * correctly when the cache does not contain a subtitle for the bound LibraryItem, and the
	 * LibraryItem provides access to a subtitle. The test will only pass if the item's subtitle is
	 * bound to the view.
	 */
	@Test
	public void testBind_dataNotCached_dataAccessible() {
		final SubtitleBinder binder = new SubtitleBinder(cache, displayableDefaults);
		cache.remove(libraryItem); // In case it was somehow cached previously

		binder.bind(textView, libraryItem);

		waitForAsyncEventsToFinish();

		verify(textView).setText(subtitle);
		assertThat("Subtitle was not added to the cache.", cache.get(libraryItem), is(subtitle));
	}

	/**
	 * Test to verify that the {@link SubtitleBinder#bind(TextView, LibraryItem)} method functions
	 * correctly when the cache does not contain a subtitle for the bound LibraryItem, and the
	 * LibraryItem fails to provide access to a subtitle. The test will only pass if the default
	 * subtitle is bound to the view.
	 */
	@Test
	public void testBind_dataNotCached_dataInaccessible() {
		final SubtitleBinder binder = new SubtitleBinder(cache, displayableDefaults);
		final LibraryItem inaccessibleItem = new InaccessibleLibraryItem();

		binder.bind(textView, inaccessibleItem);

		waitForAsyncEventsToFinish();

		verify(textView).setText(defaultSubtitle);
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
			throw new RuntimeException("wait interrupted, test aborted");
		}
	}
}