package com.matthewtamlin.mixtape.library.base_mvp;

/**
 * A data source provides access to data objects and delivers callbacks when events occur. Callbacks
 * are delivered when: <ul> <li>The current data object has been invalidated and replaced with an
 * entirely new data object.</li> <li>The data object has changed internally in a way which affects
 * the external representation of the data.</li> <li>The data source begins/ends an operation which
 * could be potentially long running and may result in data being changed.</li></ul>
 * <p>
 * All callbacks are delivered on the UI thread.
 *
 * @param <D>
 * 		the type of data supplied by the source
 */
public interface BaseDataSource<D> {
	/**
	 * Asynchronously loads data from the source and notifies the supplied callback when finished.
	 * If true is passed for the {@code forceRefresh} parameter, any relevant cached data is
	 * discarded at the start of the load operation.
	 *
	 * @param forceRefresh
	 * 		true to request invalidation of cached data, false to use the default behaviour
	 * @param callback
	 * 		the callback to be invoked when the operation finishes, null to ignore
	 */
	void loadData(boolean forceRefresh, DataLoadedListener<D> callback);

	/**
	 * Registers the supplied listener for data replaced callbacks. If the supplied listener is null
	 * or is already registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	void registerDataReplacedListener(DataReplacedListener<D> listener);

	/**
	 * Unregisters the supplied listener for data replaced callbacks. If the supplied listener is
	 * null or is not registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to unregister
	 */
	void unregisterDataReplacedListener(DataReplacedListener<D> listener);

	/**
	 * Registers the supplied listener for data modified callbacks. If the supplied listener is null
	 * or is already registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	void registerDataModifiedListener(DataModifiedListener<D> listener);

	/**
	 * Unregisters the supplied listener for data modified callbacks. If the supplied listener is
	 * null or is not registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to unregister
	 */
	void unregisterDataModifiedListener(DataModifiedListener<D> listener);

	/**
	 * Registers the supplied listener for long operation callbacks. If the supplied listener is
	 * null or is already registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	void registerLongOperationListener(LongOperationListener listener);

	/**
	 * Unregisters the supplied listener for long operation callbacks. If the supplied listener is
	 * null or is not registered, this method exits normally.
	 *
	 * @param listener
	 * 		the listener to unregister
	 */
	void unregisterLongOperationListener(LongOperationListener listener);

	/**
	 * Callback to be invoked when data is loaded, either successfully or unsuccessfully.
	 *
	 * @param <I>
	 * 		the type of data supplied by the source
	 */
	interface DataLoadedListener<I> {
		/**
		 * Invoked when data is successfully loaded.
		 *
		 * @param source
		 * 		the source of the data, not null
		 * @param data
		 * 		the data which was loaded, may be null
		 */
		void onDataLoaded(BaseDataSource<I> source, I data);

		/**
		 * Invoked when data fails to load.
		 *
		 * @param source
		 * 		the source of the data, not null
		 */
		void onLoadDataFailed(BaseDataSource<I> source);
	}

	/**
	 * Callback to be invoked when the data object of a BaseDataSource has been invalidated and
	 * replaced with an entirely new data object.
	 *
	 * @param <I>
	 * 		the type of data supplied by the source
	 */
	interface DataReplacedListener<I> {
		/**
		 * Invoked when data is invalidated and replaced.
		 *
		 * @param source
		 * 		the source of the data, not null
		 * @param oldData
		 * 		the invalidated data, may be null
		 * @param newData
		 * 		the data which replaces the invalidated data, may be null
		 */
		void onDataReplaced(BaseDataSource<I> source, I oldData, I newData);
	}

	/**
	 * Callback to be invoked when the data object of a BaseDataSource has changed internally in a
	 * way which affects the external representation of the data.
	 *
	 * @param <I>
	 * 		the type of data supplied by the source
	 */
	interface DataModifiedListener<I> {
		/**
		 * Invoked when data is modified internally in a way which affects the external
		 * representation of the data.
		 *
		 * @param source
		 * 		the source of the data, not null
		 * @param data
		 * 		the data which was modified, not null
		 */
		void onDataModified(BaseDataSource<I> source, I data);
	}

	/**
	 * Callback to be invoked when a BaseDataSource starts of finishes a potentially long running
	 * operation which may result in data being changed.
	 */
	interface LongOperationListener {
		/**
		 * Invoked when a potentially long running operation is started. Unless the process is
		 * terminated abruptly, this call is guaranteed to be followed by a call to {@link
		 * #onLongOperationFinished(BaseDataSource)} at some point in the future.
		 *
		 * @param source
		 * 		the source performing the operation, not null
		 */
		void onLongOperationStarted(BaseDataSource source);

		/**
		 * Invoked when a potentially long running operation has finished.
		 *
		 * @param source
		 * 		the source performing the operation, not null
		 */
		void onLongOperationFinished(BaseDataSource source);
	}

	/**
	 * Composition of all BaseDataSource listeners.
	 *
	 * @param <I>
	 * 		the type of data supplied by the data source
	 */
	interface FullListener<I> extends DataLoadedListener<I>, DataReplacedListener<I>,
			DataModifiedListener<I>, LongOperationListener {}
}