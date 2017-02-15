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
import com.matthewtamlin.mixtape.library.mixtape_body.BodyContract.View;
import com.matthewtamlin.mixtape.library.mixtape_body.DirectBodyPresenter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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
@SuppressWarnings("unchecked") // Warning shown by mocks, but this is ok
@RunWith(AndroidJUnit4.class)
public class TestDirectBodyPresenter {
	/**
	 * Test to verify that the {@link DirectBodyPresenter#setDataSource(ListDataSource)} method
	 * functions correctly when the presenter already has a data source. The test will only pass if
	 * the method registers the new data source for all relevant callbacks, and unregisters the old
	 * data source from all callbacks.
	 */
	@Test
	public void testSetDataSource() {
		final ListDataSource<LibraryItem> dataSource1 = mock(ListDataSource.class);
		final ListDataSource<LibraryItem> dataSource2 = mock(ListDataSource.class);

		final DirectBodyPresenter<LibraryItem, ListDataSource<LibraryItem>, View> presenter = new
				DirectBodyPresenter<>();

		presenter.setDataSource(dataSource1);

		verify(dataSource1).registerItemAddedListener(presenter);
		verify(dataSource1).registerDataModifiedListener(presenter);
		verify(dataSource1).registerItemMovedListener(presenter);
		verify(dataSource1).registerItemRemovedListener(presenter);
		verify(dataSource1).registerDataReplacedListener(presenter);
		verify(dataSource1).registerItemModifiedListener(presenter);
		verify(dataSource1).registerLongOperationListener(presenter);

		presenter.setDataSource(dataSource2);

		verify(dataSource1).unregisterItemAddedListener(presenter);
		verify(dataSource1).unregisterDataModifiedListener(presenter);
		verify(dataSource1).unregisterItemMovedListener(presenter);
		verify(dataSource1).unregisterItemRemovedListener(presenter);
		verify(dataSource1).unregisterDataReplacedListener(presenter);
		verify(dataSource1).unregisterItemModifiedListener(presenter);
		verify(dataSource1).unregisterLongOperationListener(presenter);

		verify(dataSource2).registerItemAddedListener(presenter);
		verify(dataSource2).registerDataModifiedListener(presenter);
		verify(dataSource2).registerItemMovedListener(presenter);
		verify(dataSource2).registerItemRemovedListener(presenter);
		verify(dataSource2).registerDataReplacedListener(presenter);
		verify(dataSource2).registerItemModifiedListener(presenter);
		verify(dataSource2).registerLongOperationListener(presenter);

		presenter.setDataSource(dataSource2);

		verify(dataSource2).unregisterItemAddedListener(presenter);
		verify(dataSource2).unregisterDataModifiedListener(presenter);
		verify(dataSource2).unregisterItemMovedListener(presenter);
		verify(dataSource2).unregisterItemRemovedListener(presenter);
		verify(dataSource2).unregisterDataReplacedListener(presenter);
		verify(dataSource2).unregisterItemModifiedListener(presenter);
		verify(dataSource2).unregisterLongOperationListener(presenter);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#setView(View)} method
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
	 * Test to verify that the {@link DirectBodyPresenter#setView(View)} method
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