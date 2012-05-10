/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.config;

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.page.common.dialog.FormDialogCallback;
import org.activityinfo.client.page.common.dialog.FormDialogImpl;
import org.activityinfo.client.page.common.dialog.FormDialogTether;
import org.activityinfo.client.page.common.grid.AbstractGridView;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.page.config.form.PartnerForm;
import org.activityinfo.shared.dto.PartnerDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.client.i18n.UIConstants;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.inject.Inject;

/**
 * @author Alex Bertram
 */
public class DbPartnerGrid extends AbstractGridView<PartnerDTO, DbPartnerEditor>
                            implements DbPartnerEditor.View {

    private final UIConstants messages;
    private final IconImageBundle icons;

    private Grid<PartnerDTO> grid;

    @Inject
    public DbPartnerGrid(UIConstants messages, IconImageBundle icons) {
        this.messages = messages;
        this.icons = icons;
    }

    @Override
	public void init(DbPartnerEditor editor, UserDatabaseDTO db, ListStore<PartnerDTO> store) {
        super.init(editor, store);
        this.setHeading(db.getName() + " - " + messages.partners());

    }

    @Override
    protected Grid<PartnerDTO> createGridAndAddToContainer(Store store) {
        grid = new Grid<PartnerDTO>((ListStore)store, createColumnModel());
        grid.setAutoExpandColumn("fullName");
        grid.setLoadMask(true);

        this.setLayout(new FitLayout());
        this.add(grid);


        return grid;
    }

    protected ColumnModel createColumnModel() {
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

        columns.add(new ColumnConfig("name", messages.name(), 150));
        columns.add(new ColumnConfig("fullName", messages.fullName(), 300));

        return new ColumnModel(columns);
    }

    @Override
    protected void initToolBar() {
        toolBar.addButton(UIActions.ADD, messages.addPartner(), icons.add());
        toolBar.addButton(UIActions.DELETE, messages.delete(), icons.delete());
    }

    @Override
	public FormDialogTether showAddDialog(PartnerDTO partner, FormDialogCallback callback) {

        PartnerForm form = new PartnerForm();
        form.getBinding().bind(partner);

        FormDialogImpl dlg = new FormDialogImpl(form);
        dlg.setWidth(450);
        dlg.setHeight(300);
        dlg.setHeading(messages.newPartner());

        dlg.show(callback);

        return dlg;
    }
}
