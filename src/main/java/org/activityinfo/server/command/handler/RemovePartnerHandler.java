

package org.activityinfo.server.command.handler;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.Date;

import javax.persistence.EntityManager;

import org.activityinfo.server.database.hibernate.entity.Partner;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.database.hibernate.entity.UserDatabase;
import org.activityinfo.server.database.hibernate.entity.UserPermission;
import org.activityinfo.shared.command.RemovePartner;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.IllegalAccessCommandException;
import org.activityinfo.shared.exception.PartnerHasSitesException;

import com.google.inject.Inject;

/**
 * @author Alex Bertram
 * @see org.activityinfo.shared.command.RemovePartner
 */
public class RemovePartnerHandler implements CommandHandler<RemovePartner> {

    private EntityManager em;

    @Inject
    public RemovePartnerHandler(EntityManager em) {
        this.em = em;
    }

    public CommandResult execute(RemovePartner cmd, User user) throws CommandException {

        // verify the current user has access to this site
        UserDatabase db = em.find(UserDatabase.class, cmd.getDatabaseId());
        if (db.getOwner().getId() != user.getId()) {
            UserPermission perm = db.getPermissionByUser(user);
            if (perm == null || perm.isAllowDesign()) {
                throw new IllegalAccessCommandException();
            }
        }

        // check to see if there are already sites associated with this
        // partner

        int siteCount = ((Number) em.createQuery("select count(s) from Site s where " +
                "s.activity.id in (select a.id from Activity a where a.database.id = :dbId) and " +
                "s.partner.id = :partnerId and " +
                "s.dateDeleted is null")
                .setParameter("dbId", cmd.getDatabaseId())
                .setParameter("partnerId", cmd.getPartnerId())
                .getSingleResult()).intValue();

        if (siteCount > 0) {
            throw new PartnerHasSitesException();
        }

        db.getPartners().remove(em.getReference(Partner.class, cmd.getPartnerId()));
        db.setLastSchemaUpdate(new Date());
        
        return new VoidResult();
    }
}
