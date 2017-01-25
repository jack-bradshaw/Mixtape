package com.matthewtamlin.mixtape.example;

import android.graphics.Bitmap;

import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;
import com.matthewtamlin.mixtape.library.data.LibraryWriteException;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;
import java.util.ArrayList;

public class Playlist extends ArrayList<Mp3Song> implements LibraryItem {
	@Override
	public CharSequence getTitle() throws LibraryReadException {
		if (isEmpty()) {
			return "Empty album";
		} else {
			return readId3Field(get(0).getMp3File(), FieldKey.ALBUM);
		}
	}

	@Override
	public CharSequence getSubtitle() throws LibraryReadException {
		if (isEmpty()) {
			return null;
		} else {
			return readId3Field(get(0).getMp3File(), FieldKey.ALBUM_ARTIST);
		}
	}

	@Override
	public Bitmap getArtwork(final int width, final int height) throws LibraryReadException {
		if (isEmpty()) {
			return null;
		} else {
			return get(0).getArtwork(width, height);
		}
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

	private String readId3Field(final File mp3File, final FieldKey fieldKey)
			throws LibraryReadException {
		try {
			final Tag tag = AudioFileIO.read(mp3File).getTag();
			return tag == null ? null : tag.getFirst(fieldKey);
		} catch (final Exception e) {
			throw new LibraryReadException("Cannot read ID3 tag from file " + mp3File, e);
		}
	}
}
