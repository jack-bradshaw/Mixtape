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

package com.matthewtamlin.mixtape.library_tests.data.data;

import android.graphics.drawable.Drawable;

import com.matthewtamlin.mixtape.library.data.DisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.PojoDisplayableDefaults;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the {@link PojoDisplayableDefaults} class.
 */
@RunWith(JUnit4.class)
public class TestPojoDisplayableDefaults {
	/**
	 * Test to verify that the {@link PojoDisplayableDefaults#PojoDisplayableDefaults()} constructor
	 * functions correctly when provided with valid arguments. The test will only pass if the
	 * getters return the values passed to the constructor.
	 */
	@Test
	public void testConstructor() {
		final DisplayableDefaults defaults = new PojoDisplayableDefaults();

		assertThat("Incorrect title.", defaults.getTitle(), is(nullValue()));
		assertThat("Incorrect subtitle.", defaults.getSubtitle(), is(nullValue()));
		assertThat("Incorrect artwork.", defaults.getArtwork(), is(nullValue()));
	}

	/**
	 * Test to verify that the {@link PojoDisplayableDefaults#setTitle(CharSequence)} method
	 * functions correctly when provided with a non-null title. The test will only pass if the title
	 * getter returns the new value and the other getters return the old values.
	 */
	@Test
	public void testSetTitle_nonNullTitle() {
		final PojoDisplayableDefaults defaults = new PojoDisplayableDefaults();

		final CharSequence newTitle1 = mock(CharSequence.class);
		defaults.setTitle(newTitle1);

		assertThat("Incorrect title.", defaults.getTitle(), is(newTitle1));
		assertThat("Incorrect subtitle.", defaults.getSubtitle(), is(nullValue()));
		assertThat("Incorrect artwork.", defaults.getArtwork(), is(nullValue()));

		final CharSequence newTitle2 = mock(CharSequence.class);
		defaults.setTitle(newTitle2);

		assertThat("Incorrect title.", defaults.getTitle(), is(newTitle2));
		assertThat("Incorrect subtitle.", defaults.getSubtitle(), is(nullValue()));
		assertThat("Incorrect artwork.", defaults.getArtwork(), is(nullValue()));
	}

	/**
	 * Test to verify that the {@link PojoDisplayableDefaults#setTitle(CharSequence)} method
	 * functions correctly when provided with a null title. The test will only pass if the title
	 * getter returns null and the other getters return the old values.
	 */
	@Test
	public void testSetTitle_nullTitle() {
		final PojoDisplayableDefaults defaults = new PojoDisplayableDefaults();

		defaults.setTitle(null);

		assertThat("Incorrect title.", defaults.getTitle(), is(nullValue()));
		assertThat("Incorrect subtitle.", defaults.getSubtitle(), is(nullValue()));
		assertThat("Incorrect artwork.", defaults.getArtwork(), is(nullValue()));
	}

	/**
	 * Test to verify that the {@link PojoDisplayableDefaults#setSubtitle(CharSequence)} method
	 * functions correctly when provided with a non-null subtitle. The test will only pass if the
	 * subtitle getter returns the new value and the other getters return the old values.
	 */
	@Test
	public void testSetSubtitle_nonNullSubtitle() {
		final PojoDisplayableDefaults defaults = new PojoDisplayableDefaults();

		final CharSequence newSubtitle1 = mock(CharSequence.class);
		defaults.setSubtitle(newSubtitle1);

		assertThat("Incorrect title.", defaults.getTitle(), is(nullValue()));
		assertThat("Incorrect subtitle.", defaults.getSubtitle(), is(newSubtitle1));
		assertThat("Incorrect artwork.", defaults.getArtwork(), is(nullValue()));

		final CharSequence newSubtitle2 = mock(CharSequence.class);
		defaults.setSubtitle(newSubtitle2);

		assertThat("Incorrect title.", defaults.getTitle(), is(nullValue()));
		assertThat("Incorrect subtitle.", defaults.getSubtitle(), is(newSubtitle2));
		assertThat("Incorrect artwork.", defaults.getArtwork(), is(nullValue()));
	}

	/**
	 * Test to verify that the {@link PojoDisplayableDefaults#setSubtitle(CharSequence)} method
	 * functions correctly when provided with a null subtitle. The test will only pass if the
	 * subtitle getter returns null and the other getters return the old values.
	 */
	@Test
	public void testSetSubtitle_nullSubtitle() {
		final PojoDisplayableDefaults defaults = new PojoDisplayableDefaults();

		defaults.setSubtitle(null);

		assertThat("Incorrect title.", defaults.getTitle(), is(nullValue()));
		assertThat("Incorrect subtitle.", defaults.getSubtitle(), is(nullValue()));
		assertThat("Incorrect artwork.", defaults.getArtwork(), is(nullValue()));

		final CharSequence newSubtitle2 = mock(CharSequence.class);
		defaults.setSubtitle(newSubtitle2);

		assertThat("Incorrect title.", defaults.getTitle(), is(nullValue()));
		assertThat("Incorrect subtitle.", defaults.getSubtitle(), is(newSubtitle2));
		assertThat("Incorrect artwork.", defaults.getArtwork(), is(nullValue()));
	}

	/**
	 * Test to verify that the {@link PojoDisplayableDefaults#setArtwork(Drawable)} method functions
	 * correctly when provided with non-null artwork. The test will only pass if the artwork getter
	 * returns the new value and the other getters return the old values.
	 */
	@Test
	public void testSetArtwork_nonNullArtwork() {
		final PojoDisplayableDefaults defaults = new PojoDisplayableDefaults();

		final Drawable newArtwork1 = mock(Drawable.class);
		defaults.setArtwork(newArtwork1);

		assertThat("Incorrect title.", defaults.getTitle(), is(nullValue()));
		assertThat("Incorrect subtitle.", defaults.getSubtitle(), is(nullValue()));
		assertThat("Incorrect artwork.", defaults.getArtwork(), is(newArtwork1));

		final Drawable newArtwork2 = mock(Drawable.class);
		defaults.setArtwork(newArtwork2);

		assertThat("Incorrect title.", defaults.getTitle(), is(nullValue()));
		assertThat("Incorrect subtitle.", defaults.getSubtitle(), is(nullValue()));
		assertThat("Incorrect artwork.", defaults.getArtwork(), is(newArtwork2));
	}

	/**
	 * Test to verify that the {@link PojoDisplayableDefaults#setArtwork(Drawable)} method functions
	 * correctly when provided with null artwork. The test will only pass if the artwork getter
	 * returns null and the other getters return the old values.
	 */
	@Test
	public void testSetArtwork_nullArtwork() {
		final PojoDisplayableDefaults defaults = new PojoDisplayableDefaults();

		defaults.setArtwork(null);

		assertThat("Incorrect title.", defaults.getTitle(), is(nullValue()));
		assertThat("Incorrect subtitle.", defaults.getSubtitle(), is(nullValue()));
		assertThat("Incorrect artwork.", defaults.getArtwork(), is(nullValue()));
	}
}