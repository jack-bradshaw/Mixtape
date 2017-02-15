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

package com.matthewtamlin.mixtape.library_tests.mixtape_body;

import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.matthewtamlin.mixtape.library.mixtape_body.BodyViewHolder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the {@link BodyViewHolder} class.
 */
@SuppressWarnings("ResourceType") // Using mock Views so don't need to use actual resource IDs
@RunWith(AndroidJUnit4.class)
public class TestBodyViewHolder {
	/**
	 * A mock view to use as the root view.
	 */
	private View rootView;

	/**
	 * A mock view to use as the title holder.
	 */
	private TextView titleView;

	/**
	 * A mock view to use as the subtitle holder.
	 */
	private TextView subtitleView;

	/**
	 * A mock view to use as the artwork holder.
	 */
	private ImageView artworkView;

	/**
	 * A mock view to use as the contextual menu button.
	 */
	private View menuButton;

	/**
	 * Initialises the testing objects and assigns them to member variables.
	 */
	@Before
	public void setup() {
		rootView = mock(View.class);
		titleView = mock(TextView.class);
		subtitleView = mock(TextView.class);
		artworkView = mock(ImageView.class);
		menuButton = mock(View.class);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code rootView} argument of
	 * {@link BodyViewHolder#BodyViewHolder(View, TextView, TextView, ImageView, View)} is null. The
	 * test will only pass if an IllegalArgumentException is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_nullRootView() {
		new BodyViewHolder(null,
				titleView,
				subtitleView,
				artworkView,
				menuButton);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code titleHolder} argument of
	 * {@link BodyViewHolder#BodyViewHolder(View, TextView, TextView, ImageView, View)} is null. The
	 * test will only pass if an IllegalArgumentException is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_nullTitleView() {
		new BodyViewHolder(rootView,
				null,
				subtitleView,
				artworkView,
				menuButton);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code subtitleHolder} argument
	 * of {@link BodyViewHolder#BodyViewHolder(View, TextView, TextView, ImageView, View)} is null.
	 * The test will only pass if an IllegalArgumentException is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_nullSubitleView() {
		new BodyViewHolder(rootView,
				titleView,
				null,
				artworkView,
				menuButton);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code artworkHolder} argument
	 * of {@link BodyViewHolder#BodyViewHolder(View, TextView, TextView, ImageView, View)} is null.
	 * The test will only pass if an IllegalArgumentException is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_nullArtworkView() {
		new BodyViewHolder(rootView,
				titleView,
				subtitleView,
				null,
				menuButton);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code contextualMenuButton}
	 * argument of {@link BodyViewHolder#BodyViewHolder(View, TextView, TextView, ImageView, View)}
	 * is null. The test will only pass if an IllegalArgumentException is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_nullMenuButton() {
		new BodyViewHolder(rootView,
				titleView,
				subtitleView,
				artworkView,
				null);
	}

	/**
	 * Test to verify that the {@link BodyViewHolder#BodyViewHolder(View, TextView, TextView,
	 * ImageView, View)} constructor functions correctly when provided with valid arguments. The
	 * test will only pass if the getters return the values passed to the constructor.
	 */
	@Test
	public void testConstructor_validArgs() {
		final BodyViewHolder holder = new BodyViewHolder(rootView,
				titleView,
				subtitleView,
				artworkView,
				menuButton);

		assertThat(holder.getRootView(), is(rootView));
		assertThat(holder.getTitleTextView(), is(titleView));
		assertThat(holder.getSubtitleTextView(), is(subtitleView));
		assertThat(holder.getArtworkImageView(), is(artworkView));
		assertThat(holder.getContextualMenuButton(), is(menuButton));
	}
}