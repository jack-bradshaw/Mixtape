package com.matthewtamlin.mixtape.example;

import android.graphics.Bitmap;

import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;
import com.matthewtamlin.mixtape.library.data.LibraryWriteException;

import java.io.File;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

public class Mp3Item implements LibraryItem {
	private File file;

	public Mp3Item(final File mp3File) {
		file = checkNotNull(mp3File, "mp3File cannot be null.");
	}

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

	}

	@Override
	public void setSubtitle(final CharSequence subtitle)
			throws LibraryReadException, LibraryWriteException {

	}

	@Override
	public void setArtwork(final Bitmap artwork)
			throws LibraryReadException, LibraryWriteException {

	}

	@Override
	public boolean isReadOnly() {
		return false;
	}
}