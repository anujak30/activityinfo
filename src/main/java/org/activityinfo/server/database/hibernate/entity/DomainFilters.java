/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.database.hibernate.entity;

import javax.persistence.EntityManager;

import org.hibernate.Filter;
import org.hibernate.ejb.HibernateEntityManager;

/*
 * @author Alex Bertram
 */

public final class DomainFilters {

	private DomainFilters() {}
	
    public static void applyUserFilter(User user, EntityManager em) {
         applyDeletedFilter(em);
        applyVisibleFilter(user,em);

    }

    public static void applyDeletedFilter(EntityManager em) {
        org.hibernate.Session session = ((HibernateEntityManager) em).getSession();

        /* Hide entities deleted by users  */
        session.enableFilter("hideDeleted");
    }

    public static void applyVisibleFilter(User user, EntityManager em) {
        /* Hide entities that this user does not have permission to view */
        org.hibernate.Session session = ((HibernateEntityManager) em).getSession();

        Filter filter = session.enableFilter("userVisible");
		filter.setParameter("currentUserId", user.getId());
    }
}
