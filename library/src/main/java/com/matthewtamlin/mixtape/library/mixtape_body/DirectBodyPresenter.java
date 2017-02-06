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

import com.matthewtamlin.java_utilities.testing.Tested;
import com.matthewtamlin.mixtape.library.base_mvp.BaseDataSource;
import com.matthewtamlin.mixtape.library.base_mvp.ListDataSource;
import com.matthewtamlin.mixtape.library.data.LibraryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple implementation of the BodyContract.Presenter interface where the list returned by the
 * data source is directly reflected in the view.
 *
 * @param <D>
 * 		the type of data to present
 * @param <S>
 * 		the type of data source to present from
 * @param <V>
 * 		the type of view to present to
 */
@Tested(testMethod = "unit")
public abstract class DirectBodyPresenter<
		D extends LibraryItem,
		S extends ListDataSource<D>,
		V extends BodyContract.View>
		implements BodyContract.Presenter<D, S, V> {
	/**
	 * The data source to present from.
	 */
	private S dataSource;

	/**
	 * The view to present to.
	 */
	private V view;

	@Override
	public final void setDataSource(final S dataSource) {
		unsubscribeFromDataSourceCallbacks(this.dataSource);
		this.dataSource = dataSource;
		subscribeToDataSourceCallbacks(this.dataSource);

		if (dataSource != null) {
			dataSource.loadData(false, this);
		}
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

		if (dataSource != null) {
			dataSource.loadData(false, this);
		}
	}

	@Override
	public final V getView() {
		return view;
	}

	@Override
	public void onDataLoaded(BaseDataSource<List<D>> source, List<D> data) {
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
	public void onDataModified(final BaseDataSource<List<D>> source, final List<D> data) {
		if (view != null) {
			view.notifyItemsChanged();
		}
	}

	@Override
	public void onDataMoved(final ListDataSource<D> source, final D object, final int initialIndex,
			final int finalIndex) {
		if (view != null) {
			view.notifyItemMoved(initialIndex, finalIndex);
		}
	}

	@Override
	public void onDataAdded(final ListDataSource<D> source, final D added, final int index) {
		if (view != null) {
			view.notifyItemAdded(index);
		}
	}

	@Override
	public void onDataRemoved(final ListDataSource<D> source, final D removed, final int index) {
		if (view != null) {
			view.notifyItemRemoved(index);
		}
	}

	@Override
	public void onDataReplaced(final BaseDataSource<List<D>> source, final List<D> oldData,
			final List<D> newData) {
		if (view != null) {
			view.setItems(newData);
		}
	}

	@Override
	public void onListItemModified(final ListDataSource<D> source, final D modified, final int index) {
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
	 * Unsubscribes this presenter from all callbacks delivered by the supplied data source.
	 *
	 * @param dataSource
	 * 		the data source to unsubscribe from, may be null
	 */
	protected void unsubscribeFromDataSourceCallbacks(final S dataSource) {
		if (dataSource != null) {
			dataSource.unregisterItemAddedListener(this);
			dataSource.unregisterItemRemovedListener(this);
			dataSource.unregisterDataReplacedListener(this);
			dataSource.unregisterItemMovedListener(this);
			dataSource.unregisterDataModifiedListener(this);
			dataSource.unregisterItemModifiedListener(this);
			dataSource.unregisterLongOperationListener(this);
		}
	}

	/**
	 * Subscribes this presenter to all callbacks delivered by the supplied source callbacks.
	 *
	 * @param dataSource
	 * 		the data source to subscribe to, may be null
	 */
	protected void subscribeToDataSourceCallbacks(final S dataSource) {
		if (dataSource != null) {
			dataSource.registerItemAddedListener(this);
			dataSource.registerItemRemovedListener(this);
			dataSource.registerDataReplacedListener(this);
			dataSource.registerItemMovedListener(this);
			dataSource.registerDataModifiedListener(this);
			dataSource.registerItemModifiedListener(this);
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
	 * Sets this presenter as the supplied view's presenter.
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