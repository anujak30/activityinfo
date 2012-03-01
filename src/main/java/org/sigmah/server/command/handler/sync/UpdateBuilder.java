/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command.handler.sync;

import org.json.JSONException;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.shared.command.GetSyncRegionUpdates;
import org.sigmah.shared.command.result.SyncRegionUpdate;

public interface UpdateBuilder {

    SyncRegionUpdate build(User user, GetSyncRegionUpdates request) throws JSONException;

}
