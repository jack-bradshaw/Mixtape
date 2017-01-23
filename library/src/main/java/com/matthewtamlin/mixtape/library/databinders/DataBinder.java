package com.matthewtamlin.mixtape.library.databinders;

import android.view.View;

/**
 * Binds data to views without blocking the UI thread.
 *
 * @param <D>
 * 		the type of data to bind
 * @param <V>
 * 		the type of View to bind data to
 */
public interface DataBinder<D, V extends View> {
	/**
	 * Binds data to the supplied view. This method may use asynchronous processing and must always
	 * be called on the UI thread. Passing null data clears the view.
	 *
	 * @param view
	 * 		the View to bind data to, not null
	 * @param data
	 * 		the data to bind, or an object which provides access to the data
	 */
	void bind(final V view, final D data);

	/**
	 * Cancels the current bind operation for the supplied view if one exists.
	 *
	 * @param view
	 * 		the view to cancel the bind operation for, not null
	 */
	void cancel(V view);

	/**
	 * Cancels all current bind operations.
	 */
	void cancelAll();
}