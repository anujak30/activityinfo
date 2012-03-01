/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.database.OnDataSet;
import org.sigmah.shared.command.GetInvitationList;
import org.sigmah.shared.command.result.InvitationList;
import org.sigmah.test.InjectionSupport;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.SortInfo;

@RunWith(InjectionSupport.class)
public class InvitationTest extends CommandTestCase {

    @Test
    @OnDataSet("/dbunit/schema1.db.xml")
    public void testGetList() throws Exception {
        GetInvitationList cmd = new GetInvitationList(1);
        cmd.setSortInfo(new SortInfo("userName", Style.SortDir.ASC));

        InvitationList list = execute(cmd);

        Assert.assertEquals("rows", 3, list.getData().size());

        Assert.assertEquals("Alex", list.getData().get(0).getUserName());
        Assert.assertEquals("Bavon", list.getData().get(1).getUserName());
        Assert.assertEquals("Stefan", list.getData().get(2).getUserName());

        Assert.assertTrue("alex is subscribed", list.getData().get(0).isSubscribed());
        Assert.assertFalse("bavon is not subscribed", list.getData().get(1).isSubscribed());
    }
}
