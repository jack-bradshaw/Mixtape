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

package com.matthewtamlin.mixtape.example.data;

import android.graphics.drawable.Drawable;

import com.matthewtamlin.mixtape.library.data.BaseDataSourceHelper;
import com.matthewtamlin.mixtape.library.data.LibraryItem;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;

public class HeaderDataSource extends BaseDataSourceHelper<LibraryItem> {
	private final LibraryItem item;

	public HeaderDataSource(final CharSequence title, final CharSequence subtitle,
			final Drawable artwork) {
		this.item = new LibraryItem() {
			@Override
			public CharSequence getTitle() throws LibraryReadException {
				return title;
			}

			@Override
			public CharSequence getSubtitle() throws LibraryReadException {
				return subtitle;
			}

			@Override
			public Drawable getArtwork(final int width, final int height)
					throws LibraryReadException {
				return artwork;
			}
		};
	}

	@Override
	public void loadData(final boolean forceRefresh,
			final DataLoadedListener<LibraryItem> callback) {
		callback.onDataLoaded(this, item);
	}
}