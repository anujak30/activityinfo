/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.ppt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.activityinfo.shared.report.content.BubbleMapMarker;
import org.activityinfo.shared.report.content.IconMapMarker;
import org.activityinfo.shared.report.content.MapMarker;
import org.activityinfo.shared.report.model.MapReportElement;
import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.hslf.model.AutoShape;
import org.apache.poi.hslf.model.Picture;
import org.apache.poi.hslf.model.ShapeTypes;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.sigmah.server.report.generator.MapIconPath;
import org.sigmah.server.report.generator.map.IconRectCalculator;
import org.sigmah.server.report.renderer.image.ImageMapRenderer;
import org.sigmah.server.util.ColorUtil;

import com.google.inject.Inject;

/**
 * @author Alex Bertram
 */
public class PPTMapRenderer extends ImageMapRenderer {

    @Inject
    public PPTMapRenderer(@MapIconPath String mapIconPath) {
        super(mapIconPath);
    }

    @Override
    public void render(MapReportElement element, OutputStream stream) throws IOException {
        //create a new empty slide show
        SlideShow ppt = new SlideShow();
        Dimension pageSize = computePageSize(element);
        ppt.setPageSize(pageSize);

        render(element, ppt);

        // write to stream
        ppt.write(stream);

    }

    public void render(MapReportElement element, SlideShow ppt) throws IOException {

        //add first slide
        Slide slide = ppt.createSlide();

        // calculate map offset
        Dimension pageSize = ppt.getPageSize();
        int offsetX = ((int)pageSize.getWidth() - element.getWidth())/2;
        int offsetY = ((int)pageSize.getHeight() - element.getHeight())/2;

        // add the map background image
        int basemapPictureIndx = ppt.addPicture(renderBasemap(element), Picture.PNG);
        Picture basemap = new Picture(basemapPictureIndx);
        basemap.setAnchor(new Rectangle(offsetX, offsetY, element.getWidth(), element.getHeight()));
        basemap.setLineColor(Color.BLACK);
        basemap.setLineWidth(1);
        slide.addShape(basemap);

        // keep a list of map icons
        Map<String, Integer> iconPictureIndex = new HashMap<String, Integer>();

        // Add the indicator markers to the slide as shapes
        for(MapMarker marker : element.getContent().getMarkers()) {

        	if(inView(element, marker)) {
	        	
	            if(marker instanceof IconMapMarker) {
	                addIconMarker(ppt, slide, offsetX, offsetY, iconPictureIndex, (IconMapMarker) marker);
	            } else if( marker instanceof BubbleMapMarker) {
	                addBubble(slide, offsetX, offsetY, (BubbleMapMarker) marker);
	            }
        	}
        }
    }
    
    private boolean inView(MapReportElement element, MapMarker marker) {
    	return (marker.getX()+marker.getSize()) > 0 &&
    	       (marker.getY()+marker.getSize()) > 0 &&
    	       (marker.getX()-marker.getSize()) < element.getWidth() &&
    	       (marker.getY()-marker.getSize()) < element.getHeight();
    }

    private void addBubble(Slide slide, int offsetX, int offsetY, BubbleMapMarker marker) {
        AutoShape shape = new AutoShape(ShapeTypes.Ellipse);
        shape.setAnchor(new Rectangle(
                offsetX + marker.getX() - marker.getRadius(),
                offsetY + marker.getY() - marker.getRadius(),
                marker.getRadius()*2,
                marker.getRadius()*2));

        shape.setFillColor(ColorUtil.colorFromString(marker.getColor()));
        shape.setEscherProperty(EscherProperties.FILL__FILLOPACITY, 49087);
        shape.setLineColor(bubbleStrokeColor(ColorUtil.toInteger(marker.getColor())));
        slide.addShape(shape);
    }

    private void addIconMarker(SlideShow ppt, Slide slide, int offsetX, int offsetY,
                               Map<String, Integer> iconPictureIndex, IconMapMarker marker) {
        Integer iconIndex = iconPictureIndex.get(marker.getIcon().getName());
        if(iconIndex == null) {
            try {
                iconIndex = ppt.addPicture(
                        new File(getMapIconRoot() + "/" + marker.getIcon().getName() + ".png"),
                        Picture.PNG);
            } catch (IOException e) {
                iconIndex = -1;
            }
            iconPictureIndex.put(marker.getIcon().getName(), iconIndex);
        }
        if(iconIndex != -1) {
            IconRectCalculator rectCtor = new IconRectCalculator(marker.getIcon());
            Picture icon = new Picture(iconIndex);
            icon.setAnchor(rectCtor.iconRect(
                    offsetX + marker.getX(),
                    offsetY + marker.getY()));
            slide.addShape(icon);
        }
    }

    private byte[] renderBasemap(MapReportElement element) throws IOException {

        BufferedImage image = new BufferedImage(element.getWidth(), element.getHeight(), ColorSpace.TYPE_RGB);
        Graphics2D g2d = image.createGraphics();

        drawBasemap(g2d, element);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ImageIO.write(image, "PNG", out);

        return out.toByteArray();
    }

    private Dimension computePageSize(MapReportElement element) {
        // standard sizes
        Dimension[] stdSizes = new Dimension[] {
                new Dimension(720, 540), // Onscreen Show (4:5)
                new Dimension(720, 405), // Onscreen Show (16:9)
                new Dimension(780, 540), // A4 Portrait
                new Dimension(540, 780)
        };

        for(int i=0; i!=stdSizes.length; ++i) {
            if(stdSizes[i].getWidth() > element.getWidth() &&
                    stdSizes[i].getHeight() > element.getHeight()) {

                return stdSizes[i];

            }
        }

        return new Dimension(element.getWidth(), element.getHeight());
    }
}
