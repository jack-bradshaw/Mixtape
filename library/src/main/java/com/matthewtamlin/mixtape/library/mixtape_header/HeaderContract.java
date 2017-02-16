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

package com.matthewtamlin.mixtape.library.mixtape_header;

import com.matthewtamlin.mixtape.library.base_mvp.BaseDataSource;
import com.matthewtamlin.mixtape.library.base_mvp.BasePresenter;
import com.matthewtamlin.mixtape.library.base_mvp.BaseView;
import com.matthewtamlin.mixtape.library.data.LibraryItem;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * The MVP contract for a view which presents a single LibraryItem to the user.
 */
public interface HeaderContract {
	/**
	 * A view which displays the title, subtitle and artwork of a single LibraryItem to the user, in
	 * a way which highlights the primary library item in the context.
	 */
	public interface View extends BaseView<Presenter> {
		/**
		 * Displays the title, subtitle and artwork of the provided item. Passing null clears the
		 * view.
		 *
		 * @param item
		 * 		the item to display
		 */
		void setItem(LibraryItem item);

		/**
		 * @return the item currently being displayed, may be null
		 */
		LibraryItem getItem();
	}

	/**
	 * Acts as the intermediary between a data source and a HeaderView.View, and contains the
	 * business logic needed to update the data and drive the view. Must always be subscribed to
	 * callback events from the data source and the view (if possible).
	 */
	public interface Presenter<
			D extends LibraryItem,
			S extends BaseDataSource<D>,
			V extends View>
			extends
			BasePresenter<S, V>, BaseDataSource.FullListener<D> {}
}