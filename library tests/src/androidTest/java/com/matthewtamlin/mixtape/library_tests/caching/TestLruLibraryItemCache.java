package com.matthewtamlin.mixtape.library_tests.caching;
import android.support.test.runner.AndroidJUnit4;


import com.matthewtamlin.mixtape.library.caching.LibraryItemCache;
import com.matthewtamlin.mixtape.library.caching.LruLibraryItemCache;

import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TestLruLibraryItemCache extends TestLibraryItemCache {
	private static final int SMALL_CACHE_SIZE = 10000;
	private static final int LARGE_CACHE_SIZE = 20000000;

	@Override
	protected LibraryItemCache createLibraryItemCache() {
		return new LruLibraryItemCache(SMALL_CACHE_SIZE, SMALL_CACHE_SIZE, LARGE_CACHE_SIZE);
	}
}