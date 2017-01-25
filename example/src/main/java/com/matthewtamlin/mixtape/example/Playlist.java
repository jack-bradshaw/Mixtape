package com.matthewtamlin.mixtape.example;

import android.graphics.Bitmap;

import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;
import com.matthewtamlin.mixtape.library.data.LibraryWriteException;

import java.util.ArrayList;

public class Playlist extends ArrayList<Mp3Song> implements LibraryItem {
	private CharSequence title;

	private CharSequence subtitle;

	private Bitmap artwork;

	@Override
	public CharSequence getTitle() throws LibraryReadException {
		return title;
	}

	@Override
	public CharSequence getSubtitle() throws LibraryReadException {
		return subtitle;
	}

	@Override
	public Bitmap getArtwork(final int width, final int height) throws LibraryReadException {
		return artwork;
	}

	@Override
	public void setTitle(final CharSequence title)
			throws LibraryReadException, LibraryWriteException {
		this.title = title;
	}

	@Override
	public void setSubtitle(final CharSequence subtitle)
			throws LibraryReadException, LibraryWriteException {
		this.subtitle = subtitle;
	}

	@Override
	public void setArtwork(final Bitmap artwork)
			throws LibraryReadException, LibraryWriteException {
		this.artwork = artwork;
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}
}
