package com.matthewtamlin.mixtape.library.base_mvp;

/**
 * The intermediary between a data source and a view, containing the business logic needed to update
 * the data and drive the view.
 */
public interface BasePresenter<S extends BaseDataSource, V extends BaseView> {
	/**
	 * Starts/restarts presentation by loading data from source and the updating the view. Calling
	 * this method is safe at all times, however there may not always an effect.
	 *
	 * @param forceRefresh
	 * 		true to discard cached data when loading from the source, false to use the default
	 * 		behaviour
	 */
	void present(boolean forceRefresh);

	/**
	 * Sets the data source to present from and registers the presenter for callbacks.
	 *
	 * @param dataSource
	 * 		the data source to use, null accepted
	 */
	void setDataSource(S dataSource);

	/**
	 * @return the data source currently in use, may be null
	 */
	S getDataSource();

	/**
	 * Sets the view to present to and registers the presenter with the view.
	 *
	 * @param view
	 * 		the view to use, null accepted
	 */
	void setView(V view);

	/**
	 * @return the view currently in use, may be null
	 */
	V getView();
}