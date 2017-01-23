package com.matthewtamlin.mixtape.library_tests.core;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.matthewtamlin.mixtape.library.mixtape_coordinator.CoordinatedMixtapeContainer;
import com.matthewtamlin.mixtape.library_tests.mixtape_body.GridBodyTestHarness;
import com.matthewtamlin.mixtape.library_tests.mixtape_body.ListBodyTestHarness;
import com.matthewtamlin.mixtape.library_tests.mixtape_header.SmallHeaderTestHarness;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Provides access to the test harnesses.
 */
@SuppressLint("SetTextI18n") // Not important during testing
public final class TestLauncher extends AppCompatActivity {
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final LinearLayout rootView = new LinearLayout(this);
		rootView.setOrientation(LinearLayout.VERTICAL);
		setContentView(rootView, new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));

		rootView.addView(createLaunchTestGridBodyButton());
		rootView.addView(createLaunchTestListBodyButton());
		rootView.addView(createLaunchTestSmallHeaderButton());
		rootView.addView(createLaunchTestCoordinatedHeaderBodyViewButton());
	}

	/**
	 * Creates a button which launches the GridBodyTestHarness test harness when clicked.
	 *
	 * @return the button
	 */
	private Button createLaunchTestGridBodyButton() {
		final Button b = new Button(this);
		b.setText("Launch GridBodyTestHarness activity");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				startActivity(new Intent(TestLauncher.this, GridBodyTestHarness.class));
			}
		});

		return b;
	}

	/**
	 * Creates a button which launches the ListBodyTestHarness test harness when clicked.
	 *
	 * @return the button
	 */
	private Button createLaunchTestListBodyButton() {
		final Button b = new Button(this);
		b.setText("Launch ListBodyTestHarness activity");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				startActivity(new Intent(TestLauncher.this, ListBodyTestHarness.class));
			}
		});

		return b;
	}

	/**
	 * Creates a button which launches the SmallHeaderTestHarness test harness when clicked.
	 *
	 * @return the button
	 */
	private Button createLaunchTestSmallHeaderButton() {
		final Button b = new Button(this);
		b.setText("Launch SmallHeaderTestHarness activity");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				startActivity(new Intent(TestLauncher.this, SmallHeaderTestHarness.class));
			}
		});

		return b;
	}

	/**
	 * Creates a button which launches the CoordinatedMixtapeContainerTestHarness test harness when
	 * clicked.
	 *
	 * @return the button
	 */
	private Button createLaunchTestCoordinatedHeaderBodyViewButton() {
		final Button b = new Button(this);
		b.setText("Launch CoordinatedMixtapeContainerTestHarness activity");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				startActivity(new Intent(TestLauncher.this, CoordinatedMixtapeContainer.class));
			}
		});

		return b;
	}
}