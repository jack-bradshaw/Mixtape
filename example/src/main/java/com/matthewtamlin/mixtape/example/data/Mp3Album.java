package com.matthewtamlin.mixtape.example.data;

import android.graphics.Bitmap;

import com.matthewtamlin.mixtape.example.util.Id3Util;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;
import com.matthewtamlin.mixtape.library.data.LibraryWriteException;

import java.io.IOException;
import java.util.ArrayList;

import static com.matthewtamlin.mixtape.example.util.Id3Util.MetadataField.ALBUM;
import static com.matthewtamlin.mixtape.example.util.Id3Util.MetadataField.ALBUM_ARTIST;

public class Mp3Album extends ArrayList<Mp3Song> implements LibraryItem {
	@Override
	public CharSequence getTitle() throws LibraryReadException {
		if (isEmpty()) {
			return "Empty album";
		} else {
			try {
				return Id3Util.getMetadataFromId3Tag(get(0).getMp3File(), ALBUM);
			} catch (final IOException e) {
				throw new LibraryReadException("Cannot read ID3 tag from file " +
						get(0).getMp3File(), e);
			}
		}
	}

	@Override
	public CharSequence getSubtitle() throws LibraryReadException {
		if (isEmpty()) {
			return null;
		} else {
			try {
				return Id3Util.getMetadataFromId3Tag(get(0).getMp3File(), ALBUM_ARTIST);
			} catch (final IOException e) {
				throw new LibraryReadException("Cannot read ID3 tag from file " +
						get(0).getMp3File(), e);
			}
		}
	}

	@Override
	public Bitmap getArtwork(final int width, final int height) throws LibraryReadException {
		if (isEmpty()) {
			return null;
		} else {
			try {
				return Id3Util.getCoverArtFromId3Tag(get(0).getMp3File(), width, height);
			} catch (final IOException e) {
				throw new LibraryReadException("Cannot read ID3 tag from file " +
						get(0).getMp3File(), e);
			}
		}
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
}