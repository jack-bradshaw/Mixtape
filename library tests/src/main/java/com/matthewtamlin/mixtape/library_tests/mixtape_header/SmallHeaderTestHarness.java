package com.matthewtamlin.mixtape.library_tests.mixtape_header;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.matthewtamlin.mixtape.library.mixtape_header.SmallHeader;
import com.matthewtamlin.mixtape.library_tests.R;


/**
 * Test harness for testing the {@link SmallHeader} class.
 */
@SuppressLint("SetTextI18n") // Not important during testing
public class SmallHeaderTestHarness extends HeaderContractViewTestHarness {
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

	/**
	 * The view under test.
	 */
	private SmallHeader smallHeader;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bitmap artwork = BitmapFactory.decodeResource(getResources(), R.raw.default_artwork);
		defaults = new ImmutableDisplayableDefaults("Default title", "Default artwork", artwork);

		getControlsContainer().addView(createChangeTitleBinderButton());
		getControlsContainer().addView(createChangeSubtitleBinderButton());
		getControlsContainer().addView(createChangeArtworkBinderButton());
	}

	@Override
	public SmallHeader getTestView() {
		if (smallHeader == null) {
			smallHeader = new SmallHeader(this);
		}

		return smallHeader;
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
}