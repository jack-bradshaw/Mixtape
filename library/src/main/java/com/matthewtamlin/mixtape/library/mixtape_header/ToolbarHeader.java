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
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
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

import timber.log.Timber;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

//TODO fix javadoc
/**
 * An implementation of the HeaderContract.View interface which displays a toolbar in addition to
 * the LibraryItem. The toolbar can be used to display actions specific to the item in context. This
 * view must be provided with DataBinders in order to function properly.
 */
public class ToolbarHeader extends FrameLayout implements HeaderContract.View {
	/**
	 * Contains supporting business logic and handles user interaction.
	 */
	private Presenter presenter;

	/**
	 * The item to display.
	 */
	private LibraryItem data;

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
	 * Displays the toolbar. This view should never contain more than one direct child view.
	 */
	private FrameLayout toolbarContainer;

	/**
	 * Constructs a new SimpleHeader.
	 *
	 * @param context
	 * 		the Context the header is attached to, not null
	 */
	public ToolbarHeader(final Context context) {
		super(context);
		init();
	}

	/**
	 * Constructs a new ToolbarHeader.
	 *
	 * @param context
	 * 		the Context the header is attached to, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 */
	public ToolbarHeader(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * Constructs a new ToolbarHeader.
	 *
	 * @param context
	 * 		the Context the header is attached to, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 * @param defStyleAttr
	 * 		an attribute in the current theme which supplies default attributes, pass 0	to ignore
	 */
	public ToolbarHeader(final Context context, final AttributeSet attrs, final int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	/**
	 * Constructs a new ToolbarHeader.
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
	public ToolbarHeader(final Context context, final AttributeSet attrs, final int defStyleAttr,
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

	public void setToolbar(final Toolbar toolbar) {
		toolbarContainer.removeAllViews();
		toolbarContainer.addView(toolbar);
	}

	public Toolbar getToolbar() {
		if (toolbarContainer.getChildCount() > 0) {
			return (Toolbar) toolbarContainer.getChildAt(0);
		} else {
			return null;
		}
	}

	/**
	 * @return the TextView used to display the item title
	 */
	public TextView getTitleContainer() {
		return titleTextView;
	}

	/**
	 * @return the TextView used to display the item subtitle
	 */
	public TextView getSubtitleContainer() {
		return subtitleTextView;
	}

	/**
	 * @return the ImageView used to display the item artwork
	 */
	public ImageView getArtworkContainer() {
		return artworkImageView;
	}

	/**
	 * Initialises this view. This method should only be called from a constructor.
	 */
	private void init() {
		inflate(getContext(), R.layout.toolbarheader, this);

		titleTextView = (TextView) findViewById(R.id.toolbarHeader_titleContainer);
		subtitleTextView = (TextView) findViewById(R.id.toolbarHeader_subtitleContainer);
		artworkImageView = (ImageView) findViewById(R.id.toolbarHeader_artworkContainer);
		toolbarContainer = (FrameLayout) findViewById(R.id.toolbarHeader_toolbarContainer);

		titleTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (presenter != null) {
					presenter.onTitleClicked(ToolbarHeader.this);
				}
			}
		});

		subtitleTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (presenter != null) {
					presenter.onSubtitleClicked(ToolbarHeader.this);
				}
			}
		});

		artworkImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (presenter != null) {
					presenter.onArtworkClicked(ToolbarHeader.this);
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
			Timber.w("No title data binder set, could not bind title.");
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
			Timber.w("No subtitle data binder set, could not bind subtitle.");
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
			Timber.w("No artwork data binder set, could not bind artwork.");
		}
	}
}