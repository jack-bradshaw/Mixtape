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

package com.matthewtamlin.mixtape.library.mixtape_header;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.Space;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.matthewtamlin.java_utilities.checkers.NullChecker;
import com.matthewtamlin.mixtape.library.R;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.databinders.DataBinder;
import com.matthewtamlin.mixtape.library.mixtape_header.HeaderContract.Presenter;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * A simple implementation of the HeaderContract.View interface. This view must be provided with
 * DataBinders in order to function properly.
 */
public class SmallHeader extends FrameLayout implements HeaderContract.View {
	/**
	 * Used to identify this class in the log.
	 */
	private static final String TAG = "SimpleHeader";

	/**
	 * Contains supporting business logic and handles user interaction.
	 */
	private Presenter presenter;

	/**
	 * The item to display.
	 */
	private LibraryItem data;

	/**
	 * The menu resource to display when the overflow menu button is clicked.
	 */
	private int overflowMenuResourceId = -1;

	/**
	 * Binds title data to the view.
	 */
	private DataBinder<LibraryItem, TextView> titleDataBinder;

	/**
	 * Binds subtitle data to the view.
	 */
	private DataBinder<LibraryItem, TextView> subtitleDataBinder;

	/**
	 * Binds artwork data to the view.
	 */
	private DataBinder<LibraryItem, ImageView> artworkDataBinder;

	/**
	 * Displays the title.
	 */
	private TextView titleTextView;

	/**
	 * Displays the subtitle.
	 */
	private TextView subtitleTextView;

	/**
	 * Displays the artwork.
	 */
	private ImageView artworkImageView;

	/**
	 * Holds the extra buttons.
	 */
	private LinearLayout extraButtonContainer;

	/**
	 * Displays an overflow menu when clicked.
	 */
	private ImageButton overflowMenuButton;

	/**
	 * Constructs a new SimpleHeader.
	 *
	 * @param context
	 * 		the Context the header is attached to, not null
	 */
	public SmallHeader(final Context context) {
		super(context);
		init();
	}

	/**
	 * Constructs a new SimpleHeader.
	 *
	 * @param context
	 * 		the Context the header is attached to, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 */
	public SmallHeader(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * Constructs a new SimpleHeader.
	 *
	 * @param context
	 * 		the Context the header is attached to, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 * @param defStyleAttr
	 * 		an attribute in the current theme which supplies default attributes, pass 0	to ignore
	 */
	public SmallHeader(final Context context, final AttributeSet attrs, final int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	/**
	 * Constructs a new SimpleHeader.
	 *
	 * @param context
	 * 		the Context the header is attached to, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 * @param defStyleAttr
	 * 		an attribute in the current theme which supplies default attributes, pass 0	to ignore
	 * @param defStyleRes
	 * 		a resource which supplies default attributes, only used if {@code defStyleAttr}	is 0, pass
	 * 		0 to ignore
	 */
	@RequiresApi(21) // For caller
	@TargetApi(21) // For lint
	public SmallHeader(final Context context, final AttributeSet attrs, final int defStyleAttr,
			final int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	/**
	 * Sets the DataBinder to use when binding titles, and updates the UI using the new DataBinder.
	 * This method must be called on the UI thread.
	 *
	 * @param titleDataBinder
	 * 		the DataBinder to use for titles
	 */
	public void setTitleDataBinder(final DataBinder<LibraryItem, TextView> titleDataBinder) {
		// Use reference equality since equality is hard to define for multithreaded objects
		if (this.titleDataBinder != titleDataBinder) {
			if (this.titleDataBinder != null) {
				this.titleDataBinder.cancelAll();
			}

			this.titleDataBinder = titleDataBinder;
			updateTitle();
		}
	}

	/**
	 * Sets the DataBinder to use when binding subtitles, and updates the UI using the new
	 * DataBinder. This method must be called on the UI thread.
	 *
	 * @param subtitleDataBinder
	 * 		the DataBinder to use for subtitles
	 */
	public void setSubtitleDataBinder(final DataBinder<LibraryItem, TextView> subtitleDataBinder) {
		// Use reference equality since equality is hard to define for multithreaded objects
		if (this.subtitleDataBinder != subtitleDataBinder) {
			if (this.subtitleDataBinder != null) {
				this.subtitleDataBinder.cancelAll();
			}

			this.subtitleDataBinder = subtitleDataBinder;
			updateSubtitle();
		}
	}

	/**
	 * Sets the DataBinder to use when binding artwork, and updates the UI using the new DataBinder.
	 * This method must be called on the UI thread.
	 *
	 * @param artworkDataBinder
	 * 		the DataBinder to use for artwork
	 */
	public void setArtworkDataBinder(final DataBinder<LibraryItem, ImageView> artworkDataBinder) {
		// Use reference equality since equality is hard to define for multithreaded objects
		if (this.artworkDataBinder != artworkDataBinder) {
			if (this.artworkDataBinder != null) {
				this.artworkDataBinder.cancelAll();
			}

			this.artworkDataBinder = artworkDataBinder;
			updateArtwork();
		}
	}

	@Override
	public void setPresenter(final Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setItem(final LibraryItem item) {
		this.data = item;
		updateTitle();
		updateSubtitle();
		updateArtwork();
	}

	@Override
	public LibraryItem getItem() {
		return data;
	}

	@Override
	public void setExtraButtons(final Bitmap[] buttonIcons) {
		extraButtonContainer.removeAllViews();

		if (buttonIcons != null) {
			for (int i = 0; i < buttonIcons.length; i++) {
				final ImageButton extraButton = createExtraButton(buttonIcons[i]);
				extraButtonContainer.addView(extraButton);

				// Propagate clicks back to the presenter
				final int fixedButtonIndex = i; // Need a constant copy of the iteration variable
				extraButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(final View v) {
						if (presenter != null) {
							presenter.onExtraButtonClicked(SmallHeader.this, fixedButtonIndex);
						}
					}
				});
			}

			addSpacingToExtraButtonContainer();
		}
	}

	@Override
	public void setOverflowMenuResource(final int overflowMenuResource) {
		// The resource is not used until the overflow button is clicked, so save the value
		this.overflowMenuResourceId = overflowMenuResource;
	}

	@Override
	public int getOverflowMenuResource() {
		return overflowMenuResourceId;
	}

	/**
	 * Initialises this view. This method should only be called from a constructor.
	 */
	private void init() {
		inflate(getContext(), R.layout.smallheader, this);
		getViewHandles();
		initialiseOnClickListeners();
		initialiseOverflowMenu();
	}

	/**
	 * Assigns the necessary view references to member variables.
	 */
	private void getViewHandles() {
		try {
			titleTextView = (TextView) NullChecker.checkNotNull(findViewById(R.id
					.smallHeader_titleHolder), "init failed: titleTextView not found");

			subtitleTextView = (TextView) NullChecker.checkNotNull(findViewById(R.id
					.smallHeader_subtitleHolder), "init failed: subtitleTextView not found");

			artworkImageView = (ImageView) NullChecker.checkNotNull(findViewById(R.id
					.smallHeader_artworkHolder), "init failed: artworkImageView not found");

			extraButtonContainer = (LinearLayout) NullChecker.checkNotNull(findViewById(R.id
							.smallHeader_extraButtonContainer),
					"init failed: extraButtonContainer not found");

			overflowMenuButton = (ImageButton) NullChecker.checkNotNull(findViewById(R.id
							.smallHeader_overflowMenuButton),
					"init failed: overflow menu button not found");
		} catch (final IllegalArgumentException e) {
			throw new RuntimeException("layout does not contain all required views");
		}
	}

	/**
	 * Initialises on click listeners for the title view, the subtitle view, and the artwork view.
	 * Clicks will be propagated back to the presenter.
	 */
	private void initialiseOnClickListeners() {
		titleTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (presenter != null) {
					presenter.onTitleClicked(SmallHeader.this);
				}
			}
		});

		subtitleTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (presenter != null) {
					presenter.onSubtitleClicked(SmallHeader.this);
				}
			}
		});

		artworkImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (presenter != null) {
					presenter.onArtworkClicked(SmallHeader.this);
				}
			}
		});
	}

	/**
	 * Initialises the overflow menu. The menu will be shown when the overflow menu button is
	 * pressed, and selections will be propagated back to the presenter. The menu will always
	 * display the current menu resource, unless the resource is changed while the menu is open.
	 */
	private void initialiseOverflowMenu() {
		overflowMenuButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				// If the resource hasn't been set, inflating the menu will fail
				if (overflowMenuResourceId != -1) {
					final PopupMenu menu = new PopupMenu(getContext(), overflowMenuButton);
					menu.inflate(overflowMenuResourceId);
					menu.show();

					// Propagate menu selections back to the presenter
					menu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
						@Override
						public boolean onMenuItemClick(final MenuItem item) {
							if (presenter != null) {
								presenter.onOverflowMenuItemClicked(SmallHeader.this, item);
								return true; // Handled
							} else {
								return false; // Not handled
							}
						}
					});
				}
			}
		});
	}

	/**
	 * Updates the UI to show the title of the current data item. If no title data binder has been
	 * set, this method logs the error and exits normally.
	 */
	private void updateTitle() {
		if (titleDataBinder != null) {
			titleDataBinder.bind(titleTextView, data);
		} else {
			Log.w(TAG, "No title data binder set, could not bind title.");
		}
	}

	/**
	 * Updates the UI to show the subtitle of the current data item. If no subtitle data binder has
	 * been set, this method logs the error and exits normally.
	 */
	private void updateSubtitle() {
		if (subtitleDataBinder != null) {
			subtitleDataBinder.bind(subtitleTextView, data);
		} else {
			Log.w(TAG, "No subtitle data binder set, could not bind subtitle.");
		}
	}

	/**
	 * Updates the UI to show the artwork of the current data item. If no artwork data binder has
	 * been set, this method logs the error and exits normally.
	 */
	private void updateArtwork() {
		if (artworkDataBinder != null) {
			artworkDataBinder.bind(artworkImageView, data);
		} else {
			Log.w(TAG, "No artwork data binder set, could not bind artwork.");
		}
	}

	/**
	 * Constructs a new extra button.
	 *
	 * @param icon
	 * 		the icon to show in the extra button
	 */
	private ImageButton createExtraButton(final Bitmap icon) {
		NullChecker.checkNotNull(icon);

		final ImageButton b = new ImageButton(getContext(), null, R.attr.borderlessButtonStyle);

		b.setMinimumHeight(0);
		b.setMinimumWidth(0);
		b.setPadding(0, b.getPaddingTop(), 0, b.getPaddingBottom());
		b.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
		b.setScaleType(ImageView.ScaleType.CENTER_CROP);
		b.setImageBitmap(icon);

		return b;
	}

	/**
	 * Adds a space between each view currently in the extra button container. The spaces evenly pad
	 * the container contents without changing the bounds of the contents.
	 */
	private void addSpacingToExtraButtonContainer() {
		// Iterate backwards from the end of the container to simplify indexing
		for (int i = extraButtonContainer.getChildCount(); i > 0; i--) {
			final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
					(MATCH_PARENT, MATCH_PARENT);
			layoutParams.weight = 1;

			final Space space = new Space(getContext());
			space.setLayoutParams(layoutParams);
			extraButtonContainer.addView(space, i);
		}
	}
}