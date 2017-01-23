package com.matthewtamlin.mixtape.library.data;

import java.io.IOException;

/**
 * Indicates that an error occurred while trying to read metadata from a LibraryItem.
 */
public class LibraryReadException extends IOException {
	/**
	 * Constructs a new LibraryReadException with the current stack trace.
	 */
	public LibraryReadException() {
		super();
	}

	/**
	 * Constructs a new LibraryReadException with the current stack trace and the specified
	 * detail message.
	 *
	 * @param detailMessage
	 * 		the detail message for this exception
	 */
	public LibraryReadException(final String detailMessage) {
		super(detailMessage);
	}

	/**
	 * Constructs a new LibraryReadException with the current stack trace, the specified detail
	 * message and the specified cause.
	 *
	 * @param detailMessage
	 * 		the detail message for this exception
	 * @param throwable
	 * 		the cause of this exception
	 */
	public LibraryReadException(final String detailMessage, final Throwable throwable) {
		super(detailMessage, throwable);
	}

	/**
	 * Constructs a new LibraryReadException with the current stack trace and the specified
	 * cause.
	 *
	 * @param throwable
	 * 		the cause of this exception
	 */
	public LibraryReadException(final Throwable throwable) {
		super(throwable);
	}
}