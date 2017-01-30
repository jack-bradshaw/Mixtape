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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.matthewtamlin.mixtape.library.caching.LibraryItemCache;
import com.matthewtamlin.mixtape.library.caching.LruLibraryItemCache;
import com.matthewtamlin.mixtape.library.data.DisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.ImmutableDisplayableDefaults;
import com.matthewtamlin.mixtape.library.databinders.ArtworkBinder;
import com.matthewtamlin.mixtape.library.databinders.SubtitleBinder;
import com.matthewtamlin.mixtape.library.databinders.TitleBinder;
import com.matthewtamlin.mixtape.library.mixtape_body.RecyclerViewBody;
import com.matthewtamlin.mixtape.library_tests.R;

import java.util.Random;

/**
 * Test harness for testing subclasses of the {@link RecyclerViewBody} class. This class provides
 * control buttons for testing core functionality, however the view itself is supplied by the
 * subclass.
 */
@SuppressLint("SetTextI18n") // Not important during testing
public abstract class RecyclerViewBodyTestHarness extends MixtapeBodyViewTestHarness {
	/**
	 * The cache to use when binding data to the test view.
	 */
	private final LibraryItemCache cache = new LruLibraryItemCache(10000, 10000, 10000);

	/**
	 * The defaults to use when binding data to the test view.
	 */
	private DisplayableDefaults defaults;

	/**
	 * The artwork fade duration to use when transitioning artwork in the test view, measured in
	 * milliseconds.
	 */
	private int fadeDurationMs = 0;

	@Override
	public abstract RecyclerViewBody getTestView();

	@Override
	protected void onCreate(final @Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bitmap artwork = BitmapFactory.decodeResource(getResources(), R.raw.default_artwork);
		defaults = new ImmutableDisplayableDefaults("Default title", "Default subtitle", artwork);

		getControlsContainer().addView(createChangeTitleBinderButton());
		getControlsContainer().addView(createChangeSubtitleBinderButton());
		getControlsContainer().addView(createChangeArtworkBinderButton());
		getControlsContainer().addView(createIncreaseFadeDurationButton());
		getControlsContainer().addView(createDecreaseFadeDurationButton());
		getControlsContainer().addView(createChangeLoadingIndicatorColorButton());
	}

	/**
	 * Creates a button which changes the colour of the loading indicator in the test view when
	 * clicked. The new colour is randomly selected from all available colours.
	 *
	 * @return the button, not null
	 */
	private Button createChangeLoadingIndicatorColorButton() {
		final Button b = new Button(this);
		b.setText("Change indicator color");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				final Random random = new Random();
				getTestView().setLoadingIndicatorColor(Color.argb(255, random.nextInt(255), random
						.nextInt(255), random.nextInt(255)));
			}
		});

		return b;
	}

	/**
	 * Creates a button which changes the title binder of the test view when clicked.
	 *
	 * @return the button, not null
	 */
	private Button createChangeTitleBinderButton() {
		final Button b = new Button(this);
		b.setText("Change title binder");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().setTitleDataBinder(new TitleBinder(cache, defaults));
			}
		});

		return b;
	}

	/**
	 * Creates a button which changes the subtitle binder of the test view when clicked.
	 *
	 * @return the button, not null
	 */
	private Button createChangeSubtitleBinderButton() {
		final Button b = new Button(this);
		b.setText("Change subtitle binder");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().setSubtitleDataBinder(new SubtitleBinder(cache, defaults));
			}
		});

		return b;
	}

	/**
	 * Creates a button which changes the artwork binder of the test view when clicked.
	 *
	 * @return the button, not null
	 */
	private Button createChangeArtworkBinderButton() {
		final Button b = new Button(this);
		b.setText("Change artwork binder");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().setArtworkDataBinder(new ArtworkBinder(cache, defaults,
						fadeDurationMs));
			}
		});

		return b;
	}

	/**
	 * Creates a button which increases the artwork fade duration of the test view when clicked.
	 *
	 * @return the button, not null
	 */
	private Button createIncreaseFadeDurationButton() {
		final Button b = new Button(this);
		b.setText("Inc. fade time");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				fadeDurationMs += 25;
				getTestView().setArtworkDataBinder(new ArtworkBinder(cache, defaults,
						fadeDurationMs));

			}
		});

		return b;
	}

	/**
	 * Creates a button which decreases the artwork fade duration of the test view when clicked.
	 *
	 * @return the button, not null
	 */
	private Button createDecreaseFadeDurationButton() {
		final Button b = new Button(this);
		b.setText("Dec. fade time");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				// Never less than zero
				fadeDurationMs = Math.max(fadeDurationMs - 25, 0);
				getTestView().setArtworkDataBinder(new ArtworkBinder(cache, defaults,
						fadeDurationMs));
			}
		});

		return b;
	}
}