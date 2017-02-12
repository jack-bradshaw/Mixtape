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

package com.matthewtamlin.mixtape.library_tests.data;

import android.graphics.drawable.Drawable;
import android.support.test.runner.AndroidJUnit4;

import com.matthewtamlin.mixtape.library.data.DisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.ImmutableDisplayableDefaults;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class TestImmutableDisplayableDefaults {
	private String title;

	private String subtitle;

	private Drawable artwork;

	@Before
	public void setup() {
		title = "cake";
		subtitle = "lie";
		artwork = mock(Drawable.class);
	}

	@Test
	public void testConstructAndGet_nullTitle() {
		final DisplayableDefaults defaults = new ImmutableDisplayableDefaults(null, subtitle,
				artwork);

		assertThat("Incorrect title.", defaults.getTitle(), is(nullValue()));
		assertThat("Incorrect subtitle.", defaults.getSubtitle(), is((CharSequence) subtitle));
		assertThat("Incorrect artwork.", defaults.getArtwork(), is(artwork));
	}

	@Test
	public void testConstructAndGet_nullSubtitle() {
		final DisplayableDefaults defaults = new ImmutableDisplayableDefaults(title, null, artwork);

		assertThat("Incorrect title.", defaults.getTitle(), is((CharSequence) title));
		assertThat("Incorrect subtitle.", defaults.getSubtitle(), is(nullValue()));
		assertThat("Incorrect artwork.", defaults.getArtwork(), is(artwork));
	}

	@Test
	public void testConstructAndGet_nullArtwork() {
		final DisplayableDefaults defaults = new ImmutableDisplayableDefaults(title, subtitle,
				null);

		assertThat("Incorrect title.", defaults.getTitle(), is((CharSequence) title));
		assertThat("Incorrect subtitle.", defaults.getSubtitle(), is((CharSequence) subtitle));
		assertThat("Incorrect artwork.", defaults.getArtwork(), is(nullValue()));
	}

	@Test
	public void testConstructAndGet_noNullArguments() {
		final DisplayableDefaults defaults = new ImmutableDisplayableDefaults(title, subtitle,
				artwork);

		assertThat("Incorrect title.", defaults.getTitle(), is((CharSequence) title));
		assertThat("Incorrect subtitle.", defaults.getSubtitle(), is((CharSequence) subtitle));
		assertThat("Incorrect artwork.", defaults.getArtwork(), is(artwork));
	}
}