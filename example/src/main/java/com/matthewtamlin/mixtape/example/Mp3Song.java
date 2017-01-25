package com.matthewtamlin.mixtape.example;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.matthewtamlin.android_utilities.library.helpers.BitmapEfficiencyHelper;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;
import com.matthewtamlin.mixtape.library.data.LibraryWriteException;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;

import java.io.File;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

public class Mp3Song implements LibraryItem {
	private File mp3File;

	public Mp3Song(final File mp3File) {
		this.mp3File = checkNotNull(mp3File, "mp3File cannot be null.");
	}

	@Override
	public CharSequence getTitle() throws LibraryReadException {
		return readId3Field(FieldKey.TITLE)
	}

	@Override
	public CharSequence getSubtitle() throws LibraryReadException {
		return readId3Field(FieldKey.ARTIST);
	}

	@Override
	public Bitmap getArtwork(final int width, final int height) throws LibraryReadException {
		try {
			final Tag tag = AudioFileIO.read(mp3File).getTag();
			final Artwork artwork = tag == null ? null : tag.getFirstArtwork();
			return artworkToBitmap(artwork, width, height);
		} catch (Exception e) {
			throw new LibraryReadException("Cannot read ID3 tag from mp3File " + mp3File, e);
		}
	}

	private String readId3Field(FieldKey fieldKey) throws LibraryReadException {
		try {
			final Tag tag = AudioFileIO.read(mp3File).getTag();
			return tag == null ? null : tag.getFirst(fieldKey);
		} catch (final Exception e) {
			throw new LibraryReadException("Cannot read ID3 tag from mp3File " + mp3File, e);
		}
	}

	private Bitmap artworkToBitmap(final Artwork artwork, final int width, final int height) {
		final byte[] rawBitmapArray = (artwork == null) ? null : artwork.getBinaryData();

		if (rawBitmapArray == null) {
			return null;
		} else if (width == 0 || height == 0) {
			return BitmapFactory.decodeByteArray(rawBitmapArray, 0, rawBitmapArray.length);
		} else {
			return BitmapEfficiencyHelper.decodeByteArray(rawBitmapArray, width, height);
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