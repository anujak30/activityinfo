/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.util.mapping;

import java.io.Serializable;

import org.activityinfo.shared.report.content.AiLatLng;


/**
 * One-to-one DTO for the {@link org.activityinfo.server.database.hibernate.entity.Bounds} domain object.
 * 
 * @author Alex Bertram
 */
public final class BoundingBoxDTO implements Serializable {
    private static final int LAT_MAX = 90;
	private static final int LNG_MAX = 180;
	private static final int LAT_MIN = -LAT_MAX;
	private static final int LNG_MIN = -180;
	
	private double x1;
    private double y1;
    private double x2;
    private double y2;

    /**
     * 
     * @return an empty bounding box (180, 90, -180, -90)
     */
    public static BoundingBoxDTO empty() {
    	return new BoundingBoxDTO();
    }
    
    /**
     *    
     * @return maximum geographic bounds (-180, -90, 180, 90)
     */
    public static BoundingBoxDTO maxGeoBounds() {
    	return new BoundingBoxDTO(LNG_MIN, LAT_MIN, LNG_MAX, LAT_MAX);
    }
    
    private BoundingBoxDTO() {
        this.x1 = LNG_MAX;
        this.y1 = LAT_MAX;
        this.x2 = LNG_MIN;
        this.y2 = LAT_MIN;
    }

    /**
     * 
     * @param x1
     *            Minimum x value (most westernly longitude)
     * @param y1
     *            Minimum y value (most southernly latitude)
     * @param x2
     *            Maximum x value (most easternly longitude)
     * @param y2
     *            Maximum y value (most northernly latitude)
     */
    public BoundingBoxDTO(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * Constructs a copy of the given BoundingBoxDTO
     * 
     * @param bounds
     *            the instance to copy
     */
    public BoundingBoxDTO(BoundingBoxDTO bounds) {
        this.x1 = bounds.getX1();
        this.y1 = bounds.getY1();
        this.x2 = bounds.getX2();
        this.y2 = bounds.getY2();
    }

    /**
     * Grows this BoundingBoxDTO to include the point at X, Y
     */
    public void grow(double x, double y) {
        if (x < x1) {
            x1 = x;
        }
        if (x > x2) {
            x2 = x;
        }
        if (y < y1) {
            y1 = y;
        }
        if (y > y2) {
            y2 = y;
        }
    }

    /**
     * Grows this BoundingBoxDTO to include the given bounds
     */
	public void grow(BoundingBoxDTO bounds) {
		if(bounds.x1 < x1) {
			this.x1 = bounds.x1;
		}
		if(bounds.y1 < y1) {
			this.y1 = bounds.y1;
		}
		if(bounds.x2 > x2) {
			this.x2 = bounds.x2;
		}
		if(bounds.y2 > y2) {
			this.y2 = bounds.y2;
		}
	}
    
    /**
     * 
     * @return true if the BoundingBoxDTO is empty
     */
    public boolean isEmpty() {
        return x1 > x2 || y1 > y2;
    }

    /**
     * @return the x (longitude) coordinate of the BoundingBoxDTO's centroid,
     *         (x1+x2)/2
     */
    public double getCenterX() {
        return (x1 + x2) / 2;
    }

    /**
     * 
     * @return the y (latitudinal) coordinate of the BoundingBoxDTO's centroid,
     *         (y1+y2)/2
     */
    public double getCenterY() {
        return (y1 + y2) / 2;
    }

    /**
     * Calculates the intersection of this BoundingBoxDTO with given
     * BoundingBoxDTO
     * 
     * @param b
     *            another BoundingBoxDTO with which to intersect this
     *            BoundingBoxDTO
     * @return the intersection of the two BoundingBoxDTOs
     */
    public BoundingBoxDTO intersect(BoundingBoxDTO b) {
        return new BoundingBoxDTO(Math.max(x1, b.x1), Math.max(y1, b.y1), Math.min(x2, b.x2), Math.min(y2, b.y2));
    }

    /**
     * @return true if this BoundingBoxDTO intersects with <code>b</code>
     */
    public boolean intersects(BoundingBoxDTO b) {
        return !(b.x2 < x1 || b.x1 > x2 || b.y2 < y1 || b.y1 > y2);
    }

    /**
     * 
     * @param b
     * @return true if this BoundingBoxDTO contains <code>b</code>
     */
    public boolean contains(BoundingBoxDTO b) {
        return b.x1 >= x1 && b.x2 <= x2 && b.y1 >= y1 && b.y2 <= y2;
    }

	public boolean contains(AiLatLng center) {
		return contains(center.getLng(), center.getLat());
	}

    
    /**
     * 
     * @return true if this BoundingBoxDTO contains the point at (x,y)
     */
    public boolean contains(double x, double y) {
        return x >= x1 && x <= x2 && y >= y1 && y <= y2;
    }

    /**
     * Clamps the given x coordinate to this BoundingBoxDTO's limits.
     * 
     * If <code>x</code> is between [x1, x2], return x If <code>x</code> is less
     * than x1, return x1 If <code>x</code> is greater than y1, return y1
     * 
     * @param x
     * @return the clamped value
     */
    public double clampX(double x) {
        if (x < x1) {
            return x1;
        }
        if (x > x2) {
            return x2;
        }
        return x;
    }

    /**
     * Clamps the given x coordinate to this BoundingBoxDTO's limits.
     * 
     * If <code>x</code> is between [x1, x2], return x If <code>x</code> is less
     * than x1, return x1 If <code>x</code> is greater than y1, return y1
     * 
     * @param x
     * @return the clamped value
     */
    public double clampY(double y) {
        if (y < y1) {
            return y1;
        }
        if (y > y2) {
            return y2;
        }
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BoundingBoxDTO) {
            BoundingBoxDTO b = (BoundingBoxDTO) o;
            return b.x1 == x1 && b.y1 == y1 && b.x2 == x2 && b.y2 == y2;
        }
        return false;
    }
 
    @Override
	public int hashCode() {
    	// probably not a great hash code but we can't use
    	// Double.longBits and this satisfies the contract
		return Double.valueOf(x1).hashCode();
	}

	/**
     * 
     * @return the minimum x (longitudinal) coordinate
     */
    public double getX1() {
        return x1;
    }

    /**
     * 
     * @return the minimum y (latitudinal) coordinate
     */
    public double getY1() {
        return y1;
    }

    /**
     * 
     * @return the maximum x (longitudinal) coordinate
     */
    public double getX2() {
        return x2;
    }

    /**
     * @return the maximum y (latitudinal) coordinate
     */
    public double getY2() {
        return y2;
    }

    /**
     * Sets the minimum x (longitudinal) value
     */
    public void setX1(double x1) {
        this.x1 = x1;
    }

    /**
     * Sets the minimum y (latitudinal) value
     */
    public void setY1(double y1) {
        this.y1 = y1;
    }

    /**
     * Sets the maximum x (longitudinal) value
     */
    public void setX2(double x2) {
        this.x2 = x2;
    }

    /**
     * Sets the maximum y (latitudinal) value
     */
    public void setY2(double y2) {
        this.y2 = y2;
    }
    
	public double getMinLat() {
		return y1;
	}
	
	public double getMaxLat() {
		return y2;
	}
	
	public double getMinLng() {
		return x1;
	}
	
	public double getMaxLng() {
		return x2;
	}
	
    @Override
    public String toString() {
        return "x1:" + x1 + ";x2:" + x2 + ";y1:" + y1 + ";y2:" + y2;
    }

	public double getWidth() {
		return x2-x1;
	}
	
	public double getHeight() {
		return y2-y1;
	}

	public AiLatLng centroid() {
		return new AiLatLng(getCenterY(), getCenterX());
	}

}
