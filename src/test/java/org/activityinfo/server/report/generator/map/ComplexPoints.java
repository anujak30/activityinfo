/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.generator.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.activityinfo.server.report.generator.map.TiledMap;
import org.activityinfo.server.report.generator.map.cluster.genetic.MarkerGraph;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.report.model.PointValue;
import org.activityinfo.shared.util.mapping.Extents;
import org.activityinfo.shared.util.mapping.TileMath;


/**
 *
 * Reasonably complex, real-world point input data for testing 
 *
 * @author Alex Bertram
 */
public class ComplexPoints {
    public double originalSum;
    public Extents extents;
    public List<PointValue> points;
    public List<AiLatLng> latlngs;
    public MarkerGraph graph;

    ComplexPoints() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(
                        GraphTest.class.getResourceAsStream("/msa-points.csv")));

        extents = Extents.emptyExtents();

        originalSum = 0;

        points = new ArrayList<PointValue>();
        latlngs = new ArrayList<AiLatLng>();
        while(in.ready()) {

            String line = in.readLine();
            String[] columns = line.split(",");

            int partnerId = Integer.parseInt(columns[1]);


            double lng = Double.parseDouble(columns[2]);
            double lat = Double.parseDouble(columns[3]);
            double value = Double.parseDouble(columns[4]);

            PointValue pv = new PointValue();
            pv.setValue(value);
            pv.setSite(new SiteDTO());
            points.add(pv);

            originalSum += value;

            latlngs.add(new AiLatLng(lat, lng));

            extents.grow(lat, lng);

        }

        // project
        int zoom = TileMath.zoomLevelForExtents(extents, 640, 480);
        TiledMap map = new TiledMap(640, 480, extents.center(), zoom);

        for(int i=0;i!= points.size(); ++i) {
            points.get(i).setPx(map.fromLatLngToPixel(latlngs.get(i)));
        }

        // build graph
        graph = new MarkerGraph(points, new MarkerGraph.IntersectionCalculator() {
         public boolean intersects(MarkerGraph.Node a, MarkerGraph.Node b) {
             return a.getPoint().distance(b.getPoint()) < 30;
         }
     });
    }

    public List<List<MarkerGraph.Node>> getLargestN(int count) {
        List<List<MarkerGraph.Node>> subgraphs = graph.getSubgraphs();
        Collections.sort(subgraphs, new Comparator<List<MarkerGraph.Node>>() {
            public int compare(List<MarkerGraph.Node> o1, List<MarkerGraph.Node> o2) {
                if(o1.size() > o2.size()) {
                    return -1;
                } else if(o1.size() < o2.size()) {
                    return +1;
                } else {
                    return 0;
                }
            }
        });

        while(subgraphs.size() > count) {
            subgraphs.remove(count);
        }

        return subgraphs;
    }
}
