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

package com.matthewtamlin.mixtape.library.databinders;

import android.view.View;

/**
 * Binds data to views without blocking the UI thread.
 *
 * @param <D>
 * 		the type of data to bind
 * @param <V>
 * 		the type of view to bind data to
 */
public interface DataBinder<D, V extends View> {
	/**
	 * Binds data to the supplied view. Asynchronous processing is used if necessary.
	 *
	 * @param view
	 * 		the View to bind data to, not null
	 * @param data
	 * 		the data to bind, null to clear the view
	 */
	void bind(final V view, final D data);

	/**
	 * Cancels the current bind operation for the supplied view. If no bind operation exists, the
	 * method exits normally.
	 *
	 * @param view
	 * 		the view to cancel the bind operation for, not null
	 */
	void cancel(V view);

	/**
	 * Cancels all existing bind operations.
	 */
	void cancelAll();
}