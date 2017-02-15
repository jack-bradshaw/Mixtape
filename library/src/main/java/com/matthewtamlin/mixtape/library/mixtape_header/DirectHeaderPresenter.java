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

import android.graphics.drawable.Drawable;

import com.matthewtamlin.mixtape.library.base_mvp.BaseDataSource;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;

/**
 * A simple implementation of the HeaderContract.Presenter interface where the LibraryItem returned
 * by the data source is directly reflected in the view.
 *
 * @param <S>
 * 		the type of data source
 * @param <V>
 * 		the type of view
 */
public class DirectHeaderPresenter<S extends BaseDataSource<LibraryItem>, V extends
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
	public final void setDataSource(final S dataSource) {
		unsubscribeFromDataSourceCallbacks(this.dataSource);
		this.dataSource = dataSource;
		subscribeToDataSourceCallbacks(this.dataSource);

		if (dataSource != null) {
			dataSource.loadData(true, this); // Register this class for callbacks
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
			dataSource.loadData(true, this); // Register this class for callbacks
		}
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
	 * A LibraryItem which has a null title, subtitle and artwork.
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
		public Drawable getArtwork(final int width, final int height) throws LibraryReadException {
			return null;
		}
	}
}