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

import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.matthewtamlin.mixtape.example.util.Id3Util;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;

import java.io.IOException;
import java.util.ArrayList;

import static com.matthewtamlin.mixtape.example.util.Id3Util.MetadataField.ALBUM;
import static com.matthewtamlin.mixtape.example.util.Id3Util.MetadataField.ALBUM_ARTIST;

public class Mp3Album extends ArrayList<Mp3Song> implements LibraryItem {
	@Override
	public CharSequence getTitle() throws LibraryReadException {
		if (isEmpty()) {
			return "Empty album";
		} else {
			try {
				return Id3Util.getMetadataFromId3Tag(get(0).getMp3File(), ALBUM);
			} catch (final IOException e) {
				throw new LibraryReadException("Cannot read ID3 tag from file " +
						get(0).getMp3File(), e);
			}
		}
	}

	@Override
	public CharSequence getSubtitle() throws LibraryReadException {
		if (isEmpty()) {
			return null;
		} else {
			try {
				final String albumArtist = Id3Util.getMetadataFromId3Tag(get(0).getMp3File(),
						ALBUM_ARTIST);

				if (TextUtils.isEmpty(albumArtist)) {
					return "Various artists";
				} else {
					return albumArtist;
				}
			} catch (final IOException e) {
				throw new LibraryReadException("Cannot read ID3 tag from file " +
						get(0).getMp3File(), e);
			}
		}
	}

	@Override
	public Drawable getArtwork(final int width, final int height) throws LibraryReadException {
		if (isEmpty()) {
			return null;
		} else {
			try {
				return get(0).getArtwork(width, height);
			} catch (final IOException e) {
				throw new LibraryReadException("Cannot read ID3 tag from file " +
						get(0).getMp3File(), e);
			}
		}
	}
}