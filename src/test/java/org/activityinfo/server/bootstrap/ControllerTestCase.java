/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.not;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.persistence.NoResultException;
import javax.servlet.ServletException;

import org.activityinfo.server.authentication.Authenticator;
import org.activityinfo.server.bootstrap.AbstractController;
import org.activityinfo.server.bootstrap.fixtures.MockHttpServletRequest;
import org.activityinfo.server.bootstrap.fixtures.MockHttpServletResponse;
import org.activityinfo.server.bootstrap.fixtures.MockTemplateConfiguration;
import org.activityinfo.server.bootstrap.model.ConfirmInvitePageModel;
import org.activityinfo.server.bootstrap.model.HostPageModel;
import org.activityinfo.server.bootstrap.model.LoginPageModel;
import org.activityinfo.server.bootstrap.model.PageModel;
import org.activityinfo.server.database.hibernate.dao.AuthenticationDAO;
import org.activityinfo.server.database.hibernate.dao.UserDAO;
import org.activityinfo.server.database.hibernate.entity.Authentication;
import org.activityinfo.server.database.hibernate.entity.User;
import org.easymock.IAnswer;
import org.junit.Before;

import com.google.inject.Injector;

import freemarker.template.TemplateModelException;

/**
 * @author Alex Bertram
 */
public abstract class ControllerTestCase {
    protected MockHttpServletRequest req;
    protected MockHttpServletResponse resp;
    protected Injector injector;
    protected MockTemplateConfiguration templateCfg;
    protected AbstractController controller;

    protected static final String USER_EMAIL = "alex@bertram.com";
    protected static final String CORRECT_USER_PASS = "mypass";
    protected static final String WRONG_USER_PASS = "notmypass";

    protected static final String NEW_AUTH_TOKEN = "XYZ123";

    protected static final String NEW_USER_KEY = "ABC456";
    protected static final String NEW_USER_EMAIL = "bart@bart.nl";
    protected static final String BAD_KEY = "muwahaha";
    protected User newUser;
    protected static final String NEW_USER_NAME = "Henry";
    protected static final String NEW_USER_CHOSEN_LOCALE = "fr";

    protected static final String GOOD_AUTH_TOKEN = "BXD556";
    protected static final String BAD_AUTH_TOKEN = "NONSENSE";

    public ControllerTestCase() {
        newUser = new User();
    }

    @Before
    public final void setUpDependencies() throws TemplateModelException  {
        req = new MockHttpServletRequest();
        resp = new MockHttpServletResponse();

        User user = new User();
        user.setEmail(USER_EMAIL);
        user.setHashedPassword("$2$Foo");

        newUser.setEmail(NEW_USER_EMAIL);
        newUser.setNewUser(true);
        newUser.setEmailNotification(false);
        newUser.setHashedPassword("$2$Foo");
        newUser.setChangePasswordKey(NEW_USER_KEY);

        UserDAO userDAO = createMock(UserDAO.class);
        expect(userDAO.findUserByEmail(USER_EMAIL)).andReturn(user);
        expect(userDAO.findUserByEmail(not(eq(USER_EMAIL)))).andThrow(new NoResultException());

        expect(userDAO.findUserByChangePasswordKey(NEW_USER_KEY)).andReturn(newUser);
        expect(userDAO.findUserByChangePasswordKey(BAD_KEY)).andThrow(new NoResultException());
        replay(userDAO);

        Authentication auth = new Authentication(user);
        auth.setId(GOOD_AUTH_TOKEN);

        AuthenticationDAO authDAO = createNiceMock(AuthenticationDAO.class);
        expect(authDAO.findById(eq(GOOD_AUTH_TOKEN))).andReturn(auth);
        authDAO.persist(isA(Authentication.class));
        expectLastCall().andAnswer(new IAnswer<Object>() {
            @Override
            public Object answer() throws Throwable {
                Authentication auth = (Authentication) getCurrentArguments()[0];
                auth.setId(NEW_AUTH_TOKEN);
                return null;
            }
        });
        replay(authDAO);

        Authenticator authenticator = createMock(Authenticator.class);
        expect(authenticator.check(eq(user), eq(WRONG_USER_PASS))).andReturn(false);
        expect(authenticator.check(eq(user), eq(CORRECT_USER_PASS))).andReturn(true);
        replay(authenticator);

        injector = createNiceMock(Injector.class);
        expect(injector.getInstance(AuthenticationDAO.class)).andReturn(authDAO);
        expect(injector.getInstance(Authenticator.class)).andReturn(authenticator);
        expect(injector.getInstance(UserDAO.class)).andReturn(userDAO);
        replay(injector);

        templateCfg = new MockTemplateConfiguration();
    }

    public void get() throws IOException, ServletException {
        controller.callGet(req, resp);
    }

    protected void get(String url) throws IOException, ServletException {
        req.setRequestURL(url);
        get();
    }

    protected void post() throws IOException, ServletException {
        controller.callPost(req, resp);
    }


    protected <T extends PageModel> void assertTemplateUsed(Class<T> modelclass) {
        assertEquals(PageModel.getTemplateName(modelclass), templateCfg.lastTemplateName);
    }

    private HostPageModel lastHostPageModel() {
        return ((HostPageModel) templateCfg.lastModel);
    }

    protected LoginPageModel lastLoginPageModel() {
        return (LoginPageModel) templateCfg.lastModel;
    }

    protected ConfirmInvitePageModel lastNewUserPageModel() {
        return (ConfirmInvitePageModel) templateCfg.lastModel;
    }
}
