/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.generator;

import junit.framework.Assert;

import org.activityinfo.server.report.generator.ScaleUtil;
import org.junit.Test;

/**
 * @author Alex Bertram
 */
public class ScaleTest {

    @Test
    public void testScale() {

        ScaleUtil.Scale s = ScaleUtil.computeScale(1.2, 4.2, 10);

        Assert.assertEquals(0.0, s.getValmin());
        Assert.assertEquals(4.5, s.getValmax());
        Assert.assertEquals(0.5, s.getStep());
    }

    @Test
    public void testLargeScale() {

        ScaleUtil.Scale s = ScaleUtil.computeScale(1500, 9000, 10);

        Assert.assertEquals("valmin", 1000.0, s.getValmin());
        Assert.assertEquals("valmax", 10000.0, s.getValmax());
        Assert.assertEquals("step", 1000.0, s.getStep());
    }

}
