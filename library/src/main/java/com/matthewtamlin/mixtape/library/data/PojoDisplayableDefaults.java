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

import android.graphics.drawable.Drawable;

/**
 * An implementation of the DisplayableDefaults interface which exposes setters for the values.
 */
public class PojoDisplayableDefaults implements DisplayableDefaults {
	private CharSequence title;

	private CharSequence subtitle;

	private Drawable artwork;

	public void setTitle(final CharSequence title) {
		this.title = title;
	}

	public void setSubtitle(final CharSequence subtitle) {
		this.subtitle = subtitle;
	}

	public void setArtwork(final Drawable artwork) {
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
	public Drawable getArtwork() {
		return artwork;
	}
}