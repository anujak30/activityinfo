package org.activityinfo.server.report.generator.map.cluster.genetic;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.activityinfo.server.report.generator.map.cluster.Cluster;
import org.activityinfo.shared.report.content.Point;

/**
 * Finds a given number of cluster centers using the KMeans algorithm.
 * 
 * 
 * @author Alex Bertram
 */
public final class KMeans {

    private KMeans() {
    }

    public static List<Cluster> cluster(List<MarkerGraph.Node> nodes,
        int numClusters) {

        List<Cluster> clusters = new ArrayList<Cluster>(numClusters);

        // sanity check
        if (numClusters > nodes.size() || nodes.size() == 0) {
            throw new IllegalArgumentException();
        }

        // randomize
        Collections.shuffle(nodes);

        // choose random centers
        Point[] centers = new Point[numClusters];
        for (int i = 0; i != numClusters; ++i) {
            centers[i] = nodes.get(i).getPoint();
        }

        // assign initial cluster membership
        int[] membership = new int[nodes.size()];
        assignClosest(nodes, centers, membership);

        // execute k-means algorithm until we achieve convergence
        boolean changed;
        do {

            computeCenters(nodes, membership, centers);
            changed = assignClosest(nodes, centers, membership);

        } while (changed);

        // create clusters
        for (int i = 0; i != numClusters; ++i) {
            clusters.add(new Cluster(centers[i]));
        }

        for (int j = 0; j != nodes.size(); ++j) {
            (clusters.get(membership[j])).addNode(nodes.get(j));
        }

        return clusters;
    }

    /**
     * Computes the centers of the assigned clusters
     * 
     * @param nodes
     *            The list of nodes
     * @param membership
     *            An array containing the cluster membership for each node
     * @param centers
     *            Array of center points to update
     */
    private static void computeCenters(List<MarkerGraph.Node> nodes,
        int[] membership, Point[] centers) {
        int[] sumX = new int[centers.length];
        int[] sumY = new int[centers.length];
        int[] counts = new int[centers.length];

        for (int i = 0; i != nodes.size(); ++i) {
            Point point = nodes.get(i).getPoint();
            sumX[membership[i]] += point.getX();
            sumY[membership[i]] += point.getY();
            counts[membership[i]]++;

        }

        for (int i = 0; i != centers.length; ++i) {
            if (counts[i] > 0) {
                centers[i] = new Point(
                    sumX[i] / counts[i],
                    sumY[i] / counts[i]);
            }
        }
    }

    /**
     * Updates the membership array by assigning each node to its closest
     * cluster
     * 
     * @param nodes
     * @param centers
     * @param membership
     * @return True if cluster membership has changed
     */
    private static boolean assignClosest(List<MarkerGraph.Node> nodes,
        Point[] centers, int[] membership) {
        boolean changed = false;
        for (int i = 0; i != nodes.size(); ++i) {

            // for this node, find the closest
            // cluster center

            double minDist = Double.MAX_VALUE;
            int closest = 0;

            for (int j = 0; j != centers.length; ++j) {
                double dist = nodes.get(i).getPoint().distance(centers[j]);
                if (dist < minDist) {
                    minDist = dist;
                    closest = j;
                }
            }

            // update membership if necessary

            if (membership[i] != closest) {
                membership[i] = closest;
                changed = true;
            }
        }

        return changed;
    }
}
