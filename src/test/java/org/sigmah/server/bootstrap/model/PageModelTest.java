/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Alex Bertram
 */
public class PageModelTest {

    @Test
    public void testGetTemplateName() throws Exception {
        assertEquals("page/Login.ftl", PageModel.getTemplateName(LoginPageModel.class));
    }
}
