package com.matthewtamlin.mixtape.library.mixtape_coordinator;

import com.matthewtamlin.mixtape.library.mixtape_body.BodyContract;
import com.matthewtamlin.mixtape.library.mixtape_header.HeaderContract;

/**
 * Coordinates the display of two views: a header and a body. The header is displayed at the top of
 * the view, and the body is displayed directly below it.
 */
public interface MixtapeContainerView<H extends HeaderContract.View, B extends BodyContract.View> {
	/**
	 * @return the header currently being displayed, null if there is none
	 */
	H getHeader();

	/**
	 * Sets the header to display.
	 *
	 * @param header
	 * 		the header to display, null to display none
	 */
	void setHeader(H header);

	/**
	 * @return the body currently being displayed, null if there is none
	 */
	B getBody();

	/**
	 * Sets the body to display.
	 *
	 * @param body
	 * 		the body to display, null to display none
	 */
	void setBody(B body);

	/**
	 * Sets the material elevation of the header.
	 *
	 * @param elevationDp
	 * 		the elevation, measured in display-independent pixels
	 */
	void setHeaderElevationDp(int elevationDp);

	/**
	 * Sets the material elevation of the header.
	 *
	 * @param elevationPx
	 * 		the elevation, measured in pixels
	 */
	void setHeaderElevationPx(int elevationPx);

	/**
	 * Sets the material elevation of the body.
	 *
	 * @param elevationDp
	 * 		the elevation, measured in display-independent pixels
	 */
	void setBodyElevationDp(int elevationDp);

	/**
	 * Sets the material elevation of the body.
	 *
	 * @param elevationPx
	 * 		the elevation, measured in pixels
	 */
	void setBodyElevationPx(int elevationPx);
}