/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.servlet.ServletException;

import org.activityinfo.shared.auth.AuthenticatedUser;
import org.junit.Before;
import org.junit.Test;
import org.sigmah.server.bootstrap.model.HostPageModel;
import org.sigmah.server.endpoint.content.ContentServlet;

public class HostControllerTest extends ControllerTestCase {

	private static final String CONTENT_ROOT = "/content/";
	
    @Before
    public void setupController() {
        controller = new HostController(injector, templateCfg);
        req.setRequestURL("http://www.activityinfo.org");
    }

    @Test
    public void verifyThatRequestsWithoutAuthTokensAreRedirectedToLoginPage() throws IOException, ServletException {

        get();

        assertEquals(resp.redirectUrl, CONTENT_ROOT);
    }

    @Test
    public void verifyThatRequestWithValidAuthTokensReceiveTheView() throws IOException, ServletException {
        req.addCookie(AuthenticatedUser.AUTH_TOKEN_COOKIE, GOOD_AUTH_TOKEN);

        get();

        assertTemplateUsed(HostPageModel.class);
    }

    @Test
    public void verifyThatRequestWithFakeAuthTokensAreRedirectedToLogin() throws IOException, ServletException {
        req.addCookie(AuthenticatedUser.AUTH_TOKEN_COOKIE, BAD_AUTH_TOKEN);

        get();

        assertEquals(CONTENT_ROOT, resp.redirectUrl);
    }

    @Test
    public void verifyThatBookmarksArePassedOnToLoginPage() throws IOException, ServletException {

        req.setRequestURL("http://www.activityinfo.org/#charts");

        get();

        assertTrue(resp.redirectUrl.endsWith("#charts"));
    }
}
