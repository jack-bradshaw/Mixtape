package com.matthewtamlin.mixtape.library.base_mvp;

/**
 * An abstract data source. The source may provide direct access to the data, or it may provide
 * access to further abstractions (such as data access objects).
 *
 * @param <D>
 * 		the type of data supplied
 */
public interface BaseDataSource<D> {
	/**
	 * Loads all data in the datasource and notifies the supplied callback when finished. Load
	 * operations which are pending or in progress when this method is called are cancelled. If true
	 * is passed for the {@code forceRefresh} parameter, any cached data must be invalidated before
	 * the load operation.
	 *
	 * @param forceRefresh
	 * 		true to request invalidation of any cached data, false to use the default behaviour
	 * @param callback
	 * 		the callback to be invoked when loading is finished, null to ignore
	 */
	void loadData(boolean forceRefresh, DataLoadedListener<D> callback);

	/**
	 * Registers a DataReplacedListener. The supplied listener will be notified on the UI thread
	 * each time the existing data object is replaced with a new one. If the supplied listener is
	 * null or is already registered, this method does nothing and exits normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	void registerDataReplacedListener(DataReplacedListener<D> listener);

	/**
	 * Unregisters a DataReplacedListener. The supplied listener will no longer be notified when the
	 * existing data object is replaced with a new one. If the supplied listener is null or is not
	 * registered, this method does nothing and exits normally.
	 *
	 * @param listener
	 * 		the listener to unregister
	 */
	void unregisterDataReplacedListener(DataReplacedListener<D> listener);

	/**
	 * Registers a DataModifiedListener. The supplied listener will be notified on the UI thread
	 * each time the existing data object is modified in some way which affects the data. The
	 * callback will not be delivered when the data object is replaced; to receive these callbacks
	 * instead use {@link #registerDataReplacedListener(DataReplacedListener)}. If the supplied
	 * listener is null or is already registered, this method does nothing and exits normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	void registerDataModifiedListener(DataModifiedListener<D> listener);

	/**
	 * Unregisters a DataModifiedListener. The supplied listener will no longer be notified when the
	 * data object is modified. If the supplied listener is null or is not registered, this method
	 * does nothing and exits normally.
	 *
	 * @param listener
	 * 		the listener to unregister
	 */
	void unregisterDataModifiedListener(DataModifiedListener<D> listener);

	/**
	 * Registers a LongOperationListener. The supplied listener will be notified on the UI thread
	 * each time a data source starts or ends an operation which is potentially long running and
	 * could result in data being changed in some way. If the supplied listener is null or is
	 * already registered, this method does nothing and exits normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	void registerLongOperationListener(LongOperationListener listener);

	/**
	 * Unregisters a LongOperationListener. The supplied listener will no longer be notified when
	 * the data source starts or ends potentially long running operations. If the supplied listener
	 * is null or is not registered, this method does nothing and exits normally.
	 *
	 * @param listener
	 * 		the listener to unregister
	 */
	void unregisterLongOperationListener(LongOperationListener listener);

	/**
	 * Callbacks to be invoked when data is loaded, either successfully or unsuccessfully.
	 *
	 * @param <I>
	 * 		the type of data being loaded
	 */
	interface DataLoadedListener<I> {
		/**
		 * Invoked to indicate that data loaded successfully.
		 *
		 * @param source
		 * 		the source of the data, not null
		 * @param data
		 * 		the data which was loaded, null allowed
		 */
		void onDataLoaded(BaseDataSource<I> source, I data);

		/**
		 * Invoked to indicate that data failed to load.
		 *
		 * @param source
		 * 		the source of the data, not null
		 */
		void onLoadDataFailed(BaseDataSource<I> source);
	}

	/**
	 * Callback to be invoked when the data object of a BaseDataSource has been replaced with a new
	 * data object.
	 *
	 * @param <I>
	 * 		the type of data being replaced
	 */
	interface DataReplacedListener<I> {
		/**
		 * Invoked to indicate that the existing data object of a data source has been replaced with
		 * a new one.
		 *
		 * @param source
		 * 		the source of the data, not null
		 * @param oldData
		 * 		the old data, null allowed
		 * @param newData
		 * 		the new data, null allowed
		 */
		void onDataReplaced(BaseDataSource<I> source, I oldData, I newData);
	}

	/**
	 * Callback to be invoked when the data object of a BaseDataSource is modified in a way which
	 * affects the data. This is different from the DataReplacedListener, which indicates that the
	 * data object has been entirely invalidated and replaced with a new object.
	 *
	 * @param <I>
	 * 		the type of data which was modified
	 */
	interface DataModifiedListener<I> {
		/**
		 * Invoked to indicate that a data object has been modified in a way which affects the
		 * data.
		 *
		 * @param source
		 * 		the source of the data, not null
		 * @param data
		 * 		the data which was modified, not null
		 */
		void onDataModified(BaseDataSource<I> source, I data);
	}

	/**
	 * Callback to be invoked when long running operations present and stop.
	 */
	interface LongOperationListener {
		/**
		 * Invoked to indicate that a data source is starting an operation which is potentially long
		 * running and could result in data being changed in some way. There is no guarantee that
		 * data will be changed, therefore there is no guarantee that any callback other than {@link
		 * #onLongOperationFinished(BaseDataSource)} will be called when the operation completes.
		 *
		 * @param source
		 * 		the source performing the operation
		 */
		void onLongOperationStarted(BaseDataSource source);

		/**
		 * Invoked to indicate that a data source has finished an operation which was declared as
		 * potentially long running.
		 *
		 * @param source
		 * 		the source performing the operation
		 */
		void onLongOperationFinished(BaseDataSource source);
	}

	/**
	 * Listener for all BaseDataSource callbacks.
	 *
	 * @param <I>
	 * 		the type of data supplied by the data source
	 */
	interface Listener<I> extends DataLoadedListener<I>, DataReplacedListener<I>,
			DataModifiedListener<I>, LongOperationListener {}
}