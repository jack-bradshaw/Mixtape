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

import android.os.AsyncTask;
import android.os.Environment;

import com.matthewtamlin.java_utilities.file.FileFinder;
import com.matthewtamlin.mixtape.example.util.Id3Util;
import com.matthewtamlin.mixtape.library.data.ListDataSourceHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.matthewtamlin.mixtape.example.util.Id3Util.MetadataField.ALBUM;

public class Mp3AlbumDataSource extends ListDataSourceHelper<Mp3Album> {
	private List<Mp3Album> albums = null;

	@Override
	public void loadData(final boolean forceRefresh,
			final DataLoadedListener<List<Mp3Album>> callback) {
		final AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(final Void... params) {
				if (albums == null || forceRefresh) {
					final List<Mp3Song> songs = loadMp3SongsFromMusicDirectory();
					albums = sortSongsByAlbum(songs);
				}

				return null;
			}

			@Override
			protected void onPostExecute(final Void aVoid) {
				if (callback != null) {
					callback.onDataLoaded(Mp3AlbumDataSource.this, albums);
				}
			}
		};

		task.execute();
	}

	public void deleteItem(final Mp3Album item) {
		if (albums.contains(item)) {
			final int index = albums.indexOf(item);

			albums.remove(item);

			for (final ItemRemovedListener<Mp3Album> listener : getItemRemovedListeners()) {
				listener.onDataRemoved(this, item, index);
			}
		}
	}

	private List<Mp3Song> loadMp3SongsFromMusicDirectory() {
		final List<Mp3Song> mp3Songs = new ArrayList<>();

		final File musicDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);

		final Set<File> filesInMusicDir = FileFinder.searchDownTreeFrom(musicDir);

		for (final File file : filesInMusicDir) {
			final String[] splitName = file.getName().split("\\.");

			if (splitName[splitName.length - 1].toLowerCase().equals("mp3")) {
				mp3Songs.add(new Mp3Song(file));
			}
		}

		return mp3Songs;
	}

	private List<Mp3Album> sortSongsByAlbum(final List<Mp3Song> songs) {
		final Map<String, Mp3Album> albumMap = new HashMap<>();

		for (final Mp3Song song : songs) {
			try {
				final String key = Id3Util.getMetadataFromId3Tag(song.getMp3File(), ALBUM);

				if (!albumMap.containsKey(key)) {
					albumMap.put(key, new Mp3Album());
				}

				albumMap.get(key).add(song);
			} catch (final IOException e) {
				// In production, this should be logged as a warning/error
			}
		}

		return new ArrayList<>(albumMap.values());
	}
}