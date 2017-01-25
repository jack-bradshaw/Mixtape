package com.matthewtamlin.mixtape.library.mixtape_body;

import android.view.MenuItem;

import com.matthewtamlin.java_utilities.checkers.NullChecker;
import com.matthewtamlin.java_utilities.testing.Tested;
import com.matthewtamlin.mixtape.library.base_mvp.BaseDataSource;
import com.matthewtamlin.mixtape.library.base_mvp.ListDataSource;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.databinders.ArtworkBinder;
import com.matthewtamlin.mixtape.library.databinders.SubtitleBinder;
import com.matthewtamlin.mixtape.library.databinders.TitleBinder;

import java.util.List;

/**
 * A presenter for use with {@link RecyclerViewBody} views. The default implementation does not
 * handle user interaction. To handle user interaction, override the following methods: <ul>
 * <li>{@link #onItemClicked(BodyContract.View, LibraryItem)}</li> <li>{@link
 * #onContextualMenuItemClicked(BodyContract.View, LibraryItem, MenuItem)} </li> </ul>
 *
 * @param <S>
 * 		the type of data source
 */
@Tested(testMethod = "unit")
public class RecyclerViewBodyPresenter<
		D extends LibraryItem,
		S extends ListDataSource<D>>
		extends
		DirectBodyPresenter<D, S, RecyclerViewBody> {
	/**
	 * Binds title data to the view.
	 */
	private TitleBinder titleDataBinder;

	/**
	 * Binds subtitle data to the view.
	 */
	private SubtitleBinder subtitleDataBinder;

	/**
	 * Binds artwork data to the view.
	 */
	private ArtworkBinder artworkDataBinder;

	/**
	 * Constructs a new SmallHeaderPresenter. The supplied DataBinders are passed to the view to
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
	public RecyclerViewBodyPresenter(final TitleBinder titleDataBinder, final SubtitleBinder
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
	public void setView(RecyclerViewBody view) {
		super.setView(view);

		if (view != null) {
			view.setTitleDataBinder(titleDataBinder);
			view.setSubtitleDataBinder(subtitleDataBinder);
			view.setArtworkDataBinder(artworkDataBinder);
		}
	}


	@Override
	public void onDataModified(BaseDataSource<List<D>> source,	List<D> data) {
		// If the old data is not removed from the cache, the data binders will not update the UI
		titleDataBinder.getCache().clearTitles();
		subtitleDataBinder.getCache().clearSubtitles();
		artworkDataBinder.getCache().clearArtwork();

		super.onDataModified(source, data);
	}

	@Override
	public void onListItemModified(ListDataSource<D> source, D item, int index) {
		// If the old data is not removed from the cache, the data binders will not update the UI
		titleDataBinder.getCache().removeTitle(item);
		subtitleDataBinder.getCache().removeSubtitle(item);
		artworkDataBinder.getCache().removeArtwork(item);

		super.onListItemModified(source, item, index);
	}

	@Override
	public void onItemClicked(final BodyContract.View hostView, final LibraryItem item) {
		// Default implementation does nothing
	}

	@Override
	public void onContextualMenuItemClicked(final BodyContract.View hostView, final LibraryItem
			libraryItem, final MenuItem menuItem) {
		// Default implementation does nothing
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