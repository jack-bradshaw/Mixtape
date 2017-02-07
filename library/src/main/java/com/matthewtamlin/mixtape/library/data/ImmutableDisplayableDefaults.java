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

import android.graphics.Bitmap;

/**
 * An immutable implementation of the DisplayableDefaults interface.
 */
public final class ImmutableDisplayableDefaults implements DisplayableDefaults {
	/**
	 * The default title.
	 */
	private final CharSequence title;

	/**
	 * The default subtitle.
	 */
	private final CharSequence subtitle;

	/**
	 * The default artwork.
	 */
	private final Bitmap artwork;

	/**
	 * Constructs a new ImmutableDisplayableDefaults using the supplied values.
	 *
	 * @param title
	 * 		the default title, null allowed
	 * @param subtitle
	 * 		the default subtitle, null allowed
	 * @param artwork
	 * 		the default artwork, null allowed
	 */
	public ImmutableDisplayableDefaults(final String title, final String subtitle,
			final Bitmap artwork) {
		this.title = title;
		this.subtitle = subtitle;
		this.artwork = artwork;
	}

	@Override
	public CharSequence getTitle() {
		return title;
	}

	@Override
	public CharSequence getSubtitle() {
		return subtitle;
	}

	@Override
	public Bitmap getArtwork() {
		return artwork;
	}
}