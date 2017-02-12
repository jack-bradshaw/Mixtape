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

import com.matthewtamlin.mixtape.library.base_mvp.BaseDataSource;
import com.matthewtamlin.mixtape.library.base_mvp.ListDataSource;
import com.matthewtamlin.mixtape.library.data.LibraryItem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple implementation of the BodyContract.Presenter interface where the list returned by the
 * data source is directly reflected in the view. Listeners can be registered to handle user
 * input events.
 *
 * @param <D>
 * 		the type of data to present
 * @param <S>
 * 		the type of data source to present from
 * @param <V>
 * 		the type of view to present to
 */
public class DirectBodyPresenter<
		D extends LibraryItem,
		S extends ListDataSource<D>,
		V extends BodyContract.View>
		implements BodyContract.Presenter<D, S, V> {
	private final Set<LibraryItemSelectedListener<D, S, V>> libraryItemSelectedListeners =
			new HashSet<>();

	private final Set<ContextualMenuItemSelectedListener<D, S, V>>
			contextualMenuItemSelectedListeners = new HashSet<>();

	/**
	 * The data source to present from.
	 */
	private S dataSource;

	/**
	 * The view to present to.
	 */
	private V view;

	@Override
	public void setDataSource(final S dataSource) {
		unsubscribeFromDataSourceCallbacks(this.dataSource);
		this.dataSource = dataSource;
		subscribeToDataSourceCallbacks(this.dataSource);

		if (dataSource != null) {
			dataSource.loadData(false, this);
		}
	}

	@Override
	public S getDataSource() {
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
	public V getView() {
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
			view.setItems(null);
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
	public void onDataModified(final BaseDataSource<List<D>> source, final List<D> data) {
		if (view != null) {
			view.notifyItemsChanged();
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
	public void onItemModified(final ListDataSource<D> source, final D modified,
			final int index) {
		if (view != null) {
			view.notifyItemModified(index);
		}
	}

	@Override
	public void onDataMoved(final ListDataSource<D> source, final D moved, final int initialIndex,
			final int finalIndex) {
		if (view != null) {
			view.notifyItemMoved(initialIndex, finalIndex);
		}
	}

	@Override
	public void onLibraryItemSelected(final BodyContract.View bodyView, final LibraryItem item) {
		for (final LibraryItemSelectedListener<D, S, V> listener : libraryItemSelectedListeners) {
			// Should be fine so long as there is no external interference with the view's data
			// noinspection unchecked
			listener.onLibraryItemSelected(this, (D) item);
		}
	}

	@Override
	public void onContextualMenuItemSelected(final BodyContract.View bodyView,
			final LibraryItem libraryItem,
			final MenuItem menuItem) {
		for (final ContextualMenuItemSelectedListener<D, S, V> listener :
				contextualMenuItemSelectedListeners) {
			// Should be fine so long as there is no external interference with view's data
			// noinspection unchecked
			listener.onContextualMenuItemSelected(this, (D) libraryItem, menuItem);
		}
	}

	/**
	 * Registers a library item selected listener to this presenter. If the supplied listener is
	 * null or is already registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	public void registerListener(final LibraryItemSelectedListener<D, S, V> listener) {
		if (listener != null) {
			libraryItemSelectedListeners.add(listener);
		}
	}

	/**
	 * Unregisters a library item selected listener from this data source. If the supplied listener
	 * is null or is not registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to unregister
	 */
	public void unregisterListener(final LibraryItemSelectedListener<D, S, V> listener) {
		libraryItemSelectedListeners.remove(listener);
	}

	/**
	 * Registers a contextual menu item selected listener to this presenter. If the supplied
	 * listener is null or is already registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	public void registerListener(final ContextualMenuItemSelectedListener<D, S, V> listener) {
		if (listener != null) {
			contextualMenuItemSelectedListeners.add(listener);
		}
	}

	/**
	 * Unregisters a contextual menu item selected listener from this data source. If the supplied
	 * listener is null or is not registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to unregister
	 */
	public void unregisterListener(final ContextualMenuItemSelectedListener<D, S, V> listener) {
		contextualMenuItemSelectedListeners.remove(listener);
	}

	/**
	 * Unsubscribes this presenter from all callbacks delivered by the supplied data source.
	 *
	 * @param dataSource
	 * 		the data source to unsubscribe from, may be null
	 */
	protected void unsubscribeFromDataSourceCallbacks(final S dataSource) {
		if (dataSource != null) {
			dataSource.unregisterDataReplacedListener(this);
			dataSource.unregisterDataModifiedListener(this);
			dataSource.unregisterLongOperationListener(this);
			dataSource.unregisterItemAddedListener(this);
			dataSource.unregisterItemRemovedListener(this);
			dataSource.unregisterItemMovedListener(this);
			dataSource.unregisterItemModifiedListener(this);
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
			dataSource.registerDataReplacedListener(this);
			dataSource.registerDataModifiedListener(this);
			dataSource.registerLongOperationListener(this);
			dataSource.registerItemAddedListener(this);
			dataSource.registerItemRemovedListener(this);
			dataSource.registerItemModifiedListener(this);
			dataSource.registerItemMovedListener(this);
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

	/**
	 * Callback to be invoked when a DirectBodyPresenter receives an item selection event from the
	 * view it is presenting to.
	 *
	 * @param <D>
	 * 		the type of data being presented
	 * @param <S>
	 * 		the type of data source being presented from
	 * @param <V>
	 * 		the type of view being presented to
	 */
	public interface LibraryItemSelectedListener<
			D extends LibraryItem,
			S extends ListDataSource<D>,
			V extends BodyContract.View> {
		/**
		 * Invoked when a DirectBodyPresenter receives an item selection event from the view is is
		 * presenting to.
		 *
		 * @param presenter
		 * 		the presenter, not null
		 * @param item
		 * 		the selected item, not null
		 */
		public void onLibraryItemSelected(DirectBodyPresenter<D, S, V> presenter, final D item);
	}

	/**
	 * Callback to be invoked when a DirectBodyPresenter receives a contextual menu item selection
	 * event from the view it is presenting to.
	 *
	 * @param <D>
	 * 		the type of data being presented
	 * @param <S>
	 * 		the type of data source being presented from
	 * @param <V>
	 * 		the type of view being presented to
	 */
	public interface ContextualMenuItemSelectedListener<
			D extends LibraryItem,
			S extends ListDataSource<D>,
			V extends BodyContract.View> {

		/**
		 * Invoked when a DirectBodyPresenter receives a contextual menu item selection event from
		 * the view it is presenting to
		 *
		 * @param presenter
		 * 		the presenter, not null
		 * @param libraryItem
		 * 		the library item the contextual menu is attached to, not null
		 * @param menuItem
		 * 		the selected menu item, not null
		 */
		public void onContextualMenuItemSelected(DirectBodyPresenter<D, S, V> presenter,
				final D	libraryItem, final MenuItem menuItem);
	}
}