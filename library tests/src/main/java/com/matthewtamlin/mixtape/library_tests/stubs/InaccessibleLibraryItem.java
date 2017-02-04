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

import android.graphics.Bitmap;

import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;

/**
 * An implementation of the LibraryItem interface for use in testing. Every attempt to access the
 * metadata results in a LibraryReadException being thrown.
 */
public final class InaccessibleLibraryItem implements LibraryItem {
	@Override
	public final CharSequence getTitle() throws LibraryReadException {
		throw new LibraryReadException();
	}

	@Override
	public final CharSequence getSubtitle() throws LibraryReadException {
		throw new LibraryReadException();
	}

	@Override
	public final Bitmap getArtwork(int width, int height) throws LibraryReadException {
		throw new LibraryReadException();
	}
}