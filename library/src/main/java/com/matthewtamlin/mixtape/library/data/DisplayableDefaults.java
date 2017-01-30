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
 * Supplies default titles, subtitles and artwork.
 */
public interface DisplayableDefaults {
	/**
	 * Supplies the default title. The same title does not need to be returned each time this method
	 * is called, and null may be returned.
	 *
	 * @return the default title
	 */
	CharSequence getTitle();

	/**
	 * Supplies the default subtitle. The same subtitle does not need to be returned each time this
	 * method is called, and null may be returned.
	 *
	 * @return the default subtitle
	 */
	CharSequence getSubtitle();

	/**
	 * Supplies the default artwork. The same artwork does not need to be returned each time this
	 * method is called, and null may be returned.
	 *
	 * @return the default artwork
	 */
	Bitmap getArtwork();
}