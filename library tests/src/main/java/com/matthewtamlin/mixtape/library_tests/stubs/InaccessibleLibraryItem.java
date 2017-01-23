package com.matthewtamlin.mixtape.library_tests.stubs;

import android.graphics.Bitmap;

import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;
import com.matthewtamlin.mixtape.library.data.LibraryWriteException;


/**
 * An implementation of the LibraryItem interface for use in testing. All attempts to read or write
 * metadata result in exceptions being thrown.
 */
public final class InaccessibleLibraryItem implements LibraryItem {
	@Override
	public final CharSequence getTitle() throws LibraryReadException {
		throw new LibraryReadException();
	}

	@Override
	public final CharSequence getSubtitle() throws LibraryReadException {
		throw new LibraryReadException();
	}

	@Override
	public final Bitmap getArtwork(int width, int height) throws LibraryReadException {
		throw new LibraryReadException();
	}

	@Override
	public final void setTitle(CharSequence title)
			throws LibraryReadException, LibraryWriteException {
		throw new LibraryReadException();
	}

	@Override
	public final void setSubtitle(CharSequence subtitle)
			throws LibraryReadException, LibraryWriteException {
		throw new LibraryReadException();
	}

	@Override
	public final void setArtwork(Bitmap artwork)
			throws LibraryReadException, LibraryWriteException {
		throw new LibraryReadException();
	}

	@Override
	public final boolean isReadOnly() {
		return false;
	}
}