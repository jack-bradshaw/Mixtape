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

package com.matthewtamlin.mixtape.library_tests.mixtape_container;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.matthewtamlin.android_testing_tools.library.ControlsOverViewTestHarness;
import com.matthewtamlin.mixtape.library.mixtape_body.BodyContract;
import com.matthewtamlin.mixtape.library.mixtape_container.MixtapeContainerView;
import com.matthewtamlin.mixtape.library.mixtape_header.HeaderContract;


/**
 * Test harness for testing implementations of the {@link MixtapeContainerView} interface. This
 * class provides control buttons for testing core functionality, however the view itself is
 * supplied by the subclass.
 */
@SuppressLint("SetTextI18n") // Not important during testing
public abstract class MixtapeContainerTestHarness<H extends HeaderContract.View, B extends
		BodyContract.View> extends ControlsOverViewTestHarness<MixtapeContainerView<H, B>> {
	@Override
	protected void onCreate(final @Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getControlsContainer().addView(createChangeHeaderElevationButton());
		getControlsContainer().addView(createChangeBodyElevationButton());
	}

	/**
	 * Creates a button which sets the material elevation of the header in the test view when
	 * clicked.
	 *
	 * @return the button, not null
	 */
	private Button createChangeHeaderElevationButton() {
		final Button b = new Button(this);
		b.setText("Set header elevation to 10dp");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getTestView().setHeaderElevationDp(10);
			}
		});

		return b;
	}

	/**
	 * Creates a button which sets the material elevation of the body in the test view when
	 * clicked.
	 *
	 * @return the button, not null
	 */
	private Button createChangeBodyElevationButton() {
		final Button b = new Button(this);
		b.setText("Set body elevation to 10dp");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getTestView().setBodyElevationDp(10);
			}
		});

		return b;
	}
}