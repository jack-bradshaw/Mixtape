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

import android.graphics.drawable.Drawable;

import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;

/**
 * A LibraryItem which always throws exceptions when its metadata is accessed.
 */
public final class InaccessibleLibraryItem implements LibraryItem {
	@Override
	public final CharSequence getTitle() throws LibraryReadException {
		throw new LibraryReadException("Title is never accessible.");
	}

	@Override
	public final CharSequence getSubtitle() throws LibraryReadException {
		throw new LibraryReadException("Subtitle is never accessible.");
	}

	@Override
	public final Drawable getArtwork(int width, int height) throws LibraryReadException {
		throw new LibraryReadException("Artwork is never accessible.");
	}
}