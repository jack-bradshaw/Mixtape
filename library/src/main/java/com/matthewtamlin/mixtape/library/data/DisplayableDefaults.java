package com.matthewtamlin.mixtape.library.data;

import android.graphics.Bitmap;

/**
 * Supplies titles, subtitles and artwork which can be displayed when a LibraryItem does not provide
 * such data.
 */
public interface DisplayableDefaults {
	/**
	 * Supplies the default title. The same title does not need to be returned each time this method
	 * is called, and null may be returned.
	 *
	 * @return the default title
	 */
	CharSequence getTitle();

	/**
	 * Supplies the default subtitle. The same subtitle does not need to be returned each time this
	 * method is called, and null may be returned.
	 *
	 * @return the default subtitle
	 */
	CharSequence getSubtitle();

	/**
	 * Supplies the default artwork. The same artwork does not need to be returned each time this
	 * method is called, and null may be returned.
	 *
	 * @return the default artwork
	 */
	Bitmap getArtwork();
}