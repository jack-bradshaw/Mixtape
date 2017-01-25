package com.matthewtamlin.mixtape.example.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.matthewtamlin.mixtape.example.R;
import com.matthewtamlin.mixtape.example.data.Mp3Album;
import com.matthewtamlin.mixtape.example.data.Mp3AlbumDataSource;
import com.matthewtamlin.mixtape.library.base_mvp.ListDataSource;
import com.matthewtamlin.mixtape.library.mixtape_body.GridBody;
import com.matthewtamlin.mixtape.library.mixtape_body.RecyclerViewBody;
import com.matthewtamlin.mixtape.library.mixtape_body.RecyclerViewBodyPresenter;
import com.matthewtamlin.mixtape.library.mixtape_coordinator.CoordinatedMixtapeContainer;
import com.matthewtamlin.mixtape.library.mixtape_coordinator.MixtapeContainerView;
import com.matthewtamlin.mixtape.library.mixtape_header.SmallHeader;

public class GridActivity extends AppCompatActivity {
	private CoordinatedMixtapeContainer container;

	private GridBody body;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupView();

	}

	private void setupView() {
		setContentView(R.layout.example_layout);

		container = (CoordinatedMixtapeContainer) findViewById(R.id.example_layout_coordinator);

		body = new GridBody(this);

		container.setBody(body);
	}

	private void setupBodyDataSource() {

	}

	private void setupPresenter() {

	}

	private void integrateMVP() {

	}
}
