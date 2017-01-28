package com.matthewtamlin.mixtape.example.data;

import android.graphics.Bitmap;

import com.matthewtamlin.mixtape.library.data.BaseDataSourceAdapter;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;
import com.matthewtamlin.mixtape.library.data.LibraryWriteException;

public class HeaderDataSource extends BaseDataSourceAdapter<LibraryItem> {
	private final LibraryItem item;

	public HeaderDataSource(final CharSequence title, final CharSequence subtitle,
			final Bitmap artwork) {
		this.item = new LibraryItem() {
			@Override
			public CharSequence getTitle() throws LibraryReadException {
				return title;
			}

			@Override
			public CharSequence getSubtitle() throws LibraryReadException {
				return subtitle;
			}

			@Override
			public Bitmap getArtwork(final int width, final int height)
					throws LibraryReadException {
				return artwork;
			}

			@Override
			public void setTitle(final CharSequence title)
					throws LibraryReadException, LibraryWriteException {
				throw new LibraryWriteException("Item is read only.");
			}

			@Override
			public void setSubtitle(final CharSequence subtitle)
					throws LibraryReadException, LibraryWriteException {
				throw new LibraryWriteException("Item is read only.");
			}

			@Override
			public void setArtwork(final Bitmap artwork)
					throws LibraryReadException, LibraryWriteException {
				throw new LibraryWriteException("Item is read only.");
			}

			@Override
			public boolean isReadOnly() {
				return true;
			}
		};
	}

	@Override
	public void loadData(final boolean forceRefresh,
			final DataLoadedListener<LibraryItem> callback) {
		callback.onDataLoaded(this, item);
	}
}