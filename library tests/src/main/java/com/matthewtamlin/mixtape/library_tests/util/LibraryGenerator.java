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

package com.matthewtamlin.mixtape.library_tests.util;

import android.content.res.Resources;

import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library_tests.R;
import com.matthewtamlin.mixtape.library_tests.stubs.InaccessibleLibraryItem;
import com.matthewtamlin.mixtape.library_tests.stubs.NormalLibraryItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LibraryGenerator {
	public static List<LibraryItem> generateItems(final Resources resources, final int
			numberOfitems) {
		final List<LibraryItem> items = new ArrayList<>();

		for (int i = 0; i < numberOfitems; i++) {
			if (new Random().nextBoolean()) {
				items.add(new NormalLibraryItem(resources,
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