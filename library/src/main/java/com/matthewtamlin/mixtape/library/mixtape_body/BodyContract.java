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

package com.matthewtamlin.mixtape.library.mixtape_body;

import android.view.MenuItem;

import com.matthewtamlin.mixtape.library.base_mvp.BasePresenter;
import com.matthewtamlin.mixtape.library.base_mvp.BaseView;
import com.matthewtamlin.mixtape.library.base_mvp.ListDataSource;
import com.matthewtamlin.mixtape.library.data.LibraryItem;

import java.util.List;

/**
 * The MVP contract for a view which presents a list of LibraryItems to the user.
 */
public interface BodyContract {
	/**
	 * A view which displays a list of LibraryItems to the user. The view shows a contextual menu
	 * button for each item so that the user can perform item specific actions.
	 * <p>
	 * If the list of items is modified externally, then the view must be notified of the change.
	 * Failure to do so may result in display errors, or even a complete failure of the view.
	 */
	public interface View extends BaseView<Presenter> {
		/**
		 * Sets the items to display and updates the UI. The view must be notified of any external
		 * changes to the supplied list. Supplying null is equivalent to supplying an empty list.
		 *
		 * @param items
		 * 		the items to display
		 */
		void setItems(List<? extends LibraryItem> items);

		/**
		 * Gets the items currently being displayed in this view. The view must be notified of any
		 * external changes to the returned list.
		 *
		 * @return the items currently displayed in the view, not null
		 */
		List<? extends LibraryItem> getItems();

		/**
		 * Sets the menu resource to use for the item specific contextual menus. There is no
		 * guarantee that the UI will be updated if this method is called while a menu is being
		 * displayed.
		 *
		 * @param contextualMenuResourceId
		 * 		the resource ID of the menu resource to use
		 */
		void setContextualMenuResource(int contextualMenuResourceId);

		/**
		 * @return the resource ID used for the contextual menus, -1 if not yet set
		 */
		int getContextualMenuResource();

		/**
		 * Forces the view to display the item at the specified index.
		 *
		 * @param index
		 * 		the index of the item to show, with respect to the current list
		 */
		void showItem(int index);

		/**
		 * Notifies the view of some undefined change to the current item list. This notification
		 * doesn't provide specific information about the nature of the change and should only be
		 * used if the other notifications methods are not sufficient.
		 */
		void notifyItemsChanged();

		/**
		 * Notifies the view of an addition to the current list.
		 *
		 * @param index
		 * 		the index of the added item
		 */
		void notifyItemAdded(int index);

		/**
		 * Notifies the view of a removal from the current list.
		 *
		 * @param index
		 * 		the index of the removed item
		 */
		void notifyItemRemoved(int index);

		/**
		 * Notifies the view of a change to one of the items in the current list. This method only
		 * needs to be invoked if the change affected the title, subtitle or artwork of the item.
		 * Unlike the other notifications, this notification does not signify a change in the
		 * structure of the list, but rather a change in the data represented by the contents.
		 *
		 * @param index
		 * 		the index of the changed item
		 */
		void notifyItemModified(int index);

		/**
		 * Notifies the view of a structural change to the current list where a single item was
		 * moved to a new index.
		 *
		 * @param initialIndex
		 * 		the index of the item before being moved
		 * @param finalIndex
		 * 		the index of the item after being moved
		 */
		void notifyItemMoved(int initialIndex, int finalIndex);

		/**
		 * Changes the visibility of the loading indicator.
		 *
		 * @param show
		 * 		true to show the indicator, false to hide it
		 */
		void showLoadingIndicator(boolean show);

		/**
		 * @return true if the loading indicator is currently shown, false otherwise
		 */
		boolean loadingIndicatorIsShown();

		/**
		 * Receives callbacks from a BodyContract.View.
		 */
		interface Listener {
			/**
			 * Invoked when the user selects a LibraryItem in a BodyContract.View.
			 *
			 * @param hostView
			 * 		the BodyContract.View hosting the selected LibraryItem item, not null
			 * @param item
			 * 		the selected LibraryItem, not null
			 */
			void onLibraryItemSelected(BodyContract.View hostView, LibraryItem item);

			/**
			 * Invoked when the user selects an option from an item specific contextual menu in a
			 * BodyContract.View.
			 *
			 * @param hostView
			 * 		the BodyContract.View hosting the menu, not null
			 * @param libraryItem
			 * 		the LibraryItem targeted by the contextual menu, not null
			 * @param menuItem
			 * 		the selected menu option, not null
			 */
			void onContextualMenuItemSelected(BodyContract.View hostView, LibraryItem libraryItem,
					MenuItem menuItem);
		}
	}

	/**
	 * The intermediary between a ListDataSource and a BodyContract.View. The presenter contains the
	 * business logic for updating the data source, processing data source callbacks, driving the
	 * view, and processing view callbacks.
	 *
	 * @param <D>
	 * 		the type of data to present
	 * @param <S>
	 * 		the type of data source to present from
	 * @param <V>
	 * 		the type of view to present to
	 */
	public interface Presenter<
			D extends LibraryItem,
			S extends ListDataSource<D>,
			V extends View>
			extends
			BasePresenter<S, V>,
			ListDataSource.FullListener<D>,
			View.Listener {}
}