package org.sigmah.server.report.generator.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.sigmah.server.report.generator.map.cluster.Cluster;
import org.sigmah.server.report.generator.map.cluster.Clusterer;
import org.sigmah.server.report.generator.map.cluster.ClustererFactory;
import org.sigmah.server.report.generator.map.cluster.genetic.MarkerGraph;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.report.content.AiLatLng;
import org.sigmah.shared.report.content.BubbleMapMarker;
import org.sigmah.shared.report.content.DimensionCategory;
import org.sigmah.shared.report.content.EntityCategory;
import org.sigmah.shared.report.content.MapContent;
import org.sigmah.shared.report.content.MapMarker;
import org.sigmah.shared.report.content.PieChartLegend;
import org.sigmah.shared.report.content.PieMapMarker;
import org.sigmah.shared.report.content.Point;
import org.sigmah.shared.report.model.MapSymbol;
import org.sigmah.shared.report.model.PointValue;
import org.sigmah.shared.report.model.layers.PiechartMapLayer;
import org.sigmah.shared.report.model.layers.PiechartMapLayer.Slice;
import org.sigmah.shared.report.model.layers.ScalingType;
import org.sigmah.shared.util.mapping.Extents;

public class PiechartLayerGenerator extends AbstractLayerGenerator {

    private PiechartMapLayer layer;
	private List<SiteDTO> sites;

    public PiechartLayerGenerator(PiechartMapLayer layer, List<SiteDTO> sites) {
        this.layer = layer;
        this.sites=sites;
    }

    public Extents calculateExtents() {

        // PRE---PASS - calculate extents of sites WITH non-zero
        // values for this indicator
        Extents extents = Extents.emptyExtents();
        for(SiteDTO site : sites) {
            if (site.hasLatLong() && hasValue(site, layer.getIndicatorIds())) {
                extents.grow(site.getLatitude(), site.getLongitude());
            }
        }

        return extents;
    } 

    public void generate(TiledMap map, MapContent content) {

        // create the list of input point values
        List<PointValue> points = new ArrayList<PointValue>();
        List<PointValue> unmapped = new ArrayList<PointValue>();

       
        // define our symbol scaling
        RadiiCalculator radiiCalculator;
        if(layer.getScaling() == ScalingType.None ||
                layer.getMinRadius() == layer.getMaxRadius())
        {
            radiiCalculator = new FixedRadiiCalculator(layer.getMinRadius());
        } else if(layer.getScaling() == ScalingType.Graduated) {
            radiiCalculator = new GsLogCalculator(layer.getMinRadius(), layer.getMaxRadius());
        } else {
            radiiCalculator = new FixedRadiiCalculator(layer.getMinRadius());
        }

        Clusterer clusterer = ClustererFactory.fromClustering(layer.getClustering(), radiiCalculator,
        		new BubbleIntersectionCalculator(layer.getMaxRadius()));

        generatePoints(map, layer,clusterer, points, unmapped);
        
        // add unmapped sites
        for(PointValue pv : unmapped) {
            content.getUnmappedSites().add(pv.getSite().getId());
        }
        
        List<Cluster> clusters = clusterer.cluster(map, points);

        // create the markers
        List<BubbleMapMarker> markers = new ArrayList<BubbleMapMarker>();
        for(Cluster cluster : clusters) {
            Point px = cluster.getPoint();
            AiLatLng latlng = map.fromPixelToLatLng(px);
            BubbleMapMarker marker = new PieMapMarker();

            sumSlices((PieMapMarker) marker, cluster.getPointValues());
            for(PointValue pv : cluster.getPointValues()) {
                marker.getSiteIds().add(pv.getSite().getId());
            }
            marker.setX(px.getX());
            marker.setY(px.getY());
            marker.setValue(cluster.sumValues());
            marker.setRadius((int)cluster.getRadius());
            marker.setLat(latlng.getLat());
            marker.setLng(latlng.getLng());
            marker.setAlpha(layer.getAlpha());
            marker.setIndicatorIds(new HashSet<Integer>(layer.getIndicatorIds()));
            marker.setClusterAmount(cluster.getPointValues().size());
            marker.setClustering(layer.getClustering());

            markers.add(marker);
        }

        // number markers if applicable
        if(layer.getLabelSequence() != null) {
            numberMarkers(markers);
        }

        PieChartLegend legend = new PieChartLegend();
        legend.setDefinition(layer);
        
        content.getMarkers().addAll(markers);
		content.addLegend(legend);
    }

    public void generatePoints(
            TiledMap map,
            PiechartMapLayer layer,
            Clusterer clusterer,
            List<PointValue> mapped,
            List<PointValue> unmapped) {

    	// TODO: rework method for piechart (copy/pasted from bubblelayer)
    	
        for(SiteDTO site : sites) {
            if(hasValue(site, layer.getIndicatorIds())) {
                Point px = null;
                
                if(site.hasLatLong())  {
                    px = map.fromLatLngToPixel(new AiLatLng(site.getLatitude(), site.getLongitude()));
                }

                Double value = getValue(site, layer.getIndicatorIds());
                if(value != null && value != 0) {
                    PointValue pv = new PointValue(site,
                            new MapSymbol(),
                            value, px);
                    calulateSlices(pv, site);
                    if(clusterer.isMapped(site)) {
                    	mapped.add(pv);
                    } else {
                    	unmapped.add(pv);
                    }
                }
            }
        }
    }

    public int findColor(MapSymbol symbol, PiechartMapLayer layer) {
    	return 0;
    }

    private void numberMarkers(List<BubbleMapMarker> markers) {
        // sort the markers, left-to right, top to bottom so the label
        // sequence is spatially consistent
        Collections.sort(markers, new MapMarker.LRTBComparator());

        // add the labels
        for(BubbleMapMarker marker : markers) {
            marker.setLabel(layer.getLabelSequence().next());
        }
    }

    public static class IntersectionCalculator implements MarkerGraph.IntersectionCalculator {
        private int radius;

        public IntersectionCalculator(int radius) {
            this.radius = radius;
        }

        public boolean intersects(MarkerGraph.Node a, MarkerGraph.Node b) {
            return a.getPoint().distance(b.getPoint()) < radius *2 &&
                    a.getPointValue().getSymbol().equals(b.getPointValue().getSymbol());
        }
    }
    
    private void calulateSlices(PointValue pv, SiteDTO site) {
        pv.setSlices(new ArrayList<PieMapMarker.SliceValue>());
        
        for(Slice slice : layer.getSlices()) {
            EntityCategory indicatorCategory = new EntityCategory(slice.getIndicatorId());
            Double value = site.getIndicatorValue(slice.getIndicatorId());
            if(value != null && value != 0) {
                PieMapMarker.SliceValue sliceValue = new PieMapMarker.SliceValue();
                
                sliceValue.setValue(value);
                sliceValue.setCategory(indicatorCategory);
                sliceValue.setColor(slice.getColor());
                sliceValue.setIndicatorId(slice.getIndicatorId());

                pv.getSlices().add(sliceValue);
            }
        }
    }

    private void sumSlices(PieMapMarker marker, List<PointValue> pvs) {
        Map<DimensionCategory, PieMapMarker.SliceValue> slices = new HashMap<DimensionCategory, PieMapMarker.SliceValue>();
        for(PointValue pv : pvs ) {
            for(PieMapMarker.SliceValue slice : pv.getSlices())  {
                PieMapMarker.SliceValue summedSlice = slices.get(slice.getCategory());
                if(summedSlice == null) {
                    summedSlice = new PieMapMarker.SliceValue(slice);
                    summedSlice.setIndicatorId(slice.getIndicatorId());
                    slices.put(slice.getCategory(), summedSlice);
                } else {
                    summedSlice.setValue(summedSlice.getValue() + slice.getValue());
                }
            }
        }
        marker.setSlices(new ArrayList<PieMapMarker.SliceValue>(slices.values()));
    }

	@Override
	public Margins calculateMargins() {
        return new Margins(layer.getMaxRadius());

	}

}
