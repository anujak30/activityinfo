package org.activityinfo.server.report.generator.map.cluster;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.activityinfo.server.report.generator.map.cluster.genetic.MarkerGraph;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.report.content.Point;
import org.activityinfo.shared.report.model.PointValue;

/**
 * Represents a collection of nodes that
 * are clustered together in a given chromosome
 */
public class Cluster {
    private List<PointValue> pointValues;
    private double radius;
    private Point point;
    private Rectangle rectangle;
    private String title;
    
    /**
     * Creates a cluster positioned at the given point
     * on the projected space.
     * 
     * @param point the position of the cluster
     */
    public Cluster(Point point) {
        pointValues = new ArrayList<PointValue>();
        this.point = point;
    }

    /**
     * Creates a cluster positioned on the given 
     * PointValue
     * 
     * @param pointValue
     */
    public Cluster(PointValue pointValue) {
        pointValues = new ArrayList<PointValue>(1);
        pointValues.add(pointValue);
        point = pointValue.getPx();
    }

	public void addNode(MarkerGraph.Node node) {
        pointValues.add(node.getPointValue());
    }

	public void addPointValue(PointValue pv) {
		pointValues.add(pv);
	}

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean hasTitle() {
		return this.title != null;
	}
	
	/**
     *
     * @return The weighted centroid of the cluster
     */
	public Point bboxCenter() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for(PointValue pointValue : pointValues) {
            Point p = pointValue.getPx();
            if(p.getX() < minX) {
                minX = p.getX();
            }
            if(p.getY() < minY) {
                minY = p.getY();
            }
            if(p.getX() > maxX) {
                maxX = p.getX();
            }
            if(p.getY() > maxY) {
                maxY = p.getY();
            }
        }

        return new Point((minX + maxX)/2, (minY + maxY)/2);
    }


	public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public double sumValues() {
        double sum = 0;
        for(PointValue pointValue : pointValues) {
            sum += pointValue.getValue();
        }
        return sum;
    }

	public List<PointValue> getPointValues() {
        return pointValues;
    }

	public Rectangle getRectangle() {
        return rectangle;
    }

	public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

	public boolean hasPoint() {
		return point != null;
	}
}
