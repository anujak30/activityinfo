/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.generator.map;

import java.awt.Graphics2D;
import java.awt.Image;

import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.report.content.Point;
import org.activityinfo.shared.util.mapping.Tile;
import org.activityinfo.shared.util.mapping.TileMath;

public class TiledMap {

	
	private static final int TILE_SIZE = 256;
	
	/**
	 * width in pixels of the map image
	 */
	private final int width;
	
	/**
	 * height in pixels of the map image
	 */
	private final int height;
	
	/**
	 * zoom level
	 */
	private final int zoom;
	
	
	/**
	 * The geographic center of the map
	 */
	private final AiLatLng geoCenter;
	

	/**
	 * The upper left hand corner of the image, in projected
	 * coordinate system units
	 */
	private Point origin;

	
	private Tile tileOrigin;
	
	
	public TiledMap(int width, int height, AiLatLng geographicCenter, int zoom) {
		this.width = width;
		this.height = height;
		this.zoom = zoom;
		this.geoCenter = geographicCenter;
		
		
		/*
		 * Calculate the center in pixels
		 */
		
		Point center = TileMath.fromLatLngToPixel(geographicCenter, zoom);
				
		origin = center.translate(-(width/2), -(height/2));
		
		
		tileOrigin = TileMath.tileForPoint(origin);
	}
	
	
	public void drawLayer(final Graphics2D g2d, TileProvider source)  {
		drawLayer(new TileDrawer() {

			@Override
			public void drawImage(Image image, int x, int y) {
				g2d.drawImage(image, x, y, TILE_SIZE, TILE_SIZE, null);
			}
			
		}, source);
	}
	
	public void drawLayer(TileDrawer drawer, TileProvider source)  {
		
		int x = -(origin.getX() % TILE_SIZE);		
		int tileX = tileOrigin.getX();
		
		while (x  < width) {

			int y = -(origin.getY() % TILE_SIZE);
			int tileY = tileOrigin.getY();
			
			while (y < height) {
				
				Image image = source.getImage(zoom, tileX, tileY);
				
				if(image != null) {
					drawer.drawImage(image, x, y);
				}
				
				y += TILE_SIZE;
				tileY ++;
			}
			x += TILE_SIZE;
			tileX ++;
		}
	}
	
	public Point fromLatLngToPixel(AiLatLng latLng) {
		return TileMath.fromLatLngToPixel(latLng, this.zoom)
					.translate(-origin.getDoubleX(), -origin.getDoubleY());
	}
	
	public AiLatLng fromPixelToLatLng(Point px) {
		return TileMath.inverse(px.translate(origin.getDoubleX(), origin.getDoubleY()), this.zoom);
	}
		
	public AiLatLng fromPixelToLatLng(double x, double y) {
		return fromPixelToLatLng(new Point(x,y));
	}

	public int getWidth() {
		return width;
	}


	public int getHeight() {
		return height;
	}

	public int getZoom() {
		return zoom;
	}


	public AiLatLng getGeoCenter() {
		return geoCenter;
	}
}
