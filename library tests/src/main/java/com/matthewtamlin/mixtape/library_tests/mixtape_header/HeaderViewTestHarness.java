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

package com.matthewtamlin.mixtape.library_tests.mixtape_header;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.matthewtamlin.android_testing_tools.library.ControlsBelowViewTestHarness;
import com.matthewtamlin.mixtape.library.mixtape_header.HeaderView;
import com.matthewtamlin.mixtape.library_tests.R;
import com.matthewtamlin.mixtape.library_tests.stubs.InaccessibleLibraryItem;
import com.matthewtamlin.mixtape.library_tests.stubs.NormalLibraryItem;


/**
 * Test harness for implementations of the {@link HeaderView} interface. This class provides
 * control buttons for accessing core functionality, however the view itself is supplied by
 * the subclass.
 */
@SuppressLint("SetTextI18n") // Not important during testing
public abstract class HeaderViewTestHarness extends
		ControlsBelowViewTestHarness<HeaderView> {
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getControlsContainer().addView(createSetItemNormalButton());
		getControlsContainer().addView(createSetItemInaccessibleButton());
		getControlsContainer().addView(createSetItemNullButton());
	}


	/**
	 * Creates a button which can be clicked to display a normal item in the test view.
	 *
	 * @return the button, not null
	 */
	private Button createSetItemNormalButton() {
		final Button b = new Button(this);
		b.setText("Set item (normal)");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().setItem(new NormalLibraryItem(getResources(),
						"Title",
						"Subtitle",
						R.raw.real_artwork));
			}
		});

		return b;
	}

	/**
	 * Creates a button which can be clicked to display an inaccessible item in the test view.
	 *
	 * @return the button, not null
	 */
	private Button createSetItemInaccessibleButton() {
		final Button b = new Button(this);
		b.setText("Set item (inaccessible)");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().setItem(new InaccessibleLibraryItem());
			}
		});

		return b;
	}

	/**
	 * Creates a button which can be clicked to display nothing in the test view.
	 *
	 * @return the button, not null
	 */
	private Button createSetItemNullButton() {
		final Button b = new Button(this);
		b.setText("Set item (null)");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().setItem(null);
			}
		});

		return b;
	}
}