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

package com.matthewtamlin.mixtape.library_tests.data.data;

import com.matthewtamlin.mixtape.library.base_mvp.ListDataSource.ItemAddedListener;
import com.matthewtamlin.mixtape.library.base_mvp.ListDataSource.ItemModifiedListener;
import com.matthewtamlin.mixtape.library.base_mvp.ListDataSource.ItemMovedListener;
import com.matthewtamlin.mixtape.library.base_mvp.ListDataSource.ItemRemovedListener;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.ListDataSourceHelper;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the {@link ListDataSourceHelper} class.
 */
@SuppressWarnings("unchecked") // Warning caused by mocks, but it isn't a problem
public class TestListDataSourceHelper extends TestBaseDataSourceHelper {
	/**
	 * The data source under test.
	 */
	private ListDataSourceHelper dataSource;

	@Before
	@Override
	public void setup() {
		super.setup();
		dataSource = createNewDataSource();
	}

	@Override
	public ListDataSourceHelper<LibraryItem> createNewDataSource() {
		return new ListDataSourceHelper<LibraryItem>() {
			@Override
			public void loadData(final boolean forceRefresh, final DataLoadedListener callback) {
				// Do nothing, not needed for test
			}
		};
	}

	/**
	 * Test to verify that the {@link ListDataSourceHelper#registerItemAddedListener(ItemAddedListener)}
	 * method functions correctly. The test will only pass if the non-null listeners are registered
	 * and the null listener is ignored.
	 */
	@Test
	public void testRegisterItemAddedListener() {
		final ItemAddedListener listener1 = mock(ItemAddedListener.class);
		final ItemAddedListener listener2 = mock(ItemAddedListener.class);

		dataSource.registerItemAddedListener(listener1);
		dataSource.registerItemAddedListener(listener2);
		dataSource.registerItemAddedListener(null);

		assertThat(dataSource.getItemAddedListeners().size(), is(2));
		assertThat(dataSource.getItemAddedListeners().contains(null), is(false));
		assertThat(dataSource.getItemAddedListeners().contains(listener1), is(true));
		assertThat(dataSource.getItemAddedListeners().contains(listener2), is(true));
	}

	/**
	 * Test to verify that the {@link ListDataSourceHelper#unregisterItemAddedListener(ItemAddedListener)}
	 * method functions correctly. The test will only pass if only specific listeners are
	 * unregistered.
	 */
	@Test
	public void testUnregisterItemAddedListener() {
		final ItemAddedListener listener1 = mock(ItemAddedListener.class);
		final ItemAddedListener listener2 = mock(ItemAddedListener.class);

		dataSource.registerItemAddedListener(listener1);
		dataSource.registerItemAddedListener(listener2);
		dataSource.unregisterItemAddedListener(listener1);
		dataSource.unregisterItemAddedListener(null);

		assertThat(dataSource.getItemAddedListeners().size(), is(1));
		assertThat(dataSource.getItemAddedListeners().contains(null), is(false));
		assertThat(dataSource.getItemAddedListeners().contains(listener1), is(false));
		assertThat(dataSource.getItemAddedListeners().contains(listener2), is(true));
	}

	/**
	 * Test to verify that the {@link ListDataSourceHelper#registerItemRemovedListener(ItemRemovedListener)}
	 * method functions correctly. The test will only pass if the non-null listeners are registered
	 * and the null listener is ignored.
	 */
	@Test
	public void testRegisterItemRemovedListener() {
		final ItemRemovedListener listener1 = mock(ItemRemovedListener.class);
		final ItemRemovedListener listener2 = mock(ItemRemovedListener.class);

		dataSource.registerItemRemovedListener(listener1);
		dataSource.registerItemRemovedListener(listener2);
		dataSource.registerItemRemovedListener(null);

		assertThat(dataSource.getItemRemovedListeners().size(), is(2));
		assertThat(dataSource.getItemRemovedListeners().contains(null), is(false));
		assertThat(dataSource.getItemRemovedListeners().contains(listener1), is(true));
		assertThat(dataSource.getItemRemovedListeners().contains(listener2), is(true));
	}

	/**
	 * Test to verify that the {@link ListDataSourceHelper#unregisterItemRemovedListener(ItemRemovedListener)}
	 * method functions correctly. The test will only pass if only specific listeners are
	 * unregistered.
	 */
	@Test
	public void testUnregisterItemRemovedListener() {
		final ItemRemovedListener listener1 = mock(ItemRemovedListener.class);
		final ItemRemovedListener listener2 = mock(ItemRemovedListener.class);

		dataSource.registerItemRemovedListener(listener1);
		dataSource.registerItemRemovedListener(listener2);
		dataSource.unregisterItemRemovedListener(listener1);
		dataSource.unregisterItemRemovedListener(null);

		assertThat(dataSource.getItemRemovedListeners().size(), is(1));
		assertThat(dataSource.getItemRemovedListeners().contains(null), is(false));
		assertThat(dataSource.getItemRemovedListeners().contains(listener1), is(false));
		assertThat(dataSource.getItemRemovedListeners().contains(listener2), is(true));
	}

	/**
	 * Test to verify that the {@link ListDataSourceHelper#registerItemModifiedListener(ItemModifiedListener)}
	 * method functions correctly. The test will only pass if the non-null listeners are registered
	 * and the null listener is ignored.
	 */
	@Test
	public void testRegisterItemModifiedListener() {
		final ItemModifiedListener listener1 = mock(ItemModifiedListener.class);
		final ItemModifiedListener listener2 = mock(ItemModifiedListener.class);

		dataSource.registerItemModifiedListener(listener1);
		dataSource.registerItemModifiedListener(listener2);
		dataSource.registerItemModifiedListener(null);

		assertThat(dataSource.getItemModifiedListeners().size(), is(2));
		assertThat(dataSource.getItemModifiedListeners().contains(null), is(false));
		assertThat(dataSource.getItemModifiedListeners().contains(listener1), is(true));
		assertThat(dataSource.getItemModifiedListeners().contains(listener2), is(true));
	}

	/**
	 * Test to verify that the {@link ListDataSourceHelper#unregisterItemModifiedListener(ItemModifiedListener)}
	 * method functions correctly. The test will only pass if only specific listeners are
	 * unregistered.
	 */
	@Test
	public void testUnregisterItemModifiedListener() {
		final ItemModifiedListener listener1 = mock(ItemModifiedListener.class);
		final ItemModifiedListener listener2 = mock(ItemModifiedListener.class);

		dataSource.registerItemModifiedListener(listener1);
		dataSource.registerItemModifiedListener(listener2);
		dataSource.unregisterItemModifiedListener(listener1);
		dataSource.unregisterItemModifiedListener(null);

		assertThat(dataSource.getItemModifiedListeners().size(), is(1));
		assertThat(dataSource.getItemModifiedListeners().contains(null), is(false));
		assertThat(dataSource.getItemModifiedListeners().contains(listener1), is(false));
		assertThat(dataSource.getItemModifiedListeners().contains(listener2), is(true));
	}

	/**
	 * Test to verify that the {@link ListDataSourceHelper#registerItemMovedListener(ItemMovedListener)}
	 * method functions correctly. The test will only pass if the non-null listeners are registered
	 * and the null listener is ignored.
	 */
	@Test
	public void testRegisterItemMovedListener() {
		final ItemMovedListener listener1 = mock(ItemMovedListener.class);
		final ItemMovedListener listener2 = mock(ItemMovedListener.class);

		dataSource.registerItemMovedListener(listener1);
		dataSource.registerItemMovedListener(listener2);
		dataSource.registerItemMovedListener(null);

		assertThat(dataSource.getItemMovedListeners().size(), is(2));
		assertThat(dataSource.getItemMovedListeners().contains(null), is(false));
		assertThat(dataSource.getItemMovedListeners().contains(listener1), is(true));
		assertThat(dataSource.getItemMovedListeners().contains(listener2), is(true));
	}

	/**
	 * Test to verify that the {@link ListDataSourceHelper#unregisterItemModifiedListener(ItemModifiedListener)}
	 * method functions correctly. The test will only pass if only specific listeners are
	 * unregistered.
	 */
	@Test
	public void testUnregisterItemMovedListener() {
		final ItemMovedListener listener1 = mock(ItemMovedListener.class);
		final ItemMovedListener listener2 = mock(ItemMovedListener.class);

		dataSource.registerItemMovedListener(listener1);
		dataSource.registerItemMovedListener(listener2);
		dataSource.unregisterItemMovedListener(listener1);
		dataSource.unregisterItemMovedListener(null);

		assertThat(dataSource.getItemMovedListeners().size(), is(1));
		assertThat(dataSource.getItemMovedListeners().contains(null), is(false));
		assertThat(dataSource.getItemMovedListeners().contains(listener1), is(false));
		assertThat(dataSource.getItemMovedListeners().contains(listener2), is(true));
	}
}