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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.matthewtamlin.mixtape.example.R;
import com.matthewtamlin.mixtape.example.data.Mp3Album;
import com.matthewtamlin.mixtape.example.data.Mp3AlbumDataSource;
import com.matthewtamlin.mixtape.library.base_mvp.BaseDataSource;
import com.matthewtamlin.mixtape.library.data.DisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.ImmutableDisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;
import com.matthewtamlin.mixtape.library.databinders.ArtworkBinder;
import com.matthewtamlin.mixtape.library.databinders.SubtitleBinder;
import com.matthewtamlin.mixtape.library.databinders.TitleBinder;
import com.matthewtamlin.mixtape.library.mixtape_body.DirectBodyPresenter;
import com.matthewtamlin.mixtape.library.mixtape_body.DirectBodyPresenter.ContextualMenuItemSelectedListener;
import com.matthewtamlin.mixtape.library.mixtape_body.DirectBodyPresenter.LibraryItemSelectedListener;
import com.matthewtamlin.mixtape.library.mixtape_body.GridBody;
import com.matthewtamlin.mixtape.library.mixtape_body.RecyclerBodyView;
import com.matthewtamlin.mixtape.library.mixtape_container.CoordinatedMixtapeContainer;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import timber.log.Timber;

public class AlbumsActivity extends AppCompatActivity {
	private GridBody body;

	private CoordinatedMixtapeContainer rootView;

	private Mp3AlbumDataSource dataSource;

	private LruCache<LibraryItem, CharSequence> titleCache;

	private LruCache<LibraryItem, CharSequence> subtitleCache;

	private LruCache<LibraryItem, Drawable> artworkCache;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Albums");

		setupDataSource();
		setupCaches();
		precacheText();

		setupView();
		setupPresenter();
	}

	private void setupDataSource() {
		dataSource = new Mp3AlbumDataSource(getResources());
	}

	private void setupCaches() {
		// Titles and subtitles are small enough to stay cached, so use a very high max size
		titleCache = new LruCache<>(10000);
		subtitleCache = new LruCache<>(10000);

		// Every artwork item will be a BitmapDrawable, so use the bitmap byte count for sizing
		artworkCache = new LruCache<LibraryItem, Drawable>(50000000) {
			@Override
			protected int sizeOf(final LibraryItem key, final Drawable value) {
				return ((BitmapDrawable) value).getBitmap().getByteCount();
			}
		};
	}

	private void precacheText() {
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
									try {
										titleCache.put(album, album.getTitle());
										subtitleCache.put(album, album.getSubtitle());
									} catch (final LibraryReadException e) {
										Timber.w("A library item could not be pre-cached.", e);
									}
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

	private void setupView() {
		setContentView(R.layout.example_layout);

		body = new GridBody(this);
		body.setContextualMenuResource(R.menu.album_menu);

		rootView = (CoordinatedMixtapeContainer) findViewById(R.id.example_layout_coordinator);
		rootView.setBody(body);

		final Bitmap defaultArtwork = BitmapFactory.decodeResource(getResources(), R.raw
				.default_artwork);

		final DisplayableDefaults defaults = new ImmutableDisplayableDefaults(
				"Unknown title",
				"Unknown subtitle",
				new BitmapDrawable(getResources(), defaultArtwork));

		body.setTitleDataBinder(new TitleBinder(titleCache, defaults));
		body.setSubtitleDataBinder(new SubtitleBinder(subtitleCache, defaults));
		body.setArtworkDataBinder(new ArtworkBinder(artworkCache, defaults));
	}

	private void setupPresenter() {
		final Bitmap defaultArtwork = BitmapFactory.decodeResource(getResources(), R.raw
				.default_artwork);

		final DisplayableDefaults defaults = new ImmutableDisplayableDefaults(
				"Unknown title",
				"Unknown subtitle",
				new BitmapDrawable(getResources(), defaultArtwork));

		final TitleBinder titleBinder = new TitleBinder(titleCache, defaults);
		final SubtitleBinder subtitleBinder = new SubtitleBinder(subtitleCache, defaults);
		final ArtworkBinder artworkBinder = new ArtworkBinder(artworkCache, defaults);

		final DirectBodyPresenter<Mp3Album, Mp3AlbumDataSource, RecyclerBodyView> presenter = new
				DirectBodyPresenter<>();

		presenter.registerListener(
				new LibraryItemSelectedListener<Mp3Album, Mp3AlbumDataSource, RecyclerBodyView>() {
					@Override
					public void onLibraryItemSelected(
							final DirectBodyPresenter<Mp3Album, Mp3AlbumDataSource, RecyclerBodyView> presenter,
							final Mp3Album item) {
						handleItemClick(item);
					}
				});

		presenter.registerListener(
				new ContextualMenuItemSelectedListener<Mp3Album, Mp3AlbumDataSource, RecyclerBodyView>() {
					@Override
					public void onContextualMenuItemSelected(
							final DirectBodyPresenter<Mp3Album, Mp3AlbumDataSource, RecyclerBodyView> presenter,
							final Mp3Album libraryItem, final MenuItem menuItem) {
						handleContextualMenuClick(libraryItem, menuItem);
					}
				});

		presenter.setView(body);
		presenter.setDataSource(dataSource);
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