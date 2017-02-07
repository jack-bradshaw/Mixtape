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
import static com.matthewtamlin.mixtape.library.mixtape_coordinator.CoordinatedMixtapeContainer.Constraint.SHOW_HEADER_AT_START;
import static com.matthewtamlin.mixtape.library.mixtape_coordinator.CoordinatedMixtapeContainer.Constraint.SHOW_HEADER_ON_SCROLL_TO_START;

/**
 * A MixtapeContainer which shows and hides the header based on body scroll events. There are four
 * available coordination profiles: <ul> <li>The header is always hidden, regardless of body scroll
 * events.</li> <li>The header is always shown, regardless of body scroll events.</li> <li>The
 * header is hidden unless the body is scrolled to the start position.</li> <li>The header is shown
 * whenever the body is scrolled towards the start, and hidden whenever the body is scrolled towards
 * the end.</li> </ul>
 */
public class CoordinatedMixtapeContainer extends FrameLayout implements
		MixtapeContainerView<SmallHeader, RecyclerViewBody> {
	/**
	 * Performs the actual coordination.
	 */
	private CoordinatorLayout coordinatorLayout;

	/**
	 * Contains the header and facilitates scrolling behaviours.
	 */
	private AppBarLayout headerContainer;

	/**
	 * The header to display at the top of the view.
	 */
	private SmallHeader header;

	/**
	 * The body to display beneath the header.
	 */
	private RecyclerViewBody body;

	/**
	 * The current elevation of the body.
	 */
	private int bodyElevationPx = 0;

	/**
	 * The current constraint between the header and the body.
	 */
	//TODO persist this value
	private Constraint constraint = PERSISTENT_HEADER;

	/**
	 * Constructs a new CoordinatedMixtapeCoordinatorView.
	 *
	 * @param context
	 * 		the context this view is operating in, not null
	 */
	public CoordinatedMixtapeContainer(final Context context) {
		super(context);
		init();
	}

	/**
	 * Constructs a new CoordinatedMixtapeCoordinatorView.
	 *
	 * @param context
	 * 		the Context the view is operating in, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 */
	public CoordinatedMixtapeContainer(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * Constructs a new CoordinatedMixtapeCoordinatorView.
	 *
	 * @param context
	 * 		the Context the view is operating in, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 * @param defStyleAttr
	 * 		an attribute in the current theme which supplies default attributes, pass 0	to ignore
	 */
	public CoordinatedMixtapeContainer(final Context context,
			final AttributeSet attrs,
			final int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
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
	 * Configures this view to always hide the header regardless of body scroll events. This
	 * overrides any behaviour set previously.
	 */
	public void hideHeaderAlways() {
		constraint = Constraint.HIDDEN_HEADER;
		applyCurrentConstraint();
	}

	/**
	 * Configures this view to always show the header regardless of body scroll events. This
	 * overrides any behaviour set previously.
	 */
	public void showHeaderAlways() {
		constraint = PERSISTENT_HEADER;
		applyCurrentConstraint();
	}

	/**
	 * Configures this view to hide the header unless the body is scrolled to the start position.
	 * This overrides any behaviour set previously.
	 */
	public void showHeaderAtStartOnly() {
		constraint = SHOW_HEADER_AT_START;
		applyCurrentConstraint();
	}

	/**
	 * Configures this view to show the header whenever the body is scrolled towards the start, and
	 * hide the header whenever the body is scrolled towards the end. This overrides any behaviour
	 * set previously.
	 */
	public void showHeaderOnScrollToStartOnly() {
		constraint = SHOW_HEADER_ON_SCROLL_TO_START;
		applyCurrentConstraint();
	}

	/**
	 * Creates the layout and assigns the necessary views references to member variables.
	 */
	private void init() {
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

			case SHOW_HEADER_AT_START: {
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

			case SHOW_HEADER_ON_SCROLL_TO_START: {
				if (header != null) {
					header.setVisibility(VISIBLE);
					setHeaderScrollFlags(SCROLL_FLAG_SCROLL | SCROLL_FLAG_ENTER_ALWAYS |
							SCROLL_FLAG_SNAP);
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
	protected enum Constraint {
		/**
		 * The header is always hidden, regardless of body scroll events.
		 */
		HIDDEN_HEADER,

		/**
		 * The header is always shown, regardless of body scroll events.
		 */
		PERSISTENT_HEADER,

		/**
		 * The header is hidden unless the body is scrolled to the start position.
		 */
		SHOW_HEADER_AT_START,

		/**
		 * The header is shown whenever the body is scrolled towards the start and hidden whenever
		 * the body is scrolled towards the end.
		 */
		SHOW_HEADER_ON_SCROLL_TO_START
	}
}