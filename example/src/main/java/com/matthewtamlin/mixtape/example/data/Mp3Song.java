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

package com.matthewtamlin.mixtape.example.data;

import android.graphics.Bitmap;

import com.matthewtamlin.mixtape.example.util.Id3Util;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;

import java.io.File;
import java.io.IOException;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

public class Mp3Song implements LibraryItem {
	private File mp3File;

	public Mp3Song(final File mp3File) {
		this.mp3File = checkNotNull(mp3File, "mp3File cannot be null.");
	}

	public File getMp3File() {
		return mp3File;
	}

	@Override
	public CharSequence getTitle() throws LibraryReadException {
		try {
			return Id3Util.getMetadataFromId3Tag(mp3File, Id3Util.MetadataField.TITLE);
		} catch (final IOException e) {
			throw new LibraryReadException("Cannot read ID3 tag from file " + mp3File, e);
		}
	}

	@Override
	public CharSequence getSubtitle() throws LibraryReadException {
		try {
			return Id3Util.getMetadataFromId3Tag(mp3File, Id3Util.MetadataField.ARTIST);
		} catch (final IOException e) {
			throw new LibraryReadException("Cannot read ID3 tag from file " + mp3File, e);
		}
	}

	@Override
	public Bitmap getArtwork(final int width, final int height) throws LibraryReadException {
		try {
			return Id3Util.getCoverArtFromId3Tag(mp3File, width, height);
		} catch (final IOException e) {
			throw new LibraryReadException("Cannot read ID3 tag from file " + mp3File, e);
		}
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj == this) {
			return true;
		}

		if (obj.getClass().equals(Mp3Song.class)) {
			final Mp3Song objCast = (Mp3Song) obj;

			return this.mp3File.equals(objCast.mp3File);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return mp3File == null ? -1 : mp3File.hashCode();
	}
}