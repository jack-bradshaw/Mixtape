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

import com.matthewtamlin.mixtape.library.base_mvp.BaseDataSource;
import com.matthewtamlin.mixtape.library.data.BaseDataSourceHelper;
import com.matthewtamlin.mixtape.library.data.LibraryItem;

/**
 * A BaseDataSource where the data can be set. Whenever {@link #loadData(boolean,
 * BaseDataSource.DataLoadedListener)} is called, the current data determines which callback is
 * delivered. If the data is not-null then the data loaded callback is delivered, and if the data is
 * null then the data load failed callback is delivered.
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