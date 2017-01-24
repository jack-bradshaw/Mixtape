package com.matthewtamlin.mixtape.library.mixtape_header;

import android.graphics.Bitmap;

import com.matthewtamlin.java_utilities.testing.Tested;
import com.matthewtamlin.mixtape.library.base_mvp.BaseDataSource;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;
import com.matthewtamlin.mixtape.library.data.LibraryWriteException;

/**
 * A simple implementation of the HeaderContract.Presenter interface where the LibraryItem returned
 * by the data source is directly reflected in the view.
 *
 * @param <S>
 * 		the type of data source
 * @param <V>
 * 		the type of view
 */
@Tested(testMethod = "unit")
public abstract class DirectHeaderPresenter<S extends BaseDataSource<LibraryItem>, V extends
		HeaderContract.View> implements HeaderContract.Presenter<S, V> {
	/**
	 * The data source supplying the LibraryItems.
	 */
	private S dataSource;

	/**
	 * The view being driven.
	 */
	private V view;

	@Override
	public void present() {
		if (dataSource != null) {
			dataSource.loadData(true, this); // Register this class for callbacks
		}
	}

	@Override
	public final void setDataSource(final S dataSource) {
		unsubscribeFromDataSourceCallbacks(this.dataSource);
		this.dataSource = dataSource;
		subscribeToDataSourceCallbacks(this.dataSource);
	}

	@Override
	public final S getDataSource() {
		return dataSource;
	}

	@Override
	public void setView(final V view) {
		removeViewPresenter(this.view);
		this.view = view;
		setSelfAsViewPresenter(this.view);
	}

	@Override
	public final V getView() {
		return view;
	}

	@Override
	public void onDataLoaded(final BaseDataSource source, final LibraryItem data) {
		if (view != null) {
			view.setItem(data == null ? new EmptyItem() : data);
		}
	}

	@Override
	public void onLoadDataFailed(final BaseDataSource source) {
		if (view != null) {
			view.setItem(new EmptyItem());
		}
	}

	@Override
	public void onDataModified(final BaseDataSource<LibraryItem> source, final LibraryItem data) {
		if (view != null) {
			view.setItem(data == null ? new EmptyItem() : data);
		}
	}

	@Override
	public void onDataReplaced(final BaseDataSource<LibraryItem> source, final LibraryItem oldData,
			LibraryItem newData) {
		if (view != null) {
			view.setItem(newData == null ? new EmptyItem() : newData);
		}
	}

	@Override
	public void onLongOperationStarted(BaseDataSource source) {
		// Do nothing since the view doesn't have a loading indicator
	}

	@Override
	public void onLongOperationFinished(BaseDataSource source) {
		// Do nothing since the view doesn't have a loading indicator
	}

	/**
	 * Unsubscribes this presenter from all data source callbacks.
	 *
	 * @param dataSource
	 * 		the data source to unsubscribe from, may be null
	 */
	protected void unsubscribeFromDataSourceCallbacks(final S dataSource) {
		if (dataSource != null) {
			dataSource.unregisterDataReplacedListener(this);
			dataSource.unregisterDataModifiedListener(this);
			dataSource.unregisterLongOperationListener(this);
		}
	}

	/**
	 * Subscribes this presenter to all data source callbacks.
	 *
	 * @param dataSource
	 * 		the data source to subscribe to, may be null
	 */
	protected void subscribeToDataSourceCallbacks(final S dataSource) {
		if (dataSource != null) {
			dataSource.registerDataReplacedListener(this);
			dataSource.registerDataModifiedListener(this);
			dataSource.registerLongOperationListener(this);
		}
	}

	/**
	 * Removes the presenter of the supplied view.
	 *
	 * @param view
	 * 		the view to modify, may be null
	 */
	protected void removeViewPresenter(final V view) {
		if (view != null) {
			view.setPresenter(null);
		}
	}

	/**
	 * Sets the supplied view to use this instance as its presenter.
	 *
	 * @param view
	 * 		the view to modify, may be null
	 */
	protected void setSelfAsViewPresenter(final V view) {
		if (view != null) {
			view.setPresenter(this);
		}
	}

	/**
	 * A read-only LibraryItem which has a null title, subtitle and artwork.
	 */
	private static class EmptyItem implements LibraryItem {
		@Override
		public CharSequence getTitle() throws LibraryReadException {
			return null;
		}

		@Override
		public CharSequence getSubtitle() throws LibraryReadException {
			return null;
		}

		@Override
		public Bitmap getArtwork(final int width, final int height) throws LibraryReadException {
			return null;
		}

		@Override
		public void setTitle(final CharSequence title)
				throws LibraryReadException, LibraryWriteException {
			throw new LibraryWriteException("Item is read-only.");
		}

		@Override
		public void setSubtitle(final CharSequence subtitle)
				throws LibraryReadException, LibraryWriteException {
			throw new LibraryWriteException("Item is read-only.");
		}

		@Override
		public void setArtwork(final Bitmap artwork)
				throws LibraryReadException, LibraryWriteException {
			throw new LibraryWriteException("Item is read-only.");
		}

		@Override
		public boolean isReadOnly() {
			return true;
		}
	}
}