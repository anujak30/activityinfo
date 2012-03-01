/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc;

import org.sigmah.server.attachment.ServletAttachmentUpload;
import org.sigmah.server.schedule.ReportMailerServlet;

import com.google.inject.servlet.ServletModule;

public class GwtRpcModule extends ServletModule {

    @Override
    protected void configureServlets() {
        // The CacheFilter assures that static files marked with
        // .nocache (e.g. strongly named js permutations) get sent with the
        // appropriate cache header so browsers don't ask for it again.
        filter("/ActivityInfo/*").through(CacheFilter.class);

        serve("/ActivityInfo/cmd").with(CommandServlet.class);
        serve("/Embed/cmd").with(CommandServlet.class);
        serve("/ActivityInfo/download").with(DownloadServlet.class);
        serve("/ActivityInfo/attachment").with(ServletAttachmentUpload.class);
        serve("/tasks/mailSubscriptions").with(ReportMailerServlet.class);

        // this is here for now but should be probably live elsewhere, if
        // we really need it at all
        serve("/icon").with(MapIconServlet.class);
    }
}
