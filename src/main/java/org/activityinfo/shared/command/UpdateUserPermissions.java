/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.dto.UserPermissionDTO;

/**
 * Update the permissions of a user to access a given database.
 *
 * The permissions are updated based on email address as the client
 * will not necessarily know whether a given person already has an
 * account with ActivityInfo. This means that a call to UpdateUserPermissions
 * can result in the creation of a new user account and all that entails,
 * such as an email message, etc.
 *
 */
public class UpdateUserPermissions implements MutatingCommand<VoidResult>  {

	private int databaseId;
	private UserPermissionDTO model;

	
	protected UpdateUserPermissions() {
		
	}

    public UpdateUserPermissions(UserDatabaseDTO db, UserPermissionDTO model) {
        this(db.getId(), model);
    }
	
	public UpdateUserPermissions(int databaseId, UserPermissionDTO model) {
		this.databaseId = databaseId;
        this.model = model;              

	}

    public int getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
	}

    public UserPermissionDTO getModel() {
        return model;
    }

    public void setModel(UserPermissionDTO model) {
        this.model = model;
    }
}
