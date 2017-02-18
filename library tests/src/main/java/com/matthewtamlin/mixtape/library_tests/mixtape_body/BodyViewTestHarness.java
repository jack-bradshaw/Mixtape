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

import com.matthewtamlin.android_testing_tools.library.ControlsOverViewTestHarness;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.mixtape_body.BodyContract;
import com.matthewtamlin.mixtape.library_tests.R;
import com.matthewtamlin.mixtape.library_tests.stubs.InaccessibleLibraryItem;
import com.matthewtamlin.mixtape.library_tests.stubs.NormalLibraryItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Test harness for subclasses of the {@link BodyContract.View} interface. This class provides
 * buttons for accessing core functionality, however the view itself is supplied by the subclass.
 */
@SuppressLint("SetTextI18n") // Not important during testing
public abstract class BodyViewTestHarness extends
		ControlsOverViewTestHarness<BodyContract.View> {
	/**
	 * The number of items to display in the view.
	 */
	private static final int NUMBER_OF_ITEMS = 100;

	/**
	 * The ID of the resource to use for item artwork.
	 */
	private static final int ARTWORK_RES_ID = R.raw.real_artwork;

	/**
	 * The items displayed in the view.
	 */
	private final List<LibraryItem> items = new ArrayList<>();

	@Override
	protected void onCreate(final @Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getControlsContainer().addView(createSetItemsButton());
		getControlsContainer().addView(createSetContextualMenuResourceButton());
		getControlsContainer().addView(createGoToTopButton());
		getControlsContainer().addView(createGoToEndButton());
		getControlsContainer().addView(createAddItemButton());
		getControlsContainer().addView(createRemoveItemButton());
		getControlsContainer().addView(createMoveItemButton());
		getControlsContainer().addView(createToggleLoadingIndicatorVisibilityButton());
	}

	/**
	 * Creates a button which can be clicked to display the items in the test view.
	 *
	 * @return the button, not null
	 */
	private Button createSetItemsButton() {
		final Button b = new Button(this);
		b.setText("Set items");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				items.clear();

				for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
					if (new Random().nextBoolean()) {
						items.add(new NormalLibraryItem(getResources(),
								"Title " + i,
								"Subtitle " + i,
								ARTWORK_RES_ID));
					} else {
						items.add(new InaccessibleLibraryItem());
					}
				}

				getTestView().setItems(items);
			}
		});

		return b;
	}

	/**
	 * Creates a button which can be clicked to set the contextual menu resource of the test view.
	 *
	 * @return the button, not null
	 */
	private Button createSetContextualMenuResourceButton() {
		final Button b = new Button(this);
		b.setText("Set menu resource");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().setContextualMenuResource(R.menu.test_menu);
			}
		});

		return b;
	}

	/**
	 * Creates a button which can be clicked to go to the top of the test view.
	 *
	 * @return the button, not null
	 */
	private Button createGoToTopButton() {
		final Button b = new Button(this);
		b.setText("Go to top");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().showItem(0);
			}
		});

		return b;
	}

	/**
	 * Creates a button which can be clicked to go to the end of the test view.
	 *
	 * @return the button, not null
	 */
	private Button createGoToEndButton() {
		final Button b = new Button(this);
		b.setText("Go to end");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				final int endIndex = getTestView().getItems().size() - 1;
				getTestView().showItem(endIndex);
			}
		});

		return b;
	}

	/**
	 * Creates a button which can be clicked to add an item to the test view.
	 *
	 * @return the button, not null
	 */
	private Button createAddItemButton() {
		final Button b = new Button(this);
		b.setText("Add item");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				final LibraryItem newItem = new NormalLibraryItem(getResources(),
						"Title " + items.size(),
						"Subtitle " + items.size(),
						ARTWORK_RES_ID);

				items.add(0, newItem);
				getTestView().notifyItemAdded(0);
			}
		});

		return b;
	}

	/**
	 * Creates a button which can be clicked to remove the first item in the test view.
	 *
	 * @return the button, not null
	 */
	private Button createRemoveItemButton() {
		final Button b = new Button(this);
		b.setText("Remove item 0");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				if (items.size() > 0) {
					items.remove(0);
					getTestView().notifyItemRemoved(0);
				}
			}
		});

		return b;
	}

	/**
	 * Creates a button which can be clicked to swap the first two items in the test view.
	 *
	 * @return the button, not null
	 */
	private Button createMoveItemButton() {
		final Button b = new Button(this);
		b.setText("Swap first two items");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				if (items.size() > 1) {
					final LibraryItem item = items.get(0);
					items.remove(0);
					items.add(1, item);

					getTestView().notifyItemMoved(0, 1);
				}
			}
		});

		return b;
	}

	/**
	 * Creates a button which can be clicked to toggle the visibility of the loading indicator in
	 * the test view.
	 *
	 * @return the button, not null
	 */
	private Button createToggleLoadingIndicatorVisibilityButton() {
		final Button b = new Button(this);
		b.setText("Show/hide loading indicator");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().showLoadingIndicator(!getTestView().loadingIndicatorIsShown());
			}
		});

		return b;
	}
}