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

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.matthewtamlin.android_testing_tools.library.EspressoHelper;
import com.matthewtamlin.mixtape.library.mixtape_body.GridBody;
import com.matthewtamlin.mixtape.library.mixtape_body.RecyclerBodyView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TestGridBody extends TestRecyclerViewBody {
	@Rule
	private final ActivityTestRule<GridBodyTestHarness> rule =
			new ActivityTestRule<>(GridBodyTestHarness.class);

	private GridBody testViewDirect;

	private ViewInteraction testViewEspresso;

	@Before
	public void setup() {
		testViewDirect = rule.getActivity().getTestView();
		testViewEspresso = EspressoHelper.viewToViewInteraction(testViewDirect);
	}

	@Override
	public RecyclerBodyView getBodyViewDirect() {
		if (testViewDirect == null) {
			testViewDirect = rule.getActivity().getTestView();
		}

		return testViewDirect;
	}

	@Override
	public ViewInteraction getBodyViewEspresso() {
		if (testViewEspresso == null) {
			testViewEspresso = EspressoHelper.viewToViewInteraction(getBodyViewDirect());
		}

		return testViewEspresso;
	}
}