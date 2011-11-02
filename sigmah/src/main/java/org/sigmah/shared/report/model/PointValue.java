/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.model;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.report.content.AiLatLng;
import org.sigmah.shared.report.content.PieMapMarker;
import org.sigmah.shared.report.content.Point;

public class PointValue {
    public SiteDTO site;
    public MapSymbol symbol;
    public double value;
    public Point px;
    public Rectangle iconRect;
    public List<PieMapMarker.SliceValue> slices;
	private Map<Integer, Integer> adminMembership = new HashMap<Integer, Integer>();

    public PointValue() {
    }

    public PointValue(SiteDTO site, MapSymbol symbol, double value, Point px) {
        this.site = site;
        this.symbol = symbol;
        this.value = value;
        this.px = px;
    }
    
    public PointValue(SiteDTO site, Point px, Rectangle iconRect) {
        this.site = site;
        this.px = px;
        this.iconRect = iconRect;
        this.value = 1;
    }
    
    public PointValue(SiteDTO site, Point px, Rectangle iconRect, double value) {
    	this(site, px, iconRect);
    	
        this.value = value;
    }

	public void setAdminMembership(Map<Integer, Integer> adminMembership) {
		this.adminMembership = adminMembership;
	}

	public Map<Integer, Integer> getAdminMembership() {
		return adminMembership;
	}


	public AiLatLng getLatLng() {
		if(site.hasLatLong()) {
			return new AiLatLng(site.getLatitude(), site.getLongitude());
		} else {
			return null;
		}
	}

	public boolean hasLatLng() {
		return getLatLng() != null;
	}

	public boolean hasPoint() {
		return px != null;
	}

	public Point getPoint() {
		return px;
	}

}