package com.matthewtamlin.mixtape.library.mixtape_header;

import android.graphics.Bitmap;
import android.view.MenuItem;

import com.matthewtamlin.mixtape.library.base_mvp.BaseDataSource;
import com.matthewtamlin.mixtape.library.base_mvp.BasePresenter;
import com.matthewtamlin.mixtape.library.base_mvp.BaseView;
import com.matthewtamlin.mixtape.library.data.LibraryItem;

/**
 * Defines a contract between the model, view and presenter
 */
public interface HeaderContract {
	/**
	 * Displays a the title, subtitle and artwork of a single LibraryItem to the user, along with an
	 * overflow menu button and a series of extra buttons. This view is designed to be used in a
	 * MixtapeContainerView with a BodyContract.View.
	 */
	public interface View extends BaseView<Presenter> {
		/**
		 * Sets the item to display and updates the UI.
		 *
		 * @param item
		 * 		the item to display
		 */
		void setItem(LibraryItem item);

		/**
		 * Returns the item currently displayed in this view. If no item is displayed, then null
		 * will be returned.
		 *
		 * @return the item displayed in the view
		 */
		LibraryItem getItem();

		/**
		 * Sets the extra buttons to display and updates the UI. An extra button is generated for
		 * each Bitmap in the passed array. Pass null or an empty array to show no extra buttons.
		 *
		 * @param buttonIcons
		 * 		the icons to display in the extra buttons, not null
		 */
		void setExtraButtons(Bitmap[] buttonIcons);

		/**
		 * Sets the menu resource to use each time the overflow menu button is clicked. This method
		 * will not update the menu if it is currently being displayed.
		 *
		 * @param menuResource
		 * 		the resource ID to use for the overflow menu
		 */
		void setOverflowMenuResource(int menuResource);

		/**
		 * Returns the resource ID of the current overflow menu. If no resource has been set, then
		 * -1 is returned.
		 *
		 * @return the resource ID of the overflow menu resource
		 */
		int getOverflowMenuResource();

		/**
		 * Listens to user input events from HeaderContract.Views.
		 */
		interface Listener {
			/**
			 * Invoked to indicate that the user has clicked the title in a HeaderContract.View.
			 *
			 * @param hostView
			 * 		the HeaderContract.View hosting the clicked title, not the actual view which was
			 * 		clicked, not null
			 */
			void onTitleClicked(HeaderContract.View hostView);

			/**
			 * Invoked to indicate that the user has clicked the subtitle in a HeaderContract
			 * .View.
			 *
			 * @param hostView
			 * 		the HeaderContract.View hosting the clicked subtitle, not the actual view which was
			 * 		clicked, not null
			 */
			void onSubtitleClicked(HeaderContract.View hostView);

			/**
			 * Invoked to indicate that the user has clicked the artwork in a HeaderContract .View.
			 *
			 * @param hostView
			 * 		the HeaderContract.View hosting the clicked artwork, not the actual view which was
			 * 		clicked, not null
			 */
			void onArtworkClicked(HeaderContract.View hostView);

			/**
			 * Invoked to indicate that the user has clicked an extra button in a HeaderContract
			 * .View.
			 *
			 * @param hostView
			 * 		the HeaderContract.View hosting the clicked subtitle, not the actual view which was
			 * 		clicked, not null
			 * @param index
			 * 		the index of the clicked button, with reference to the order of the Bitmaps passed
			 * 		to {@link HeaderContract.View#setExtraButtons(Bitmap[])}
			 */
			void onExtraButtonClicked(HeaderContract.View hostView, int index);

			/**
			 * Invoked to indicate that the user has selected an overflow menu item in a
			 * HeaderContract.View.
			 *
			 * @param hostView
			 * 		the HeaderContract.View hosting the menu, not the actual view which was clicked,
			 * 		not null
			 * @param menuItem
			 * 		the menu item which was clicked, not null
			 */
			void onOverflowMenuItemClicked(HeaderContract.View hostView, MenuItem menuItem);
		}
	}

	/**
	 * Acts as the intermediary between a data source and a HeaderView.View, and contains the
	 * business logic needed to update the data and drive the view. Must always be subscribed to
	 * callback events from the data source and the view (if possible).
	 */
	public interface Presenter<S extends BaseDataSource<LibraryItem>, V extends View> extends
			BasePresenter<S, V>, BaseDataSource.Listener<LibraryItem>, View.Listener {}
}