/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.config.design;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.resetToDefault;
import static org.easymock.EasyMock.verify;

import java.util.List;

import org.activityinfo.client.MockEventBus;
import org.activityinfo.client.dispatch.DispatcherStub;
import org.activityinfo.client.mock.StateManagerStub;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.common.grid.ConfirmCallback;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.page.config.design.DesignPresenter;
import org.activityinfo.client.page.config.design.IndicatorFolder;
import org.activityinfo.client.page.entry.place.DataEntryPlace;
import org.activityinfo.shared.command.CreateEntity;
import org.activityinfo.shared.command.Delete;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.UpdateEntity;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.DTOs;
import org.activityinfo.shared.dto.SchemaDTO;
import org.easymock.IAnswer;
import org.junit.Assert;
import org.junit.Test;
import org.activityinfo.client.i18n.UIConstants;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.TreeStore;

public class DesignTest {


    @Test
    public void testSave() {

        // Dummy Data
        SchemaDTO schema = DTOs.PEAR();


        // Collaborator
        MockEventBus eventBus = new MockEventBus();

        // Collaborator
        DispatcherStub service = new DispatcherStub();
        service.setResult(GetSchema.class, schema);
        service.setResult(UpdateEntity.class, new VoidResult());

        // Collaborator
        DesignPresenter.View view = createNiceMock(DesignPresenter.View.class);
        replay(view);

        // Localisation resources
        UIConstants constants = createNiceMock(UIConstants.class);
        replay(constants);

        DesignPresenter designer = new DesignPresenter(eventBus, service, new StateManagerStub(),
                view, constants);
        designer.go(schema.getDatabaseById(1));

        // Verify that following a change to the record, a save call
        // triggers an update command

        ActivityDTO activity = (ActivityDTO) ((TreeStore)designer.getStore()).getRootItems().get(0);
        Record record = designer.getStore().getRecord(activity);

        record.set("name", "New Name");

        designer.onUIAction(UIActions.SAVE);

        UpdateEntity cmd = service.getLastExecuted(UpdateEntity.class);

        Assert.assertTrue(cmd.getChanges().containsKey("name"));
        Assert.assertEquals("New Name", cmd.getChanges().get("name"));

    }

    @Test
    public void testSaveOnNavigateAway() {

        // Dummy Data
        SchemaDTO schema = DTOs.PEAR();


        // Collaborator
        MockEventBus eventBus = new MockEventBus();

        // Collaborator
        DispatcherStub service = new DispatcherStub();
        service.setResult(GetSchema.class, schema);
        service.setResult(UpdateEntity.class, new VoidResult());

        // Collaborator
        DesignPresenter.View view = createNiceMock(DesignPresenter.View.class);
        replay(view);

        // Collaborator
        UIConstants constants = createNiceMock(UIConstants.class);
        replay(constants);

        DesignPresenter designer = new DesignPresenter(eventBus, service, new StateManagerStub(),
                view, constants);
        designer.go(schema.getDatabaseById(1));

        // Verify that following a change to the record, a save call
        // triggers an update command

        ActivityDTO activity = (ActivityDTO) ((TreeStore)designer.getStore()).getRootItems().get(0);
        Record record = designer.getStore().getRecord(activity);

        record.set("name", "New Name");

        designer.requestToNavigateAway(new DataEntryPlace(), new NavigationCallback() {
            public void onDecided(boolean allowed) {

            }
        });

        UpdateEntity cmd = service.getLastExecuted(UpdateEntity.class);

        Assert.assertTrue(cmd.getChanges().containsKey("name"));
        Assert.assertEquals("New Name", cmd.getChanges().get("name"));

    }

    @Test
    public void testDelete() {


        // Dummy Data
        SchemaDTO schema = DTOs.PEAR();

        // Collaborator
        DispatcherStub service = new DispatcherStub();
        service.setResult(GetSchema.class, schema);
        service.setResult(Delete.class, new VoidResult());

        // Collaborator
        DesignPresenter.View view = createNiceMock(DesignPresenter.View.class);

        expect(view.getSelection()).andReturn(schema.getActivityById(91));
        view.confirmDeleteSelected(isA(ConfirmCallback.class));
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                ((ConfirmCallback)getCurrentArguments()[0]).confirmed();
                return null;
            }
        });
        replay(view);

        // Collaborator
        UIConstants constants = createNiceMock(UIConstants.class);
        replay(constants);

        DesignPresenter designer = new DesignPresenter(new MockEventBus(), service, new StateManagerStub(),
                view, constants);
        designer.go(schema.getDatabaseById(1));

        // Verify that the proper delete command executes

        designer.onUIAction(UIActions.DELETE);

        Delete cmd = service.getLastExecuted(Delete.class);

        Assert.assertEquals("Activity", cmd.getEntityName());
        Assert.assertEquals(91, cmd.getId());

    }

    @Test
    public void testDeleteEnabled() {

        // Dummy Data
        SchemaDTO schema = DTOs.PEAR();

        // Collaborator
        DispatcherStub service = new DispatcherStub();
        service.setResult(GetSchema.class, schema);
        service.setResult(Delete.class, new VoidResult());

        // Collaborator
        DesignPresenter.View view = createNiceMock(DesignPresenter.View.class);
        view.setActionEnabled(UIActions.DELETE, false);
        replay(view);

        // Collaborator
        UIConstants constants = createNiceMock(UIConstants.class);
        replay(constants);

        DesignPresenter designer = new DesignPresenter(new MockEventBus(), service, new StateManagerStub(),
                view, constants);
        designer.go(schema.getDatabaseById(1));

        // Verify that the delete command is initially disabled
        verify(view);

        // Verify that the delete command is enabled when an activity is selected
        resetToDefault(view);
        view.setActionEnabled(UIActions.DELETE, true);
        replay(view);

        designer.onSelectionChanged(schema.getActivityById(91));

        verify(view);

        // Verify that the delete command is disabled when a folder is selected
        reset(view);
        view.setActionEnabled(UIActions.DELETE, false);
        replay(view);

        designer.onSelectionChanged(new IndicatorFolder(null));

        verify(view);

    }

    @Test
    public void testNewActivityComesWithFolders() {

        // Test data
        SchemaDTO schema = DTOs.PEAR();

        // Collaborator : EventBus
        MockEventBus eventBus = new MockEventBus();

        // Collaborator : Command Service
        DispatcherStub service = new DispatcherStub();
        service.setResult(GetSchema.class, schema);
        service.setResult(CreateEntity.class, new CreateResult(991));

        // Collaborator : View
        MockDesignTree view = new MockDesignTree();

        // Constants
        UIConstants constants = createNiceMock(UIConstants.class);
        replay(constants);

        // Class under test
        DesignPresenter designer = new DesignPresenter(eventBus, service, new StateManagerStub(), view, constants);

        // VERIFY that when an activity is added, it appears at the end of the list with two
        // sub folders

        designer.go(schema.getDatabaseById(1));
        
        view.newEntityProperties.put("name", "Psychosocial support");
        designer.onNew("Activity");

        List<ModelData> rootItems = designer.getTreeStore().getRootItems();
        ActivityDTO addedActivity = (ActivityDTO) rootItems.get(rootItems.size() - 1);

        Assert.assertEquals("Psychosocial support", addedActivity.getName());
        Assert.assertEquals("child nodes", 2, designer.getTreeStore().getChildCount(addedActivity));

    }

}
