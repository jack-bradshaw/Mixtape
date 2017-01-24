package com.matthewtamlin.mixtape.library.base_mvp;

import java.util.List;

/**
 * A DataSource where the data is structured as a List. In addition to the inherited callbacks,
 * callbacks are delivered when: <ul><li>An item is added to the list.</li> <li>An item is removed
 * from the list.</li> <li>An item in the list is modified in a way which affects the external
 * representation of the data.</li> <li>An item is moved to a different position within the
 * list.</li></ul>
 * <p>
 * All callbacks are delivered on the UI thread.
 *
 * @param <T>
 * 		the type of objects contained within the data list
 */
public interface ListDataSource<T> extends BaseDataSource<List<T>> {
	/**
	 * Registers the supplied listener for data added callbacks. If the supplied listener is null or
	 * is already registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	void registerDataAddedListener(DataAddedListener<T> listener);

	/**
	 * Unregisters the supplied listener for data added callbacks. If the supplied listener is null
	 * or is not registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to unregister
	 */
	void unregisterDataAddedListener(DataAddedListener<T> listener);

	/**
	 * Registers the supplied listener for data removed callbacks. If the supplied listener is null
	 * or is already registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	void registerDataRemovedListener(DataRemovedListener<T> listener);

	/**
	 * Unregisters the supplied listener for data removed callbacks. If the supplied listener is
	 * null or is not registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to unregister
	 */
	void unregisterDataRemovedListener(DataRemovedListener<T> listener);

	/**
	 * Registers the supplied listener for list item modified callbacks. If the supplied listener is
	 * null or is already registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	void registerListItemModifiedListener(ListItemModifiedListener<T> listener);

	/**
	 * Unregisters the supplied listener for list item modified callbacks. If the supplied listener
	 * is null or is not registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to unregister
	 */
	void unregisterListItemModifiedListener(ListItemModifiedListener<T> listener);

	/**
	 * Registers the supplied listener for list data moved callbacks. If the supplied listener is
	 * null or is already registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	void registerDataMovedListener(DataMovedListener<T> listener);

	/**
	 * Unregisters the supplied listener for data moved callbacks. If the supplied listener is null
	 * or is not registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to unregister
	 */
	void unregisterDataMovedListener(DataMovedListener<T> listener);

	/**
	 * Callback to be invoked when an object is added to the dataset of a ListDataSource.
	 *
	 * @param <I>
	 * 		the type of objects contained within the list
	 */
	interface DataAddedListener<I> {
		/**
		 * Invoked to indicate that an object was added to the dataset of a ListDataSource.
		 *
		 * @param source
		 * 		the source the object was added to, not null
		 * @param object
		 * 		the object which was added, null accepted
		 * @param index
		 * 		the index at which the object was added
		 */
		void onDataAdded(ListDataSource<I> source, I object, int index);
	}

	/**
	 * Callback to be invoked when an object is removed from the dataset of a ListDataSource.
	 *
	 * @param <I>
	 * 		the type of objects contained within the list
	 */
	interface DataRemovedListener<I> {
		/**
		 * Invoked to indicate that an object was removed from the dataset of a ListDataSource.
		 *
		 * @param source
		 * 		the source the object was removed from, not null
		 * @param object
		 * 		the object which was removed, null accepted
		 * @param index
		 * 		the index of the object before it was removed
		 */
		void onDataRemoved(ListDataSource<I> source, I object, int index);
	}

	/**
	 * Callback to be invoked when an object in the dataset of a ListDataSource is modified in a way
	 * which affects the data.
	 *
	 * @param <I>
	 * 		the type of objects contained within the list
	 */
	interface ListItemModifiedListener<I> {
		/**
		 * Invoked to indicate that an object in the dataset of a ListDataSource was modified in a
		 * way which affects the data.
		 *
		 * @param source
		 * 		the source containing the modified object, not null
		 * @param object
		 * 		the object which was modified, not null
		 * @param index
		 * 		the index of the modified object
		 */
		void onListItemModified(ListDataSource<I> source, I object, int index);
	}

	/**
	 * Callback to be invoked when an object in the dataset of a ListDataSource is moved to a
	 * different position.
	 *
	 * @param <I>
	 * 		the type of objects contained within the list
	 */
	interface DataMovedListener<I> {
		/**
		 * Invoked to indicate that an object in the dataset of a ListDataSource was moved to a
		 * different position.
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
	 * Can listen to all callbacks a BaseDataSource can deliver.
	 *
	 * @param <I>
	 * 		the type of objects contained within the list
	 */
	interface FullListener<I> extends BaseDataSource.FullListener<List<I>>, DataAddedListener<I>,
			DataRemovedListener<I>, ListItemModifiedListener<I>,
			DataMovedListener<I> {}
}