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


/**
 * Test harness for the {@link GridBody} class.
 */
@SuppressLint("SetTextI18n") // Not important during testing
public class GridBodyTestHarness extends RecyclerViewBodyTestHarness {
	/**
	 * The view under test.
	 */
	private GridBody testView = null;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getControlsContainer().addView(createIncreaseColumnCountButton());
		getControlsContainer().addView(createDecreaseColumnCountButton());
	}

	@Override
	public GridBody getTestView() {
		if (testView == null) {
			testView = new GridBody(this);
		}

		return testView;
	}

	/**
	 * Creates a button which can be clicked to increase the number of columns displayed in the test
	 * view.
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
				final int currentCount = getTestView().getNumberOfColumns();
				getTestView().setNumberOfColumns(currentCount + 1);
			}
		});

		return b;
	}

	/**
	 * Creates a button which can be clicked to decrease the number of columns displayed in the test
	 * view.
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
				final int currentCount = getTestView().getNumberOfColumns();
				getTestView().setNumberOfColumns(Math.max(currentCount - 1, 1));
			}
		});

		return b;
	}
}