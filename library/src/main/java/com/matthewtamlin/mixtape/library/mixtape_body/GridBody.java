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

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.matthewtamlin.mixtape.library.R;
import com.matthewtamlin.mixtape.library.data.LibraryItem;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;
import static com.matthewtamlin.java_utilities.checkers.IntChecker.checkGreaterThan;

/**
 * A RecyclerViewBody which displays the list of items with a grid of cards. Each card shows the
 * title, subtitle and artwork of an item, as well as a three-dot overflow button for the contextual
 * menu. The number of columns can be customised, and defaults to 2.
 * <p>
 * The simplest way to use a GridBody is with a RecyclerViewBodyPresenter. For example:<pre>{@code
 *   RecyclerViewBodyPresenter<LibraryItem, ListDataSource<LibraryItem>> presenter
 * 	   = new MyPresenter();
 *   ListDataSource<LibraryItem> dataSource = new MyDataSource();
 *   presenter.setDataSource(dataSource);
 *   GridBody body = new GridBody();
 *   presenter.setView(body);}
 * </pre><p>
 * This setup will pull data from the data source and display it in the view.
 */
public class GridBody extends RecyclerViewBody {
	/**
	 * The number of columns to display.
	 */
	private int numberOfColumns;

	/**
	 * The color to use when displaying the item titles in the UI, as an ARGB hex code. The default
	 * value is black.
	 */
	private int titleTextColor = 0xFF000000;

	/**
	 * The color to use when displaying the item subtitles in the UI, as an ARGB hex code. The
	 * default value is light grey.
	 */
	private int subtitleTextColor = 0xFF808080;

	/**
	 * The color to use for the overflow menu buttons, as an ARGB hex code. The default color is
	 * black.
	 */
	private int overflowButtonColor = 0xFF000000;

	/**
	 * the color to use for the background of the item cards in the UI, as an ARGB hex code. The
	 * default color is white.
	 */
	private int cardBackgroundColor = 0xFFFFFFFF;

	/**
	 * Constructs a new GridBody.
	 *
	 * @param context
	 * 		the Context the body is attached to, not null
	 */
	public GridBody(final Context context) {
		super(context);
		init(null, 0, 0);
	}

	/**
	 * Constructs a new GridBody.
	 *
	 * @param context
	 * 		the Context the body is attached to, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 */
	public GridBody(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0, 0);
	}

	/**
	 * Constructs a new GridBody.
	 *
	 * @param context
	 * 		the Context the body is attached to, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 * @param defStyleAttr
	 * 		an attribute in the current theme which supplies default attributes, pass 0	to ignore
	 */
	public GridBody(final Context context, final AttributeSet attrs, final int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs, defStyleAttr, 0);
	}

	/**
	 * Sets the number of grid columns to display.
	 *
	 * @param numberOfColumns
	 * 		the number of columns to display, greater than zero
	 * @throws IllegalArgumentException
	 * 		if {@code numberOfColumns} is less than zero
	 */
	public void setNumberOfColumns(final int numberOfColumns) {
		this.numberOfColumns = checkGreaterThan(numberOfColumns, 0);
		getRecyclerView().setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
	}

	/**
	 * @return the current number of columns
	 */
	public int getNumberOfColumns() {
		return numberOfColumns;
	}

	@Override
	protected BodyViewHolder supplyNewBodyViewHolder(final ViewGroup parent) {
		final View gridItem = LayoutInflater.from(getContext()).inflate(R.layout
				.gridbodyitem, parent, false);

		final TextView titleView = (TextView) gridItem.findViewById(R.id.gridBodyItem_title);
		final TextView subtitleView = (TextView) gridItem.findViewById(R.id.gridBodyItem_subtitle);
		final ImageView artworkView = (ImageView) gridItem.findViewById(R.id.gridBodyItem_artwork);
		final View menuButton = gridItem.findViewById(R.id.gridBodyItem_menu);

		return new BodyViewHolder(gridItem, titleView, subtitleView, artworkView, menuButton);
	}

	@Override
	protected void onViewHolderBound(final BodyViewHolder viewHolder, final LibraryItem data) {
		super.onViewHolderBound(viewHolder, data);

		((CardView) viewHolder.getRootView()).setCardBackgroundColor(cardBackgroundColor);
		viewHolder.getTitleTextView().setTextColor(titleTextColor);
		viewHolder.getSubtitleTextView().setTextColor(subtitleTextColor);
		((ImageButton) viewHolder.getContextualMenuButton()).setColorFilter(overflowButtonColor);
	}

	@Override
	protected void onRecyclerViewCreated(final RecyclerView recyclerView) {
		recyclerView.setLayoutManager(new GridLayoutManager(getContext(),
				numberOfColumns < 1 ? 1 : numberOfColumns, VERTICAL, false));
	}

	@Override
	public void setTitleTextColor(final int color) {
		titleTextColor = color;

		// Ensures the UI updates
		notifyItemsChanged();
	}

	@Override
	public void setSubtitleTextColor(final int color) {
		subtitleTextColor = color;

		// Ensures the UI updates
		notifyItemsChanged();
	}

	@Override
	public void setOverflowMenuButtonColor(final int color) {
		overflowButtonColor = color;

		// Ensures the UI updates
		notifyItemsChanged();
	}

	/**
	 * Sets the color of the cards in the UI.
	 *
	 * @param color
	 * 		the color to use, as an ARGB hex code
	 */
	public void setCardBackgroundColor(final int color) {
		cardBackgroundColor = color;

		// Ensures the UI updates
		notifyItemsChanged();
	}

	/**
	 * Processes the attributes supplied at construction.
	 *
	 * @param attrs
	 * 		configuration attributes, null allowed
	 * @param defStyleAttr
	 * 		an attribute in the current theme which supplies default attributes, pass 0	to ignore
	 * @param defStyleRes
	 * 		a resource which supplies default attributes, only used if {@code defStyleAttr}	is 0, pass
	 * 		0 to ignore
	 */
	private void init(final AttributeSet attrs, final int defStyleAttr,
			final int defStyleRes) {
		final TypedArray attributes = getContext().obtainStyledAttributes(attrs,
				R.styleable.GridBody, defStyleAttr, defStyleRes);

		setNumberOfColumns(attributes.getInt(R.styleable.GridBody_numberOfColumns,
				DEFAULT_NUMBER_OF_COLUMNS));

		attributes.recycle();
	}
}