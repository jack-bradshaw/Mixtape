package com.matthewtamlin.mixtape.example.data;

import android.os.AsyncTask;
import android.os.Environment;

import com.matthewtamlin.java_utilities.file.FileFinder;
import com.matthewtamlin.mixtape.library.data.ListDataSourceAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class Mp3SongDataSource extends ListDataSourceAdapter<Mp3Song> {
	private List<Mp3Song> songs = null;

	@Override
	public void loadData(final boolean forceRefresh,
			final DataLoadedListener<List<Mp3Song>> callback) {
		final AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(final Void... params) {
				if (songs == null || forceRefresh) {
					songs = loadMp3SongsFromMusicDirectory();
				}

				return null;
			}

			@Override
			protected void onPostExecute(final Void aVoid) {
				if (callback != null) {
					callback.onDataLoaded(Mp3SongDataSource.this, songs);
				}
			}
		};

		task.execute();
	}

	public void deleteItem(final Mp3Song item) {
		if (songs.contains(item)) {
			final int index = songs.indexOf(item);

			songs.remove(item);

			for (final ItemRemovedListener<Mp3Song> listener : getItemRemovedListeners()) {
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
}