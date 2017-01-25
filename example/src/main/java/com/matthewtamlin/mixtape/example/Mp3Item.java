package com.matthewtamlin.mixtape.example;

import com.matthewtamlin.mixtape.library.data.LibraryItem;

import java.io.File;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

public class Mp3Item implements LibraryItem {
	private File file;

	public Mp3Item(final File mp3File) {
		file = checkNotNull(mp3File, "mp3File cannot be null.");
	}

	
}