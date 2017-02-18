/*
 * Copyright 2017 Matthew Tamlin
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

package com.matthewtamlin.mixtape.library_tests.stubs;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.matthewtamlin.android_utilities.library.helpers.BitmapEfficiencyHelper;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;


/**
 * An LibraryItem where the titles, subtitle and artwork are defined at instantiation.
 */
public final class NormalLibraryItem implements LibraryItem {
	/**
	 * Provides access to the app resources.
	 */
	private Resources resources;

	/**
	 * The title of this item, may be null.
	 */
	private CharSequence title;

	/**
	 * The subtitle of this item, may be null.
	 */
	private CharSequence subtitle;

	/**
	 * The artwork of this item may be null.
	 */
	private int artworkId;

	/**
	 * Constructs a new NormalLibraryItem.
	 *
	 * @param title
	 * 		the title of this item, may be null
	 * @param subtitle
	 * 		the subtitle of this item, may be null
	 * @param artworkId
	 * 		a resource ID corresponding to a Bitmap to use as the artwork of this item
	 */
	public NormalLibraryItem(final Resources resources,
			final CharSequence title,
			final CharSequence subtitle,
			final int artworkId) {
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
	public Drawable getArtwork(final int width, final int height) throws LibraryReadException {
		final Bitmap artwork = BitmapEfficiencyHelper.decodeResource(resources, artworkId, width,
				height);
		return new BitmapDrawable(resources, artwork);
	}


	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		} else if (obj == this) {
			return true;
		} else {
			return obj.getClass().equals(NormalLibraryItem.class);
		}
	}

	@Override
	public String toString() {
		return "Library item (title: " + title + ")";
	}
}