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

package com.matthewtamlin.mixtape.example.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.matthewtamlin.mixtape.example.R;
import com.matthewtamlin.mixtape.example.data.HeaderDataSource;
import com.matthewtamlin.mixtape.example.data.Mp3Song;
import com.matthewtamlin.mixtape.example.data.Mp3SongDataSource;
import com.matthewtamlin.mixtape.library.base_mvp.BaseDataSource;
import com.matthewtamlin.mixtape.library.caching.LibraryItemCache;
import com.matthewtamlin.mixtape.library.caching.LruLibraryItemCache;
import com.matthewtamlin.mixtape.library.data.DisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.ImmutableDisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;
import com.matthewtamlin.mixtape.library.databinders.ArtworkBinder;
import com.matthewtamlin.mixtape.library.databinders.SubtitleBinder;
import com.matthewtamlin.mixtape.library.databinders.TitleBinder;
import com.matthewtamlin.mixtape.library.mixtape_body.BodyContract;
import com.matthewtamlin.mixtape.library.mixtape_body.ListBody;
import com.matthewtamlin.mixtape.library.mixtape_body.RecyclerViewBodyPresenter;
import com.matthewtamlin.mixtape.library.mixtape_coordinator.CoordinatedMixtapeContainer;
import com.matthewtamlin.mixtape.library.mixtape_header.HeaderContract;
import com.matthewtamlin.mixtape.library.mixtape_header.SmallHeader;
import com.matthewtamlin.mixtape.library.mixtape_header.SmallHeaderPresenter;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PlaylistActivity extends AppCompatActivity {
	private CoordinatedMixtapeContainer rootView;

	private SmallHeader header;

	private ListBody body;

	private HeaderDataSource headerDataSource;

	private Mp3SongDataSource bodyDataSource;

	private SmallHeaderPresenter<HeaderDataSource> headerPresenter;

	private RecyclerViewBodyPresenter<Mp3Song, Mp3SongDataSource> bodyPresenter;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.example_layout);

		setupHeaderView();
		setupBodyView();
		setupContainerView();

		setupDataSources();

		setupHeaderPresenter();
		setupBodyPresenter();

		precacheText();
	}

	private void setupHeaderView() {
		header = new SmallHeader(this);

		header.setOverflowMenuResource(R.menu.header_menu);

		header.setExtraButtons(new Bitmap[]{
				BitmapFactory.decodeResource(getResources(), R.drawable.ic_play),
				BitmapFactory.decodeResource(getResources(), R.drawable.ic_share),
				BitmapFactory.decodeResource(getResources(), R.drawable.ic_shuffle)
		});
	}

	private void setupBodyView() {
		body = new ListBody(this);
		body.setContextualMenuResource(R.menu.song_menu);
	}

	private void setupContainerView() {
		rootView = (CoordinatedMixtapeContainer) findViewById(R.id.example_layout_coordinator);

		rootView.setBody(body);
		rootView.setHeader(header);
		rootView.showHeaderAtStartOnly();
	}

	private void setupDataSources() {
		bodyDataSource = new Mp3SongDataSource(getResources());

		final Bitmap headerArtwork = BitmapFactory.decodeResource(getResources(),
				R.raw.header_artwork);
		headerDataSource = new HeaderDataSource("All Songs",
				"Various artists",
				new BitmapDrawable(getResources(), headerArtwork));
	}

	private void setupHeaderPresenter() {
		final Bitmap defaultArtwork = BitmapFactory.decodeResource(getResources(), R.raw
				.default_artwork);
		final DisplayableDefaults defaults = new ImmutableDisplayableDefaults("Playlist",
				"Unknown artists",
				new BitmapDrawable(getResources(), defaultArtwork));

		final LibraryItemCache cache = new LruLibraryItemCache(10000, 10000, 100000);
		final TitleBinder titleBinder = new TitleBinder(cache, defaults);
		final SubtitleBinder subtitleBinder = new SubtitleBinder(cache, defaults);
		final ArtworkBinder artworkBinder = new ArtworkBinder(cache, defaults);

		headerPresenter = new SmallHeaderPresenter<HeaderDataSource>
				(titleBinder, subtitleBinder, artworkBinder) {
			@Override
			public void onExtraButtonClicked(final HeaderContract.View headerView, final int index) {
				handleHeaderExtraButtonClicked(index);
			}

			@Override
			public void onOverflowMenuItemSelected(final HeaderContract.View headerView,
					final MenuItem menuItem) {
				handleHeaderOverflowMenuItemClicked(menuItem);
			}
		};

		headerPresenter.setView(header);
		headerPresenter.setDataSource(headerDataSource);
	}

	private void setupBodyPresenter() {
		final Bitmap defaultArtwork = BitmapFactory.decodeResource(getResources(), R.raw
				.default_artwork);
		final DisplayableDefaults defaults = new ImmutableDisplayableDefaults("Unknown title",
				"Unknown artist",
				new BitmapDrawable(getResources(), defaultArtwork));

		final LibraryItemCache cache = new LruLibraryItemCache(10000, 10000, 1000000);
		final TitleBinder titleBinder = new TitleBinder(cache, defaults);
		final SubtitleBinder subtitleBinder = new SubtitleBinder(cache, defaults);
		final ArtworkBinder artworkBinder = new ArtworkBinder(cache, defaults);

		bodyPresenter = new RecyclerViewBodyPresenter<Mp3Song, Mp3SongDataSource>
				(titleBinder, subtitleBinder, artworkBinder) {
			@Override
			public void onContextualMenuItemSelected(final BodyContract.View bodyView,
					final LibraryItem item, final MenuItem menuItem) {
				handleBodyItemMenuItemClicked(item, menuItem);
			}

			@Override
			public void onLibraryItemSelected(final BodyContract.View bodyView, final LibraryItem item) {
				handleBodyItemClicked(item);
			}
		};

		bodyPresenter.setView(body);
		bodyPresenter.setDataSource(bodyDataSource);
	}

	private void precacheText() {
		final LibraryItemCache bodyTitleCache = bodyPresenter.getTitleDataBinder().getCache();
		final LibraryItemCache bodySubtitleCache = bodyPresenter.getSubtitleDataBinder().getCache();

		bodyDataSource.loadData(true, new BaseDataSource.DataLoadedListener<List<Mp3Song>>() {
			@Override
			public void onDataLoaded(final BaseDataSource<List<Mp3Song>> source,
					final List<Mp3Song> data) {
				Executors.newSingleThreadExecutor().execute(new Runnable() {
					@Override
					public void run() {
						final Executor cacheExecutor = Executors.newCachedThreadPool();

						for (final Mp3Song song : data) {
							cacheExecutor.execute(new Runnable() {
								@Override
								public void run() {
									bodyTitleCache.cacheTitle(song, true);
									bodySubtitleCache.cacheSubtitle(song, true);
								}
							});
						}
					}
				});
			}

			@Override
			public void onLoadDataFailed(final BaseDataSource<List<Mp3Song>> source) {
				// Do nothing
			}
		});
	}

	private void handleHeaderExtraButtonClicked(final int index) {
		switch (index) {
			case 0: {
				displayMessage("Playing all songs...");
				break;
			}

			case 1: {
				final Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, "https://github.com/MatthewTamlin/Mixtape");
				sendIntent.setType("text/plain");
				startActivity(Intent.createChooser(sendIntent, "Download Mixtape to listen!"));

				break;
			}

			case 2: {
				displayMessage("Shuffling all songs...");
			}
		}
	}

	private void handleHeaderOverflowMenuItemClicked(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.header_menu_download_all_songs: {
				displayMessage("Downloading all songs...");

				break;
			}

			case R.id.header_menu_remove_downloads: {
				displayMessage("Downloads removed");
			}
		}
	}

	private void handleBodyItemMenuItemClicked(final LibraryItem item, final MenuItem menuItem) {
		switch (menuItem.getItemId()) {
			case R.id.song_menu_playNext: {
				try {
					displayMessage("Playing \"" + item.getTitle() + "\" next");
				} catch (LibraryReadException e) {
					displayMessage("Playing \"untitled\" next");
				}

				break;
			}

			case R.id.song_menu_addToQueue: {
				try {
					displayMessage("Added \"" + item.getTitle() + "\" to queue");
				} catch (LibraryReadException e) {
					displayMessage("Added \"untitled\" to queue");
				}

				break;
			}

			case R.id.song_menu_remove: {
				try {
					displayMessage("Deleted \"" + item.getTitle() + "\"");
				} catch (LibraryReadException e) {
					displayMessage("Deleted \"untitled\"");
				}

				bodyDataSource.deleteItem((Mp3Song) item);
			}
		}
	}

	private void handleBodyItemClicked(final LibraryItem item) {
		try {
			displayMessage("Playing \"" + item.getTitle() + "\"...");
		} catch (LibraryReadException e) {
			displayMessage("Playing \"untitled\"...");
		}
	}

	private void displayMessage(final String message) {
		Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
	}
}