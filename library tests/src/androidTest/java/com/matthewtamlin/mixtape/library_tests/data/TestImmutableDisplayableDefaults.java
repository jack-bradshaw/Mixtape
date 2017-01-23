package com.matthewtamlin.mixtape.library_tests.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;


import com.matthewtamlin.mixtape.library.data.DisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.ImmutableDisplayableDefaults;
import com.matthewtamlin.mixtape.library_tests.test.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(AndroidJUnit4.class)
public class TestImmutableDisplayableDefaults {
	private static final CharSequence TITLE = "cake";
	private static final CharSequence SUBTITLE = "lie";
	private static final int IMAGE_RES_ID = R.raw.image1;

	private Bitmap artwork;

	private DisplayableDefaults displayableDefaults;

	@Before
	public void setup() {
		// Decode an image for use in testing
		final Context context = InstrumentationRegistry.getTargetContext();
		artwork = BitmapFactory.decodeResource(context.getResources(), IMAGE_RES_ID);
		assertThat("precondition failed: artwork is null", artwork, is(notNullValue()));

		displayableDefaults = new ImmutableDisplayableDefaults(TITLE, SUBTITLE,
				artwork);
	}

	@Test
	public void testGetTitle_shouldReturnTestTitle() {
		assertThat(displayableDefaults.getTitle(), is(TITLE));
	}

	@Test
	public void testGetSubtitle_shouldReturnTestSubtitle() {
		assertThat(displayableDefaults.getSubtitle(), is(SUBTITLE));
	}

	@Test
	public void testGetArtwork_shouldReturnTestArtwork() {
		assertThat(displayableDefaults.getArtwork(), is(artwork));
	}
}