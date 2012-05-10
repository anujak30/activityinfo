/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.shared.command.BatchCommand;
import org.activityinfo.shared.command.CreateEntity;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.UpdateEntity;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.LocationTypeDTO;
import org.activityinfo.shared.dto.Published;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.test.InjectionSupport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/schema1.db.xml")
public class ActivityTest extends CommandTestCase {

	@Before
	public void setUser() {
		setUser(1);
	}

    @Test
    public void testActivity() throws CommandException {

        /*
           * Initial data load
           */

        SchemaDTO schema = execute(new GetSchema());

        UserDatabaseDTO db = schema.getDatabaseById(1);

        /*
           * Create a new activity
           */

        LocationTypeDTO locType = schema.getCountryById(1).getLocationTypes().get(0);

        ActivityDTO act = new ActivityDTO();
        act.setName("Warshing the dishes");
        act.setLocationTypeId(locType.getId());
        act.setReportingFrequency(ActivityDTO.REPORT_MONTHLY);

        CreateResult cresult = execute(CreateEntity.Activity(db, act));

        int newId = cresult.getNewId();

        /*
           * Reload schema to verify the changes have stuck
           */

        schema = execute(new GetSchema());

        act = schema.getActivityById(newId);

        Assert.assertEquals("name", "Warshing the dishes", act.getName());
        Assert.assertEquals("locationType", locType.getName(), act.getLocationType().getName());
        Assert.assertEquals("reportingFrequency", ActivityDTO.REPORT_MONTHLY, act.getReportingFrequency());
        Assert.assertEquals("public", Published.NOT_PUBLISHED.getIndex(), act.getPublished());

    }

    @Test
    public void updateSortOrderTest() throws Throwable {

        /* Update Sort Order */
        Map<String, Object> changes1 = new HashMap<String, Object>();
        changes1.put("sortOrder", 2);
        Map<String, Object> changes2 = new HashMap<String, Object>();
        changes2.put("sortOrder", 1);

        execute(new BatchCommand(
                new UpdateEntity("Activity", 1, changes1),
                new UpdateEntity("Activity", 2, changes2)
        ));

        /* Confirm the order is changed */

        SchemaDTO schema = execute(new GetSchema());
        Assert.assertEquals(2, schema.getDatabaseById(1).getActivities().get(0).getId());
        Assert.assertEquals(1, schema.getDatabaseById(1).getActivities().get(1).getId());
    }

    @Test
    public void updatePublished() throws Throwable {

        /* Update Sort Order */
        Map<String, Object> changes = new HashMap<String, Object>();
        changes.put("published", Published.ALL_ARE_PUBLISHED.getIndex());
    
        execute(new UpdateEntity("Activity", 1, changes));

        /* Confirm the order is changed */

        SchemaDTO schema = execute(new GetSchema());
        Assert.assertEquals(Published.ALL_ARE_PUBLISHED.getIndex(), schema.getActivityById(1).getPublished());
    }
}
