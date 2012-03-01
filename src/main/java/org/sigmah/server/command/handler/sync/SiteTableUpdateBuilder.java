/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command.handler.sync;

import org.json.JSONException;
import org.sigmah.server.database.hibernate.entity.ReportingPeriod;
import org.sigmah.server.database.hibernate.entity.Site;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.shared.command.GetSyncRegionUpdates;
import org.sigmah.shared.command.result.SyncRegionUpdate;

import com.bedatadriven.rebar.sync.server.JpaUpdateBuilder;

public class SiteTableUpdateBuilder implements UpdateBuilder {

    private final JpaUpdateBuilder builder = new JpaUpdateBuilder();

    @Override
    public SyncRegionUpdate build(User user, GetSyncRegionUpdates request) throws JSONException {
        SyncRegionUpdate update = new SyncRegionUpdate();
        update.setComplete(true);
        update.setVersion("2");

        if(! "2".equals(request.getLocalVersion()) ) {

            builder.createTableIfNotExists(Site.class);
            builder.createTableIfNotExists(ReportingPeriod.class);
            builder.executeStatement("create index if not exists site_activity on site (ActivityId)");

            // TODO: fix rebar to handle these types of classes correctly
            builder.executeStatement("create table if not exists AttributeValue (SiteId integer, AttributeId integer, Value integer)");
            builder.executeStatement("create table if not exists IndicatorValue (ReportingPeriodId integer, IndicatorId integer, Value real)");
            update.setSql(builder.asJson());
        }

        return update;
    }
}
