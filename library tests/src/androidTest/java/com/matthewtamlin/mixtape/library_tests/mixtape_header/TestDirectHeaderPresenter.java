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
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;
import com.matthewtamlin.mixtape.library.mixtape_header.DirectHeaderPresenter;
import com.matthewtamlin.mixtape.library.mixtape_header.HeaderContract;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
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
public abstract class TestDirectHeaderPresenter<S extends BaseDataSource<LibraryItem>, V extends
		HeaderContract.View> {
	/**
	 * A data source for use in testing, supplied by the subclass.
	 */
	private S dataSource;

	/**
	 * A view for use in testing, supplied by the subclass.
	 */
	private V view;

	/**
	 * A presenter which has a data source but no view, supplied by the subclass then customised.
	 */
	private DirectHeaderPresenter<S, V> presenterWithDataSourceOnly;

	/**
	 * A presenter which has a view but no data source, supplied by the subclass then customised.
	 */
	private DirectHeaderPresenter<S, V> presenterWithViewOnly;

	/**
	 * A presenter which has a view and a data source, supplied by the subclass then customised.
	 */
	private DirectHeaderPresenter<S, V> presenterWithDataSourceAndView;

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

		assertThat("Precondition failed. presenterWithViewOnly is null",
				presenterWithViewOnly, is(notNullValue()));
		assertThat("Precondition failed. presenterWithViewOnly has a data source",
				presenterWithViewOnly.getDataSource(), is(nullValue()));
		assertThat("Precondition failed. presenterWithViewOnly has no view",
				presenterWithViewOnly.getView(), is(view));

		assertThat("Precondition failed. presenterWithDataSourceAndView is null",
				presenterWithDataSourceAndView, is(notNullValue()));
		assertThat("Precondition failed. presenterWithDataSourceAndView has no data source",
				presenterWithDataSourceAndView.getDataSource(), is(dataSource));
		assertThat("Precondition failed. presenterWithDataSourceAndView has no view",
				presenterWithDataSourceAndView.getView(), is(view));
	}

	/**
	 * @return a new mock data source, not null
	 */
	protected abstract S createMockDataSource();

	/**
	 * @return a new mock view, not null
	 */
	protected abstract V createMockView();

	/**
	 * @return a new presenter, not null
	 */
	protected abstract DirectHeaderPresenter<S, V> createPresenter();

	/**
	 * Test to verify that the {@link DirectHeaderPresenter#present(boolean)} method functions
	 * correctly when the presenter does not have a data source. The test will only pass if the
	 * method exits normally.
	 */
	@Test
	public void testPresent_withoutDataSource() {
		presenterWithViewOnly.present(true);
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter#present(boolean)} method functions
	 * correctly when the presenter does not have a view. The test will only pass if the method
	 * exits normally.
	 */
	public void testPresent_withoutView() {
		presenterWithDataSourceOnly.present(true);

		verify(dataSource).loadData(true, any(BaseDataSource.DataLoadedListener.class));
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter#present(boolean)} method functions
	 * correctly when the presenter has a data source and a view. The test will only pass if the
	 * method loads data from the data source, and sets the data to display in the view.
	 */
	@Test
	@SuppressWarnings("unchecked") // This is fine since it's a mock
	public void testPresent_withDataSourceAndView() {
		presenterWithDataSourceAndView.present(true);

		verify(dataSource).loadData(eq(true), any(BaseDataSource.DataLoadedListener.class));
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter#setDataSource(BaseDataSource)} method
	 * functions correctly when the presenter does not already have a data source. The test will
	 * only pass if the method registers for all relevant callbacks from the new data source.
	 */
	@Test
	public void testSetDataSource_withoutDataSource() {
		final S newDataSource = createMockDataSource();

		presenterWithViewOnly.setDataSource(newDataSource);

		verify(newDataSource).registerDataModifiedListener(presenterWithViewOnly);
		verify(newDataSource).registerDataReplacedListener(presenterWithViewOnly);
		verify(newDataSource).registerLongOperationListener(presenterWithViewOnly);
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter#setDataSource(BaseDataSource)} method
	 * functions correctly when the presenter already has a data source. The test will only pass if
	 * the method registers the new data source for all relevant callbacks, and unregisters the old
	 * data source from all callbacks.
	 */
	@Test
	public void testSetDataSource_withDataSource() {
		final S newDataSource = createMockDataSource();

		presenterWithDataSourceOnly.setDataSource(newDataSource);

		verify(dataSource).unregisterDataModifiedListener(presenterWithDataSourceOnly);
		verify(dataSource).unregisterDataReplacedListener(presenterWithDataSourceOnly);
		verify(dataSource).unregisterLongOperationListener(presenterWithDataSourceOnly);

		verify(newDataSource).registerDataModifiedListener(presenterWithDataSourceOnly);
		verify(newDataSource).registerDataReplacedListener(presenterWithDataSourceOnly);
		verify(newDataSource).registerLongOperationListener(presenterWithDataSourceOnly);
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter#setView(HeaderContract.View)} method
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
	 * Test to verify that the {@link DirectHeaderPresenter#setView(HeaderContract.View)} method
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
	 * Test to verify that the {@link DirectHeaderPresenter#onDataLoaded(BaseDataSource,
	 * LibraryItem)} method functions correctly when the presenter does not have a view. The test
	 * will only pass if the method exits normally.
	 */
	@Test
	public void testOnDataLoaded_withoutView() {
		presenterWithDataSourceOnly.onDataLoaded(dataSource, mock(LibraryItem.class));
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter#onDataLoaded(BaseDataSource,
	 * LibraryItem)} method functions correctly when the presenter has a view. The test will only
	 * pass if the loaded data is passed to the view.
	 */
	@Test
	public void testOnDataLoaded_withView() {
		final LibraryItem item = mock(LibraryItem.class);

		presenterWithDataSourceAndView.onDataLoaded(dataSource, item);

		verify(view).setItem(item);
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter#onLoadDataFailed(BaseDataSource)} method
	 * functions correctly when the presenter does not have a view. The test will only pass if the
	 * method exits normally.
	 */
	@Test
	public void testOnLoadDataFailed_withoutView() {
		presenterWithDataSourceOnly.onLoadDataFailed(dataSource);
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter#onLoadDataFailed(BaseDataSource)} method
	 * functions correctly when the presenter has a view. The test will only pass if the view is
	 * updated to show no items.
	 *
	 * @throws LibraryReadException
	 * 		may be thrown during testing, resulting in failure
	 */
	@Test
	public void testOnLoadDataFailed_withView() throws LibraryReadException {
		presenterWithDataSourceAndView.onLoadDataFailed(dataSource);

		final ArgumentCaptor<LibraryItem> captor = ArgumentCaptor.forClass(LibraryItem.class);
		verify(view).setItem(captor.capture());
		assertThat(captor.getValue().getTitle(), is(nullValue()));
		assertThat(captor.getValue().getSubtitle(), is(nullValue()));
		assertThat(captor.getValue().getArtwork(anyInt(), anyInt()), is(nullValue()));
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter#onDataModified(BaseDataSource,
	 * LibraryItem)} method functions correctly when the presenter does not have a view. The test
	 * will only pass if the method exits normally.
	 */
	@Test
	public void testOnDataModified_withoutView() {
		presenterWithDataSourceOnly.onDataModified(dataSource, mock(LibraryItem.class));
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter#onDataModified(BaseDataSource,
	 * LibraryItem)} method functions properly when the presenter has a view. The test will only
	 * pass if the view is notified of the event.
	 */
	@Test
	public void testOnDataModified_withView() {
		final LibraryItem newData = mock(LibraryItem.class);

		presenterWithDataSourceAndView.onDataModified(dataSource, newData);

		verify(view).setItem(newData);
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter#onDataReplaced(BaseDataSource,
	 * LibraryItem, LibraryItem)} method functions correctly when the presenter does not have a
	 * view. The test will only pass if the method exits normally.
	 */
	@Test
	public void testOnDataReplaced_withoutView() {
		presenterWithDataSourceOnly.onDataReplaced(dataSource, mock(LibraryItem.class), mock
				(LibraryItem.class));
	}

	/**
	 * Test to verify that the {@link DirectHeaderPresenter#onDataReplaced(BaseDataSource,
	 * LibraryItem, LibraryItem)} method functions properly when the presenter has a view. The test
	 * will only pass if the view is updated with the new data.
	 */
	@Test
	public void testOnDataReplaced_withView() {
		final LibraryItem newData = mock(LibraryItem.class);

		presenterWithDataSourceAndView.onDataReplaced(dataSource, mock(LibraryItem.class), newData);

		verify(view).setItem(newData);
	}
}