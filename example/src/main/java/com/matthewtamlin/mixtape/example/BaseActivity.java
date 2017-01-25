package com.matthewtamlin.mixtape.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.mixtape_body.RecyclerViewBody;
import com.matthewtamlin.mixtape.library.mixtape_coordinator.CoordinatedMixtapeContainer;
import com.matthewtamlin.mixtape.library.mixtape_header.SmallHeader;

import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {
	private CoordinatedMixtapeContainer container;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);

		container = (CoordinatedMixtapeContainer) findViewById(R.id.base_activity_root);
		container.setHeader(getHeader());
		container.setBody(getBody());
	}

	public CoordinatedMixtapeContainer getContainer() {
		return container;
	}

	public List<LibraryItem> createBodyItems() {

	}

	public LibraryItem getHeaderItem() {
		
	}

	public abstract SmallHeader getHeader();

	public abstract RecyclerViewBody getBody();
}