/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.config.design;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.replay;

import java.util.HashMap;
import java.util.Map;

import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.page.common.dialog.FormDialogCallback;
import org.activityinfo.client.page.common.dialog.FormDialogTether;
import org.activityinfo.client.page.common.grid.ConfirmCallback;
import org.activityinfo.client.page.config.design.DesignPresenter;
import org.activityinfo.shared.dto.EntityDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.TreeStore;

public class MockDesignTree implements DesignPresenter.View {

    public ModelData selection = null;
    public Map<String, Object> newEntityProperties = new HashMap<String, Object>();

    public void init(DesignPresenter presenter, UserDatabaseDTO db, TreeStore store) {

    }

    public FormDialogTether showNewForm(EntityDTO entity, FormDialogCallback callback) {

        for (String property : newEntityProperties.keySet()) {
            ((ModelData) entity).set(property, newEntityProperties.get(property));
        }

        FormDialogTether tether = createNiceMock(FormDialogTether.class);
        replay(tether);

        callback.onValidated(tether);
        return tether;
    }

    protected void mockEditEntity(EntityDTO entity) {

    }

    public void setActionEnabled(String actionId, boolean enabled) {

    }

    public void confirmDeleteSelected(ConfirmCallback callback) {

    }

    public ModelData getSelection() {
        return selection;
    }

    public AsyncMonitor getDeletingMonitor() {
        return null;
    }

    public AsyncMonitor getSavingMonitor() {
        return null;
    }
}
