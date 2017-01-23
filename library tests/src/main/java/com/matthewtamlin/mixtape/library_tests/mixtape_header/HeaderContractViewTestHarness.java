package com.matthewtamlin.mixtape.library_tests.mixtape_header;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.matthewtamlin.android_testing_tools.library.ControlsBelowViewTestHarness;
import com.matthewtamlin.mixtape.library.mixtape_header.HeaderContract;
import com.matthewtamlin.mixtape.library_tests.R;
import com.matthewtamlin.mixtape.library_tests.stubs.InaccessibleLibraryItem;
import com.matthewtamlin.mixtape.library_tests.stubs.ReadOnlyLibraryItem;


/**
 * Test harness for testing implementations of the {@link HeaderContract.View} interface. This class
 * provides control buttons for testing core functionality, however the view itself is supplied by
 * the subclass.
 */
@SuppressLint("SetTextI18n") // Not important during testing
public abstract class HeaderContractViewTestHarness extends
		ControlsBelowViewTestHarness<HeaderContract.View> {
	/**
	 * The view under test.
	 */
	private HeaderContract.View testView;

	@Override
	protected void onCreate(final @Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getControlsContainer().addView(createSetItemNormalButton());
		getControlsContainer().addView(createSetItemInaccessibleButton());
		getControlsContainer().addView(createSetItemNullButton());
		getControlsContainer().addView(createSetExtraButtonsButton());
		getControlsContainer().addView(createSetContextualMenuResourceButton());
	}


	/**
	 * Creates a button which sets the item in the test view when clicked. The item will return
	 * metadata when queried.
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
				testView.setItem(new ReadOnlyLibraryItem(getResources(),
						"Title",
						"Subtitle",
						R.raw.real_artwork));
			}
		});

		return b;
	}

	/**
	 * Creates a button which sets the item in the test view when clicked. The item will throw
	 * exceptions when queried.
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
				testView.setItem(new InaccessibleLibraryItem());
			}
		});

		return b;
	}

	/**
	 * Creates a button which sets the item in the test view when clicked. The item will return null
	 * metadata when queried.
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
				testView.setItem(null);
			}
		});

		return b;
	}

	/**
	 * Creates a button which sets the extra buttons in the test view when clicked.
	 *
	 * @return the button, not null
	 */
	private Button createSetExtraButtonsButton() {
		final Button b = new Button(this);
		b.setText("Set extra buttons");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				final Bitmap[] icons = new Bitmap[3];
				icons[0] = BitmapFactory.decodeResource(getResources(), R.drawable.ic_play);
				icons[1] = BitmapFactory.decodeResource(getResources(), R.drawable.ic_share);
				icons[2] = BitmapFactory.decodeResource(getResources(), R.drawable.ic_shuffle);
				testView.setExtraButtons(icons);
			}
		});

		return b;
	}

	/**
	 * Creates a button which sets the contextual menu resource in the test view when clicked.
	 *
	 * @return
	 */
	private Button createSetContextualMenuResourceButton() {
		final Button b = new Button(this);
		b.setText("Set menu resource");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				testView.setOverflowMenuResource(R.menu.test_menu);
			}
		});

		return b;
	}
}