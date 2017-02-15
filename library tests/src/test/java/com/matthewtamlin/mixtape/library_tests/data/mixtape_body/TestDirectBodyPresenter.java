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

package com.matthewtamlin.mixtape.library_tests.data.mixtape_body;

import android.support.test.runner.AndroidJUnit4;

import com.matthewtamlin.mixtape.library.base_mvp.BaseDataSource;
import com.matthewtamlin.mixtape.library.base_mvp.ListDataSource;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.mixtape_body.BodyContract;
import com.matthewtamlin.mixtape.library.mixtape_body.DirectBodyPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for subclasses of the DirectBodyPresenter class.
 *
 * @param <S>
 * 		the type of data source used by the presenter under test
 * @param <V>
 * 		the type of view controlled by the presenter under test
 */
@RunWith(AndroidJUnit4.class)
public class TestDirectBodyPresenter {
	/**
	 * A data source for use in testing, supplied by the subclass.
	 */
	private ListDataSource<LibraryItem> dataSource;

	/**
	 * A view for use in testing, supplied by the subclass.
	 */
	private BodyContract.View view;

	/**
	 * Initialises the testing variables and checks that all preconditions pass. Subclasses should
	 * always call the superclass implementation, however this class will call on the abstract
	 * methods so they must be ready first.
	 */
	@Before
	public void setup() {
		dataSource = createMockDataSource();
		view = createMockView();

		presenterWithDataSourceOnly = createPresenter();
		presenterWithDataSourceOnly.setDataSource(dataSource);
		presenterWithDataSourceOnly.setView(null);

		presenterWithViewOnly = createPresenter();
		presenterWithViewOnly.setDataSource(null);
		presenterWithViewOnly.setView(view);

		presenterWithDataSourceAndView = createPresenter();
		presenterWithDataSourceAndView.setDataSource(dataSource);
		presenterWithDataSourceAndView.setView(view);

		assertThat("Precondition failed. presenterWithDataSourceOnly is null",
				presenterWithDataSourceOnly, is(notNullValue()));
		assertThat("Precondition failed. presenterWithDataSourceOnly has no data source",
				presenterWithDataSourceOnly.getDataSource(), is(dataSource));
		assertThat("Precondition failed. presenterWithDataSourceOnly has a view",
				presenterWithDataSourceOnly.getView(), is(nullValue()));

		assertThat("Precondition failed. presenterWithViewOnly is null", presenterWithViewOnly,
				is(notNullValue()));
		assertThat("Precondition failed. presenterWithViewOnly has a data source",
				presenterWithViewOnly.getDataSource(), is(nullValue()));
		assertThat("Precondition failed. presenterWithViewOnly has no view",
				presenterWithViewOnly.getView(), is(view));

		assertThat("Precondition failed. presenterWithDataSourceAndView is null",
				presenterWithDataSourceAndView,
				is(notNullValue()));
		assertThat("Precondition failed. presenterWithDataSourceAndView has no data source",
				presenterWithDataSourceAndView.getDataSource(), is(dataSource));
		assertThat("Precondition failed. presenterWithDataSourceAndView has no view",
				presenterWithDataSourceAndView.getView(), is(view));
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#setDataSource(ListDataSource)} method
	 * functions correctly when the presenter does not already have a data source. The test will
	 * only pass if the method registers for all relevant callbacks from the new data source.
	 */
	@Test
	public void testSetDataSource_withoutDataSource() {
		final S newDataSource = createMockDataSource();

		presenterWithViewOnly.setDataSource(newDataSource);

		verify(newDataSource).registerItemAddedListener(presenterWithViewOnly);
		verify(newDataSource).registerDataModifiedListener(presenterWithViewOnly);
		verify(newDataSource).registerItemMovedListener(presenterWithViewOnly);
		verify(newDataSource).registerItemRemovedListener(presenterWithViewOnly);
		verify(newDataSource).registerDataReplacedListener(presenterWithViewOnly);
		verify(newDataSource).registerItemModifiedListener(presenterWithViewOnly);
		verify(newDataSource).registerLongOperationListener(presenterWithViewOnly);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#setDataSource(ListDataSource)} method
	 * functions correctly when the presenter already has a data source. The test will only pass if
	 * the method registers the new data source for all relevant callbacks, and unregisters the old
	 * data source from all callbacks.
	 */
	@Test
	public void testSetDataSource_withDataSource() {
		final S newDataSource = createMockDataSource();

		presenterWithDataSourceOnly.setDataSource(newDataSource);

		verify(dataSource).unregisterItemAddedListener(presenterWithDataSourceOnly);
		verify(dataSource).unregisterDataModifiedListener(presenterWithDataSourceOnly);
		verify(dataSource).unregisterItemMovedListener(presenterWithDataSourceOnly);
		verify(dataSource).unregisterItemRemovedListener(presenterWithDataSourceOnly);
		verify(dataSource).unregisterDataReplacedListener(presenterWithDataSourceOnly);
		verify(dataSource).unregisterItemModifiedListener(presenterWithDataSourceOnly);
		verify(dataSource).unregisterLongOperationListener(presenterWithDataSourceOnly);

		verify(newDataSource).registerItemAddedListener(presenterWithDataSourceOnly);
		verify(newDataSource).registerDataModifiedListener(presenterWithDataSourceOnly);
		verify(newDataSource).registerItemMovedListener(presenterWithDataSourceOnly);
		verify(newDataSource).registerItemRemovedListener(presenterWithDataSourceOnly);
		verify(newDataSource).registerDataReplacedListener(presenterWithDataSourceOnly);
		verify(newDataSource).registerItemModifiedListener(presenterWithDataSourceOnly);
		verify(newDataSource).registerLongOperationListener(presenterWithDataSourceOnly);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#setView(BodyContract.View)} method
	 * functions correctly when the presenter does not already have a view. The test will only pass
	 * if the method registers the presenter as the new view's presenter.
	 */
	@Test
	public void testSetView_withoutView() {
		final V newView = createMockView();

		presenterWithDataSourceOnly.setView(newView);

		verify(newView).setPresenter(presenterWithDataSourceOnly);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#setView(BodyContract.View)} method
	 * functions correctly when the presenter already has a view. The test will only pass if the
	 * presenter is registered with the new view and unregistered with the old view.
	 */
	@Test
	public void testSetView_withView() {
		final V newView = createMockView();

		presenterWithViewOnly.setView(newView);

		verify(view).setPresenter(null);
		verify(newView).setPresenter(presenterWithViewOnly);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onDataLoaded(BaseDataSource, List)} method
	 * functions correctly when the presenter does not have a view. The test will only pass if the
	 * method exits normally.
	 */
	@Test
	@SuppressWarnings("unchecked") // This is fine since it's a mock
	public void testOnDataLoaded_withoutView() {
		presenterWithDataSourceOnly.onDataLoaded(dataSource, mock(List.class));
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onDataLoaded(BaseDataSource, List)} method
	 * functions correctly when the presenter has a view. The test will only pass if the loaded data
	 * is passed to the view.
	 */
	@Test
	@SuppressWarnings("unchecked") // This is fine since it's a mock
	public void testOnDataLoaded_withView() {
		final List<LibraryItem> items = mock(List.class);

		presenterWithDataSourceAndView.onDataLoaded(dataSource, items);

		verify(view).setItems(any(List.class));
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onLoadDataFailed(BaseDataSource)} method
	 * functions correctly when the presenter does not have a view. The test will only pass if the
	 * method exits normally.
	 */
	@Test
	@SuppressWarnings("unchecked") // This is fine since it's a mock
	public void testOnLoadDataFailed_withoutView() {
		presenterWithDataSourceOnly.onLoadDataFailed(dataSource);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onLoadDataFailed(BaseDataSource)} method
	 * functions correctly when the presenter has a view. The test will only pass if the view is
	 * updated to show no items.
	 */
	@Test
	@SuppressWarnings("unchecked") // This is fine since it's a mock
	public void testOnLoadDataFailed_withView() {
		presenterWithDataSourceAndView.onLoadDataFailed(dataSource);

		final ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
		verify(view).setItems(captor.capture());

		assertThat("view items were not updated", captor.getValue().size(), is(0));
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onDataModified(BaseDataSource, List)}
	 * method functions correctly when the presenter does not have a view. The test will only pass
	 * if the method exits normally.
	 */
	@Test
	@SuppressWarnings("unchecked") // This is fine since it's a mock
	public void testOnDataModified_withoutView() {
		presenterWithDataSourceOnly.onDataModified(dataSource, mock(List.class));
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onDataModified(BaseDataSource, List)}
	 * method functions properly when the presenter has a view. The test will only pass if the view
	 * is notified of the event.
	 */
	@Test
	@SuppressWarnings("unchecked") // This is fine since it's a mock
	public void testOnDataModified_withView() {
		presenterWithDataSourceAndView.onDataModified(dataSource, mock(List.class));

		verify(view).notifyItemsChanged();
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onDataMoved(ListDataSource, LibraryItem,
	 * int, int)} method functions correctly when the presenter does not have a view. The test will
	 * only pass if the method exits normally.
	 */
	@Test
	public void testOnDataMoved_withoutView() {
		presenterWithDataSourceOnly.onDataMoved(dataSource, mock(LibraryItem.class), 1, 2);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onDataMoved(ListDataSource, LibraryItem,
	 * int, int)} method functions properly when the presenter has a view. The test will only pass
	 * if the view is notified of the event.
	 */
	@Test
	public void testOnDataMoved_withView() {
		final int startIndex = 1;
		final int finalIndex = 2;

		presenterWithDataSourceAndView
				.onDataMoved(dataSource, mock(LibraryItem.class), startIndex, finalIndex);

		verify(view).notifyItemMoved(1, 2);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onDataAdded(ListDataSource, LibraryItem,
	 * int)} method functions correctly when the presenter does not have a view. The test will only
	 * pass if the method exits normally.
	 */
	@Test
	public void testOnDataAdded_withoutView() {
		presenterWithDataSourceOnly.onDataAdded(dataSource, mock(LibraryItem.class), 1);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onDataAdded(ListDataSource, LibraryItem,
	 * int)} method functions properly when the presenter has a view. The test will only pass if the
	 * view is notified of the event.
	 */
	@Test
	public void testOnDataAdded_withView() {
		final LibraryItem addedItem = mock(LibraryItem.class);
		final int addedIndex = 1;

		presenterWithDataSourceAndView.onDataAdded(dataSource, addedItem, addedIndex);

		verify(view).notifyItemAdded(addedIndex);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onDataRemoved(ListDataSource, LibraryItem,
	 * int)} method functions correctly when the presenter does not have a view. The test will only
	 * pass if the method exits normally.
	 */
	@Test
	public void testOnDataRemoved_withoutView() {
		presenterWithDataSourceOnly.onDataRemoved(dataSource, mock(LibraryItem.class), 1);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onDataRemoved(ListDataSource, LibraryItem,
	 * int)} method functions properly when the presenter has a view. The test will only pass if the
	 * view is notified of the event.
	 */
	@Test
	public void testOnDataRemoved_withView() {
		final LibraryItem addedItem = mock(LibraryItem.class);
		final int removedIndex = 1;

		presenterWithDataSourceAndView.onDataRemoved(dataSource, addedItem, removedIndex);

		verify(view).notifyItemRemoved(removedIndex);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onDataReplaced(BaseDataSource, List,
	 * List)} method functions correctly when the presenter does not have a view. The test will only
	 * pass if the method exits normally.
	 */
	@Test
	@SuppressWarnings("unchecked") // This is fine since it's a mock
	public void testOnDataReplaced_withoutView() {
		presenterWithDataSourceOnly.onDataReplaced(dataSource, mock(List.class), mock(List.class));
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onDataReplaced(BaseDataSource, List,
	 * List)} method functions properly when the presenter has a view. The test will only pass if
	 * the view is updated with the new data.
	 */
	@Test
	@SuppressWarnings("unchecked") // This is fine since it's a mock
	public void testOnDataReplaced_withView() {
		final List newData = mock(List.class);

		presenterWithDataSourceAndView.onDataReplaced(dataSource, mock(List.class), newData);

		verify(view).setItems(newData);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onItemModified(ListDataSource,
	 * LibraryItem, int)} method functions correctly when the presenter does not have a view. The
	 * test will only pass if the method exits normally.
	 */
	@Test
	public void testOnListItemModified_withoutView() {
		presenterWithDataSourceOnly.onItemModified(dataSource, mock(LibraryItem.class), 1);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onItemModified(ListDataSource,
	 * LibraryItem, int)} method functions properly when the presenter has a view. The test will
	 * only pass if the view is notified of the event.
	 */
	@Test
	public void testOnListItemModified_withView() {
		final int modifiedIndex = 1;

		presenterWithDataSourceAndView
				.onItemModified(dataSource, mock(LibraryItem.class), modifiedIndex);

		verify(view).notifyItemModified(modifiedIndex);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onLongOperationStarted(BaseDataSource)}
	 * method functions correctly when the presenter does not have a view. The test will only pass
	 * if the method exits normally.
	 */
	@Test
	public void testOnLongOperationStarted_withoutView() {
		presenterWithDataSourceOnly.onLongOperationStarted(dataSource);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onLongOperationStarted(BaseDataSource)}
	 * method functions properly when the presenter has a view. The test will only pass if the view
	 * is notified of the event.
	 */
	@Test
	public void testOnLongOperationStarted_withView() {
		presenterWithDataSourceAndView.onLongOperationStarted(dataSource);

		verify(view).showLoadingIndicator(true);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onLongOperationFinished(BaseDataSource)}
	 * method functions correctly when the presenter does not have a view. The test will only pass
	 * if the method exits normally.
	 */
	@Test
	public void testOnLongOperationFinished_withoutView() {
		presenterWithDataSourceOnly.onLongOperationFinished(dataSource);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onLongOperationFinished(BaseDataSource)}
	 * method functions properly when the presenter has a view. The test will only pass if the view
	 * is notified of the event.
	 */
	@Test
	public void testOnLongOperationFinished_withView() {
		presenterWithDataSourceAndView.onLongOperationFinished(dataSource);

		verify(view).showLoadingIndicator(false);
	}
}