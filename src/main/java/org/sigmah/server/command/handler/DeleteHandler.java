/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command.handler;

import java.util.Date;

import javax.persistence.EntityManager;

import org.activityinfo.shared.command.Delete;
import org.activityinfo.shared.command.result.CommandResult;
import org.sigmah.server.database.hibernate.entity.Deleteable;
import org.sigmah.server.database.hibernate.entity.ReallyDeleteable;
import org.sigmah.server.database.hibernate.entity.Site;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.database.hibernate.entity.UserDatabase;

import com.google.inject.Inject;

public class DeleteHandler implements CommandHandler<Delete> {
    private EntityManager em;

    @Inject
    public DeleteHandler(EntityManager em) {
        this.em = em;
    }

    @Override
    public CommandResult execute(Delete cmd, User user) {
        // TODO check permissions for delete!
        // These handler should redirect to one of the Entity policy classes.
        Class entityClass = entityClassForEntityName(cmd.getEntityName());
		Object entity = em.find(entityClass, cmd.getId());
        
        if (entity instanceof Deleteable) {
            Deleteable deleteable = (Deleteable) entity;
            deleteable.delete();
            
            if(entity instanceof Site) {
            	((Site)entity).setDateEdited(new Date());
            }
        }
        
        if (entity instanceof ReallyDeleteable) {
        	ReallyDeleteable reallyDeleteable = (ReallyDeleteable)entity;
        	reallyDeleteable.deleteReferences();
        	em.remove(reallyDeleteable);
        }
        
        return null;
    }

    private Class<Deleteable> entityClassForEntityName(String entityName) {
        try {
            return (Class<Deleteable>) Class.forName(UserDatabase.class.getPackage().getName() + "." + entityName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Invalid entity name '" + entityName + "'", e);
        } catch (ClassCastException e) {
            throw new RuntimeException("Entity type '" + entityName + "' not Deletable", e);
        }
    }
}
