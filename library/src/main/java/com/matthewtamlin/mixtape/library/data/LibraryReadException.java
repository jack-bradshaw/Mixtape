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