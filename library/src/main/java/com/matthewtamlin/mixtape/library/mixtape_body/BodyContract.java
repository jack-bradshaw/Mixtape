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
 * The contract between the model, view and presenter for the body.
 */
public interface BodyContract {
	/**
	 * Displays a series of LibraryItems to the user. Each item is displayed in a distinct UI
	 * element, so that the user can interact with each item individually. Each UI element can show
	 * the title, subtitle and artwork of a LibraryItem, however there is no guarantee that all
	 * three will be shown. Additionally, each UI element must have a button which displays a
	 * contextual menu when clicked. Scrolling behaviour is used if there are too many elements to
	 * fit on the screen simultaneously.
	 * <p>
	 * If the list of items is modified externally, then the view must be notified using one of the
	 * notify methods. Failure to do so may result in incorrect data bring displayed, or even
	 * complete failure of the view.
	 */
	public interface View extends BaseView<Presenter> {
		/**
		 * Sets the items to display and updates the UI. To display nothing, pass an empty list.
		 *
		 * @param items
		 * 		the items to display, not null
		 */
		void setItems(List<? extends LibraryItem> items);

		/**
		 * Returns the items currently displayed in this view. The view must be notified of any
		 * external changes to the returned list.
		 *
		 * @return the items displayed in the view, not null
		 */
		List<? extends LibraryItem> getItems();

		/**
		 * Sets the menu resource to use for the contextual menu on each UI element. This method
		 * will not update any menus which are currently being displayed.
		 *
		 * @param contextualMenuResourceId
		 * 		the resource ID to use for the contextual menus
		 */
		void setContextualMenuResource(int contextualMenuResourceId);

		/**
		 * Returns the resource ID of the current contextual menu. If no resource has been set, then
		 * -1 is returned.
		 *
		 * @return the resource ID of the contextual menu resource
		 */
		int getContextualMenuResource();

		/**
		 * Scrolls the view to show a particular item.
		 *
		 * @param itemIndex
		 * 		the index of the item to show
		 */
		void showItem(int itemIndex);

		/**
		 * Notifies the view of some undefined change to the current item list. This notification
		 * doesn't provide specific information about the nature of the change, therefore {@link
		 * #notifyItemAdded(int)}, {@link #notifyItemRemoved(int)}, {@link #notifyItemModified(int)}
		 * and/or {@link #notifyItemMoved(int, int)} should be used if possible.
		 */
		void notifyItemsChanged();

		/**
		 * Notifies the view of an addition to the current item list.
		 *
		 * @param index
		 * 		the index of the added item
		 */
		void notifyItemAdded(int index);

		/**
		 * Notifies the view of a removal from the current item list.
		 *
		 * @param index
		 * 		the index of the removed item
		 */
		void notifyItemRemoved(int index);

		/**
		 * Notifies the view of a change to one of the items in the item list. Unlike the other
		 * notifications, this notification does not signify a change in the structure of the list,
		 * but rather a change in the data represented by the contents.
		 *
		 * @param index
		 * 		the index of the changed item
		 */
		void notifyItemModified(int index);

		/**
		 * Notifies the view of an item being moved within the item list.
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
		 * Can be registered to receive user input callbacks from BodyContract.Views.
		 */
		interface Listener {
			/**
			 * Invoked to indicate that the user has clicked on a BodyContract.View element.
			 *
			 * @param hostView
			 * 		the BodyContract.View hosting the clicked element, not the actual view which was
			 * 		clicked, not null
			 * @param item
			 * 		the data item associated with the clicked view, not null
			 */
			void onItemClicked(BodyContract.View hostView, LibraryItem item);

			/**
			 * Invoked to indicate that the user has selected a contextual menu item in a
			 * BodyContract.View.
			 *
			 * @param hostView
			 * 		the BodyContract.View hosting the menu, not the actual view which was clicked, not
			 * 		null
			 * @param libraryItem
			 * 		the data item associated with the contextual menu, not null
			 * @param menuItem
			 * 		the menu item which was clicked, not null
			 */
			void onContextualMenuItemClicked(BodyContract.View hostView, LibraryItem libraryItem,
					MenuItem menuItem);
		}
	}

	/**
	 * Acts as the intermediary between a ListDataSource and a BodyContract.View, and contains the
	 * business logic needed to update the data and drive the view. Must always be subscribed to
	 * callback events from the data source and the view (if available).
	 *
	 * @param <S>
	 * 		the type of data returned by the data source
	 * @param <V>
	 * 		the type of view
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