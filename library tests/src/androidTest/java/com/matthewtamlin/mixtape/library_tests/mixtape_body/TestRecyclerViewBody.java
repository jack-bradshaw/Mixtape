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

package com.matthewtamlin.mixtape.library_tests.mixtape_body;

import static org.hamcrest.MatcherAssert.assertThat;

import android.support.test.espresso.ViewInteraction;
import android.support.test.runner.AndroidJUnit4;

import com.matthewtamlin.mixtape.library.databinders.ArtworkBinder;
import com.matthewtamlin.mixtape.library.databinders.DataBinder;
import com.matthewtamlin.mixtape.library.databinders.SubtitleBinder;
import com.matthewtamlin.mixtape.library.databinders.TitleBinder;
import com.matthewtamlin.mixtape.library.mixtape_body.RecyclerBodyView;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public abstract class TestRecyclerViewBody extends TestBodyView {
	@Test
	public void testSetAndGetTitleBinder() {
		final TitleBinder binder = mock(TitleBinder.class);

		getBodyViewDirect().setTitleDataBinder(binder);

		assertThat(getBodyViewDirect().getTitleDataBinder(), is((DataBinder) binder));
	}

	@Test
	public void testSetAndGetSubtitleBinder() {
		final SubtitleBinder binder = mock(SubtitleBinder.class);

		getBodyViewDirect().setSubtitleDataBinder(binder);

		assertThat(getBodyViewDirect().getSubtitleDataBinder(), is((DataBinder) binder));
	}

	@Test
	public void testSetAndGetArtworkBinder() {
		final ArtworkBinder binder = mock(ArtworkBinder.class);

		getBodyViewDirect().setArtworkDataBinder(binder);

		assertThat(getBodyViewDirect().getArtworkDataBinder(), is((DataBinder) binder));
	}

	@Test
	public void testAddTopReachedListener() {

	}

	@Test
	public void testRemoveTopReachedListener() {

	}

	@Test
	public void testClearTopReachedListeners() {

	}

	@Override
	public abstract RecyclerBodyView getBodyViewDirect();

	public abstract ViewInteraction getBodyViewEspresso();
}