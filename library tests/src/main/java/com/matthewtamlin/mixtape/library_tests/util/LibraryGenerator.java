package com.matthewtamlin.mixtape.library_tests.util;

import android.content.res.Resources;

import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library_tests.R;
import com.matthewtamlin.mixtape.library_tests.stubs.InaccessibleLibraryItem;
import com.matthewtamlin.mixtape.library_tests.stubs.ReadOnlyLibraryItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LibraryGenerator {
	public static List<LibraryItem> generateItems(final Resources resources, final int
			numberOfitems) {
		final List<LibraryItem> items = new ArrayList<>();

		for (int i = 0; i < numberOfitems; i++) {
			if (new Random().nextBoolean()) {
				items.add(new ReadOnlyLibraryItem(resources,
						"Title " + i,
						"Subtitle " + i,
						R.raw.real_artwork));
			} else {
				items.add(new InaccessibleLibraryItem());
			}
		}

		return items;
	}
}