/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.UserResult;

/**
 * Queries the list of users authorized to access a given
 * {@link org.activityinfo.server.database.hibernate.entity.UserDatabase}
 *
 * The resulting {@link org.activityinfo.shared.dto.UserPermissionDTO} are
 * a projection of the UserLogin,
 * UserPermission, and
 * Partner tables.
 *
 */
public class GetUsers extends PagingGetCommand<UserResult>  {

    private int databaseId;

    private GetUsers() {
        // required
    }

	public GetUsers(int databaseId) {
		super();
		this.databaseId = databaseId;
	}

    /**
     * Gets the id of the database for which to query the list of UserPermissions.
     *
     * @return the id of the database for which to query the list of authorized users.
     */
	public int getDatabaseId() {
		return databaseId;
	}

    /**
     * Sets the id of the database for which toquery the list of UserPermissions.
      * @param databaseId
     */
	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
	}
}
