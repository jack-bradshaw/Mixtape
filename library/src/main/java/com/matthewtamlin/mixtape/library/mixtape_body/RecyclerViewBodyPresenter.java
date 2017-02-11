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

package com.matthewtamlin.mixtape.library.mixtape_body;

import android.view.MenuItem;

import com.matthewtamlin.java_utilities.checkers.NullChecker;
import com.matthewtamlin.mixtape.library.base_mvp.BaseDataSource;
import com.matthewtamlin.mixtape.library.base_mvp.ListDataSource;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.databinders.ArtworkBinder;
import com.matthewtamlin.mixtape.library.databinders.SubtitleBinder;
import com.matthewtamlin.mixtape.library.databinders.TitleBinder;

import java.util.List;

/**
 * A DirectBodyPresenter which can be used with a RecyclerViewBody. Although this class is not
 * abstract, the user interaction handling methods do nothing. To handle user interactions, override
 * {@link #onLibraryItemSelected(BodyContract.View, LibraryItem)} and {@link
 * #onContextualMenuItemSelected (BodyContract.View, LibraryItem, MenuItem)}.
 *
 * @param <D>
 * 		the type of data to present
 * @param <S>
 * 		the type of data source to present from
 */
public class RecyclerViewBodyPresenter<
		D extends LibraryItem,
		S extends ListDataSource<D>>
		extends
		DirectBodyPresenter<D, S, RecyclerViewBody> {
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
	 * Constructs a new ToolbarHeaderPresenter.
	 *
	 * @param titleDataBinder
	 * 		the DataBinder to use when binding titles to the UI, not null
	 * @param subtitleDataBinder
	 * 		the DataBinder to use when binding subtitles to the UI, not null
	 * @param artworkDataBinder
	 * 		the DataBinder to use when binding artwork to the UI, not null
	 * @throws IllegalArgumentException
	 * 		if {@code titleDataBinder} is null
	 * @throws IllegalArgumentException
	 * 		if {@code subtitleDataBinder} is null
	 * @throws IllegalArgumentException
	 * 		if {@code artworkDataBinder} is null
	 */
	public RecyclerViewBodyPresenter(final TitleBinder titleDataBinder,
			final SubtitleBinder subtitleDataBinder,
			final ArtworkBinder artworkDataBinder) {
		super();

		this.titleDataBinder = NullChecker.checkNotNull(titleDataBinder,
				"titleDataBinder cannot be null");
		this.subtitleDataBinder = NullChecker.checkNotNull(subtitleDataBinder,
				"subtitleDataBinder cannot be null");
		this.artworkDataBinder = NullChecker.checkNotNull(artworkDataBinder,
				"artworkDataBinder cannot be null");
	}

	@Override
	public void setView(final RecyclerViewBody view) {
		super.setView(view);

		if (view != null) {
			view.setTitleDataBinder(titleDataBinder);
			view.setSubtitleDataBinder(subtitleDataBinder);
			view.setArtworkDataBinder(artworkDataBinder);
		}
	}


	@Override
	public void onDataModified(final BaseDataSource<List<D>> source, final List<D> data) {
		// If the old data is not removed from the cache, the data binders will not update the UI
		titleDataBinder.getCache().evictAll();
		subtitleDataBinder.getCache().evictAll();
		artworkDataBinder.getCache().evictAll();

		super.onDataModified(source, data);
	}

	@Override
	public void onItemModified(final ListDataSource<D> source, final D modified, final int index) {
		// If the old data is not removed from the cache, the data binders will not update the UI
		titleDataBinder.getCache().remove(modified);
		subtitleDataBinder.getCache().remove(modified);
		artworkDataBinder.getCache().remove(modified);

		super.onItemModified(source, modified, index);
	}

	@Override
	public void onLibraryItemSelected(final BodyContract.View bodyView, final LibraryItem item) {
		// Default implementation does nothing
	}

	@Override
	public void onContextualMenuItemSelected(final BodyContract.View bodyView, final LibraryItem
			libraryItem, final MenuItem menuItem) {
		// Default implementation does nothing
	}

	/**
	 * @return the TitleBinder used to bind titles to the UI, not null
	 */
	public TitleBinder getTitleDataBinder() {
		return titleDataBinder;
	}

	/**
	 * @return the SubtitleBinder used to bind subtitles to the UI, not null
	 */
	public SubtitleBinder getSubtitleDataBinder() {
		return subtitleDataBinder;
	}

	/**
	 * @return the ArtworkBinder used to bind artwork to the UI, not null
	 */
	public ArtworkBinder getArtworkDataBinder() {
		return artworkDataBinder;
	}
}