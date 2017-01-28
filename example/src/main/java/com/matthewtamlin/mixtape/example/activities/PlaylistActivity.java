package com.matthewtamlin.mixtape.example.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.matthewtamlin.mixtape.example.R;
import com.matthewtamlin.mixtape.example.data.HeaderDataSource;
import com.matthewtamlin.mixtape.example.data.Mp3Song;
import com.matthewtamlin.mixtape.example.data.Mp3SongDataSource;
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
import com.matthewtamlin.mixtape.library.mixtape_header.HeaderContract;
import com.matthewtamlin.mixtape.library.mixtape_header.SmallHeader;
import com.matthewtamlin.mixtape.library.mixtape_header.SmallHeaderPresenter;

public class PlaylistActivity extends AppCompatActivity {
	private CoordinatedMixtapeContainer rootView;

	private SmallHeader header;

	private GridBody body;

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
		body = new GridBody(this);
		body.setContextualMenuResource(R.menu.song_menu);
	}

	private void setupContainerView() {
		rootView = (CoordinatedMixtapeContainer) findViewById(R.id.example_layout_coordinator);
		rootView.setBody(body);
		rootView.setHeader(header);
	}

	private void setupDataSources() {
		bodyDataSource = new Mp3SongDataSource();

		final Bitmap headerArtwork = BitmapFactory.decodeResource(getResources(),
				R.raw.header_artwork);
		headerDataSource = new HeaderDataSource("All Songs", "Various artists", headerArtwork);
	}

	private void setupHeaderPresenter() {
		final DisplayableDefaults defaults = new ImmutableDisplayableDefaults(
				"Playlist", "Unknown artists", BitmapFactory.decodeResource(getResources(),
				R.raw.default_artwork));

		final LibraryItemCache cache = new LruLibraryItemCache(10000, 10000, 100000);
		final TitleBinder titleBinder = new TitleBinder(cache, defaults);
		final SubtitleBinder subtitleBinder = new SubtitleBinder(cache, defaults);
		final ArtworkBinder artworkBinder = new ArtworkBinder(cache, defaults, 300);

		headerPresenter = new SmallHeaderPresenter<HeaderDataSource>
				(titleBinder, subtitleBinder, artworkBinder) {
			@Override
			public void onExtraButtonClicked(final HeaderContract.View hostView, final int index) {
				handleHeaderExtraButtonClicked(index);
			}

			@Override
			public void onOverflowMenuItemClicked(final HeaderContract.View hostView,
					final MenuItem menuItem) {
				handleHeaderOverflowMenuItemClicked(menuItem);
			}
		};

		headerPresenter.setView(header);
		headerPresenter.setDataSource(headerDataSource);
		headerPresenter.present(true);
	}

	private void setupBodyPresenter() {
		final DisplayableDefaults defaults = new ImmutableDisplayableDefaults(
				"Unknown title", "Unknown artist", BitmapFactory.decodeResource(getResources(),
				R.raw.default_artwork));

		final LibraryItemCache cache = new LruLibraryItemCache(10000, 10000, 1000000);
		final TitleBinder titleBinder = new TitleBinder(cache, defaults);
		final SubtitleBinder subtitleBinder = new SubtitleBinder(cache, defaults);
		final ArtworkBinder artworkBinder = new ArtworkBinder(cache, defaults, 300);

		bodyPresenter = new RecyclerViewBodyPresenter<Mp3Song, Mp3SongDataSource>
				(titleBinder, subtitleBinder, artworkBinder) {
			@Override
			public void onContextualMenuItemClicked(final BodyContract.View hostView,
					final LibraryItem item, final MenuItem menuItem) {
				handleBodyItemMenuItemClicked(item, menuItem);
			}

			@Override
			public void onItemClicked(final BodyContract.View hostView, final LibraryItem item) {
				handleBodyItemClicked(item);
			}
		};

		bodyPresenter.setView(body);
		bodyPresenter.setDataSource(bodyDataSource);
	}

	private void handleHeaderExtraButtonClicked(final int index) {
		switch (index) {
			case 0: {
				displayMessage("Playing all songs...");
				break;
			}

			case 1: {
				//TODO show share UI
				break;
			}

			case 2: {
				displayMessage("Shuffling all songs...");
			}
		}
	}

	private void handleHeaderOverflowMenuItemClicked(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.header_menu_download_all_immediately: {
				displayMessage("Downloading all songs now...");

				break;
			}

			case R.id.header_menu_download_all_later: {
				displayMessage("All songs will be downloaded later...");
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