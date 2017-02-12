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

import com.matthewtamlin.java_utilities.checkers.NullChecker;
import com.matthewtamlin.mixtape.library.base_mvp.BaseDataSource;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.databinders.ArtworkBinder;
import com.matthewtamlin.mixtape.library.databinders.SubtitleBinder;
import com.matthewtamlin.mixtape.library.databinders.TitleBinder;

/**
 * Presenter for use with {@link ToolbarHeader} views. User interaction can be handled by extending
 * the class and overriding the view callback methods. The default implementations of these methods
 * are empty.
 *
 * @param <S>
 * 		the type of data source to present from
 */
public class ToolbarHeaderPresenter<S extends BaseDataSource<LibraryItem>>
		extends DirectHeaderPresenter<S, ToolbarHeader> {
	/**
	 * Binds title data to the view.
	 */
	private final TitleBinder titleDataBinder;

	/**
	 * Binds subtitle data to the view.
	 */
	private final SubtitleBinder subtitleDataBinder;

	/**
	 * Binds artwork data to the view.
	 */
	private final ArtworkBinder artworkDataBinder;

	/**
	 * Constructs a new ToolbarHeaderPresenter. The supplied DataBinders are passed to the view to
	 * bind data to the UI.
	 *
	 * @param titleDataBinder
	 * 		binds titles to the UI, not null
	 * @param subtitleDataBinder
	 * 		bind subtitle to the UI, not null
	 * @param artworkDataBinder
	 * 		binds artwork to the UI, not null
	 * @throws IllegalArgumentException
	 * 		if {@code titleDataBinder} is null
	 * @throws IllegalArgumentException
	 * 		if {@code subtitleDataBinder} is null
	 * @throws IllegalArgumentException
	 * 		if {@code artworkDataBinder} is null
	 */
	public ToolbarHeaderPresenter(final TitleBinder titleDataBinder, final SubtitleBinder
			subtitleDataBinder, final ArtworkBinder artworkDataBinder) {
		super();

		this.titleDataBinder = NullChecker.checkNotNull(titleDataBinder,
				"titleDataBinder cannot be null");
		this.subtitleDataBinder = NullChecker.checkNotNull(subtitleDataBinder,
				"subtitleDataBinder cannot be null");
		this.artworkDataBinder = NullChecker.checkNotNull(artworkDataBinder,
				"artworkDataBinder cannot be null");
	}

	@Override
	public void setView(final ToolbarHeader view) {
		super.setView(view);

		if (view != null) {
			view.setTitleDataBinder(titleDataBinder);
			view.setSubtitleDataBinder(subtitleDataBinder);
			view.setArtworkDataBinder(artworkDataBinder);
		}
	}

	@Override
	public void onDataModified(final BaseDataSource<LibraryItem> source, final LibraryItem data) {
		titleDataBinder.getCache().remove(data);
		subtitleDataBinder.getCache().remove(data);
		artworkDataBinder.getCache().remove(data);

		super.onDataModified(source, data);
	}

	/**
	 * @return the TitleBinder used to bind titles to the UI, not null
	 */
	public final TitleBinder getTitleDataBinder() {
		return titleDataBinder;
	}

	/**
	 * @return the SubtitleBinder used to bind subtitles to the UI, not null
	 */
	public final SubtitleBinder getSubtitleDataBinder() {
		return subtitleDataBinder;
	}

	/**
	 * @return the ArtworkBinder used to bind artwork to the UI, not null
	 */
	public final ArtworkBinder getArtworkDataBinder() {
		return artworkDataBinder;
	}
}