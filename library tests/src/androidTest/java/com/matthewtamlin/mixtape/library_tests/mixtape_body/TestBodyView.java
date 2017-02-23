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
import com.matthewtamlin.mixtape.library.mixtape_body.BodyView;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

@SuppressWarnings("unchecked")
@RunWith(AndroidJUnit4.class)
public abstract class TestBodyView {
	@Test
	public void testSetAndGetItems() {
		final List<LibraryItem> items = new ArrayList<>();
		final LibraryItem item1 = mock(LibraryItem.class);
		final LibraryItem item2 = mock(LibraryItem.class);
		items.add(item1);
		items.add(item2);

		getBodyViewDirect().setItems(items);

		assertThat("Returned list has wrong size.", getBodyViewDirect().getItems().size(),
				is(2));
		assertThat("Returned list doesn't contain item 1.", getBodyViewDirect().getItems()
				.contains(item1), is(true));
		assertThat("Returned list doesn't contain item 1.", getBodyViewDirect().getItems()
				.contains(item2), is(true));
	}

	@Test
	public void testSetAndGetContextualMenuResource() {
		final int id = 10010010;

		getBodyViewDirect().setContextualMenuResource(id);

		assertThat("Returned wrong ID.", getBodyViewDirect().getContextualMenuResource(), is(id));
	}

	public abstract BodyView getBodyViewDirect();
}