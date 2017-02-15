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
import com.matthewtamlin.mixtape.library.data.ListDataSourceHelper;
import com.matthewtamlin.mixtape.library.mixtape_body.BodyContract.View;
import com.matthewtamlin.mixtape.library.mixtape_body.DirectBodyPresenter;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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
	private DirectBodyPresenter<LibraryItem, ListDataSource<LibraryItem>, View> presenter;

	public void setup() {
		presenter = new DirectBodyPresenter<>();
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#setDataSource(ListDataSource)} method
	 * functions correctly when the presenter already has a data source. The test will only pass if
	 * the method registers the new data source for all relevant callbacks, and unregisters the old
	 * data source from all callbacks.
	 */
	@Test
	public void testSetDataSource_withoutView() {
		final ListDataSource<LibraryItem> dataSource1 = mock(ListDataSource.class);
		final ListDataSource<LibraryItem> dataSource2 = mock(ListDataSource.class);

		presenter.setDataSource(dataSource1);

		verify(dataSource1).registerItemAddedListener(presenter);
		verify(dataSource1).registerDataModifiedListener(presenter);
		verify(dataSource1).registerItemMovedListener(presenter);
		verify(dataSource1).registerItemRemovedListener(presenter);
		verify(dataSource1).registerDataReplacedListener(presenter);
		verify(dataSource1).registerItemModifiedListener(presenter);
		verify(dataSource1).registerLongOperationListener(presenter);

		verify(dataSource1).loadData(anyBoolean(), presenter);

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

		verify(dataSource1).loadData(anyBoolean(), presenter);

		presenter.setDataSource(null);

		verify(dataSource2).unregisterItemAddedListener(presenter);
		verify(dataSource2).unregisterDataModifiedListener(presenter);
		verify(dataSource2).unregisterItemMovedListener(presenter);
		verify(dataSource2).unregisterItemRemovedListener(presenter);
		verify(dataSource2).unregisterDataReplacedListener(presenter);
		verify(dataSource2).unregisterItemModifiedListener(presenter);
		verify(dataSource2).unregisterLongOperationListener(presenter);
	}

	@Test
	public void testSetDataSource_withView() {
		final View view = mock(View.class);
		presenter.setView(view);

		final ArrayList<LibraryItem> data1 = new ArrayList<>();
		final ListDataSource<LibraryItem> dataSource1 = createNewDataSource(data1);

		final ArrayList<LibraryItem> data2 = new ArrayList<>();
		final ListDataSource<LibraryItem> dataSource2 = createNewDataSource(data2);

		presenter.setDataSource(dataSource1);
		verify(view).setItems(data1);

		presenter.setDataSource(dataSource2);
		verify(view).setItems(data2);

		presenter.setDataSource(dataSource2);
		verify(view).setItems(null);
	}

	@Test
	public void testSetView_withoutDataSource() {
		final View view1 = mock(View.class);
		final View view2 = mock(View.class);

		presenter.setView(view1);

		verify(view1).setPresenter(presenter);

		presenter.setView(view2);

		verify(view1).setPresenter(null);
		verify(view2).setPresenter(presenter);

		presenter.setView(null);

		verify(view2).setPresenter(null);
	}

	@Test
	public void testSetView_withDataSource() {
		final ArrayList<LibraryItem> data = new ArrayList<>();
		final ListDataSource<LibraryItem> dataSource = createNewDataSource(data);
		presenter.setDataSource(dataSource);

		final View view1 = mock(View.class);
		final View view2 = mock(View.class);

		presenter.setView(view1);

		verify(view1).setPresenter(presenter);
		verify(view1).setItems(null);

		presenter.setView(view2);

		verify(view1).setPresenter(null);
		verify(view2).setPresenter(presenter);
		verify(view2).setItems(data);

		presenter.setView(null);

		verify(view2).setPresenter(null);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onDataLoaded(BaseDataSource, List)} method
	 * functions correctly when the presenter does not have a view. The test will only pass if the
	 * method exits normally.
	 */
	@Test
	@SuppressWarnings("unchecked") // This is fine since it's a mock
	public void testOnDataLoaded_withoutView() {
		final List<LibraryItem> data = new ArrayList<>();
		final ListDataSource<LibraryItem> dataSource = createNewDataSource(data);
		presenter.setDataSource(dataSource);

		presenter.onDataLoaded(dataSource, data);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onDataLoaded(BaseDataSource, List)} method
	 * functions correctly when the presenter has a view. The test will only pass if the loaded data
	 * is passed to the view.
	 */
	@Test
	@SuppressWarnings("unchecked") // This is fine since it's a mock
	public void testOnDataLoaded_withView() {
		final List<LibraryItem> data = new ArrayList<>();
		final ListDataSource<LibraryItem> dataSource = createNewDataSource(data);
		presenter.setDataSource(dataSource);

		final View view = mock(View.class);
		presenter.setView(view);

		verify(view, times(1)).setItems(data);

		presenter.onDataLoaded(dataSource, data);

		verify(view, times(2)).setItems(data);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onLoadDataFailed(BaseDataSource)} method
	 * functions correctly when the presenter does not have a view. The test will only pass if the
	 * method exits normally.
	 */
	@Test
	@SuppressWarnings("unchecked") // This is fine since it's a mock
	public void testOnLoadDataFailed_withoutView() {
		final List<LibraryItem> data = new ArrayList<>();
		final ListDataSource<LibraryItem> dataSource = createNewDataSource(data);
		presenter.setDataSource(dataSource);

		presenter.onLoadDataFailed(dataSource);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onLoadDataFailed(BaseDataSource)} method
	 * functions correctly when the presenter has a view. The test will only pass if the view is
	 * updated to show no items.
	 */
	@Test
	@SuppressWarnings("unchecked") // This is fine since it's a mock
	public void testOnLoadDataFailed_withView() {
		final List<LibraryItem> data = new ArrayList<>();
		final ListDataSource<LibraryItem> dataSource = createNewDataSource(data);
		presenter.setDataSource(dataSource);

		final View view = mock(View.class);
		presenter.setView(view);

		verify(view, never()).setItems(null);

		presenter.onLoadDataFailed(dataSource);

		verify(view, times(1)).setItems(null);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onDataModified(BaseDataSource, List)}
	 * method functions correctly when the presenter does not have a view. The test will only pass
	 * if the method exits normally.
	 */
	@Test
	@SuppressWarnings("unchecked") // This is fine since it's a mock
	public void testOnDataModified_withoutView() {
		final List<LibraryItem> data = new ArrayList<>();
		final ListDataSource<LibraryItem> dataSource = createNewDataSource(data);
		presenter.setDataSource(dataSource);

		presenter.onDataModified(dataSource, data);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onDataModified(BaseDataSource, List)}
	 * method functions properly when the presenter has a view. The test will only pass if the view
	 * is notified of the event.
	 */
	@Test
	@SuppressWarnings("unchecked") // This is fine since it's a mock
	public void testOnDataModified_withView() {
		final List<LibraryItem> data = new ArrayList<>();
		final ListDataSource<LibraryItem> dataSource = createNewDataSource(data);
		presenter.setDataSource(dataSource);

		final View view = mock(View.class);
		presenter.setView(view);

		verify(view, never()).notifyItemsChanged();

		presenter.onDataModified(dataSource, data);

		verify(view, times(1)).notifyItemsChanged();
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onDataMoved(ListDataSource, LibraryItem,
	 * int, int)} method functions correctly when the presenter does not have a view. The test will
	 * only pass if the method exits normally.
	 */
	@Test
	public void testOnDataMoved_withoutView() {
		final List<LibraryItem> data = new ArrayList<>();
		final ListDataSource<LibraryItem> dataSource = createNewDataSource(data);
		presenter.setDataSource(dataSource);

		presenter.onDataMoved(dataSource, mock(LibraryItem.class), 1, 2);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onDataMoved(ListDataSource, LibraryItem,
	 * int, int)} method functions properly when the presenter has a view. The test will only pass
	 * if the view is notified of the event.
	 */
	@Test
	public void testOnDataMoved_withView() {
		final List<LibraryItem> data = new ArrayList<>();
		final ListDataSource<LibraryItem> dataSource = createNewDataSource(data);
		presenter.setDataSource(dataSource);

		final View view = mock(View.class);
		presenter.setView(view);

		verify(view, never()).notifyItemMoved(anyInt(), anyInt());

		presenter.onDataMoved(dataSource, mock(LibraryItem.class), 1, 2);

		verify(view, never()).notifyItemMoved(1, 2);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onDataAdded(ListDataSource, LibraryItem,
	 * int)} method functions correctly when the presenter does not have a view. The test will only
	 * pass if the method exits normally.
	 */
	@Test
	public void testOnDataAdded_withoutView() {
		final List<LibraryItem> data = new ArrayList<>();
		final ListDataSource<LibraryItem> dataSource = createNewDataSource(data);
		presenter.setDataSource(dataSource);

		presenter.onDataAdded(dataSource, mock(LibraryItem.class), 0);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onDataAdded(ListDataSource, LibraryItem,
	 * int)} method functions properly when the presenter has a view. The test will only pass if the
	 * view is notified of the event.
	 */
	@Test
	public void testOnDataAdded_withView() {
		final List<LibraryItem> data = new ArrayList<>();
		final ListDataSource<LibraryItem> dataSource = createNewDataSource(data);
		presenter.setDataSource(dataSource);

		final View view = mock(View.class);
		presenter.setView(view);

		verify(view, never()).notifyItemAdded(anyInt());

		presenter.onDataAdded(dataSource, mock(LibraryItem.class), 1);

		verify(view, never()).notifyItemAdded(1);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onDataRemoved(ListDataSource, LibraryItem,
	 * int)} method functions correctly when the presenter does not have a view. The test will only
	 * pass if the method exits normally.
	 */
	@Test
	public void testOnDataRemoved_withoutView() {
		final List<LibraryItem> data = new ArrayList<>();
		final ListDataSource<LibraryItem> dataSource = createNewDataSource(data);
		presenter.setDataSource(dataSource);

		presenter.onDataRemoved(dataSource, mock(LibraryItem.class), 0);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onDataRemoved(ListDataSource, LibraryItem,
	 * int)} method functions properly when the presenter has a view. The test will only pass if the
	 * view is notified of the event.
	 */
	@Test
	public void testOnDataRemoved_withView() {
		final List<LibraryItem> data = new ArrayList<>();
		final ListDataSource<LibraryItem> dataSource = createNewDataSource(data);
		presenter.setDataSource(dataSource);

		final View view = mock(View.class);
		presenter.setView(view);

		verify(view, never()).notifyItemRemoved(anyInt());

		presenter.onDataAdded(dataSource, mock(LibraryItem.class), 1);

		verify(view, never()).notifyItemRemoved(1);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onDataReplaced(BaseDataSource, List,
	 * List)} method functions correctly when the presenter does not have a view. The test will only
	 * pass if the method exits normally.
	 */
	@Test
	@SuppressWarnings("unchecked") // This is fine since it's a mock
	public void testOnDataReplaced_withoutView() {
		final List<LibraryItem> data = new ArrayList<>();
		final ListDataSource<LibraryItem> dataSource = createNewDataSource(data);
		presenter.setDataSource(dataSource);

		presenter.onDataReplaced(dataSource, mock(List.class), mock(List.class));
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onDataReplaced(BaseDataSource, List,
	 * List)} method functions properly when the presenter has a view. The test will only pass if
	 * the view is updated with the new data.
	 */
	@Test
	@SuppressWarnings("unchecked") // This is fine since it's a mock
	public void testOnDataReplaced_withView() {
		final List<LibraryItem> originalData = new ArrayList<>();
		final SettableListDataSource dataSource = createNewDataSource(originalData);
		presenter.setDataSource(dataSource);

		final View view = mock(View.class);
		presenter.setView(view);

		final ArrayList<LibraryItem> newData = new ArrayList<>();
		dataSource.setData(newData);
		presenter.onDataReplaced(dataSource, originalData, newData);

		verify(view).setItems(newData);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onItemModified(ListDataSource,
	 * LibraryItem, int)} method functions correctly when the presenter does not have a view. The
	 * test will only pass if the method exits normally.
	 */
	@Test
	public void testOnListItemModified_withoutView() {
		final List<LibraryItem> data = new ArrayList<>();
		final ListDataSource<LibraryItem> dataSource = createNewDataSource(data);
		presenter.setDataSource(dataSource);

		presenter.onItemModified(dataSource, mock(LibraryItem.class), 1);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onItemModified(ListDataSource,
	 * LibraryItem, int)} method functions properly when the presenter has a view. The test will
	 * only pass if the view is notified of the event.
	 */
	@Test
	public void testOnListItemModified_withView() {
		final List<LibraryItem> data = new ArrayList<>();
		final ListDataSource<LibraryItem> dataSource = createNewDataSource(data);
		presenter.setDataSource(dataSource);

		final View view = mock(View.class);
		presenter.setView(view);

		verify(view, never()).notifyItemModified(anyInt());

		presenter.onItemModified(dataSource, mock(LibraryItem.class), 1);

		verify(view, never()).notifyItemModified(1);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onLongOperationStarted(BaseDataSource)}
	 * method functions correctly when the presenter does not have a view. The test will only pass
	 * if the method exits normally.
	 */
	@Test
	public void testOnLongOperationStarted_withoutView() {
		final List<LibraryItem> data = new ArrayList<>();
		final ListDataSource<LibraryItem> dataSource = createNewDataSource(data);
		presenter.setDataSource(dataSource);

		presenter.onLongOperationStarted(dataSource);
	}

	/**
	 * Test to verify that the {@link DirectBodyPresenter#onLongOperationStarted(BaseDataSource)}
	 * method functions properly when the presenter has a view. The test will only pass if the view
	 * is notified of the event.
	 */
	@Test
	public void testOnLongOperationStarted_withView() {
		final List<LibraryItem> data = new ArrayList<>();
		final ListDataSource<LibraryItem> dataSource = createNewDataSource(data);
		presenter.setDataSource(dataSource);

		final View view = mock(View.class);
		presenter.setView(view);

		verify(view, never()).showLoadingIndicator(true);

		presenter.onLongOperationStarted(dataSource);

		verify(view).showLoadingIndicator(true);
		verify(view, never()).showLoadingIndicator(false);
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

	private SettableListDataSource createNewDataSource(final List<LibraryItem> items) {
		final SettableListDataSource dataSource = new SettableListDataSource();
		dataSource.setData(items);
		return dataSource;
	}
}

class SettableListDataSource extends ListDataSourceHelper<LibraryItem> {
	private List<LibraryItem> data;

	public void setData(final List<LibraryItem> data) {
		this.data = data;
	}

	@Override
	public void loadData(final boolean forceRefresh,
			final DataLoadedListener<List<LibraryItem>> callback) {
		callback.onDataLoaded(this, data);
	}
}