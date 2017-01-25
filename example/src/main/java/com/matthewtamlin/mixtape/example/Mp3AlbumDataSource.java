package com.matthewtamlin.mixtape.example;

import android.os.AsyncTask;
import android.os.Environment;

import com.matthewtamlin.java_utilities.file.FileFinder;
import com.matthewtamlin.mixtape.library.data.ListDataSourceAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class Mp3AlbumDataSource extends ListDataSourceAdapter<Playlist> {
	private final Id3Util.MetadataField sortBy;

	private List<Playlist> playlists = null;

	public Mp3AlbumDataSource(final Id3Util.MetadataField sortBy) {
		this.sortBy = sortBy;
	}

	@Override
	public void loadData(final boolean forceRefresh,
			final DataLoadedListener<List<Playlist>> callback) {
		final AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(final Void... params) {
				if (playlists == null || forceRefresh) {
					final List<Mp3Song> songs = loadMp3SongsFromMusicDirectory();
					playlists = sortSongsIntoPlaylists(songs);
				}

				return null;
			}

			@Override
			protected void onPostExecute(final Void aVoid) {
				callback.onDataLoaded(Mp3SongDataSource.this, songs);
			}
		};

		task.execute();
	}

	private List<Mp3Song> loadMp3SongsFromMusicDirectory() {
		final List<Mp3Song> mp3Songs = new ArrayList<>();

		final File musicDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
		final Set<File> filesInMusicDir = FileFinder.searchDownTreeFrom(musicDir);

		for (final File file : filesInMusicDir) {
			final String[] splitName = file.getName().split(".");

			if (splitName[splitName.length - 1].toLowerCase().equals("mp3")) {
				mp3Songs.add(new Mp3Song(file));
			}
		}

		return mp3Songs;
	}

	private void sortSongsIntoPlaylists(final List<Mp3Song> songs) {
		final Map<String, Playlist> playlistMap = new HashMap<>();

		for (final Mp3Song song : songs) {
			try {
				final String key = Id3Util.getMetadataFromId3Tag(song.getMp3File(), sortBy);

				if (!playlistMap.containsKey(key)) {
					final
					playlistMap.put(key, new PL)
				}
			} catch (final IOException e) {
				// Disregard song
			}
		}
	}
}
