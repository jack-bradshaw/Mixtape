package com.matthewtamlin.mixtape.example.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.matthewtamlin.mixtape.example.R;

@SuppressLint("SetTextI18n") // Not important in example
public class LauncherActivity extends AppCompatActivity {
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launcher_activity);

		final LinearLayout root = (LinearLayout) findViewById(R.id.launcher_activity_root);
		root.addView(createLaunchGridActivityButton());
		root.addView(createLaunchPlaylistActivityButton());
	}

	private Button createLaunchGridActivityButton() {
		final Button b = new Button(this);
		b.setText("Launch grid activity button");

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				startActivity(new Intent(LauncherActivity.this, AlbumsActivity.class));
			}
		});

		return b;
	}

	private Button createLaunchPlaylistActivityButton() {
		final Button b = new Button(this);
		b.setText("Launch playlist activity");

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				startActivity(new Intent(LauncherActivity.this, PlaylistActivity.class));
			}
		});

		return b;
	}
}