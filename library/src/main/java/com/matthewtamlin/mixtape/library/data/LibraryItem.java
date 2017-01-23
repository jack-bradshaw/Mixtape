/*
 * Copyright 2016 Matthew Tamlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.matthewtamlin.mixtape.library.data;

import android.graphics.Bitmap;

/**
 * Represents a single item of content in a library and provides access to its metadata. Exactly
 * what classifies as the title, subtitle and artwork is determined by the implementation.
 */
public interface LibraryItem {
	/**
	 * Accesses the title of this LibraryItem. This method may be long running.
	 *
	 * @return the title of this item, null if none exists
	 * @throws LibraryReadException
	 * 		if the title cannot be read
	 */
	CharSequence getTitle() throws LibraryReadException;

	/**
	 * Accesses the subtitle of this LibraryItem. This method may be long running.
	 *
	 * @return the subtitle of this item, null if none exists
	 * @throws LibraryReadException
	 * 		if the subtitle cannot be read
	 */
	CharSequence getSubtitle() throws LibraryReadException;

	/**
	 * Accesses the artwork of this item. This method may be long running. The width and height
	 * parameters are used to optimise the memory used by the artwork. If either value is less than
	 * or equal to zero, then the unoptimised artwork is returned.
	 *
	 * @param width
	 * 		the desired width of the returned artwork, measured in pixels, greater than zero
	 * @param height
	 * 		the desired height of the returned artwork, measured in pixels, greater than zero
	 * @return the artwork of this item, null if none exists
	 * @throws LibraryReadException
	 * 		if the artwork cannot be read
	 */
	Bitmap getArtwork(int width, int height) throws LibraryReadException;

	/**
	 * Sets the title of this item.
	 *
	 * @param title
	 * 		the value to set as the title, null accepted
	 * @throws LibraryReadException
	 * 		if the title cannot be read
	 * @throws LibraryWriteException
	 * 		if the title cannot be modified
	 */
	void setTitle(final CharSequence title) throws LibraryReadException, LibraryWriteException;

	/**
	 * Sets the subtitle of this item.
	 *
	 * @param subtitle
	 * 		the value to set as the subtitle, null accepted
	 * @throws LibraryReadException
	 * 		if the subtitle cannot be read
	 * @throws LibraryWriteException
	 * 		if the subtitle cannot be modified
	 */
	void setSubtitle(final CharSequence subtitle) throws LibraryReadException,
			LibraryWriteException;

	/**
	 * Sets the artwork of this item.
	 *
	 * @param artwork
	 * 		the value to set as the artwork, null accepted
	 * @throws LibraryReadException
	 * 		if the artwork cannot be read
	 * @throws LibraryWriteException
	 * 		if the artwork cannot be modified
	 */
	void setArtwork(final Bitmap artwork) throws LibraryReadException, LibraryWriteException;

	/**
	 * Indicates whether or not this LibraryItem can be modified. If this item is read only, then
	 * every call to {@link #setTitle(CharSequence)}, {@link #setSubtitle(CharSequence)} or {@link
	 * #setArtwork(Bitmap)} will cause a LibraryWriteException to be thrown.
	 *
	 * @return true if this item is read only, false otherwise
	 */
	boolean isReadOnly();
}