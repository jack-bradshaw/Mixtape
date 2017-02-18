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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.view.View;
import android.widget.Button;

import com.matthewtamlin.mixtape.library.data.DisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.ImmutableDisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.databinders.ArtworkBinder;
import com.matthewtamlin.mixtape.library.databinders.SubtitleBinder;
import com.matthewtamlin.mixtape.library.databinders.TitleBinder;
import com.matthewtamlin.mixtape.library.mixtape_body.GridBody;
import com.matthewtamlin.mixtape.library.mixtape_body.ListBody;
import com.matthewtamlin.mixtape.library.mixtape_body.RecyclerViewBody;
import com.matthewtamlin.mixtape.library.mixtape_container.CoordinatedMixtapeContainer;
import com.matthewtamlin.mixtape.library.mixtape_header.ToolbarHeader;
import com.matthewtamlin.mixtape.library_tests.R;
import com.matthewtamlin.mixtape.library_tests.stubs.InaccessibleLibraryItem;
import com.matthewtamlin.mixtape.library_tests.stubs.NormalLibraryItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Test harness for the {@link CoordinatedMixtapeContainer} class.
 */
@SuppressLint("SetTextI18n") // Not important during testing
public class CoordinatedMixtapeContainerTestHarness extends
		MixtapeContainerViewTestHarness<ToolbarHeader, RecyclerViewBody> {
	/**
	 * The number of library items to display in the view.
	 */
	private static final int NUMBER_OF_ITEMS = 100;

	/**
	 * The view under test.
	 */
	private CoordinatedMixtapeContainer testView;

	/**
	 * The defaults to use when binding data to the test view.
	 */
	private DisplayableDefaults defaults;

	/**
	 * Size limited cache for storing titles.
	 */
	private final LruCache<LibraryItem, CharSequence> titleCache = new LruCache<>(1000);

	/**
	 * Size limited cache for storing subtitles.
	 */
	private final LruCache<LibraryItem, CharSequence> subtitleCache = new LruCache<>(1000);

	/**
	 * Size limited cache for storing artwork.
	 */
	private final LruCache<LibraryItem, Drawable> artworkCache =
			new LruCache<LibraryItem, Drawable>(1000000) {
				@Override
				protected int sizeOf(final LibraryItem key, final Drawable value) {
					// All LibraryItems use BitmapDrawable for the artwork
					final Bitmap artworkBitmap = ((BitmapDrawable) value).getBitmap();
					return artworkBitmap.getByteCount();
				}
			};

	/**
	 * The item to display in the header.
	 */
	private LibraryItem headerItem;

	/**
	 * The items to display in the body.
	 */
	private List<LibraryItem> bodyItems;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bitmap artwork = BitmapFactory.decodeResource(getResources(), R.raw.default_artwork);

		defaults = new ImmutableDisplayableDefaults("Default title", "Default subtitle",
				new BitmapDrawable(getResources(), artwork));

		getControlsContainer().addView(createUseToolbarHeaderButton());
		getControlsContainer().addView(createUseNullHeaderButton());
		getControlsContainer().addView(createUseGridBodyButton());
		getControlsContainer().addView(createUseListBodyButton());
		getControlsContainer().addView(createUseNullBodyButton());
		getControlsContainer().addView(createShowHeaderAlwaysButton());
		getControlsContainer().addView(createHideHeaderAlwaysButton());
		getControlsContainer().addView(createShowHeaderAtTopOnly());
		getControlsContainer().addView(createShowHeaderOnDownwardsScrollOnly());
	}

	@Override
	public CoordinatedMixtapeContainer getTestView() {
		if (testView == null) {
			testView = new CoordinatedMixtapeContainer(this);
		}

		return testView;
	}

	/**
	 * Creates the header and body items and assigns them to member variables.
	 */
	private void createLibraryItems() {
		headerItem = new NormalLibraryItem(getResources(), "Header title", "Header subtitle",
				R.raw.real_artwork);

		bodyItems = new ArrayList<>();

		for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
			if (new Random().nextBoolean()) {
				bodyItems.add(new NormalLibraryItem(getResources(),
						"Title " + i,
						"Subtitle " + i,
						R.raw.real_artwork));
			} else {
				bodyItems.add(new InaccessibleLibraryItem());
			}
		}
	}

	/**
	 * Creates a button which can be clicked ot set the header of the test view to a new {@link
	 * ToolbarHeader}.
	 *
	 * @return the button, not null
	 */
	private Button createUseToolbarHeaderButton() {
		final Button b = new Button(this);
		b.setText("Use toolbar header");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final ToolbarHeader header =
						new ToolbarHeader(CoordinatedMixtapeContainerTestHarness.this);

				header.setItem(headerItem);
				header.setTitleDataBinder(new TitleBinder(titleCache, defaults));
				header.setSubtitleDataBinder(new SubtitleBinder(subtitleCache, defaults));
				header.setArtworkDataBinder(new ArtworkBinder(artworkCache, defaults));

				testView.setHeader(header);
			}
		});

		return b;
	}

	/**
	 * Creates a button which can be clicked to clear the test view header.
	 *
	 * @return the button, not null
	 */
	private Button createUseNullHeaderButton() {
		final Button b = new Button(this);
		b.setText("Remove header");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				testView.setHeader(null);
			}
		});

		return b;
	}

	/**
	 * Creates a button which can be clicked to set the body of the test view to a new {@link
	 * GridBody}.
	 *
	 * @return the button, not null
	 */
	private Button createUseGridBodyButton() {
		final Button b = new Button(this);
		b.setText("Use grid body");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final GridBody body = new GridBody(CoordinatedMixtapeContainerTestHarness.this);

				body.setItems(bodyItems);
				body.setTitleDataBinder(new TitleBinder(titleCache, defaults));
				body.setSubtitleDataBinder(new SubtitleBinder(subtitleCache, defaults));
				body.setArtworkDataBinder(new ArtworkBinder(artworkCache, defaults));

				testView.setBody(body);
			}
		});

		return b;
	}

	/**
	 * Creates a button which can be clicked to set the body of the test view to a new {@link
	 * ListBody}.
	 *
	 * @return the button, not null
	 */
	private Button createUseListBodyButton() {
		final Button b = new Button(this);
		b.setText("Use list body");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final ListBody body = new ListBody(CoordinatedMixtapeContainerTestHarness.this);

				body.setItems(bodyItems);
				body.setTitleDataBinder(new TitleBinder(titleCache, defaults));
				body.setSubtitleDataBinder(new SubtitleBinder(subtitleCache, defaults));
				body.setArtworkDataBinder(new ArtworkBinder(artworkCache, defaults));

				testView.setBody(body);
			}
		});

		return b;
	}

	/**
	 * Creates a button which can be clicked to clear the test view body.
	 *
	 * @return the button, not null
	 */
	private Button createUseNullBodyButton() {
		final Button b = new Button(this);
		b.setText("Clear body");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				testView.setBody(null);
			}
		});

		return b;
	}

	/**
	 * Creates a button which can be clicked to configure the test view to always show the header.
	 *
	 * @return the button, not null
	 */
	private Button createShowHeaderAlwaysButton() {
		final Button b = new Button(this);
		b.setText("Show header always");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				testView.showHeaderAlways();
			}
		});

		return b;
	}

	/**
	 * Creates a button which can be clicked to configure the test view to always hide the header.
	 *
	 * @return the button, not null
	 */
	private Button createHideHeaderAlwaysButton() {
		final Button b = new Button(this);
		b.setText("Hide header always");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				testView.hideHeaderAlways();
			}
		});

		return b;
	}

	/**
	 * Creates a button which can be clicked to configure the test view to only show the header at
	 * when the top of the body is shown.
	 *
	 * @return the button, not null
	 */
	private Button createShowHeaderAtTopOnly() {
		final Button b = new Button(this);
		b.setText("Show header at top only");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				testView.showHeaderAtStartOnly();
			}
		});

		return b;
	}

	/**
	 * Creates a button which can be clicked to configure the test view to only show the header on
	 * downwards body scroll events.
	 *
	 * @return the button, not null
	 */
	private Button createShowHeaderOnDownwardsScrollOnly() {
		final Button b = new Button(this);
		b.setText("Show header on downwards scroll only");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				testView.showHeaderOnScrollToStartOnly();
			}
		});

		return b;
	}
}