package com.matthewtamlin.mixtape.example.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.matthewtamlin.mixtape.example.R;
import com.matthewtamlin.mixtape.example.data.HeaderDataSource;
import com.matthewtamlin.mixtape.example.data.Mp3Album;
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

	private LibraryItemCache cache;

	private DisplayableDefaults defaults;

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

		bodyPresenter.setView(body);
		bodyPresenter.setDataSource(bodyDataSource);
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
		headerDataSource = new HeaderDataSource("All songs", "Various artists", headerArtwork);
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
	}

	private void setupBodyPresenter() {
		final DisplayableDefaults defaults = new ImmutableDisplayableDefaults(
				"Unknown title", "Unknown subtitle", BitmapFactory.decodeResource(getResources(),
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
	}

	private void handleHeaderExtraButtonClicked(final int index) {

	}

	private void handleHeaderOverflowMenuItemClicked(final MenuItem item) {

	}

	private void handleContextualMenuClick(final LibraryItem item, final MenuItem menuItem) {
		switch (menuItem.getItemId()) {
			case R.id.album_menu_playNext: {
				try {
					Snackbar.make(rootView, "Playing \"" + item.getTitle() + "\" next",
							Snackbar.LENGTH_LONG).show();
				} catch (LibraryReadException e) {
					Snackbar.make(rootView, "Playing untitled next", Snackbar.LENGTH_LONG).show();
				}

				break;
			}

			case R.id.album_menu_addToQueue: {
				try {
					Snackbar.make(rootView, "Adding \"" + item.getTitle() + "\" to queue",
							Snackbar.LENGTH_LONG).show();
				} catch (LibraryReadException e) {
					Snackbar.make(rootView, "Adding \"untitled\" to queue", Snackbar.LENGTH_LONG)
							.show();
				}

				break;
			}

			case R.id.album_menu_remove: {
				try {
					Snackbar.make(rootView, "Deleted \"" + item.getTitle() + "\"",
							Snackbar.LENGTH_LONG).show();
				} catch (LibraryReadException e) {
					Snackbar.make(rootView, "Deleted \"untitled\"", Snackbar.LENGTH_LONG).show();
				}

				bodyDataSource.deleteItem((Mp3Album) item);
			}
		}
	}

	private void handleBodyItemClicked(final LibraryItem item) {
		try {
			Snackbar.make(rootView, "Playing \"" + item.getTitle() + "\"",
					Snackbar.LENGTH_LONG).show();
		} catch (LibraryReadException e) {
			Snackbar.make(rootView, "Playing \"untitled\"", Snackbar.LENGTH_LONG).show();
		}
	}
}