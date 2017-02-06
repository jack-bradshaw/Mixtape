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
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static com.matthewtamlin.java_utilities.checkers.IntChecker.checkGreaterThanOrEqualTo;
import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * Decorates a RecyclerView by displaying a horizontal divider beneath each item. The ends of the
 * divider can be inset by passing inset values to the constructor.
 */
final class HorizontalDividerDecoration extends RecyclerView.ItemDecoration {
	/**
	 * The padding to add to the left end of the divider, measured in pixels.
	 */
	private final int leftInsetPx;

	/**
	 * The padding to add to the right end of the divider, measured in pixels.
	 */
	private final int rightInsetPx;

	/**
	 * The drawable used to actually display the divider.
	 */
	private final Drawable dividerDrawable;

	/**
	 * Constructs a new HorizontalDividerDecoration.
	 *
	 * @param context
	 * 		the Context this decoration is operating in, not null
	 * @param leftInsetPx
	 * 		the inset to apply to the left end of each divider, measured in pixels, at least zero
	 * @param rightInsetPx
	 * 		the inset to apply to the right end of the divider, measured in pixels, at least zero
	 * @throws IllegalArgumentException
	 * 		if {@code context} is null
	 * @throws IllegalArgumentException
	 * 		if {@code leftInsetPx} is less than zero
	 * @throws IllegalArgumentException
	 * 		if {@code rightInsetPx} is less than zero
	 */
	HorizontalDividerDecoration(final Context context,
			final int leftInsetPx,
			final int rightInsetPx) {
		this.leftInsetPx = checkGreaterThanOrEqualTo(leftInsetPx, 0);
		this.rightInsetPx = checkGreaterThanOrEqualTo(rightInsetPx, 0);

		// Use the list divider resource for the drawable
		final TypedArray a = context.obtainStyledAttributes(new int[]{android.R.attr.listDivider});
		dividerDrawable = a.getDrawable(0);
		a.recycle();
	}

	@Override
	public void onDraw(final Canvas c, final RecyclerView parent, final RecyclerView.State state) {
		// Apply the insets via the left and right position
		final int leftPosition = parent.getPaddingLeft() + leftInsetPx;
		final int rightPosition = parent.getWidth() - parent.getPaddingRight() - rightInsetPx;

		// Show the decoration below every RecyclerView item
		for (int i = 0; i < parent.getChildCount(); i++) {
			// Need to draw relative to the RecyclerView child
			final View child = parent.getChildAt(i);
			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
					.getLayoutParams();

			// Position the divider below the child item and set the thickness
			final int top = child.getBottom() + params.bottomMargin;
			final int bottom = top + dividerDrawable.getIntrinsicHeight();

			// Apply the position variables then draw the divider
			dividerDrawable.setBounds(leftPosition, top, rightPosition, bottom);
			dividerDrawable.draw(c);
		}
	}

	@Override
	public void getItemOffsets(final Rect outRect, final View view, final RecyclerView parent,
			final RecyclerView.State state) {
		outRect.set(0, 0, 0, dividerDrawable.getIntrinsicHeight());
	}
}