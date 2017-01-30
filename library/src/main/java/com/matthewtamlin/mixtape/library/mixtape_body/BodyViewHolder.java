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

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.matthewtamlin.java_utilities.checkers.NullChecker;
import com.matthewtamlin.java_utilities.testing.Tested;

/**
 * A ViewHolder for displaying a LibraryItems in a BodyContract.View using a RecyclerView. The view
 * holder contains: <ul> <li>The root view of the RecyclerView item.</li> <li>A TextView for
 * displaying the title.</li> <li>A TextView for displaying the subtitle.</li> <li>An ImageView for
 * displaying the artwork.</li> <li>A Button for displaying a contextual menu.</li></ul> </br> This
 * class makes use of the builder pattern for constructing new instances.
 */
@Tested(testMethod = "unit")
public final class BodyViewHolder extends RecyclerView.ViewHolder {
	/**
	 * The root view of the RecyclerView item.
	 */
	private final View rootView;

	/**
	 * A TextView for displaying titles. Must be a child of the root view.
	 */
	private final TextView titleTextView;

	/**
	 * A TextView for displaying subtitles. Must be a child of the root view.
	 */
	private final TextView subtitleTextView;

	/**
	 * An ImageView for displaying artwork. Must be a child of the root view.
	 */
	private final ImageView artworkImageView;

	/**
	 * A clickable view for triggering the display of an contextual menu. Must be a child of the
	 * root view.
	 */
	private final View contextualMenuButton;

	/**
	 * Constructs a new BodyViewHolder from a Builder. All the views of the supplied Builder must be
	 * non-null.
	 *
	 * @param builder
	 * 		the Builder to base the BodyViewHolder on, not null
	 * @throws IllegalArgumentException
	 * 		if {@code builder} is null
	 * @throws IllegalArgumentException
	 * 		if the root view of {@code builder} is null
	 * @throws IllegalArgumentException
	 * 		if the title text view of {@code builder} is null
	 * @throws IllegalArgumentException
	 * 		if the subtitle text view of {@code builder} is null
	 * @throws IllegalArgumentException
	 * 		if the artwork image view of {@code builder} is null
	 * @throws IllegalArgumentException
	 * 		if the contextual menu button of {@code builder} is null
	 */
	private BodyViewHolder(final Builder builder) {
		super(builder.rootView);

		NullChecker.checkNotNull(builder, "builder cannot be null");
		rootView = NullChecker.checkNotNull(builder.rootView);
		titleTextView = NullChecker.checkNotNull(builder.titleTextView);
		subtitleTextView = NullChecker.checkNotNull(builder.subtitleTextView);
		artworkImageView = NullChecker.checkNotNull(builder.artworkImageView);
		contextualMenuButton = NullChecker.checkNotNull(builder.contextualMenuButton);
	}

	/**
	 * @return the root view of the RecyclerView item, not null
	 */
	public final View getRootView() {
		return rootView;
	}

	/**
	 * @return the TextView for displaying titles, not null
	 */
	public final TextView getTitleTextView() {
		return titleTextView;
	}

	/**
	 * @return the TextView for displaying subtitles, not null
	 */
	public final TextView getSubtitleTextView() {
		return subtitleTextView;
	}

	/**
	 * @return the ImageView for displaying artwork, not null
	 */
	public final ImageView getArtworkImageView() {
		return artworkImageView;
	}

	/**
	 * @return the clickable view for triggering the display of a contextual menu, not null
	 */
	public final View getContextualMenuButton() {
		return contextualMenuButton;
	}

	/**
	 * Creates a new Builder which can be used to create a BodyViewHolder.
	 *
	 * @param rootView
	 * 		the root view to use when building the ViewHolder, not null
	 * @return the new Builder
	 * @throws IllegalArgumentException
	 * 		if {@code rootView} is null
	 */
	public static Builder builder(View rootView) {
		return new Builder(rootView);
	}

	/**
	 * Builder class for the BodyViewHolder. A BodyViewHolder cannot be successfully built until the
	 * builder's title text view, subtitle text view, artwork image view and contextual menu button
	 * are set.
	 */
	public static final class Builder {
		/**
		 * The root view of the RecyclerView item.
		 */
		private View rootView;

		/**
		 * A TextView for displaying titles. Must be a child of the root view.
		 */
		private TextView titleTextView;

		/**
		 * A TextView for displaying subtitles. Must be a child of the root view.
		 */
		private TextView subtitleTextView;

		/**
		 * An ImageView for displaying artwork. Must be a child of the root view.
		 */
		private ImageView artworkImageView;

		/**
		 * A clickable view for triggering the display of an contextual menu. Must be a child of the
		 * root view.
		 */
		private View contextualMenuButton;

		/**
		 * Constructs a new Builder.
		 *
		 * @param rootView
		 * 		the root view to use when building the ViewHolder
		 * @throws IllegalArgumentException
		 * 		if {@code rootView} is null
		 */
		private Builder(final View rootView) {
			this.rootView = NullChecker.checkNotNull(rootView, "rootView cannot be null");
		}

		/**
		 * Sets the title TextView to use when building the ViewHolder.
		 *
		 * @param titleTextView
		 * 		the title TextView to use, not null
		 * @return this Builder
		 * @throws IllegalArgumentException
		 * 		if {@code titleTextView} is null
		 */
		public Builder withTitleTextView(final TextView titleTextView) {
			NullChecker.checkNotNull(titleTextView, "titleTextView cannot be null");

			// All checks must pass before modifying the member variables
			this.titleTextView = titleTextView;
			return this;
		}

		/**
		 * Sets the title TextView to use when building the ViewHolder. The supplied resource ID
		 * must correspond to a child of the root view, and the child must be a TextView.
		 *
		 * @param titleTextViewResId
		 * 		the resource ID of the title TextView
		 * @return this Builder
		 * @throws IllegalArgumentException
		 * 		if {@code titleTextViewResId} does not correspond to a TextView
		 * @throws IllegalArgumentException
		 * 		if {@code titleTextViewResId} does not correspond to a child of the root view
		 */
		public Builder withTitleTextView(final int titleTextViewResId) {
			final TextView view = (TextView) rootView.findViewById(titleTextViewResId);
			NullChecker.checkNotNull(view, "no corresponding child");
			withTitleTextView(view);
			return this;
		}

		/**
		 * Sets the subtitle TextView to use when building the ViewHolder.
		 *
		 * @param subtitleTextView
		 * 		the subtitle TextView to use, not null
		 * @return this Builder
		 * @throws IllegalArgumentException
		 * 		if {@code subtitleTextView} is null
		 */
		public Builder withSubtitleTextView(final TextView subtitleTextView) {
			NullChecker.checkNotNull(subtitleTextView, "subtitleTextView cannot be null");

			// All checks must pass before modifying the member variables
			this.subtitleTextView = subtitleTextView;
			return this;
		}

		/**
		 * Sets the subtitle TextView to use when building the ViewHolder. The supplied resource ID
		 * must correspond to a child of the root view, and the child must be a TextView.
		 *
		 * @param subtitleTextViewResId
		 * 		the resource ID of the subtitle TextView
		 * @return this Builder
		 * @throws IllegalArgumentException
		 * 		if {@code subtitleTextViewResId} does not correspond to a TextView
		 * @throws IllegalArgumentException
		 * 		if {@code subtitleTextViewResId} does not correspond to a child of the root view
		 */
		public Builder withSubtitleTextView(final int subtitleTextViewResId) {
			final TextView view = (TextView) rootView.findViewById(subtitleTextViewResId);
			NullChecker.checkNotNull(view, "no corresponding child");
			withSubtitleTextView(view);
			return this;
		}

		/**
		 * Sets the artwork ImageView to use when building the ViewHolder.
		 *
		 * @param artworkImageView
		 * 		the artwork ImageView to use, not null
		 * @return this Builder
		 * @throws IllegalArgumentException
		 * 		if {@code artworkImageView} is null
		 */
		public Builder withArtworkImageView(final ImageView artworkImageView) {
			NullChecker.checkNotNull(artworkImageView, "artworkImageView cannot be null");

			// All checks must pass before modifying the member variables
			this.artworkImageView = artworkImageView;
			return this;
		}

		/**
		 * Sets the artwork ImageView to use when building the ViewHolder. The supplied resource ID
		 * must correspond to a child of the root view, and the child must be an ImageView.
		 *
		 * @param artworkImageViewResId
		 * 		the resource ID of the artwork ImageView
		 * @return this Builder
		 * @throws IllegalArgumentException
		 * 		if {@code artworkImageViewResId} does not correspond to a ImageView
		 * @throws IllegalArgumentException
		 * 		if {@code artworkImageViewResId} does not correspond to a child of the root view
		 */
		public Builder withArtworkImageView(final int artworkImageViewResId) {
			final ImageView view = (ImageView) rootView.findViewById(artworkImageViewResId);
			NullChecker.checkNotNull(view, "no corresponding child");
			withArtworkImageView(view);
			return this;
		}

		/**
		 * Sets the contextual menu button to use when building the ViewHolder.
		 *
		 * @param contextualMenuButton
		 * 		the clickable view to use as the contextual menu button, not null
		 * @return this Builder
		 * @throws IllegalArgumentException
		 * 		if {@code contextualMenuButton} is null
		 */
		public Builder withContextualMenuButton(final View contextualMenuButton) {
			NullChecker.checkNotNull(contextualMenuButton, "contextualMenuButton cannot be null");

			// All checks must be pass before modifying the member variables
			this.contextualMenuButton = contextualMenuButton;
			return this;
		}

		/**
		 * Sets the contextual menu button to use when building the ViewHolder. The supplied
		 * resource ID must correspond to a child of the root view, and the child must be a Button.
		 *
		 * @param contextualMenuButtonResId
		 * 		the resource ID of the clickable view to use as the contextual menu button
		 * @return this Builder
		 * @throws IllegalArgumentException
		 * 		if {@code contextualMenuButtonResId} does not correspond to a child of the root view
		 */
		public Builder withContextualMenuButton(final int contextualMenuButtonResId) {
			final View view = rootView.findViewById(contextualMenuButtonResId);
			NullChecker.checkNotNull(view, "no corresponding child");
			withContextualMenuButton(view);
			return this;
		}

		/**
		 * Constructs a BodyViewHolder from this Builder.
		 *
		 * @return the new BodyViewHolder
		 */
		public BodyViewHolder build() {
			return new BodyViewHolder(this);
		}
	}
}