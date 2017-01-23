package com.matthewtamlin.mixtape.library.mixtape_header;

import android.view.MenuItem;

import com.matthewtamlin.java_utilities.checkers.NullChecker;
import com.matthewtamlin.java_utilities.testing.Tested;
import com.matthewtamlin.mixtape.library.base_mvp.BaseDataSource;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.databinders.ArtworkBinder;
import com.matthewtamlin.mixtape.library.databinders.SubtitleBinder;
import com.matthewtamlin.mixtape.library.databinders.TitleBinder;


/**
 * Presenter for use with {@link SmallHeader} views. The default implementation does not handle user
 * interaction. To handle user interaction, override the following methods:  <ul> <li>{@link
 * #onTitleClicked(HeaderContract.View)}</li> <li>{@link #onSubtitleClicked(HeaderContract.View)}</li>
 * <li>{@link #onArtworkClicked(HeaderContract.View)}</li> <li>{@link
 * #onExtraButtonClicked(HeaderContract.View, int)}</li> <li>{@link #onOverflowMenuItemClicked
 * (HeaderContract.View, MenuItem)}</li> </ul>
 *
 * @param <S>
 * 		the type of data source
 */
@Tested(testMethod = "unit")
public class SmallHeaderPresenter<S extends BaseDataSource<LibraryItem>>
		extends DirectHeaderPresenter<S, SmallHeader> {
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
	public SmallHeaderPresenter(final TitleBinder titleDataBinder, final SubtitleBinder
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
	public void setView(final SmallHeader view) {
		super.setView(view);

		if (view != null) {
			view.setTitleDataBinder(titleDataBinder);
			view.setSubtitleDataBinder(subtitleDataBinder);
			view.setArtworkDataBinder(artworkDataBinder);
		}
	}

	@Override
	public void onDataModified(final BaseDataSource<LibraryItem> source, final LibraryItem data) {
		titleDataBinder.getCache().removeTitle(data);
		subtitleDataBinder.getCache().removeSubtitle(data);
		artworkDataBinder.getCache().removeArtwork(data);

		super.onDataModified(source, data);
	}

	@Override
	public void onTitleClicked(final HeaderContract.View hostView) {
		// Default implementation does nothing
	}

	@Override
	public void onSubtitleClicked(final HeaderContract.View hostView) {
		// Default implementation does nothing
	}

	@Override
	public void onArtworkClicked(final HeaderContract.View hostView) {
		// Default implementation does nothing
	}

	@Override
	public void onExtraButtonClicked(final HeaderContract.View hostView, int index) {
		// Default implementation does nothing
	}

	@Override
	public void onOverflowMenuItemClicked(final HeaderContract.View hostView, MenuItem menuItem) {
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