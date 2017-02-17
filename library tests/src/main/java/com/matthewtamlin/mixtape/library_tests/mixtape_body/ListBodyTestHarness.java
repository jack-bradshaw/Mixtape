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

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.matthewtamlin.mixtape.library.mixtape_body.ListBody;

/**
 * Test harness for testing the {@link ListBody} class.
 */
@SuppressLint("SetTextI18n") // Not important during testing
public class ListBodyTestHarness extends RecyclerViewBodyTestHarness {
	/**
	 * The view being tested in this test harness. The view is null initially and must be created
	 * when needed.
	 */
	private ListBody testView = null;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getControlsContainer().addView(createToggleDividerVisibilityButton());
		getControlsContainer().addView(createToggleArtworkVisibilityButton());
	}

	@Override
	public ListBody getTestView() {
		if (testView == null) {
			testView = new ListBody(this);
		}

		return testView;
	}

	/**
	 * Creates a button which toggles the visibility of the horizontal dividers in the test view
	 * when clicked.
	 *
	 * @return the button, not null
	 */
	private Button createToggleDividerVisibilityButton() {
		final Button b = new Button(this);
		b.setText("Show/hide dividers");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				if (testView != null) {
					testView.showDividers(!testView.dividersAreShown());
				}
			}
		});

		return b;
	}

	/**
	 * Creates a button which toggles the visibility of the artwork in the test view when clicked.
	 *
	 * @return the button, not null
	 */
	private Button createToggleArtworkVisibilityButton() {
		final Button b = new Button(this);
		b.setText("Show/hide artwork");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				if (testView != null) {
					testView.showArtwork(!testView.artworkIsShown());
				}
			}
		});

		return b;
	}
}