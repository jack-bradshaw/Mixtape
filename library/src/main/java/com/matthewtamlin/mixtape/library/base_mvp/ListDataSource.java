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

package com.matthewtamlin.mixtape.library.base_mvp;

import java.util.List;

/**
 * A DataSource which provides access to a list of items. In addition to the inherited callbacks,
 * callbacks are delivered when: <ul><li>An item is added to the list.</li> <li>An item is removed
 * from the list.</li> <li>An item in the list is modified in a way which affects the external
 * representation of the data.</li> <li>An item is moved to a different position within the
 * list.</li></ul>
 * <p>
 * All callbacks are delivered on the UI thread.
 *
 * @param <T>
 * 		the type of objects contained within the list
 */
public interface ListDataSource<T> extends BaseDataSource<List<T>> {
	/**
	 * Registers the supplied listener for item added callbacks. If the supplied listener is null or
	 * is already registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	void registerItemAddedListener(ItemAddedListener<T> listener);

	/**
	 * Unregisters the supplied listener for item added callbacks. If the supplied listener is null
	 * or is not registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to unregister
	 */
	void unregisterItemAddedListener(ItemAddedListener<T> listener);

	/**
	 * Registers the supplied listener for item removed callbacks. If the supplied listener is null
	 * or is already registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	void registerItemRemovedListener(ItemRemovedListener<T> listener);

	/**
	 * Unregisters the supplied listener for item removed callbacks. If the supplied listener is
	 * null or is not registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to unregister
	 */
	void unregisterItemRemovedListener(ItemRemovedListener<T> listener);

	/**
	 * Registers the supplied listener for item modified callbacks. If the supplied listener is
	 * null or is already registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	void registerListItemModifiedListener(ListItemModifiedListener<T> listener);

	/**
	 * Unregisters the supplied listener for item modified callbacks. If the supplied listener
	 * is null or is not registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to unregister
	 */
	void unregisterListItemModifiedListener(ListItemModifiedListener<T> listener);

	/**
	 * Registers the supplied listener for item moved callbacks. If the supplied listener is
	 * null or is already registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	void registerItemMovedListener(ItemMovedListener<T> listener);

	/**
	 * Unregisters the supplied listener for item moved callbacks. If the supplied listener is null
	 * or is not registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to unregister
	 */
	void unregisterItemMovedListener(ItemMovedListener<T> listener);

	/**
	 * Callback to be invoked when a ListDataSource adds an item to its current list.
	 *
	 * @param <I>
	 * 		the type of objects contained within the list
	 */
	interface ItemAddedListener<I> {
		/**
		 * Invoked when an item is added to a list.
		 *
		 * @param source
		 * 		the source of the list, not null
		 * @param item
		 * 		the item which was added, may be null
		 * @param index
		 * 		the index at which the object was added
		 */
		void onDataAdded(ListDataSource<I> source, I item, int index);
	}

	/**
	 * Callback to be invoked when a ListDataSource removes an item from its current list.
	 *
	 * @param <I>
	 * 		the type of objects contained within the list
	 */
	interface ItemRemovedListener<I> {
		/**
		 * Invoked when an item is removed from a list.
		 *
		 * @param source
		 * 		the source of the list, not null
		 * @param item
		 * 		the item which was removed, may be null
		 * @param index
		 * 		the index at which the object was removed from
		 */
		void onDataRemoved(ListDataSource<I> source, I item, int index);
	}

	/**
	 * Callback to be invoked when an item in a ListDataSource is modified in a way which affects
	 * the item's external representation.
	 *
	 * @param <I>
	 * 		the type of objects contained within the list
	 */
	interface ListItemModifiedListener<I> {
		/**
		 * Invoked when an item in the list is modified in a way which affects the item's external
		 * representation.
		 *
		 * @param source
		 * 		the source of the list containing the modified item, not null
		 * @param item
		 * 		the object which was modified, not null
		 * @param index
		 * 		the index of the modified object
		 */
		void onListItemModified(ListDataSource<I> source, I item, int index);
	}

	/**
	 * Callback to be invoked when an item in the list of a ListDataSource is moved to another
	 * location in the list.
	 *
	 * @param <I>
	 * 		the type of objects contained within the list
	 */
	interface ItemMovedListener<I> {
		/**
		 * Invoked when an item in the list is moved to another location in the list.
		 *
		 * @param source
		 * 		the source containing the moved object, not null
		 * @param object
		 * 		the object which was moved, may be null
		 * @param initialIndex
		 * 		the index of the object before being moved
		 * @param finalIndex
		 * 		the index of the object after being moved
		 */
		void onDataMoved(ListDataSource<I> source, I object, int initialIndex, int finalIndex);
	}

	/**
	 * Can listen to all callbacks a ListDataSource can deliver.
	 *
	 * @param <I>
	 * 		the type of objects contained within the list
	 */
	interface FullListener<I> extends BaseDataSource.FullListener<List<I>>, ItemAddedListener<I>,
			ItemRemovedListener<I>, ListItemModifiedListener<I>,
			ItemMovedListener<I> {}
}