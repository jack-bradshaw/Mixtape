package com.matthewtamlin.mixtape.library.base_mvp;

/**
 * Acts as the intermediary between a data source and a view, and contains the business logic needed
 * to update the data and drive the view. Presenters must always be subscribed to callback events
 * from the data source and the view.
 */
public interface BasePresenter<S extends BaseDataSource, V extends BaseView> {
	/**
	 * Starts/restarts presentation by loading data from source and the updating the view. It is
	 * valid to call this method at anytime, however there may not always an effect.
	 *
	 * @param forceRefresh
	 * 		true to request invalidation of any cached data, false to use the default behaviour
	 */
	void present(boolean forceRefresh);

	/**
	 * @param dataSource
	 * 		the data source to use, null accepted
	 */
	void setDataSource(S dataSource);

	/**
	 * @return the data source currently in use, may be null
	 */
	S getDataSource();

	/**
	 * @param view
	 * 		the view to use, null accepted
	 */
	void setView(V view);

	/**
	 * @return the view currently in use, may be null
	 */
	V getView();
}