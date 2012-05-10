/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.renderer.ppt;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.Shape;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.junit.Ignore;
import org.junit.Test;

public class PptAlphaCheck {


    @Test
    @Ignore("learning test only")
    public void testAlphaValues() throws IOException {


        InputStream in = PptAlphaCheck.class.getResourceAsStream("/alpha.ppt");
        SlideShow ppt = new SlideShow( new HSLFSlideShow(in) );

        Slide[] slides = ppt.getSlides();
        Shape[] shapes = slides[0].getShapes();

        for(int i=0; i!=shapes.length; ++i) {
            System.out.println(String.format("slide %d, opacity = %d", i,
                    shapes[i].getEscherProperty(EscherProperties.FILL__FILLOPACITY)));
           
            
        }

    }
}
