package com.matthewtamlin.mixtape.example;

import com.matthewtamlin.mixtape.library.data.ListDataSourceAdapter;

import java.util.List;

public class PlaylistDataSource extends ListDataSourceAdapter<Playlist> {
	@Override
	public void loadData(final boolean forceRefresh,
			final DataLoadedListener<List<Playlist>> callback) {

	}
}
