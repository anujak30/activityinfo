/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.config;

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.page.common.dialog.FormDialogCallback;
import org.activityinfo.client.page.common.dialog.FormDialogImpl;
import org.activityinfo.client.page.common.dialog.FormDialogTether;
import org.activityinfo.client.page.common.grid.AbstractEditorGridView;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.page.config.form.UserForm;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.dto.UserPermissionDTO;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

/**
 * @author Alex Bertram
 */
public class DbUserGrid extends AbstractEditorGridView<UserPermissionDTO, DbUserEditor>
        implements DbUserEditor.View {

    private EditorGrid<UserPermissionDTO> grid;
    private UserDatabaseDTO db;

    public DbUserGrid() {
        setHeading(I18N.CONSTANTS.users());
        setLayout(new FitLayout());
    }

    @Override
	public void init(DbUserEditor presenter, UserDatabaseDTO db, ListStore<UserPermissionDTO> store) {
        this.db = db;
        super.init(presenter, store);
        this.setHeading(db.getName() + " - " + I18N.CONSTANTS.users());
    }


    @Override
    protected Grid<UserPermissionDTO> createGridAndAddToContainer(Store store) {

        final List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

        columns.add(new ColumnConfig("name", I18N.CONSTANTS.name(), 100));
        columns.add(new ColumnConfig("email", I18N.CONSTANTS.email(), 150));
        columns.add(new ColumnConfig("partner", I18N.CONSTANTS.partner(), 150));

        CheckColumnConfig allowView = new CheckColumnConfig("allowViewSimple", I18N.CONSTANTS.allowView(), 75);
        allowView.setDataIndex("allowView");
        allowView.setToolTip(I18N.CONSTANTS.allowViewLong());
        columns.add(allowView);

        CheckColumnConfig allowEdit = new CheckColumnConfig("allowEditSimple", I18N.CONSTANTS.allowEdit(), 75);
        allowEdit.setDataIndex("allowEdit");
        allowEdit.setToolTip(I18N.CONSTANTS.allowEditLong());
        columns.add(allowEdit);

        // only users with the right to manage all users can view/change these attributes
        CheckColumnConfig allowViewAll = null;
        CheckColumnConfig allowEditAll = null;

        if (db.isManageAllUsersAllowed()) {
            allowViewAll = new CheckColumnConfig("allowViewAll", I18N.CONSTANTS.allowViewAll(), 75);
            allowViewAll.setToolTip(I18N.CONSTANTS.allowViewAllLong());
            columns.add(allowViewAll);

            allowEditAll = new CheckColumnConfig("allowEditAll", I18N.CONSTANTS.allowEditAll(), 75);
            allowEditAll.setToolTip(I18N.CONSTANTS.allowEditAllLong());
            columns.add(allowEditAll);
        }

        CheckColumnConfig allowManageUsers = null;
        if (db.isManageUsersAllowed()) {
        	allowManageUsers = new CheckColumnConfig("allowManageUsers", I18N.CONSTANTS.allowManageUsers(), 150);
            columns.add(allowManageUsers);
        }

        CheckColumnConfig allowManageAllUsers = null;
        if (db.isManageAllUsersAllowed()) {
        	allowManageAllUsers = new CheckColumnConfig("allowManageAllUsers", I18N.CONSTANTS.manageAllUsers(), 150);
            columns.add(allowManageAllUsers);
        }

        // only users with the right to design them selves can change the design attribute
        CheckColumnConfig allowDesign = null;
        if (db.isDesignAllowed()) {
            allowDesign = new CheckColumnConfig("allowDesign", I18N.CONSTANTS.allowDesign(), 75);
            allowDesign.setToolTip(I18N.CONSTANTS.allowDesignLong());
            columns.add(allowDesign);
        }

        final ListStore<UserPermissionDTO> listStore = (ListStore) store;
        grid = new EditorGrid<UserPermissionDTO>(listStore, new ColumnModel(columns));
        grid.setLoadMask(true);
        if (allowDesign != null) {
            grid.addPlugin(allowDesign);
        }
        //   grid.addPlugin(allowView);
        if (allowViewAll != null) {
            grid.addPlugin(allowViewAll);
        }
        grid.addPlugin(allowEdit);
        if (allowEditAll != null) {
            grid.addPlugin(allowEditAll);
        }
        if (allowManageUsers != null) {
            grid.addPlugin(allowManageUsers);
        }
        if (allowManageAllUsers != null) {
            grid.addPlugin(allowManageAllUsers);
        }
        grid.addListener(Events.CellClick, new Listener<GridEvent>() {

            @Override
			public void handleEvent(GridEvent ge) {

                if (ge.getColIndex() >= 4) {

                    String property = columns.get(ge.getColIndex()).getDataIndex();
                    Record record = listStore.getRecord(listStore.getAt(ge.getRowIndex()));
                    presenter.onRowEdit(property, (Boolean) record.get(property), record);
                }
            }
        });

        add(grid);

        return grid;
    }

    @Override
    protected void initToolBar() {
        toolBar.addSaveSplitButton();
        toolBar.addButton(UIActions.ADD, I18N.CONSTANTS.addUser(), IconImageBundle.ICONS.addUser());
        toolBar.addButton(UIActions.DELETE, I18N.CONSTANTS.delete(), IconImageBundle.ICONS.deleteUser());
        toolBar.addButton(UIActions.EXPORT, I18N.CONSTANTS.export(), IconImageBundle.ICONS.excel());
        toolBar.addButton(UIActions.MAILING_LIST, I18N.CONSTANTS.CopyAddressToClipBoard() , IconImageBundle.ICONS.dataEntry());
    }

    @Override
	public FormDialogTether showNewForm(UserPermissionDTO user, FormDialogCallback callback) {
        UserForm form = new UserForm(db);
        form.getBinding().bind(user);

        FormDialogImpl dlg = new FormDialogImpl(form);
        dlg.setHeading(I18N.CONSTANTS.newUser());
        dlg.setWidth(400);
        dlg.setHeight(300);

        dlg.show(callback);

        return dlg;
    }
}
