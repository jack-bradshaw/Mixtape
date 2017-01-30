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

import com.matthewtamlin.mixtape.library.mixtape_body.GridBody;

import static android.support.v7.widget.AppCompatDrawableManager.get;


/**
 * Test harness for testing the {@link GridBody} class.
 */
@SuppressLint("SetTextI18n") // Not important during testing
public class GridBodyTestHarness extends RecyclerViewBodyTestHarness {
	/**
	 * The view being tested in this test harness. The view is null initially and must be created
	 * when needed.
	 */
	private GridBody testView = null;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


	}

	@Override
	public GridBody getTestView() {
		if (testView == null) {
			testView = new GridBody(this);
		}

		return testView;
	}

	/**
	 * Creates the control buttons and adds them to the layout.
	 */
	private void createControlButtons() {
		getControlsContainer().addView(createIncreaseColumnCountButton());
		getControlsContainer().addView(createDecreaseColumnCountButton());
	}

	/**
	 * Creates a button which increases the number of columns in the test view when clicked.
	 *
	 * @return the button, not null
	 */
	private Button createIncreaseColumnCountButton() {
		final Button b = new Button(this);
		b.setText("Inc. col count");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				final int currentNumberOfColumns = getTestView().getNumberOfColumns();
				getTestView().setNumberOfColumns(currentNumberOfColumns + 1);
			}
		});

		return b;
	}

	/**
	 * Creates a button which decreases the number of columns in the test view when clicked.
	 *
	 * @return the button, not null
	 */
	private Button createDecreaseColumnCountButton() {
		final Button b = new Button(this);
		b.setText("Dec. col count");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				final int currentNumberOfColumns = getTestView().getNumberOfColumns();

				if (currentNumberOfColumns > 1) {
					getTestView().setNumberOfColumns(currentNumberOfColumns - 1);
				}
			}
		});

		return b;
	}
}