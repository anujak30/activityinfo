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
import java.util.List;

import org.activityinfo.shared.report.content.Point;
import org.activityinfo.shared.report.model.PointValue;

/**
 * Graph structure used to determine which groups of PointValues (nodes) could
 * potentially overlap with each other.
 * 
 */
public class MarkerGraph {
    private List<List<Node>> subgraphs;

    /**
     * Represents an edge between two <code>PointValue</code>s (
     * <code>Node</code>s) on the map. Two <code>PointValue</code> are connected
     * if they potentially overlap.
     */
    public static class Edge {
        private Node a;
        private Node b;
        private int subgraph = -1;

        public Edge(Node a, Node b) {
            this.a = a;
            this.b = b;
        }

        public Node neighbor(Node node) {
            return node == a ? b : a;
        }

        public double length() {
            return a.getPoint().distance(b.getPoint());
        }

        public Node getA() {
            return a;
        }

        public Node getB() {
            return b;
        }
    }

    /**
     * Wraps <code>PointValue</code> as a node in the graph
     */
    public static class Node {
        private List<Edge> edges;
        private PointValue pv;
        private int subgraph = -1;

        public Node(PointValue pv) {
            this.pv = pv;
            this.edges = new ArrayList<Edge>();
        }

        public void addEdge(Edge e) {
            edges.add(e);
        }

        public Point getPoint() {
            return pv.getPx();
        }

        public PointValue getPointValue() {
            return pv;
        }

        public List<Edge> getEdges() {
            return edges;
        }

        public double getValue() {
            return pv.getValue();
        }
    }

    public interface IntersectionCalculator {
        public boolean intersects(Node a, Node b);
    }

    private List<Node> nodes;
    private List<Edge> edges;

    /**
     * Constructs a graph of <code>PointValue</code>
     * 
     * @param points
     */
    public MarkerGraph(List<PointValue> points,
        IntersectionCalculator icalculator) {

        nodes = new ArrayList<Node>();
        edges = new ArrayList<Edge>();

        for (PointValue pv : points) {
            nodes.add(new Node(pv));
        }

        for (int i = 0; i != nodes.size(); ++i) {
            Node ni = nodes.get(i);

            int j = i + 1;
            while (j < nodes.size()) {
                Node nj = nodes.get(j);

                if (icalculator.intersects(ni, nj)) {

                    // check for coincidence, which screws things up
                    // if we leave in place
                    if (ni.getPoint().equals(nj.getPoint())) {
                        // merge nodes in the case of coincidence
                        ni.getPointValue().setValue(
                            ni.getPointValue().getValue()
                                + nj.getPointValue().getValue());
                        for (Edge edge : nj.getEdges()) {
                            edge.neighbor(nj).getEdges().remove(edge);
                            edges.remove(edge);
                        }
                        nodes.remove(j);
                    } else {
                        // otherwise connect them in the graph
                        Edge e = new Edge(ni, nj);
                        ni.addEdge(e);
                        nj.addEdge(e);
                        edges.add(e);
                        j++;
                    }
                } else {
                    j++;
                }
            }
        }

        subgraphs = new ArrayList<List<Node>>();

        int nextSubgraph = 0;
        for (Node node : nodes) {
            if (node.subgraph < 0) {

                List<Node> subgraph = new ArrayList<Node>();
                subgraph.add(node);
                subgraphs.add(subgraph);

                node.subgraph = nextSubgraph++;

                assignToSubgraph(node, subgraph);
            }
        }
    }

    private void assignToSubgraph(Node node, List<Node> subgraph) {
        for (Edge edge : node.getEdges()) {
            if (edge.subgraph < 0) {
                edge.subgraph = node.subgraph;
                Node neighbor = edge.neighbor(node);
                if (neighbor.subgraph < 0) {
                    neighbor.subgraph = node.subgraph;
                    subgraph.add(neighbor);
                    assignToSubgraph(neighbor, subgraph);
                }
            }
        }
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<List<Node>> getSubgraphs() {
        return subgraphs;
    }
}
