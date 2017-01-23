package com.matthewtamlin.mixtape.library_tests.stubs;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;
import com.matthewtamlin.mixtape.library.data.LibraryWriteException;


/**
 * An implementation of the LibraryItem interface for use in testing. Metadata is stored locally,
 * such that read and write exceptions never occur. Metadata can be modified at any time.
 */
public final class ReadOnlyLibraryItem implements LibraryItem {
	private Resources resources;

	/**
	 * The title of this item.
	 */
	private CharSequence title;

	/**
	 * The subtitle of this item.
	 */
	private CharSequence subtitle;

	/**
	 * The artwork of this item.
	 */
	private int artworkId;

	/**
	 * Constructs a new ReadOnlyLibraryItem.
	 *
	 * @param title
	 * 		the title of this item
	 * @param subtitle
	 * 		the subtitle of this item
	 * @param artworkId
	 * 		a resource ID which specifies the artwork of this item
	 */
	public ReadOnlyLibraryItem(final Resources resources, final CharSequence title,
			final CharSequence subtitle, final int artworkId) {
		this.resources = resources;
		this.title = title;
		this.subtitle = subtitle;
		this.artworkId = artworkId;
	}

	@Override
	public CharSequence getTitle() throws LibraryReadException {
		return title;
	}

	@Override
	public CharSequence getSubtitle() throws LibraryReadException {
		return subtitle;
	}

	@Override
	public Bitmap getArtwork(int width, int height) throws LibraryReadException {
		return BitmapFactory.decodeResource(resources, artworkId);
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

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		} else if (obj == this) {
			return true;
		} else {
			return obj.getClass().equals(ReadOnlyLibraryItem.class);
		}
	}

	@Override
	public String toString() {
		return "Library item, title: " + title;
	}
}