/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.database.hibernate.dao;

import java.util.Set;

import org.sigmah.server.database.hibernate.entity.Activity;

/**
 * DAO for the {@link org.activityinfo.server.database.hibernate.entity.Activity} domain object. Implemented automatically by proxy,
 * see the Activity class for query definitions.
 *
 * @author Alex Bertram
 */
public interface ActivityDAO extends DAO<Activity, Integer> {

    Integer queryMaxSortOrder(int databaseId);
    
    Set<Activity> getActivitiesByDatabaseId(int databaseId); 

}
