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

package com.matthewtamlin.mixtape.library_tests.mixtape_body;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import com.matthewtamlin.mixtape.library.mixtape_body.RecyclerBodyView;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;

public class RecyclerBodyViewViewActions {
	public static ViewAction scrollToStart() {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return isAssignableFrom(RecyclerBodyView.class);
			}

			@Override
			public String getDescription() {
				return "scroll to start";
			}

			@Override
			public void perform(final UiController uiController, final View view) {
				((RecyclerBodyView) view).showItem(0);
			}
		};
	}

	public static ViewAction scrollToEnd() {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return isAssignableFrom(RecyclerBodyView.class);
			}

			@Override
			public String getDescription() {
				return "scroll to end";
			}

			@Override
			public void perform(final UiController uiController, final View view) {
				final RecyclerBodyView castView = (RecyclerBodyView) view;
				castView.showItem(castView.getItems().size() - 1);
			}
		};
	}
}