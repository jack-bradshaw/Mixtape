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

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.matthewtamlin.mixtape.library.mixtape_body.BodyViewHolder;
import com.matthewtamlin.mixtape.library_tests.test.R;

import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * Unit tests for the {@link BodyViewHolder} class.
 */
@SuppressWarnings("ResourceType") // Using mock Views so don't need to use actual resource IDs
@RunWith(AndroidJUnit4.class)
public class TestBodyViewHolder {
	/**
	 * A view for use in testing.
	 */
	private View rootView;

	/**
	 * A TextView for use in testing. This view is the child of the root view.
	 */
	private TextView titleView;

	/**
	 * A TextView for use in testing. This view is the child of the root view.
	 */
	private TextView subtitleView;

	/**
	 * An ImageView for use in testing. This view is the child of the root view.
	 */
	private ImageView artworkView;

	/**
	 * A View for use in testing. This view is the child of the root view.
	 */
	private View contextualMenuButton;

	@Before
	public void setup() {
		// Inflate the root view and get references to its children
		final Context context = InstrumentationRegistry.getContext();
		rootView = LayoutInflater.from(context).inflate(R.layout.testbodyviewholder, null);
		titleView = (TextView) rootView.findViewById(R.id.testBodyViewHolder_titleView);
		subtitleView = (TextView) rootView.findViewById(R.id.testBodyViewHolder_subtitleView);
		artworkView = (ImageView) rootView.findViewById(R.id.testBodyViewHolder_artworkView);
		contextualMenuButton = rootView.findViewById(R.id.testBodyViewHolder_contextualMenuButton);
	}
}