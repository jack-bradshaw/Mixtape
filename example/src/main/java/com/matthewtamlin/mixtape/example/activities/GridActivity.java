package com.matthewtamlin.mixtape.example.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.matthewtamlin.mixtape.example.R;
import com.matthewtamlin.mixtape.example.data.Mp3Album;
import com.matthewtamlin.mixtape.example.data.Mp3AlbumDataSource;
import com.matthewtamlin.mixtape.library.caching.LibraryItemCache;
import com.matthewtamlin.mixtape.library.caching.LruLibraryItemCache;
import com.matthewtamlin.mixtape.library.data.DisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.ImmutableDisplayableDefaults;
import com.matthewtamlin.mixtape.library.databinders.ArtworkBinder;
import com.matthewtamlin.mixtape.library.databinders.SubtitleBinder;
import com.matthewtamlin.mixtape.library.databinders.TitleBinder;
import com.matthewtamlin.mixtape.library.mixtape_body.GridBody;
import com.matthewtamlin.mixtape.library.mixtape_body.RecyclerViewBodyPresenter;
import com.matthewtamlin.mixtape.library.mixtape_coordinator.CoordinatedMixtapeContainer;

public class GridActivity extends AppCompatActivity {
	private GridBody body;

	private LibraryItemCache cache;

	private Mp3AlbumDataSource dataSource;

	private RecyclerViewBodyPresenter<Mp3Album, Mp3AlbumDataSource> presenter;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupView();
		setupDataSource();
		setupPresenter();

		presenter.setView(body);
		presenter.setDataSource(dataSource);
		presenter.present(true);
	}

	private void setupView() {
		setContentView(R.layout.example_layout);

		final CoordinatedMixtapeContainer container = (CoordinatedMixtapeContainer) findViewById
				(R.id.example_layout_coordinator);

		body = new GridBody(this);

		container.setBody(body);
	}

	private void setupDataSource() {
		dataSource = new Mp3AlbumDataSource();
	}

	private void setupPresenter() {
		final Bitmap defaultArtwork = BitmapFactory.decodeResource(getResources(), R.raw
				.default_artwork);

		final DisplayableDefaults defaults = new ImmutableDisplayableDefaults(
				"Unknown title", "Unknown subtitle", defaultArtwork);

		cache = new LruLibraryItemCache(10000, 10000, 10000000);

		final TitleBinder titleBinder = new TitleBinder(cache, defaults);
		final SubtitleBinder subtitleBinder = new SubtitleBinder(cache, defaults);
		final ArtworkBinder artworkBinder = new ArtworkBinder(cache, defaults, 300);

		presenter = new RecyclerViewBodyPresenter<>(titleBinder, subtitleBinder, artworkBinder);
	}
}
