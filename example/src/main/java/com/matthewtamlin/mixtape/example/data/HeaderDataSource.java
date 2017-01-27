package com.matthewtamlin.mixtape.example.data;

import com.matthewtamlin.mixtape.library.data.BaseDataSourceAdapter;
import com.matthewtamlin.mixtape.library.data.LibraryItem;

public class HeaderDataSource extends BaseDataSourceAdapter<LibraryItem> {
	private LibraryItem item;

	public void setData(final LibraryItem item) {
		final LibraryItem oldData = this.item;

		if (this.item != item) {
			this.item = item;

			for (final DataReplacedListener<LibraryItem> listener : getDataReplacedListeners()) {
				listener.onDataReplaced(this, oldData, item);
			}
		}
	}

	@Override
	public void loadData(final boolean forceRefresh,
			final DataLoadedListener<LibraryItem> callback) {
		if (item != null) {
			callback.onDataLoaded(this, item);
		} else {
			callback.onLoadDataFailed(this);
		}
	}
}
