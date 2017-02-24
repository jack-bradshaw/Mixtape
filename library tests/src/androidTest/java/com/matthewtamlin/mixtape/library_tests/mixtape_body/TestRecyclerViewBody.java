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

import android.support.test.espresso.ViewInteraction;
import android.support.test.runner.AndroidJUnit4;

import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.databinders.ArtworkBinder;
import com.matthewtamlin.mixtape.library.databinders.DataBinder;
import com.matthewtamlin.mixtape.library.databinders.SubtitleBinder;
import com.matthewtamlin.mixtape.library.databinders.TitleBinder;
import com.matthewtamlin.mixtape.library.mixtape_body.RecyclerBodyView;
import com.matthewtamlin.mixtape.library.mixtape_body.RecyclerBodyView.TopReachedListener;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static com.matthewtamlin.mixtape.library_tests.mixtape_body.RecyclerBodyViewViewActions.scrollToEnd;
import static com.matthewtamlin.mixtape.library_tests.mixtape_body.RecyclerBodyViewViewActions.scrollToStart;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public abstract class TestRecyclerViewBody extends TestBodyView {
	private static final int NUMBER_OF_ITEMS = 100;

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
	public void testAddAndRemoveTopReachedListener() {
		final List<LibraryItem> items = new ArrayList<>();

		for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
			items.add(mock(LibraryItem.class));
		}

		getBodyViewDirect().setItems(items);
		getBodyViewEspresso().perform(scrollToEnd());

		final TopReachedListener listener1 = mock(TopReachedListener.class);
		final TopReachedListener listener2 = mock(TopReachedListener.class);

		getBodyViewDirect().addTopReachedListener(listener1);
		getBodyViewDirect().addTopReachedListener(listener2);
		getBodyViewDirect().addTopReachedListener(null);

		getBodyViewEspresso().perform(scrollToStart());

		verify(listener1, times(1)).onTopReached(getBodyViewDirect());
		verify(listener2, times(1)).onTopReached(getBodyViewDirect());

		getBodyViewEspresso().perform(scrollToEnd());

		verify(listener1, times(1)).onTopReached(getBodyViewDirect());
		verify(listener2, times(1)).onTopReached(getBodyViewDirect());

		getBodyViewEspresso().perform(scrollToStart());

		verify(listener1, times(2)).onTopReached(getBodyViewDirect());
		verify(listener2, times(2)).onTopReached(getBodyViewDirect());

		getBodyViewDirect().removeTopReachedListener(listener1);
		getBodyViewDirect().removeTopReachedListener(listener2);

		getBodyViewEspresso().perform(scrollToEnd());

		verify(listener1, times(2)).onTopReached(getBodyViewDirect());
		verify(listener2, times(2)).onTopReached(getBodyViewDirect());

		getBodyViewEspresso().perform(scrollToStart());

		verify(listener1, times(3)).onTopReached(getBodyViewDirect());
		verify(listener2, times(3)).onTopReached(getBodyViewDirect());
	}

	@Override
	public abstract RecyclerBodyView getBodyViewDirect();

	public abstract ViewInteraction getBodyViewEspresso();
}