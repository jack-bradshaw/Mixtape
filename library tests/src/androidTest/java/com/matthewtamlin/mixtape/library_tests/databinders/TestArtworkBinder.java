package com.matthewtamlin.mixtape.library_tests.databinders;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ImageView;

import com.matthewtamlin.mixtape.library.caching.LibraryItemCache;
import com.matthewtamlin.mixtape.library.caching.LruLibraryItemCache;
import com.matthewtamlin.mixtape.library.data.DisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.ImmutableDisplayableDefaults;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;
import com.matthewtamlin.mixtape.library.databinders.ArtworkBinder;
import com.matthewtamlin.mixtape.library_tests.stubs.InaccessibleLibraryItem;
import com.matthewtamlin.mixtape.library_tests.stubs.ReadOnlyLibraryItem;
import com.matthewtamlin.mixtape.library_tests.test.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class TestArtworkBinder {
	/**
	 * The length of time to pause for when waiting for background tasks to finish.
	 */
	private static final int PAUSE_DURATION = 500;

	private static final int FADE_IN_DURATION_MS = 200;

	/**
	 * The resource ID for an image to use as the artwork and the default artwork.
	 */
	private static final int ARTWORK_RES_ID = R.raw.image1;

	/**
	 * The image to use as the artwork.
	 */
	private Bitmap artwork;

	/**
	 * The image to use as the default artwork.
	 */
	private Bitmap defaultArtwork;

	/**
	 * A read-only LibraryItem for use in testing, which uses {@code artwork} for the artwork and
	 * null for the other metadata.
	 */
	private LibraryItem libraryItem;

	/**
	 * A cache for use in testing.
	 */
	private LruLibraryItemCache cache;

	/**
	 * Defaults for use in testing.
	 */
	private DisplayableDefaults displayableDefaults;

	/**
	 * An ImageView for use in testing. This view should be mocked so that method invocations can be
	 * recorded and reviewed.
	 */
	private ImageView imageView;

	/**
	 * Initialises the testing objects and assigns them to member variables.
	 */
	@Before
	public void setup() throws LibraryReadException {
		final Resources res = InstrumentationRegistry.getTargetContext().getResources();

		artwork = BitmapFactory.decodeResource(res, ARTWORK_RES_ID);
		defaultArtwork = BitmapFactory.decodeResource(res, ARTWORK_RES_ID);
		assertThat("Precondition failed, artwork is null.", artwork, is(notNullValue()));
		assertThat("Precondition failed, default artwork is null.", defaultArtwork,
				is(notNullValue()));

		libraryItem = new ReadOnlyLibraryItem(res, null, null, R.raw.image1);
		cache = new LruLibraryItemCache(1, 1, 1000000); // Should be more than enough for the test
		displayableDefaults = new ImmutableDisplayableDefaults(null, null, defaultArtwork);
		imageView = mock(ImageView.class);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code cache} argument of {@link
	 * ArtworkBinder#bind(ImageView, LibraryItem)} is null. The test will only pass if an
	 * IllegalArgumentException is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_invalidArgs_nullCache() {
		new ArtworkBinder(null, displayableDefaults, FADE_IN_DURATION_MS);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code cache} argument of {@link
	 * ArtworkBinder#bind(ImageView, LibraryItem)} is null. The test will only pass if an
	 * IllegalArgumentException is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_invalidArgs_nullDefaults() {
		new ArtworkBinder(cache, null, FADE_IN_DURATION_MS);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code fadeInDurationMs}
	 * argument of {@link ArtworkBinder#bind(ImageView, LibraryItem)} is less than zero. The test
	 * will only pass if an IllegalArgumentException is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_invalidArgs_negativeFadeInDuration() {
		new ArtworkBinder(cache, displayableDefaults, -1);
	}

	/**
	 * Test to verify that the {@link ArtworkBinder#ArtworkBinder(LibraryItemCache,
	 * DisplayableDefaults, int)} constructor functions correctly when the provided with valid
	 * arguments. This case examines the boundary case where the fade in duration is 0ms.
	 */
	@Test
	public void testConstructor_validArgs_zeroFadeIn() {
		new ArtworkBinder(cache, displayableDefaults, 0); // Shouldn't throw exception
	}

	/**
	 * Test to verify that the {@link ArtworkBinder#ArtworkBinder(LibraryItemCache,
	 * DisplayableDefaults, int)} constructor functions correctly when the provided with valid
	 * arguments. This case examines the boundary case where the fade in duration is 1ms.
	 */
	@Test
	public void testConstructor_validArgs_PositiveFadeIn() {
		new ArtworkBinder(cache, displayableDefaults, 0); // Shouldn't throw exception
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code view} argument of {@link
	 * ArtworkBinder#bind(ImageView, LibraryItem)} is null. The test will only pass if an
	 * IllegalArgumentException is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBind_invalidArgs_nullView() {
		final ArtworkBinder binder = new ArtworkBinder(cache, displayableDefaults,
				FADE_IN_DURATION_MS);
		binder.bind(null, libraryItem);
	}

	/**
	 * Test to verify that the {@link ArtworkBinder#bind(ImageView, LibraryItem)} method functions
	 * correctly when the {@code data} argument is null. The test will only pass if null is bound to
	 * the view.
	 */
	@Test
	public void testBind_nullData() {
		final ArtworkBinder binder = new ArtworkBinder(cache, displayableDefaults,
				FADE_IN_DURATION_MS);

		binder.bind(imageView, null);

		pause(); // Allow time for async processing to complete
		verify(imageView, atLeastOnce()).setImageBitmap(null);
		verify(imageView, never()).setImageBitmap(artwork);
		verify(imageView, never()).setImageBitmap(defaultArtwork);
	}

	/**
	 * Test to verify that the {@link ArtworkBinder#bind(ImageView, LibraryItem)} method functions
	 * correctly when the cache already contains the artwork of the data passed to the {@code data}
	 * argument. The test will only pass if the artwork is bound to the view.
	 */
	@Test
	public void testBind_dataCached() {
		final ArtworkBinder binder = new ArtworkBinder(cache, displayableDefaults,
				FADE_IN_DURATION_MS);
		cache.cacheTitle(libraryItem, false);

		binder.bind(imageView, libraryItem);

		pause(); // Allow time for async processing to complete
		verify(imageView).setImageBitmap(artwork); // Called once to clear and once to set
	}

	/**
	 * Test to verify that the {@link ArtworkBinder#bind(ImageView, LibraryItem)} method functions
	 * correctly when the cache does not contain the artwork of the data passed to the {@code data}
	 * argument. The test will only pass if the artwork is bound to the view.
	 */
	@Test
	public void testBind_dataAccessibleButNotCached() {
		final ArtworkBinder binder = new ArtworkBinder(cache, displayableDefaults,
				FADE_IN_DURATION_MS);
		cache.removeTitle(libraryItem); // In case it was somehow cached previously

		binder.bind(imageView, libraryItem);

		pause(); // Allow time for async processing to complete
		verify(imageView).setImageBitmap(artwork);
	}

	/**
	 * Test to verify that the {@link ArtworkBinder#bind(ImageView, LibraryItem)} method functions
	 * correctly when the cache does not contain the artwork of the data passed to the {@code data}
	 * argument, and the data cannot be directly accessed. The test will only pass if the default
	 * artwork is bound to the view.
	 */
	@Test
	public void testBind_dataInaccessibleAndNotCached() {
		final ArtworkBinder binder = new ArtworkBinder(cache, displayableDefaults,
				FADE_IN_DURATION_MS);
		final LibraryItem inaccessibleItem = new InaccessibleLibraryItem();

		binder.bind(imageView, inaccessibleItem);

		pause(); // Allow time for async processing to complete
		verify(imageView).setImageBitmap(defaultArtwork);
	}

	/**
	 * Suspends execution of the current thread. The duration is defined by the {@code
	 * PAUSE_DURATION} constant.
	 */
	private void pause() {
		try {
			Thread.sleep(PAUSE_DURATION);
		} catch (InterruptedException e) {
			throw new RuntimeException("wait interrupted, test aborted");
		}
	}
}