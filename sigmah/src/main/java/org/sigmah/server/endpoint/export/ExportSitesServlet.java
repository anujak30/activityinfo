/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.export;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sigmah.server.command.DispatcherSync;
import org.sigmah.server.dao.AuthenticationDAO;
import org.sigmah.server.domain.Authentication;
import org.sigmah.server.domain.DomainFilters;
import org.sigmah.server.endpoint.gwtrpc.handler.HandlerUtil;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

/**
 * Exports complete data to an Excel file
 *
 * @author Alex Bertram
 */
@Singleton
public class ExportSitesServlet extends HttpServlet {
    private DispatcherSync dispatcher;
    
    @Inject
    public ExportSitesServlet(DispatcherSync dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Set<Integer> activities = new HashSet<Integer>();
        for (String activity : req.getParameterValues("a")) {
            activities.add(Integer.parseInt(activity));
        }

        SchemaDTO schema = dispatcher.execute(new GetSchema());

        SiteExporter export = new SiteExporter(dispatcher);
        for (UserDatabaseDTO db : schema.getDatabases()) {
            for (ActivityDTO activity : db.getActivities()) {
                if (activities.size() == 0 || activities.contains(activity.getId())) {
                    export.export(activity);
                }
            }
        }

        resp.setContentType("application/vnd.ms-excel");
        if (req.getHeader("User-Agent").indexOf("MSIE") != -1) {
            resp.addHeader("Content-Disposition", "attachment; filename=ActivityInfo.xls");
        } else {
            resp.addHeader("Content-Disposition", "attachment; filename=" +
                    ("ActivityInfo Export " + new Date().toString() + ".xls").replace(" ", "_"));
        }

        OutputStream os = resp.getOutputStream();
        export.getBook().write(os);
    }
}