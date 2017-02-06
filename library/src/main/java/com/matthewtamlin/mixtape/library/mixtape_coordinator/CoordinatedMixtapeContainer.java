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

package com.matthewtamlin.mixtape.library.mixtape_coordinator;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.matthewtamlin.android_utilities.library.helpers.DimensionHelper;
import com.matthewtamlin.mixtape.library.R;
import com.matthewtamlin.mixtape.library.mixtape_body.RecyclerViewBody;
import com.matthewtamlin.mixtape.library.mixtape_header.SmallHeader;

import static android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS;
import static android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL;
import static android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.matthewtamlin.mixtape.library.mixtape_coordinator.CoordinatedMixtapeContainer.Constraint.PERSISTENT_HEADER;
import static com.matthewtamlin.mixtape.library.mixtape_coordinator.CoordinatedMixtapeContainer.Constraint.SHOW_HEADER_AT_TOP_ONLY;
import static com.matthewtamlin.mixtape.library.mixtape_coordinator.CoordinatedMixtapeContainer.Constraint.SHOW_HEADER_ON_DOWNWARD_SCROLL_ONLY;

/**
 * Displays and coordinates a header and a body. The header can be any view, but the body must be a
 * RecyclerViewBody. Scroll events in the body affect the display of the header according to the
 * current configuration. The available configurations are: <ul> <li>The header is always hidden,
 * regardless of body scroll events.</li> <li>The header is always shown, regardless of body scroll
 * events.</li> <li>The header is always hidden except when the body is scrolled all the way to the
 * top. Scrolling away from the top re-hides the header.</li> <li>The header is shown when the body
 * is scrolled towards the top, and hidden when the body is scrolled towards the bottom.</li> </ul>
 */
public final class CoordinatedMixtapeContainer extends FrameLayout implements
		MixtapeContainerView<SmallHeader, RecyclerViewBody> {
	/**
	 * The layout which actually performs the coordination of the header and the body.
	 */
	private CoordinatorLayout coordinatorLayout;

	/**
	 * Contains the header and facilitates scrolling behaviours.
	 */
	private AppBarLayout headerContainer;

	/**
	 * The header view to display at the top of the view.
	 */
	private SmallHeader header;

	/**
	 * The body view to display beneath the header.
	 */
	private RecyclerViewBody body;

	/**
	 * The material elevation of the body view.
	 */
	private int bodyElevationPx = 0;

	/**
	 * The constraint to apply to the header and body to facilitate coordinated scrolling.
	 */
	private Constraint constraint = PERSISTENT_HEADER;

	/**
	 * Constructs a new CoordinatedMixtapeCoordinatorView.
	 *
	 * @param context
	 * 		the context this view is attached to, not null
	 */
	public CoordinatedMixtapeContainer(final Context context) {
		super(context);
		createLayout();
	}

	/**
	 * Constructs a new CoordinatedMixtapeCoordinatorView.
	 *
	 * @param context
	 * 		the Context the view is attached to, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 */
	public CoordinatedMixtapeContainer(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		createLayout();
	}

	/**
	 * Constructs a new CoordinatedMixtapeCoordinatorView.
	 *
	 * @param context
	 * 		the Context the view is attached to, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 * @param defStyleAttr
	 * 		an attribute in the current theme which supplies default attributes, pass 0	to ignore
	 */
	public CoordinatedMixtapeContainer(final Context context, final AttributeSet attrs, final int
			defStyleAttr) {
		super(context, attrs, defStyleAttr);
		createLayout();
	}

	@Override
	public SmallHeader getHeader() {
		return header;
	}

	@Override
	public void setHeader(final SmallHeader header) {
		headerContainer.removeView(this.header);

		if (header != null) {
			headerContainer.addView(header);
			header.setLayoutParams(new AppBarLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
		}

		this.header = header;

		applyCurrentConstraint();
	}

	@Override
	public RecyclerViewBody getBody() {
		return body;
	}

	@Override
	public void setBody(final RecyclerViewBody body) {
		coordinatorLayout.removeView(this.body);

		if (body != null) {
			coordinatorLayout.addView(body);

			final CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams
					(MATCH_PARENT, MATCH_PARENT);
			layoutParams.setBehavior(new AppBarLayout.ScrollingViewBehavior());
			body.setLayoutParams(layoutParams);
		}

		this.body = body;

		setBodyElevationPx(bodyElevationPx);
		applyCurrentConstraint();
	}

	@Override
	public void setHeaderElevationDp(final int elevationDp) {
		setHeaderElevationPx(DimensionHelper.dpToPx(getContext(), elevationDp));
	}

	@Override
	public void setHeaderElevationPx(final int elevationPx) {
		// No need to check for null, since the header container is never be null
		ViewCompat.setElevation(headerContainer, DimensionHelper.dpToPx(getContext(), elevationPx));
	}

	@Override
	public void setBodyElevationDp(final int elevationDp) {
		setBodyElevationPx(DimensionHelper.dpToPx(getContext(), elevationDp));
	}

	@Override
	public void setBodyElevationPx(final int elevationPx) {
		this.bodyElevationPx = elevationPx;

		// Check must be done since the body may be null
		if (body != null) {
			ViewCompat.setElevation(body, DimensionHelper.dpToPx(getContext(), bodyElevationPx));
		}
	}

	/**
	 * Configures this view so that the header is always hidden, regardless of body scroll events.
	 */
	public void hideHeaderAlways() {
		constraint = Constraint.HIDDEN_HEADER;
		applyCurrentConstraint();
	}

	/**
	 * Configures this view so that the header is always shown, regardless of body scroll events.
	 */
	public void showHeaderAlways() {
		constraint = PERSISTENT_HEADER;
		applyCurrentConstraint();
	}

	/**
	 * Configures this view so that the header is always hidden except when the body is scrolled all
	 * the way to the top. Scrolling away from the top re-hides the header.
	 */
	public void showHeaderAtTopOnly() {
		constraint = SHOW_HEADER_AT_TOP_ONLY;
		applyCurrentConstraint();
	}

	/**
	 * Configures this view so that the header is shown when the body is scrolled towards the top,
	 * and hidden when the body is scrolled towards the bottom.
	 */
	public void showHeaderOnDownwardScrollOnly() {
		constraint = SHOW_HEADER_ON_DOWNWARD_SCROLL_ONLY;
		applyCurrentConstraint();
	}

	/**
	 * Creates the layout and assigns necessary views references to member variables.
	 */
	private void createLayout() {
		inflate(getContext(), R.layout.coordinatedheaderbodyview, this);
		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatedHeaderBodyView_root);
		headerContainer = (AppBarLayout) findViewById(R.id.coordinatedHeaderBodyView_container);
	}

	/**
	 * Applies the current constraint to the header and the body. If the current constraint is null,
	 * the {@link Constraint#PERSISTENT_HEADER} constraint is used.
	 */
	private void applyCurrentConstraint() {
		switch (constraint) {
			case HIDDEN_HEADER: {
				if (header != null) {
					header.setVisibility(GONE);
					setHeaderScrollFlags(0);
				}

				if (body != null) {
					body.clearRegisteredTopReachedListeners();
				}

				break;
			}

			case PERSISTENT_HEADER: {
				if (header != null) {
					header.setVisibility(VISIBLE);
					setHeaderScrollFlags(0);
				}

				if (body != null) {
					body.clearRegisteredTopReachedListeners();
				}

				break;
			}

			case SHOW_HEADER_AT_TOP_ONLY: {
				if (header != null) {
					header.setVisibility(VISIBLE);
					setHeaderScrollFlags(SCROLL_FLAG_SCROLL | SCROLL_FLAG_SNAP);
				}

				if (body != null) {
					body.registerTopReachedListener(new RecyclerViewBody.TopReachedListener() {
						@Override
						public void onTopReached(final RecyclerViewBody recyclerViewBody) {
							headerContainer.setExpanded(true);
						}
					});
				}

				break;
			}

			case SHOW_HEADER_ON_DOWNWARD_SCROLL_ONLY: {
				if (header != null) {
					header.setVisibility(VISIBLE);
					setHeaderScrollFlags(SCROLL_FLAG_SCROLL | SCROLL_FLAG_ENTER_ALWAYS |
							SCROLL_FLAG_SNAP);
				}

				if (body != null) {
					body.clearRegisteredTopReachedListeners();
				}

				break;
			}

			default: { // Use persistent case as default
				if (header != null) {
					header.setVisibility(View.VISIBLE);
					setHeaderScrollFlags(0);
				}

				if (body != null) {
					body.clearRegisteredTopReachedListeners();
				}
			}
		}
	}

	/**
	 * Applies the supplied scroll flags to the header.
	 *
	 * @param flags
	 * 		the flags to apply, 0 to clear all flags
	 */
	private void setHeaderScrollFlags(final int flags) {
		final AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) header
				.getLayoutParams();
		layoutParams.setScrollFlags(flags);
		header.setLayoutParams(layoutParams);
	}

	/**
	 * The supported header-body constraints.
	 */
	enum Constraint {
		/**
		 * The header is always hidden, regardless of body scroll events.
		 */
		HIDDEN_HEADER,

		/**
		 * The header is always shown, regardless of body scroll events.
		 */
		PERSISTENT_HEADER,

		/**
		 * The header is always hidden except when the body is scrolled to the very top. Scrolling
		 * away from the top hides the header again.
		 */
		SHOW_HEADER_AT_TOP_ONLY,

		/**
		 * The header is always shown on downwards scroll events in the body, and hidden on upwards
		 * scroll events.
		 */
		SHOW_HEADER_ON_DOWNWARD_SCROLL_ONLY
	}
}