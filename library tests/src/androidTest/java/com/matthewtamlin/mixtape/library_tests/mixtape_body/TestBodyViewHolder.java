package com.matthewtamlin.mixtape.library_tests.mixtape_body;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.matthewtamlin.mixtape.library.mixtape_body.BodyViewHolder;
import com.matthewtamlin.mixtape.library.mixtape_body.BodyViewHolder.Builder;
import com.matthewtamlin.mixtape.library_tests.test.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Unit tests for the {@link BodyViewHolder} class.
 */
@SuppressWarnings("ResourceType") // Using mock Views so don't need to use actual resource IDs
@RunWith(AndroidJUnit4.class)
public class TestBodyViewHolder {
	/**
	 * A view for use in testing.
	 */
	private View rootView;

	/**
	 * A TextView for use in testing. This view is the child of the root view.
	 */
	private TextView titleView;

	/**
	 * A TextView for use in testing. This view is the child of the root view.
	 */
	private TextView subtitleView;

	/**
	 * An ImageView for use in testing. This view is the child of the root view.
	 */
	private ImageView artworkView;

	/**
	 * A View for use in testing. This view is the child of the root view.
	 */
	private View contextualMenuButton;

	@Before
	public void setup() {
		// Inflate the root view and get references to its children
		final Context context = InstrumentationRegistry.getContext();
		rootView = LayoutInflater.from(context).inflate(R.layout.testbodyviewholder, null);
		titleView = (TextView) rootView.findViewById(R.id.testBodyViewHolder_titleView);
		subtitleView = (TextView) rootView.findViewById(R.id.testBodyViewHolder_subtitleView);
		artworkView = (ImageView) rootView.findViewById(R.id.testBodyViewHolder_artworkView);
		contextualMenuButton = rootView.findViewById(R.id.testBodyViewHolder_contextualMenuButton);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code rootView} argument of
	 * {@link Builder#builder(View)} is null.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBuilder_constructor_nullRootView() {
		BodyViewHolder.builder(null);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code titleTextView} argument
	 * of {@link Builder#withTitleTextView(TextView)} is null.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBuilder_withTitleTextView_1_viewIsNull() {
		final Builder builder = BodyViewHolder.builder(rootView);
		builder.withTitleTextView(null);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code titleTextViewResId}
	 * argument of {@link Builder#withTitleTextView(int)} does not correspond to a child of the root
	 * view.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBuilder_withTitleTextView_2_viewIsNull() {
		final Builder builder = BodyViewHolder.builder(rootView);
		builder.withTitleTextView(-1);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code subtitleTextView}
	 * argument of {@link Builder#withSubtitleTextView(TextView)} is null.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBuilder_withSubtitleTextView_1_viewIsNull() {
		final Builder builder = BodyViewHolder.builder(rootView);
		builder.withSubtitleTextView(null);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code subtitleTextViewResId}
	 * argument of {@link Builder#withSubtitleTextView(int)} does not correspond to a child of the
	 * root view.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBuilder_withSubtitleTextView_2_viewIsNull() {
		final Builder builder = BodyViewHolder.builder(rootView);
		builder.withSubtitleTextView(-1);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code artworkImageView}
	 * argument of {@link Builder#withArtworkImageView(ImageView)} is null.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBuilder_withArtworkImageView_1_viewIsNull() {
		final Builder builder = BodyViewHolder.builder(rootView);
		builder.withArtworkImageView(null);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code artworkImageViewResId}
	 * argument of {@link Builder#withArtworkImageView(int)} does not correspond to a child of the
	 * root view.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBuilder_withArtworkImageView_2_viewIsNull() {
		final Builder builder = BodyViewHolder.builder(rootView);
		builder.withArtworkImageView(-1);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code contextualMenuButton}
	 * argument of {@link Builder#withContextualMenuButton(View)} is null.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBuilder_withContextualMenuButton_1_viewIsNull() {
		final Builder builder = BodyViewHolder.builder(rootView);
		builder.withContextualMenuButton(null);
	}

	/**
	 * Test to verify that the correct exception is thrown when the {@code
	 * contextualMenuButtonResId} argument of {@link Builder#withContextualMenuButton(int)} does not
	 * correspond to a child of the root view.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBuilder_withContextualMenuButton_2_viewIsNull() {
		final Builder builder = BodyViewHolder.builder(rootView);
		builder.withContextualMenuButton(-1);
	}

	/**
	 * Test to verify that the {@link BodyViewHolder} method functions correctly when constructed
	 * using direct view references. The test will only pass if the BodyViewHolder returns all the
	 * correct views.
	 */
	@Test
	public void testBodyViewHolder_usingValidBuilder_1() {
		final Builder builder = BodyViewHolder.builder(rootView);
		builder.withTitleTextView(titleView);
		builder.withSubtitleTextView(subtitleView);
		builder.withArtworkImageView(artworkView);
		builder.withContextualMenuButton(contextualMenuButton);

		final BodyViewHolder bodyViewHolder = builder.build();
		assertThat("wrong title view", bodyViewHolder.getTitleTextView(), is(titleView));
		assertThat("wrong subtitle view", bodyViewHolder.getSubtitleTextView(), is
				(subtitleView));
		assertThat("wrong artwork view", bodyViewHolder.getArtworkImageView(), is(artworkView));
		assertThat("wrong contextual menu button", bodyViewHolder.getContextualMenuButton(), is
				(contextualMenuButton));
	}

	/**
	 * Test to verify that the {@link BodyViewHolder} method functions correctly when constructed
	 * using indirect resource IDs. The test will only pass if the BodyViewHolder returns all the
	 * correct views.
	 */
	@Test
	public void testBodyViewHolder_usingValidBuilder_2() {
		final Builder builder = BodyViewHolder.builder(rootView);
		builder.withTitleTextView(R.id.testBodyViewHolder_titleView);
		builder.withSubtitleTextView(R.id.testBodyViewHolder_subtitleView);
		builder.withArtworkImageView(R.id.testBodyViewHolder_artworkView);
		builder.withContextualMenuButton(R.id.testBodyViewHolder_contextualMenuButton);

		final BodyViewHolder bodyViewHolder = builder.build();
		assertThat("wrong title view", bodyViewHolder.getTitleTextView(), is(titleView));
		assertThat("wrong subtitle view", bodyViewHolder.getSubtitleTextView(), is
				(subtitleView));
		assertThat("wrong artwork view", bodyViewHolder.getArtworkImageView(), is(artworkView));
		assertThat("wrong contextual menu button", bodyViewHolder.getContextualMenuButton(), is
				(contextualMenuButton));
	}
}