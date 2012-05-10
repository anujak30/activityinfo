/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command.handler.sync;

import org.activityinfo.shared.command.GetSyncRegionUpdates;
import org.activityinfo.shared.command.result.SyncRegionUpdate;
import org.json.JSONException;
import org.sigmah.server.database.hibernate.entity.User;

public interface UpdateBuilder {

    SyncRegionUpdate build(User user, GetSyncRegionUpdates request) throws JSONException;

}
