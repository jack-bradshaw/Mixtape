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

package com.matthewtamlin.mixtape.library.base_mvp;

/**
 * The intermediary between a data source and a view. Presenters contain the business logic for
 * updating the data source, processing data modified events, driving the view, and processing user
 * input events.
 */
public interface BasePresenter<S extends BaseDataSource, V extends BaseView> {
	/**
	 * Starts/restarts presentation by loading data from source and the updating the view. Calling
	 * this method is safe at all times, however there may not always an effect.
	 *
	 * @param forceRefresh
	 * 		true to discard cached data when loading from the source, false to use the default
	 * 		behaviour
	 */
	void present(boolean forceRefresh);

	/**
	 * Sets the data source and registers this presenter for callbacks. Passing null removes any
	 * existing data source without setting a new one.
	 *
	 * @param dataSource
	 * 		the data source to use
	 */
	void setDataSource(S dataSource);

	/**
	 * @return the current data source, null if there is none
	 */
	S getDataSource();

	/**
	 * Sets the view and registers this presenter. Passing null removes any existing view without
	 * setting a new one.
	 *
	 * @param view
	 * 		the view to use
	 */
	void setView(V view);

	/**
	 * @return the current view, may be null
	 */
	V getView();
}