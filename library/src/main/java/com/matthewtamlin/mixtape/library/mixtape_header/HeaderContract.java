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

import android.graphics.Bitmap;
import android.view.MenuItem;

import com.matthewtamlin.mixtape.library.base_mvp.BaseDataSource;
import com.matthewtamlin.mixtape.library.base_mvp.BasePresenter;
import com.matthewtamlin.mixtape.library.base_mvp.BaseView;
import com.matthewtamlin.mixtape.library.data.LibraryItem;

/**
 * The MVP contract for a view which presents a single LibraryItem to the user.
 */
public interface HeaderContract {
	/**
	 * A view which displays the title, subtitle and artwork of a single LibraryItem to the user,
	 * along with an overflow menu button and a series of additional buttons.
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

		/**
		 * Removes any existing extra buttons and creates new ones. For each Bitmap in the supplied
		 * array, a button is created with the Bitmap as the icon. Clicks are relayed to the
		 * presenter using the indexing of the supplied array.
		 *
		 * @param buttonIcons
		 * 		the icons to display in the buttons
		 */
		void setExtraButtons(Bitmap[] buttonIcons);

		/**
		 * Sets the menu resource to use for the overflow menu. There is no guarantee that the UI
		 * will be updated if this method is called while the menu is being displayed.
		 *
		 * @param menuResource
		 * 		the resource ID to use for the overflow menu
		 */
		void setOverflowMenuResource(int menuResource);

		/**
		 * @return the resource ID used for the overflow menus, -1 if not yet set
		 */
		int getOverflowMenuResource();

		/**
		 * Receives callbacks from a HeaderContract.View.
		 */
		interface Listener {
			/**
			 * Invoked when the user clicks the title in a HeaderContract.View.
			 *
			 * @param headerView
			 * 		the HeaderContract.View hosting the clicked title, not null
			 */
			void onTitleClicked(HeaderContract.View headerView);

			/**
			 * Invoked when the user clicks the subtitle in a HeaderContract.View.
			 *
			 * @param headerView
			 * 		the HeaderContract.View hosting the clicked subtitle, not null
			 */
			void onSubtitleClicked(HeaderContract.View headerView);

			/**
			 * Invoked when the user clicks the artwork in a HeaderContract.View.
			 *
			 * @param headerView
			 * 		the HeaderContract.View hosting the clicked artwork, not null
			 */
			void onArtworkClicked(HeaderContract.View headerView);

			/**
			 * Invoked when the user clicks an extra button in a HeaderContract.View.
			 *
			 * @param headerView
			 * 		the HeaderContract.View hosting the clicked subtitle, not the actual view which was
			 * 		clicked, not null
			 * @param index
			 * 		the index of the clicked button, with reference to the array last passed to {@link
			 * 		HeaderContract.View#setExtraButtons(Bitmap[])}
			 */
			void onExtraButtonClicked(HeaderContract.View headerView, int index);

			/**
			 * Invoked when the user selects an option from the overflow menu of a HeaderContract
			 * .View.
			 *
			 * @param headerView
			 * 		the HeaderContract.View hosting the menu, not null
			 * @param menuItem
			 * 		the selected menu option, not null
			 */
			void onOverflowMenuItemClicked(HeaderContract.View headerView, MenuItem menuItem);
		}
	}

	/**
	 * Acts as the intermediary between a data source and a HeaderView.View, and contains the
	 * business logic needed to update the data and drive the view. Must always be subscribed to
	 * callback events from the data source and the view (if possible).
	 */
	public interface Presenter<S extends BaseDataSource<LibraryItem>, V extends View> extends
			BasePresenter<S, V>, BaseDataSource.FullListener<LibraryItem>, View.Listener {}
}