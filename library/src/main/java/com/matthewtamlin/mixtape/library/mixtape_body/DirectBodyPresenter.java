package com.matthewtamlin.mixtape.library.mixtape_body;

import com.matthewtamlin.java_utilities.testing.Tested;
import com.matthewtamlin.mixtape.library.base_mvp.BaseDataSource;
import com.matthewtamlin.mixtape.library.base_mvp.ListDataSource;
import com.matthewtamlin.mixtape.library.data.LibraryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple implementation of the BodyContract.Presenter interface where the data structure returned
 * by the data source is directly reflected in the view.
 *
 * @param <S>
 * 		the type of data source
 * @param <V>
 * 		the type of view
 */
@Tested(testMethod = "unit")
public abstract class DirectBodyPresenter<S extends ListDataSource<LibraryItem>, V extends
		BodyContract.View> implements BodyContract.Presenter<S, V> {
	/**
	 * The data source supplying the LibraryItems.
	 */
	private S dataSource;

	/**
	 * The view being driven.
	 */
	private V view;

	@Override
	public void present(final boolean forceRefresh) {
		if (dataSource != null) {
			dataSource.loadData(forceRefresh, null); // Already listening so don't pass a callback
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
	public void onDataLoaded(BaseDataSource<List<LibraryItem>> source, List<LibraryItem> data) {
		if (view != null) {
			view.setItems(data);
		}
	}

	@Override
	public void onLoadDataFailed(final BaseDataSource source) {
		if (view != null) {
			view.setItems(new ArrayList<LibraryItem>());
		}
	}

	@Override
	public void onDataModified(final BaseDataSource<List<LibraryItem>> source,
			final List<LibraryItem> data) {
		if (view != null) {
			view.notifyItemsChanged();
		}
	}

	@Override
	public void onDataMoved(final ListDataSource<LibraryItem> source, final LibraryItem object,
			final int initialIndex, final int finalIndex) {
		if (view != null) {
			view.notifyItemMoved(initialIndex, finalIndex);
		}
	}

	@Override
	public void onDataAdded(final ListDataSource<LibraryItem> source, final LibraryItem object,
			final int index) {
		if (view != null) {
			view.notifyItemAdded(index);
		}
	}

	@Override
	public void onDataRemoved(final ListDataSource<LibraryItem> source, final LibraryItem object,
			final int index) {
		if (view != null) {
			view.notifyItemRemoved(index);
		}
	}

	@Override
	public void onDataReplaced(final BaseDataSource<List<LibraryItem>> source,
			final List<LibraryItem> oldData, final List<LibraryItem> newData) {
		if (view != null) {
			view.setItems(newData);
		}
	}

	@Override
	public void onListItemModified(final ListDataSource<LibraryItem> source, final LibraryItem
			object, final int index) {
		if (view != null) {
			view.notifyItemModified(index);
		}
	}

	@Override
	public void onLongOperationStarted(final BaseDataSource source) {
		if (view != null) {
			view.showLoadingIndicator(true);
		}
	}

	@Override
	public void onLongOperationFinished(final BaseDataSource source) {
		if (view != null) {
			view.showLoadingIndicator(false);
		}
	}

	/**
	 * Unsubscribes this presenter from all data source callbacks.
	 *
	 * @param dataSource
	 * 		the data source to unsubscribe from, may be null
	 */
	protected void unsubscribeFromDataSourceCallbacks(final S dataSource) {
		if (dataSource != null) {
			dataSource.unregisterDataAddedListener(this);
			dataSource.unregisterDataRemovedListener(this);
			dataSource.unregisterDataReplacedListener(this);
			dataSource.unregisterDataMovedListener(this);
			dataSource.unregisterDataModifiedListener(this);
			dataSource.unregisterListItemModifiedListener(this);
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
			dataSource.registerDataAddedListener(this);
			dataSource.registerDataRemovedListener(this);
			dataSource.registerDataReplacedListener(this);
			dataSource.registerDataMovedListener(this);
			dataSource.registerDataModifiedListener(this);
			dataSource.registerListItemModifiedListener(this);
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
}