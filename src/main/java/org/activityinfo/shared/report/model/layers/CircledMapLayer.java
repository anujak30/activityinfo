package org.activityinfo.shared.report.model.layers;

import javax.xml.bind.annotation.XmlElement;

/*
 * Convenience class for layers using a circle to visualize indicators
 */
public abstract class CircledMapLayer extends AbstractMapLayer {

	private int minRadius = 8;
	private int maxRadius = 32;
	private double alpha = 0.75;
	private ScalingType scaling = ScalingType.Graduated;

	@XmlElement
	public int getMinRadius() {
	    return minRadius;
	}

	public void setMinRadius(int minRadius) {
	    this.minRadius = minRadius;
	}

	@XmlElement
	public int getMaxRadius() {
	    return maxRadius;
	}

	public void setMaxRadius(int maxRadius) {
	    this.maxRadius = maxRadius;
	}

	@XmlElement
	public double getAlpha() {
	    return alpha;
	}

	public void setAlpha(double alpha) {
	    this.alpha = alpha;
	}

	@XmlElement(defaultValue = "none")
	public ScalingType getScaling() {
	    return scaling;
	}

	public void setScaling(ScalingType scaling) {
	    this.scaling = scaling;
	}
}
