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

/**
 * A user interface element which is controlled by a presenter.
 *
 * @param <P>
 * 		the type of presenter to be used with the view
 */
public interface BaseView<P extends BasePresenter> {
	/**
	 * Sets the presenter to propagate user interaction events to. To remove the existing presenter,
	 * pass null.
	 *
	 * @param presenter
	 * 		the presenter to use, null accepted
	 */
	void setPresenter(P presenter);
}