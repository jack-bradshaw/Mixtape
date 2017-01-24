package com.matthewtamlin.mixtape.library.base_mvp;

/**
 * An element of the user interface. Views contain minimal business logic and delegate handling of
 * user input to a presenter.
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