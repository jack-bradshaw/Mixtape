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

package com.matthewtamlin.mixtape.library_tests.mixtape_header;

import android.support.test.runner.AndroidJUnit4;


import com.matthewtamlin.mixtape.library.base_mvp.BaseDataSource;
import com.matthewtamlin.mixtape.library.caching.LibraryItemCache;
import com.matthewtamlin.mixtape.library.caching.LruLibraryItemCache;
import com.matthewtamlin.mixtape.library.data.DisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.ImmutableDisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.databinders.ArtworkBinder;
import com.matthewtamlin.mixtape.library.databinders.SubtitleBinder;
import com.matthewtamlin.mixtape.library.databinders.TitleBinder;
import com.matthewtamlin.mixtape.library.mixtape_header.HeaderContract;
import com.matthewtamlin.mixtape.library.mixtape_header.SmallHeader;
import com.matthewtamlin.mixtape.library.mixtape_header.SmallHeaderPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class TestSmallHeaderPresenter extends TestDirectHeaderPresenter {
	private TitleBinder titleBinder;
	private SubtitleBinder subtitleBinder;
	private ArtworkBinder artworkBinder;

	private SmallHeaderPresenter presenter;

	@Before
	public void setup() {
		// Create binders
		final LibraryItemCache cache = new LruLibraryItemCache(1000, 1000, 1000);
		final DisplayableDefaults defaults = new ImmutableDisplayableDefaults(null, null, null);
		titleBinder = new TitleBinder(cache, defaults);
		subtitleBinder = new SubtitleBinder(cache, defaults);
		artworkBinder = new ArtworkBinder(cache, defaults);

		// Create a presenter for testing
		presenter = createPresenter();

		super.setup();
	}

	@Test
	public void testGetTitleDataBinder() {
		assertThat("wrong title binder", presenter.getTitleDataBinder(), is(titleBinder));
	}

	@Test
	public void testGetSubtitleDataBinder() {
		assertThat("wrong subtitle binder", presenter.getSubtitleDataBinder(), is(subtitleBinder));
	}

	@Test
	public void testGetArtworkDataBinder() {
		assertThat("wrong artwork binder", presenter.getArtworkDataBinder(), is(artworkBinder));
	}

	@Override
	@SuppressWarnings("unchecked") // This is fine since it's a mock
	protected BaseDataSource<LibraryItem> createMockDataSource() {
		return mock(BaseDataSource.class);
	}

	@Override
	protected HeaderContract.View createMockView() {
		return mock(SmallHeader.class);
	}

	@Override
	protected SmallHeaderPresenter createPresenter() {
		return new SmallHeaderPresenter(titleBinder, subtitleBinder, artworkBinder);
	}
}
