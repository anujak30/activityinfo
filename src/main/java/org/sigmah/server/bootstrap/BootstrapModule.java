/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap;

import com.google.inject.servlet.ServletModule;

/**
 * The Bootstrap module is responsible for the minimal static
 * html necessary to login, retrieve lost passwords, etc.
 */
public class BootstrapModule extends ServletModule {

    @Override
    protected void configureServlets() {
        serve(HostController.ENDPOINT).with(HostController.class);
        serve(LoginController.ENDPOINT).with(LoginController.class);
        serve(ConfirmInviteController.ENDPOINT).with(ConfirmInviteController.class);
        serve(LogoutController.ENDPOINT).with(LogoutController.class);
        serve(ResetPasswordController.ENDPOINT).with(ResetPasswordController.class);
        serve(ChangePasswordController.ENDPOINT).with(ChangePasswordController.class);
        
        serve("/ActivityInfo/ActivityInfo.nocache.js").with(SelectionServlet.class);
        serve("/ActivityInfo/ActivityInfo.appcache").with(SelectionServlet.class);
        serve("/ActivityInfo/ActivityInfo.gears.manifest").with(SelectionServlet.class);
    }
}
