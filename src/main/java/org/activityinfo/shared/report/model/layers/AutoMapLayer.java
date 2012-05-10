package org.activityinfo.shared.report.model.layers;

/**
 * Map Layer which tries to visualize all available map data optimized for usability. 
 */
public class AutoMapLayer extends AbstractMapLayer {

	@Override
	public boolean supportsMultipleIndicators() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getTypeName() {
		return "Automatic";
	}
}
