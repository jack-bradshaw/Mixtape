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
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
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

import static com.matthewtamlin.android_utilities.library.helpers.DimensionHelper.dpToPx;

/**
 * A RecyclerViewBody which displays the list of items in a vertical list. Each list item shows the
 * title, subtitle and artwork of an item, as well as a three-dot overflow button for the contextual
 * menu. The view can be customised by hiding all artwork (shown by default) and showing horizontal
 * dividers between items (hidden by default).
 */
public class ListBody extends RecyclerViewBody {
	/**
	 * The padding to apply to the ends of the list dividers. This value was selected to align the
	 * ends of the dividers with the components of each list item, with reference to the layout
	 * defined in resource 'R.layout.listbodyitem'.
	 */
	private static final int DECORATION_PADDING_DP = 8;

	/**
	 * Whether or not horizontal dividers should be shown between each item in the list.
	 */
	private boolean showDividers;

	/**
	 * Whether or not artwork should be shown in the list.
	 */
	private boolean showArtwork;

	/**
	 * The RecyclerView.ItemDecoration to show below each list item.
	 */
	private HorizontalDividerDecoration horizontalDividerDecoration;

	/**
	 * The color to use when displaying item titles in the UI, as an ARGB hex code. The default
	 * value is black.
	 */
	private int titleTextColor = 0xFF000000;

	/**
	 * The color to use when displaying item subtitles in the UI, as an ARGB hex code. The default
	 * value is light grey.
	 */
	private int subtitleTextColor = 0xFF808080;

	/**
	 * The color to use for the overflow menu buttons, as an ARGB hex code. The default color is
	 * black.
	 */
	private int overflowButtonColor = 0xFF000000;

	/**
	 * Constructs a new ListBody.
	 *
	 * @param context
	 * 		the Context the body is attached to, not null
	 */
	public ListBody(final Context context) {
		super(context);
		init(null, 0, 0);
	}

	/**
	 * Constructs a new ListBody.
	 *
	 * @param context
	 * 		the Context the body is attached to, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 */
	public ListBody(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0, 0);
	}

	/**
	 * Constructs a new ListBody.
	 *
	 * @param context
	 * 		the Context the body is attached to, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 * @param defStyleAttr
	 * 		an attribute in the current theme which supplies default attributes, pass 0	to ignore
	 */
	public ListBody(final Context context, final AttributeSet attrs, final int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs, defStyleAttr, 0);
	}

	@Override
	protected BodyViewHolder supplyNewBodyViewHolder(final ViewGroup parent) {
		final View listItem = LayoutInflater.from(getContext()).inflate(R.layout
				.listbodyitem, parent, false);

		final TextView titleView = (TextView) listItem.findViewById(R.id.listBodyItem_title);
		final TextView subtitleView = (TextView) listItem.findViewById(R.id.listBodyItem_subtitle);
		final ImageView artworkView = (ImageView) listItem.findViewById(R.id.listBodyItem_artwork);
		final View menuButton = listItem.findViewById(R.id.listIBodyItem_menu);

		return new BodyViewHolder(listItem, titleView, subtitleView, artworkView, menuButton);
	}

	@Override
	protected void onViewHolderBound(final BodyViewHolder viewHolder,
			final LibraryItem data) {
		viewHolder.getArtworkImageView().setVisibility(showArtwork ? VISIBLE : GONE);

		viewHolder.getTitleTextView().setTextColor(titleTextColor);
		viewHolder.getSubtitleTextView().setTextColor(subtitleTextColor);
		((ImageButton) viewHolder.getContextualMenuButton()).setColorFilter(overflowButtonColor);
	}

	@Override
	protected void onRecyclerViewCreated(final RecyclerView recyclerView) {
		super.onRecyclerViewCreated(recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
		this.overflowButtonColor = color;

		// Ensures the UI updates
		notifyItemsChanged();
	}

	/**
	 * Shows/hides horizontal dividers between the list items.
	 *
	 * @param show
	 * 		true to show the dividers, false to hide them
	 */
	public void showDividers(final boolean show) {
		showDividers = show;

		getRecyclerView().removeItemDecoration(horizontalDividerDecoration);

		if (showDividers) {
			getRecyclerView().addItemDecoration(horizontalDividerDecoration);
		}

		getRecyclerView().invalidateItemDecorations();
	}

	/**
	 * @return true if dividers are shown between the list items, false otherwise
	 */
	public boolean dividersAreShown() {
		return showDividers;
	}

	/**
	 * Shows/hides the artwork of the items in the list.
	 *
	 * @param show
	 * 		true to show artwork, false to hide it
	 */
	public void showArtwork(final boolean show) {
		showArtwork = show;
		getRecyclerView().getAdapter().notifyDataSetChanged(); // Forces UI update
	}

	/**
	 * @return true if the list is displaying artwork, false otherwise
	 */
	public boolean artworkIsShown() {
		return showArtwork;
	}

	/**
	 * Sets the color to use for the dividers in-between the list items. The visibility of the
	 * dividers is not changed by calling this method
	 *
	 * @param color
	 * 		the color to use, as an ARGB hex code
	 */
	public void setDividerColor(final int color) {
		horizontalDividerDecoration.setDividerColor(color);

		// Make sure the UI updates
		getRecyclerView().removeItemDecoration(horizontalDividerDecoration);

		if (showDividers) {
			getRecyclerView().addItemDecoration(horizontalDividerDecoration);
		}

		getRecyclerView().invalidateItemDecorations();
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
		final int decorationInsets = dpToPx(getContext(), DECORATION_PADDING_DP);
		horizontalDividerDecoration = new HorizontalDividerDecoration(getContext(),
				decorationInsets, decorationInsets);
		horizontalDividerDecoration.setDividerColor(Color.BLACK);

		final TypedArray attributes = getContext().obtainStyledAttributes(attrs,
				R.styleable.ListBody, defStyleAttr, defStyleRes);

		// The methods handle both setting member variables and updating the UI
		showDividers(attributes.getBoolean(R.styleable.ListBody_showListDividers, false));
		showArtwork(attributes.getBoolean(R.styleable.ListBody_showListArtwork, true));

		attributes.recycle();
	}
}