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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.matthewtamlin.java_utilities.checkers.IntChecker;
import com.matthewtamlin.java_utilities.testing.Tested;
import com.matthewtamlin.mixtape.library.R;

/**
 * A RecyclerViewBody which displays the items in a grid of cards. Each card shows the title,
 * subtitle and artwork of an item, as well as a three-dot overflow button for the contextual
 * menu. The number of columns can be customised, and defaults to 2.
 */
@Tested(testMethod = "manual")
public final class GridBody extends RecyclerViewBody {
	/**
	 * Bundle key for saving and restoring the superclass state.
	 */
	private static final String STATE_KEY_SUPER = "GridBody.super";

	/**
	 * Bundle key for saving and restoring the numberOfColumns member variable.
	 */
	private static final String STATE_KEY_NUMBER_OF_COLUMNS = "GridBody.numberOfColumns";

	/**
	 * The default value to use for the number of columns. The default is 2 since this displays well
	 * on the average sized phone screen.
	 */
	private static final int DEFAULT_NUMBER_OF_COLUMNS = 2;

	/**
	 * The number of columns to display.
	 */
	private int numberOfColumns = DEFAULT_NUMBER_OF_COLUMNS;

	/**
	 * Constructs a new GridBody.
	 *
	 * @param context
	 * 		the Context the body is attached to, not null
	 */
	public GridBody(final Context context) {
		super(context);
		processAttributes(null, 0, 0);
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
		processAttributes(attrs, 0, 0);
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
		processAttributes(attrs, defStyleAttr, 0);
	}

	/**
	 * Sets the number of grid columns to display.
	 *
	 * @param numberOfColumns
	 * 		the number of columns to display, greater than zero
	 * @throws IllegalArgumentException
	 * 		if {@code numberOfColumns} is not greater than zero
	 */
	public final void setNumberOfColumns(int numberOfColumns) {
		this.numberOfColumns = IntChecker.checkGreaterThan(numberOfColumns, 0);
		getRecyclerView().setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
	}

	/**
	 * @return the number of columns currently displayed
	 */
	public final int getNumberOfColumns() {
		return numberOfColumns;
	}

	@Override
	protected final BodyViewHolder supplyNewBodyViewHolder(final ViewGroup parent) {
		final View gridItem = LayoutInflater.from(getContext()).inflate(R.layout
				.gridbodyitem, parent, false);

		final TextView titleView = (TextView) gridItem.findViewById(R.id.gridBodyItem_title);
		final TextView subtitleView = (TextView) gridItem.findViewById(R.id.gridBodyItem_subtitle);
		final ImageView artworkView = (ImageView) gridItem.findViewById(R.id.gridBodyItem_artwork);
		final View menuButton = gridItem.findViewById(R.id.gridBodyItem_menu);

		return new BodyViewHolder(gridItem, titleView, subtitleView, artworkView, menuButton);
	}

	@Override
	protected final void onRecyclerViewCreated(final RecyclerView recyclerView) {
		recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns < 1 ? 1
				: numberOfColumns, LinearLayoutManager.VERTICAL, false));
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		final Bundle savedState = new Bundle();
		savedState.putParcelable(STATE_KEY_SUPER, super.onSaveInstanceState());
		savedState.putInt(STATE_KEY_NUMBER_OF_COLUMNS, numberOfColumns);
		return savedState;
	}

	@Override
	protected void onRestoreInstanceState(final Parcelable parcelableState) {
		if (parcelableState instanceof Bundle) {
			final Bundle bundleState = (Bundle) parcelableState;
			super.onRestoreInstanceState(bundleState.getParcelable(STATE_KEY_SUPER));

			setNumberOfColumns(bundleState.getInt(STATE_KEY_NUMBER_OF_COLUMNS,
					DEFAULT_NUMBER_OF_COLUMNS));
		} else {
			super.onRestoreInstanceState(parcelableState);
		}
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
	private void processAttributes(final AttributeSet attrs, final int defStyleAttr,
			final int defStyleRes) {
		// Use a TypedArray to process all three types of attributes
		final TypedArray attributes = getContext().obtainStyledAttributes(attrs,
				R.styleable.GridBody, defStyleAttr, defStyleRes);

		setNumberOfColumns(attributes.getInt(R.styleable.GridBody_numberOfColumns,
				DEFAULT_NUMBER_OF_COLUMNS));

		attributes.recycle();
	}
}