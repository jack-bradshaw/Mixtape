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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
		root.addView(createLaunchAlbumActivityButton());
		root.addView(createLaunchPlaylistActivityButton());
	}

	private Button createLaunchAlbumActivityButton() {
		final Button b = new Button(this);
		b.setText("Launch album activity");

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