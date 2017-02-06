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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.matthewtamlin.mixtape.example.R;
import com.matthewtamlin.mixtape.example.data.Mp3Album;
import com.matthewtamlin.mixtape.example.data.Mp3AlbumDataSource;
import com.matthewtamlin.mixtape.example.data.Mp3Song;
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
import com.matthewtamlin.mixtape.library.mixtape_body.GridBody;
import com.matthewtamlin.mixtape.library.mixtape_body.RecyclerViewBodyPresenter;
import com.matthewtamlin.mixtape.library.mixtape_coordinator.CoordinatedMixtapeContainer;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AlbumsActivity extends AppCompatActivity {
	private GridBody body;

	private CoordinatedMixtapeContainer rootView;

	private Mp3AlbumDataSource dataSource;

	private RecyclerViewBodyPresenter<Mp3Album, Mp3AlbumDataSource> presenter;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Albums");

		setupView();
		setupDataSource();
		setupPresenter();

		precacheText();
	}

	private void setupView() {
		setContentView(R.layout.example_layout);

		body = new GridBody(this);
		body.setContextualMenuResource(R.menu.album_menu);

		rootView = (CoordinatedMixtapeContainer) findViewById(R.id.example_layout_coordinator);
		rootView.setBody(body);
	}

	private void setupDataSource() {
		dataSource = new Mp3AlbumDataSource();
	}

	private void setupPresenter() {
		final Bitmap defaultArtwork = BitmapFactory.decodeResource(getResources(), R.raw
				.default_artwork);

		final DisplayableDefaults defaults = new ImmutableDisplayableDefaults(
				"Unknown title", "Unknown subtitle", defaultArtwork);

		final LibraryItemCache cache = new LruLibraryItemCache(10000, 10000, 10000000);

		final TitleBinder titleBinder = new TitleBinder(cache, defaults);
		final SubtitleBinder subtitleBinder = new SubtitleBinder(cache, defaults);
		final ArtworkBinder artworkBinder = new ArtworkBinder(cache, defaults, 300);

		presenter = new RecyclerViewBodyPresenter<Mp3Album, Mp3AlbumDataSource>
				(titleBinder, subtitleBinder, artworkBinder) {
			@Override
			public void onContextualMenuItemSelected(final BodyContract.View hostView,
					final LibraryItem libraryItem, final MenuItem menuItem) {
				handleContextualMenuClick(libraryItem, menuItem);
			}

			@Override
			public void onItemSelected(final BodyContract.View hostView, final LibraryItem item) {
				handleItemClick(item);
			}
		};

		presenter.setView(body);
		presenter.setDataSource(dataSource);
	}

	private void precacheText() {
		final LibraryItemCache bodyTitleCache = presenter.getTitleDataBinder().getCache();
		final LibraryItemCache bodySubtitleCache = presenter.getSubtitleDataBinder().getCache();

		dataSource.loadData(true, new BaseDataSource.DataLoadedListener<List<Mp3Album>>() {
			@Override
			public void onDataLoaded(final BaseDataSource<List<Mp3Album>> source,
					final List<Mp3Album> data) {
				Executors.newSingleThreadExecutor().execute(new Runnable() {
					@Override
					public void run() {
						final Executor cacheExecutor = Executors.newCachedThreadPool();

						for (final Mp3Album album : data) {
							cacheExecutor.execute(new Runnable() {
								@Override
								public void run() {
									bodyTitleCache.cacheTitle(album, true);
									bodySubtitleCache.cacheSubtitle(album, true);
								}
							});
						}
					}
				});
			}

			@Override
			public void onLoadDataFailed(final BaseDataSource<List<Mp3Album>> source) {
				// Do nothing
			}
		});
	}

	private void handleContextualMenuClick(final LibraryItem item, final MenuItem menuItem) {
		switch (menuItem.getItemId()) {
			case R.id.album_menu_playNext: {
				try {
					displayMessage("Playing \"" + item.getTitle() + "\" next");
				} catch (LibraryReadException e) {
					displayMessage("Playing \"untitled\" next");
				}

				break;
			}

			case R.id.album_menu_addToQueue: {
				try {
					displayMessage("Added \"" + item.getTitle() + "\" to queue");
				} catch (LibraryReadException e) {
					displayMessage("Added \"untitled\" to queue");
				}

				break;
			}

			case R.id.album_menu_remove: {
				try {
					displayMessage("Deleted \"" + item.getTitle() + "\"");
				} catch (LibraryReadException e) {
					displayMessage("Deleted \"untitled\"");
				}

				dataSource.deleteItem((Mp3Album) item);
			}
		}
	}

	private void handleItemClick(final LibraryItem item) {
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