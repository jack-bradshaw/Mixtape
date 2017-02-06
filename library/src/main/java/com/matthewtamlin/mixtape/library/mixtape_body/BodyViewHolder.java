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

/**
 * A ViewHolder for use in a RecyclerViewBody. The view holder contains a root view, views for
 * displaying a LibraryItem metadata, and a view which can be clicked to display a contextual menu.
 */
public final class BodyViewHolder extends RecyclerView.ViewHolder {
	/**
	 * The root view.
	 */
	private final View rootView;

	/**
	 * A TextView for displaying LibraryItem titles. Must be a child of the root view.
	 */
	private final TextView titleHolder;

	/**
	 * A TextView for displaying LibraryItem subtitles. Must be a child of the root view.
	 */
	private final TextView subtitleHolder;

	/**
	 * An ImageView for displaying LibraryItem artwork. Must be a child of the root view.
	 */
	private final ImageView artworkHolder;

	/**
	 * A view which can be clicked to display a contextual menu. Must be a child of the root view.
	 */
	private final View contextualMenuButton;

	/**
	 * Constructs a new BodyViewHolder. The root view must be a hierarchical parent of the other
	 * views.
	 *
	 * @param rootView
	 * 		the root view of the RecyclerView item, not null
	 * @param titleHolder
	 * 		a UI element for displaying LibraryItem titles, not null
	 * @param subtitleHolder
	 * 		a UI element for displaying LibraryItem subtitles, not null
	 * @param artworkHolder
	 * 		a UI element for displaying LibraryItem artwork, not null
	 * @param contextualMenuButton
	 * 		a UI element which can be clicked to display a contextual menu, not null
	 * @throws IllegalArgumentException
	 * 		if {@code rootView} is null
	 * @throws IllegalArgumentException
	 * 		if {@code titleHolder} is null
	 * @throws IllegalArgumentException
	 * 		if {@code subtitleHolder} is null
	 * @throws IllegalArgumentException
	 * 		if {@code artworkHolder} is null
	 * @throws IllegalArgumentException
	 * 		if {@code contextualMenuButton} is null
	 */
	public BodyViewHolder(final View rootView,
			final TextView titleHolder,
			final TextView subtitleHolder,
			final ImageView artworkHolder,
			final View contextualMenuButton) {
		super(rootView);

		this.rootView = NullChecker.checkNotNull(titleHolder);
		this.titleHolder = NullChecker.checkNotNull(titleHolder);
		this.subtitleHolder = NullChecker.checkNotNull(subtitleHolder);
		this.artworkHolder = NullChecker.checkNotNull(artworkHolder);
		this.contextualMenuButton = NullChecker.checkNotNull(contextualMenuButton);
	}

	/**
	 * @return the root view, not null
	 */
	public final View getRootView() {
		return rootView;
	}

	/**
	 * @return the view for displaying LibraryItem titles, not null
	 */
	public final TextView getTitleTextView() {
		return titleHolder;
	}

	/**
	 * @return the view for displaying LibraryItem subtitles, not null
	 */
	public final TextView getSubtitleTextView() {
		return subtitleHolder;
	}

	/**
	 * @return the view for displaying LibraryItem artwork, not null
	 */
	public final ImageView getArtworkImageView() {
		return artworkHolder;
	}

	/**
	 * @return the view which can be clicked to display a contextual menu, not null
	 */
	public final View getContextualMenuButton() {
		return contextualMenuButton;
	}
}