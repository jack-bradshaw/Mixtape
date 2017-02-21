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

import com.matthewtamlin.mixtape.library.data.LibraryItem;

import java.util.List;

/**
 * A view which displays a list of LibraryItems to the user. The view shows a contextual menu button
 * for each item so that the user can perform item specific actions.
 * <p>
 * If the list of items is modified externally, then the view must be notified of the change.
 * Failure to do so may result in display errors, or even a complete failure of the view.
 */
public interface BodyView {
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
	 * Sets the menu resource to use for the item specific contextual menus. There is no guarantee
	 * that the UI will be updated if this method is called while a menu is being displayed.
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
	 * doesn't provide specific information about the nature of the change and should only be used
	 * if the other notifications methods are not sufficient.
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
	 * Notifies the view of a change to one of the items in the current list. This method only needs
	 * to be invoked if the change affected the title, subtitle or artwork of the item. Unlike the
	 * other notifications, this notification does not signify a change in the structure of the
	 * list, but rather a change in the data represented by the contents.
	 *
	 * @param index
	 * 		the index of the changed item
	 */
	void notifyItemModified(int index);

	/**
	 * Notifies the view of a structural change to the current list where a single item was moved to
	 * a new index.
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
	 * Registers the supplied listener for  library item selected callbacks. If the supplied
	 * listener is null or is already registered, then the method exits normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	public void addLibraryItemSelectedListener(LibraryItemSelectedListener listener);

	/**
	 * Unregisters the supplied listener from library item selected callbacks. If the supplied
	 * listener is null or is not registered, then the method exits normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	public void removeLibraryItemSelectedListener(LibraryItemSelectedListener listener);

	/**
	 * Registers the supplied listener for contextual menu item selected callbacks. If the supplied
	 * listener is null or is already registered, then the method exits normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	public void addContextualMenuItemSelectedListener(MenuItemSelectedListener listener);

	/**
	 * Unregisters the supplied listener from contextual menu item selected callbacks. If the
	 * supplied listener is null or is not registered, then the method exits normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	public void removeContextualMenuItemSelectedListener(MenuItemSelectedListener
			listener);

	/**
	 * Callback to be invoked when a library item is selected in a BodyView.
	 */
	public interface LibraryItemSelectedListener {
		/**
		 * Invoked when a LibraryItem is selected in a BodyView.
		 *
		 * @param bodyView
		 * 		the BodyView hosting the selected LibraryItem item, not null
		 * @param item
		 * 		the selected LibraryItem, not null
		 */
		void onLibraryItemSelected(BodyView bodyView, LibraryItem item);
	}

	/**
	 * Callback to be invoked when an item specific contextual menu option is selected in a
	 * BodyView.
	 */
	public interface MenuItemSelectedListener {
		/**
		 * Invoked when an item specific contextual menu option is selected in a BodyView.
		 *
		 * @param bodyView
		 * 		the BodyView hosting the menu, not null
		 * @param libraryItem
		 * 		the LibraryItem the contextual menu is attached to, not null
		 * @param menuItem
		 * 		the selected menu option, not null
		 */
		void onContextualMenuItemSelected(BodyView bodyView, LibraryItem libraryItem,
				MenuItem menuItem);
	}
}