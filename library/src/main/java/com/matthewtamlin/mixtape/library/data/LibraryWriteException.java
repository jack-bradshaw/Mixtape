package com.matthewtamlin.mixtape.library.data;

import java.io.IOException;

/**
 * Indicates that an error occurred while trying to write metadata to a LibraryItem.
 */
public class LibraryWriteException extends IOException {
	/**
	 * Constructs a new LibraryWriteException with the current stack trace.
	 */
	public LibraryWriteException() {
		super();
	}

	/**
	 * Constructs a new LibraryWriteException with the current stack trace and the specified detail
	 * message.
	 *
	 * @param detailMessage
	 * 		the detail message for this exception
	 */
	public LibraryWriteException(final String detailMessage) {
		super(detailMessage);
	}

	/**
	 * Constructs a new LibraryWriteException with the current stack trace, the specified detail
	 * message and the specified cause.
	 *
	 * @param detailMessage
	 * 		the detail message for this exception
	 * @param throwable
	 * 		the cause of this exception
	 */
	public LibraryWriteException(final String detailMessage, final Throwable throwable) {
		super(detailMessage, throwable);
	}

	/**
	 * Constructs a new LibraryWriteException with the current stack trace and the specified cause.
	 *
	 * @param throwable
	 * 		the cause of this exception
	 */
	public LibraryWriteException(final Throwable throwable) {
		super(throwable);
	}
}