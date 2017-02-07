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

import com.matthewtamlin.mixtape.library.base_mvp.ListDataSource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Partial implementation of the ListDataSource interface which handles listener registration.
 * Getters are provided for accessing the registered listeners.
 *
 * @param <D>
 * 		the type of objects contained within the list
 */
public abstract class ListDataSourceHelper<D> extends BaseDataSourceHelper<List<D>> implements
		ListDataSource<D> {
	/**
	 * All item added listeners which are currently registered. This set must never contain null.
	 */
	private final Set<ItemAddedListener<D>> itemAddedListeners = new HashSet<>();

	/**
	 * All item removed listeners which are currently registered. This set must never contain null.
	 */
	private final Set<ItemRemovedListener<D>> itemRemovedListeners = new HashSet<>();

	/**
	 * All item modified listeners which are currently registered. This set must never contain
	 * null.
	 */
	private final Set<ListItemModifiedListener<D>> itemModifiedListeners = new HashSet<>();

	/**
	 * All item moved listeners which are currently registered. This set must never contain null.
	 */
	private final Set<ItemMovedListener<D>> itemMovedListeners = new HashSet<>();

	@Override
	public void registerItemAddedListener(final ItemAddedListener<D> listener) {
		if (listener != null) {
			itemAddedListeners.add(listener);
		}
	}

	@Override
	public void unregisterItemAddedListener(final ItemAddedListener<D> listener) {
		itemAddedListeners.remove(listener);
	}

	@Override
	public void registerItemRemovedListener(final ItemRemovedListener<D> listener) {
		if (listener != null) {
			itemRemovedListeners.add(listener);
		}
	}

	@Override
	public void unregisterItemRemovedListener(final ItemRemovedListener<D> listener) {
		itemRemovedListeners.remove(listener);
	}

	@Override
	public void registerItemModifiedListener(final ListItemModifiedListener<D> listener) {
		if (listener != null) {
			itemModifiedListeners.add(listener);
		}
	}

	@Override
	public void unregisterItemModifiedListener(final ListItemModifiedListener<D> listener) {
		itemModifiedListeners.remove(listener);
	}

	@Override
	public void registerItemMovedListener(final ItemMovedListener<D> listener) {
		if (listener != null) {
			itemMovedListeners.add(listener);
		}
	}

	@Override
	public void unregisterItemMovedListener(final ItemMovedListener<D> listener) {
		itemMovedListeners.remove(listener);
	}

	/**
	 * Returns all item added listeners which are currently registered for callbacks. This method
	 * may return an empty set, but it will never return null. Furthermore, the returned set will
	 * never contain null.
	 *
	 * @return the listeners
	 */
	public Set<ItemAddedListener<D>> getItemAddedListeners() {
		return itemAddedListeners;
	}

	/**
	 * Returns all item removed listeners which are currently registered for callbacks. This method
	 * may return an empty set, but it will never return null.
	 *
	 * @return the listeners
	 */
	public Set<ItemRemovedListener<D>> getItemRemovedListeners() {
		return itemRemovedListeners;
	}

	/**
	 * Returns all item modified listeners which are currently registered for callbacks. This method
	 * may return an empty set, but it will never return null. Furthermore, the returned set will
	 * never contain null.
	 *
	 * @return the listeners
	 */
	public Set<ListItemModifiedListener<D>> getItemModifiedListeners() {
		return itemModifiedListeners;
	}

	/**
	 * Returns all item moved listeners which are currently registered for callbacks. This method
	 * may return an empty set, but it will never return null. Furthermore, the returned set will
	 * never contain null.
	 *
	 * @return the listeners
	 */
	public Set<ItemMovedListener<D>> getItemMovedListeners() {
		return itemMovedListeners;
	}
}