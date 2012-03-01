/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command.handler;

import org.sigmah.server.database.hibernate.dao.ReportDefinitionDAO;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.shared.command.GetReportDef;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.XmlResult;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

/**
 * @author Alex Bertram
 * @see org.sigmah.shared.command.GetReportDef
 */
public class GetReportDefHandler implements CommandHandler<GetReportDef> {

    private ReportDefinitionDAO reportDAO;

    @Inject
    public void setReportDAO(ReportDefinitionDAO dao) {
        this.reportDAO = dao;
    }

    @Override
    public CommandResult execute(GetReportDef cmd, User user)
            throws CommandException {
        return new XmlResult(reportDAO.findById(cmd.getId()).getXml());
    }

}
