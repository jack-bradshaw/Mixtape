package com.matthewtamlin.mixtape.library.base_mvp;

import java.util.List;

/**
 * A data source where the data is structured as a List.
 *
 * @param <T>
 * 		the type of objects contained within the list
 */
public interface ListDataSource<T> extends BaseDataSource<List<T>> {
	/**
	 * Registers a DataAddedListener. The supplied listener will be notified on the UI thread each
	 * time an object is added to the list. If the supplied listener is null or is already
	 * registered, this method does nothing and exits normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	void registerDataAddedListener(DataAddedListener<T> listener);

	/**
	 * Unregisters a DataAddedListener. The supplied listener will no longer be notified when an
	 * objects are added to the list. If the supplied listener is null or is not registered, this
	 * method does nothing and exits normally.
	 *
	 * @param listener
	 * 		the listener to unregister
	 */
	void unregisterDataAddedListener(DataAddedListener<T> listener);

	/**
	 * Registers a DataRemovedListener. The supplied listener will be notified on the UI thread each
	 * time an object is removed from the list. If the supplied listener is null or is already
	 * registered, this method does nothing and exits normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	void registerDataRemovedListener(DataRemovedListener<T> listener);

	/**
	 * Unregisters a DataRemovedListener. The supplied listener will no longer be notified when
	 * object are removed from the list. If the supplied listener is null or is not registered, this
	 * method does nothing and exits normally.
	 *
	 * @param listener
	 * 		the listener to unregister
	 */
	void unregisterDataRemovedListener(DataRemovedListener<T> listener);

	/**
	 * Registers a ListItemModifiedListener. The supplied listener will be notified on the UI thread
	 * each time an object in the list is modified in a way which affects the data. If the supplied
	 * listener is null or is already registered, this method does nothing and exits normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	void registerListItemModifiedListener(ListItemModifiedListener<T> listener);

	/**
	 * Unregisters a ListItemModifiedListener. The supplied listener will no longer be notified when
	 * object in the list is modified in a way which affects the data. If the supplied listener is
	 * null or is not registered, this method does nothing and exits normally.
	 *
	 * @param listener
	 * 		the listener to unregister
	 */
	void unregisterListItemModifiedListener(ListItemModifiedListener<T> listener);

	/**
	 * Registers a DataMovedListener. The supplied listener will be notified on the UI thread each
	 * time an object in the list is moved to a different position. If the supplied listener is null
	 * or is already registered, this method does nothing and exits normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	void registerDataMovedListener(DataMovedListener<T> listener);

	/**
	 * Unregisters a DataMovedListener. The supplied listener will no longer be notified when an
	 * object in the list is moved to a different position. If the supplied listener is null or is
	 * not registered, this method does nothing and exits normally.
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
	interface Listener<I> extends BaseDataSource.Listener<List<I>>, DataAddedListener<I>,
			DataRemovedListener<I>, ListItemModifiedListener<I>,
			DataMovedListener<I> {}
}