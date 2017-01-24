package com.matthewtamlin.mixtape.library.base_mvp;

/**
 * The intermediary between a data source and a view, containing the business logic needed
 * to update the data and drive the view.
 */
public interface BasePresenter<S extends BaseDataSource, V extends BaseView> {
	/**
	 * Starts/restarts presentation by loading data from source and the updating the view.
	 * Calling this method is safe at all times, however there may not always an effect.
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