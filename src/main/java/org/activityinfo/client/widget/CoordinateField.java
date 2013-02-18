/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.widget;


import org.activityinfo.client.i18n.I18N;

import com.extjs.gxt.ui.client.widget.form.TextField;

/**
 * GXT Field for Geographical coordinates. The type of the field is double,
 * but users can enter coordinates in practically any format, which are converted on the fly.
 */
public class CoordinateField extends TextField<Double> {

    public enum Axis {
        LATITUDE,
        LONGITUDE
    }

    /**
     * Because of the conversion between DMS and degrees decimal,
     * we may loose some precision. This becomes a problem when the
     * coordinate is clamped to the administrative bounds, and the
     * resulting value is *exactly* on the boundary. When rounded,
     * the coordinate can fall on the wrong side of the boundary,
     * resulting in a validation error.
     *
     * The delta value below should be sufficient to allow for such
     * imprecision.
     */
    public static final double DELTA = 0.00001;
	
	private CoordinateEditor editor;

    /**
     * @param axis
     */
	public CoordinateField(Axis axis) {
		super();
		editor = new CoordinateEditor(axis);
		this.setPropertyEditor(editor);
        this.setValidator(editor);
        this.setValidateOnBlur(true);
	}

    /**
     * Sets the bounds for this field 
     * @param name the name of the bounds to present to users in the event of violation,
     * (e.g. "Kapisa Province Boundary"
     * @param minValue minimum allowed value for this field
     * @param maxValue maximum allowed value for this field
     */
	public void setBounds(String name, double minValue, double maxValue) {
		editor.setMinValue(minValue - DELTA);
		editor.setMaxValue(maxValue + DELTA);
        editor.setOutOfBoundsMessage(I18N.MESSAGES.coordOutsideBounds(name));
	}
}
