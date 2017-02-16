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

import com.matthewtamlin.mixtape.library.base_mvp.BaseDataSource.DataModifiedListener;
import com.matthewtamlin.mixtape.library.base_mvp.BaseDataSource.DataReplacedListener;
import com.matthewtamlin.mixtape.library.base_mvp.BaseDataSource.LongOperationListener;
import com.matthewtamlin.mixtape.library.data.BaseDataSourceHelper;
import com.matthewtamlin.mixtape.library.data.LibraryItem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the {@link BaseDataSourceHelper} class. Subclasses which extend the tests must be
 * ready for calls to {@link #createNewDataSource()} before the superclass {@link #setup()} method
 * is called.
 */
@SuppressWarnings("unchecked") // Warning caused by mocks, but it isn't a problem
@RunWith(JUnit4.class)
public class TestBaseDataSourceHelper {
	/**
	 * The data source under test.
	 */
	private BaseDataSourceHelper<LibraryItem> dataSource;

	/**
	 * Initialises the testing objects and assigns them to member variables.
	 */
	@Before
	public void setup() {
		dataSource = createNewDataSource();
		assertThat("createNewDataSource must not return null,", dataSource, is(not(nullValue())));
	}

	/**
	 * Test to verify that the {@link BaseDataSourceHelper#registerDataReplacedListener(DataReplacedListener)}
	 * method functions correctly. The test will only pass if the non-null listeners are registered
	 * and the null listener is ignored.
	 */
	@Test
	public void testRegisterDataReplacedListener() {
		final DataReplacedListener listener1 = mock(DataReplacedListener.class);
		final DataReplacedListener listener2 = mock(DataReplacedListener.class);

		dataSource.registerDataReplacedListener(listener1);
		dataSource.registerDataReplacedListener(listener2);
		dataSource.registerDataReplacedListener(null);

		assertThat(dataSource.getDataReplacedListeners().size(), is(2));
		assertThat(dataSource.getDataReplacedListeners().contains(null), is(false));
		assertThat(dataSource.getDataReplacedListeners().contains(listener1), is(true));
		assertThat(dataSource.getDataReplacedListeners().contains(listener2), is(true));
	}

	/**
	 * Test to verify that the {@link BaseDataSourceHelper#unregisterDataReplacedListener(DataReplacedListener)}
	 * method functions correctly. The test will only pass if only specific listeners are
	 * unregistered.
	 */
	@Test
	public void testUnregisterDataReplacedListener() {
		final DataReplacedListener listener1 = mock(DataReplacedListener.class);
		final DataReplacedListener listener2 = mock(DataReplacedListener.class);

		dataSource.registerDataReplacedListener(listener1);
		dataSource.registerDataReplacedListener(listener2);
		dataSource.unregisterDataReplacedListener(listener1);
		dataSource.unregisterDataReplacedListener(null);

		assertThat(dataSource.getDataReplacedListeners().size(), is(1));
		assertThat(dataSource.getDataReplacedListeners().contains(null), is(false));
		assertThat(dataSource.getDataReplacedListeners().contains(listener1), is(false));
		assertThat(dataSource.getDataReplacedListeners().contains(listener2), is(true));
	}

	/**
	 * Test to verify that the {@link BaseDataSourceHelper#registerDataModifiedListener(DataModifiedListener)}
	 * method functions correctly. The test will only pass if the non-null listeners are registered
	 * and the null listener is ignored.
	 */
	@Test
	public void testRegisterDataModifiedListener() {
		final DataModifiedListener listener1 = mock(DataModifiedListener.class);
		final DataModifiedListener listener2 = mock(DataModifiedListener.class);

		dataSource.registerDataModifiedListener(listener1);
		dataSource.registerDataModifiedListener(listener2);
		dataSource.registerDataModifiedListener(null);

		assertThat(dataSource.getDataModifiedListeners().size(), is(2));
		assertThat(dataSource.getDataModifiedListeners().contains(null), is(false));
		assertThat(dataSource.getDataModifiedListeners().contains(listener1), is(true));
		assertThat(dataSource.getDataModifiedListeners().contains(listener2), is(true));
	}

	/**
	 * Test to verify that the {@link BaseDataSourceHelper#unregisterDataModifiedListener(DataModifiedListener)}
	 * method functions correctly. The test will only pass if only specific listeners are
	 * unregistered.
	 */
	@Test
	public void testUnregisterDataModifiedListener() {
		final DataModifiedListener listener1 = mock(DataModifiedListener.class);
		final DataModifiedListener listener2 = mock(DataModifiedListener.class);

		dataSource.registerDataModifiedListener(listener1);
		dataSource.registerDataModifiedListener(listener2);
		dataSource.unregisterDataModifiedListener(listener1);
		dataSource.unregisterDataModifiedListener(null);

		assertThat(dataSource.getDataModifiedListeners().size(), is(1));
		assertThat(dataSource.getDataModifiedListeners().contains(null), is(false));
		assertThat(dataSource.getDataModifiedListeners().contains(listener1), is(false));
		assertThat(dataSource.getDataModifiedListeners().contains(listener2), is(true));
	}

	/**
	 * Test to verify that the {@link BaseDataSourceHelper#registerLongOperationListener(LongOperationListener)}
	 * method functions correctly. The test will only pass if the non-null listeners are registered
	 * and the null listener is ignored.
	 */
	@Test
	public void testRegisterLongOperationListener() {
		final LongOperationListener listener1 = mock(LongOperationListener.class);
		final LongOperationListener listener2 = mock(LongOperationListener.class);

		dataSource.registerLongOperationListener(listener1);
		dataSource.registerLongOperationListener(listener2);
		dataSource.registerLongOperationListener(null);

		assertThat(dataSource.getLongOperationListeners().size(), is(2));
		assertThat(dataSource.getLongOperationListeners().contains(null), is(false));
		assertThat(dataSource.getLongOperationListeners().contains(listener1), is(true));
		assertThat(dataSource.getLongOperationListeners().contains(listener2), is(true));
	}

	/**
	 * Test to verify that the {@link BaseDataSourceHelper#unregisterLongOperationListener(LongOperationListener)}
	 * method functions correctly. The test will only pass if only specific listeners are
	 * unregistered.
	 */
	@Test
	public void testUnregisterLongOperationListener() {
		final LongOperationListener listener1 = mock(LongOperationListener.class);
		final LongOperationListener listener2 = mock(LongOperationListener.class);

		dataSource.registerLongOperationListener(listener1);
		dataSource.registerLongOperationListener(listener2);
		dataSource.unregisterLongOperationListener(listener1);
		dataSource.unregisterLongOperationListener(null);

		assertThat(dataSource.getLongOperationListeners().size(), is(1));
		assertThat(dataSource.getLongOperationListeners().contains(null), is(false));
		assertThat(dataSource.getLongOperationListeners().contains(listener1), is(false));
		assertThat(dataSource.getLongOperationListeners().contains(listener2), is(true));
	}

	/**
	 * @return a new BaseDataSourceHelper, not null
	 */
	public BaseDataSourceHelper createNewDataSource() {
		return new BaseDataSourceHelper<LibraryItem>() {
			@Override
			public void loadData(final boolean forceRefresh,
					final DataLoadedListener<LibraryItem> callback) {
				// Do nothing, not needed for test
			}
		};
	}
}