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

package com.matthewtamlin.mixtape.library_tests.data.mixtape_header;


import android.support.test.runner.AndroidJUnit4;

import com.matthewtamlin.mixtape.library.base_mvp.BaseDataSource;
import com.matthewtamlin.mixtape.library.data.BaseDataSourceHelper;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.mixtape_header.DirectHeaderPresenter;
import com.matthewtamlin.mixtape.library.mixtape_header.HeaderContract.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the {@link DirectHeaderPresenter} class.
 */
@SuppressWarnings("unchecked") // Warning caused by mocks, but it isn't a problem
@RunWith(AndroidJUnit4.class)
public class TestDirectHeaderPresenter {
	/**
	 * The presenter under test.
	 */
	private DirectHeaderPresenter<LibraryItem, BaseDataSource<LibraryItem>, View> presenter;

	/**
	 * Initialises the testing objects and assigns them to member variables.
	 */
	@Before
	public void setup() {
		presenter = new DirectHeaderPresenter<>();
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter#setDataSource(BaseDataSource)} method
	 * functions correctly when the presenter does not have a view. The test will only pass if the
	 * presenter is registered and unregisters for callbacks, and if the correct calls are made to
	 * load data.
	 */
	@Test
	public void testSetDataSource_withoutView() {
		final BaseDataSource<LibraryItem> dataSource1 = mock(BaseDataSource.class);
		final BaseDataSource<LibraryItem> dataSource2 = mock(BaseDataSource.class);

		presenter.setDataSource(dataSource1);

		verify(dataSource1).registerDataReplacedListener(presenter);
		verify(dataSource1).registerDataModifiedListener(presenter);
		verify(dataSource1).registerLongOperationListener(presenter);

		verify(dataSource1).loadData(anyBoolean(), eq(presenter));

		presenter.setDataSource(dataSource2);

		verify(dataSource1).unregisterDataReplacedListener(presenter);
		verify(dataSource1).unregisterDataModifiedListener(presenter);
		verify(dataSource1).unregisterLongOperationListener(presenter);

		verify(dataSource2).registerDataReplacedListener(presenter);
		verify(dataSource2).registerLongOperationListener(presenter);
		verify(dataSource2).registerDataModifiedListener(presenter);

		verify(dataSource1).loadData(anyBoolean(), eq(presenter));

		presenter.setDataSource(null);

		verify(dataSource2).unregisterDataReplacedListener(presenter);
		verify(dataSource2).unregisterDataModifiedListener(presenter);
		verify(dataSource2).unregisterLongOperationListener(presenter);
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter#setDataSource(BaseDataSource)} method
	 * functions correctly when the presenter has a view. The test will only pass if the view is
	 * updated with the new data when the data source is changed.
	 */
	@Test
	public void testSetDataSource_withView() {
		final View view = mock(View.class);
		presenter.setView(view);

		final LibraryItem data1 = mock(LibraryItem.class);
		final BaseDataSource<LibraryItem> dataSource1 = new SettableBaseDataSource(data1);

		final LibraryItem data2 = mock(LibraryItem.class);
		final BaseDataSource<LibraryItem> dataSource2 = new SettableBaseDataSource(data2);

		presenter.setDataSource(dataSource1);
		verify(view).setItem(data1);

		presenter.setDataSource(dataSource2);
		verify(view, atLeastOnce()).setItem(data2);

		presenter.setDataSource(null);
		verify(view).setItem(null);
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter#setView(View)} method functions
	 * correctly when the presenter does not have a data source. The test will only pass if the
	 * presenter registers/unregisters itself with the views.
	 */
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

	/**
	 * Test to verify that the {@link DirectHeaderPresenter#setView(View)} method functions
	 * correctly when the presenter has a data source. The test will only pass if the presenter
	 * registers/unregisters itself with the views and loads data into the views.
	 */
	@Test
	public void testSetView_withDataSource() {
		final LibraryItem data = mock(LibraryItem.class);
		final BaseDataSource<LibraryItem> dataSource = new SettableBaseDataSource(data);
		presenter.setDataSource(dataSource);

		final View view1 = mock(View.class);
		final View view2 = mock(View.class);

		presenter.setView(view1);

		verify(view1).setPresenter(presenter);
		verify(view1).setItem(data);

		presenter.setView(view2);

		verify(view1).setPresenter(null);
		verify(view2).setPresenter(presenter);
		verify(view2).setItem(data);

		presenter.setView(null);

		verify(view2).setPresenter(null);
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter} class functions correctly when the data
	 * source delivers a data loaded callback and there is no view. The test will only pass if all
	 * methods exit normally.
	 */
	@Test
	public void testOnDataLoaded_withoutView() {
		final LibraryItem data = mock(LibraryItem.class);
		final BaseDataSource<LibraryItem> dataSource = new SettableBaseDataSource(data);
		presenter.setDataSource(dataSource);

		presenter.onDataLoaded(dataSource, data);
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter} class functions correctly when the data
	 * source delivers a data loaded callback and there is a view. The test will only pass if the
	 * data in the view is updated.
	 */
	@Test
	public void testOnDataLoaded_withView() {
		final LibraryItem data = mock(LibraryItem.class);
		final BaseDataSource<LibraryItem> dataSource = new SettableBaseDataSource(data);
		presenter.setDataSource(dataSource);

		final View view = mock(View.class);
		presenter.setView(view);

		verify(view, times(1)).setItem(data);

		presenter.onDataLoaded(dataSource, data);

		verify(view, times(2)).setItem(data);
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter} class functions correctly when the data
	 * source delivers a data load failed callback and there is no view. The test will only pass if
	 * all methods exit normally.
	 */
	@Test
	public void testOnLoadDataFailed_withoutView() {
		final LibraryItem data = mock(LibraryItem.class);
		final BaseDataSource<LibraryItem> dataSource = new SettableBaseDataSource(data);
		presenter.setDataSource(dataSource);

		presenter.onLoadDataFailed(dataSource);
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter} class functions correctly when the data
	 * source delivers a data load failed callback and there is a view. The test will only pass if
	 * the view is updated to display no data.
	 */
	@Test
	public void testOnLoadDataFailed_withView() {
		final LibraryItem data = mock(LibraryItem.class);
		final BaseDataSource<LibraryItem> dataSource = new SettableBaseDataSource(data);
		presenter.setDataSource(dataSource);

		final View view = mock(View.class);
		presenter.setView(view);

		verify(view, never()).setItem(null);

		presenter.onLoadDataFailed(dataSource);

		verify(view, times(1)).setItem(null);
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter} class functions correctly when the data
	 * source delivers a data replaced callback and there is no view. The test will only pass if all
	 * methods exit normally.
	 */
	@Test
	public void testOnDataReplaced_withoutView() {
		final LibraryItem data = mock(LibraryItem.class);
		final BaseDataSource<LibraryItem> dataSource = new SettableBaseDataSource(data);
		presenter.setDataSource(dataSource);

		presenter.onDataReplaced(dataSource, mock(LibraryItem.class), mock(LibraryItem.class));
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter} functions correctly when the data
	 * source delivers a data replaced callback and there is a view. The test will only pass if the
	 * view updated to display the new data.
	 */
	@Test
	public void testOnDataReplaced_withView() {
		final LibraryItem originalData = mock(LibraryItem.class);
		final SettableBaseDataSource dataSource = new SettableBaseDataSource(originalData);
		presenter.setDataSource(dataSource);

		final View view = mock(View.class);
		presenter.setView(view);

		final LibraryItem newData = mock(LibraryItem.class);
		dataSource.setData(newData);
		presenter.onDataReplaced(dataSource, originalData, newData);

		verify(view, atLeastOnce()).setItem(newData);
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter} functions correctly when the data
	 * source delivers a data modified callback and there is no view. The test will only pass if all
	 * methods exit normally.
	 */
	@Test
	public void testOnDataModified_withoutView() {
		final LibraryItem data = mock(LibraryItem.class);
		final BaseDataSource<LibraryItem> dataSource = new SettableBaseDataSource(data);
		presenter.setDataSource(dataSource);

		presenter.onDataModified(dataSource, data);
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter} functions correctly when the data
	 * source delivers a data modified callback and there is a view. The test will only pass if the
	 * view is notified of the event.
	 */
	@Test
	public void testOnDataModified_withView() {
		final LibraryItem data = mock(LibraryItem.class);
		final BaseDataSource<LibraryItem> dataSource = new SettableBaseDataSource(data);
		presenter.setDataSource(dataSource);

		final View view = mock(View.class);
		presenter.setView(view);

		verify(view, never()).notifyItemChanged();

		presenter.onDataModified(dataSource, data);

		verify(view, times(1)).notifyItemChanged();
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter} functions correctly when the data
	 * source delivers a long operation started callback and there is no view. The test will only
	 * pass if all methods exit normally.
	 */
	@Test
	public void testOnLongOperationStarted_withoutView() {
		final LibraryItem data = mock(LibraryItem.class);
		final BaseDataSource<LibraryItem> dataSource = new SettableBaseDataSource(data);
		presenter.setDataSource(dataSource);

		presenter.onLongOperationStarted(dataSource);
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter} functions correctly when the data
	 * source delivers a long operation started callback and there is a view. The test will only
	 * pass if all methods exit normally.
	 */
	@Test
	public void testOnLongOperationStarted_withView() {
		final LibraryItem data = mock(LibraryItem.class);
		final BaseDataSource<LibraryItem> dataSource = new SettableBaseDataSource(data);
		presenter.setDataSource(dataSource);

		final View view = mock(View.class);
		presenter.setView(view);

		presenter.onLongOperationStarted(dataSource);
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter} functions correctly when the data
	 * source delivers a long operation finished callback and there is no view. The test will only
	 * pass if all methods exit normally.
	 */
	@Test
	public void testOnLongOperationFinished_withoutView() {
		final LibraryItem data = mock(LibraryItem.class);
		final BaseDataSource<LibraryItem> dataSource = new SettableBaseDataSource(data);
		presenter.setDataSource(dataSource);

		presenter.onLongOperationFinished(dataSource);
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter} functions correctly when the data
	 * source delivers a long operation finished callback and there is a view. The test will only
	 * pass if all methods exit normally.
	 */
	@Test
	public void testOnLongOperationFinished_withView() {
		final LibraryItem data = mock(LibraryItem.class);
		final BaseDataSource<LibraryItem> dataSource = new SettableBaseDataSource(data);
		presenter.setDataSource(dataSource);

		final View view = mock(View.class);
		presenter.setView(view);

		presenter.onLongOperationFinished(dataSource);
	}
}

/**
 * A BaseDataSource where the data can be set. Whenever {@link #loadData(boolean,
 * DataLoadedListener)} is called, the current data determines which callback is delivered. If the
 * data is not-null then the data loaded callback is delivered, and if the data is null then the
 * data load failed callback is delivered.
 */
class SettableBaseDataSource extends BaseDataSourceHelper<LibraryItem> {
	/**
	 * The current data, may be null.
	 */
	private LibraryItem data;

	/**
	 * Constructs a new SettableBaseDataSource with the supplied data.
	 */
	public SettableBaseDataSource(final LibraryItem data) {
		this.data = data;
	}

	/**
	 * Sets the data to return when {@link #loadData(boolean, DataLoadedListener)} is called.
	 * Calling this method does not trigger {@link DataReplacedListener#onDataReplaced(BaseDataSource,
	 * Object, Object)} callbacks.
	 *
	 * @param data
	 * 		the new data, may be null
	 */
	public void setData(final LibraryItem data) {
		this.data = data;
	}

	@Override
	public void loadData(final boolean forceRefresh,
			final DataLoadedListener<LibraryItem> callback) {
		if (data == null) {
			callback.onLoadDataFailed(this);
		} else {
			callback.onDataLoaded(this, data);
		}
	}
}