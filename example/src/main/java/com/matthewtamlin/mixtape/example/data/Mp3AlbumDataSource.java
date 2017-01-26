package com.matthewtamlin.mixtape.example.data;

import android.os.AsyncTask;
import android.os.Environment;

import com.matthewtamlin.java_utilities.file.FileFinder;
import com.matthewtamlin.mixtape.example.util.Id3Util;
import com.matthewtamlin.mixtape.library.data.ListDataSourceAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.matthewtamlin.mixtape.example.util.Id3Util.MetadataField.ALBUM;

public class Mp3AlbumDataSource extends ListDataSourceAdapter<Mp3Album> {
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