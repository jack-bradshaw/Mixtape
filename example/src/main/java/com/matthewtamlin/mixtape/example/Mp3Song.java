package com.matthewtamlin.mixtape.example;

import android.graphics.Bitmap;

import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;
import com.matthewtamlin.mixtape.library.data.LibraryWriteException;

import java.io.File;
import java.io.IOException;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

public class Mp3Song implements LibraryItem {
	private File mp3File;

	public Mp3Song(final File mp3File) {
		this.mp3File = checkNotNull(mp3File, "mp3File cannot be null.");
	}

	public File getMp3File() {
		return mp3File;
	}

	@Override
	public CharSequence getTitle() throws LibraryReadException {
		try {
			return Id3Util.getMetadataFromId3Tag(mp3File, Id3Util.MetadataField.TITLE);
		} catch (final IOException e) {
			throw new LibraryReadException("Cannot read ID3 tag from file " + mp3File, e);
		}
	}

	@Override
	public CharSequence getSubtitle() throws LibraryReadException {
		try {
			return Id3Util.getMetadataFromId3Tag(mp3File, Id3Util.MetadataField.ARTIST);
		} catch (final IOException e) {
			throw new LibraryReadException("Cannot read ID3 tag from file " + mp3File, e);
		}
	}

	@Override
	public Bitmap getArtwork(final int width, final int height) throws LibraryReadException {
		try {
			return Id3Util.getCoverArtFromId3Tag(mp3File, width, height);
		} catch (final IOException e) {
			throw new LibraryReadException("Cannot read ID3 tag from file " + mp3File, e);
		}
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

	@Override
	public void setTitle(final CharSequence title)
			throws LibraryReadException, LibraryWriteException {
		throw new LibraryWriteException("Item is read only.");
	}
}