package org.activityinfo.client.page.config;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.dispatch.monitor.NullAsyncMonitor;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.mvp.CanCreate;
import org.activityinfo.client.mvp.CanDelete;
import org.activityinfo.client.mvp.CanUpdate;
import org.activityinfo.client.mvp.Filter;
import org.activityinfo.client.page.common.columns.EditCheckColumnConfig;
import org.activityinfo.client.page.common.columns.EditableLocalDateColumn;
import org.activityinfo.client.page.common.columns.ReadLockedPeriodTypeColumn;
import org.activityinfo.client.page.common.columns.ReadTextColumn;
import org.activityinfo.client.page.common.dialog.FormDialogCallback;
import org.activityinfo.client.page.common.dialog.FormDialogImpl;
import org.activityinfo.client.page.common.dialog.FormDialogTether;
import org.activityinfo.client.page.common.toolbar.ActionListener;
import org.activityinfo.client.page.common.toolbar.ActionToolBar;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.page.config.LockedPeriodsPresenter.LockedPeriodListEditor;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.LockedPeriodDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

public class LockedPeriodGrid extends ContentPanel implements LockedPeriodListEditor {
	
	private EventBus eventBus = new SimpleEventBus();
	private boolean mustConfirmDelete = true;
	
	// UI stuff
	private ListStore<LockedPeriodDTO> lockedPeriodStore;
	private EditorGrid<LockedPeriodDTO> gridLockedPeriods;
	private AsyncMonitor deletingMonitor = new NullAsyncMonitor();
	private AsyncMonitor creatingMonitor = new NullAsyncMonitor();
	private AsyncMonitor loadingMonitor = new NullAsyncMonitor();
	private AsyncMonitor updatingMonitor = new NullAsyncMonitor();

	// Data
	private LockedPeriodDTO lockedPeriod;
	private ActivityDTO activityFilter= null;

	// Nested views
	private AddLockedPeriodDialog addLockedPeriod;
	private ActionToolBar toolbarActions;
	private FormDialogImpl<AddLockedPeriodDialog> form;

	public LockedPeriodGrid() {
		super();
		
		initializeComponent();
		
		createListStore();
		createActionToolbar();
		createAddLockedPeriodDialog();
		createGrid();
	}

	private void createGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		
		ColumnConfig columnEnabled = new EditCheckColumnConfig("enabled", I18N.CONSTANTS.enabledColumn(), 55);
		columnEnabled.setSortable(false);
		
	    ColumnConfig columnPeriodType = new ReadLockedPeriodTypeColumn();
	    columnPeriodType.setSortable(false);
	    
	    configs.add(columnEnabled);
	    configs.add(columnPeriodType);
	    configs.add(new ReadTextColumn("parentName", I18N.CONSTANTS.parentName(), 150));
	    configs.add(new ReadTextColumn("name", I18N.CONSTANTS.name(), 100));
	    configs.add(new EditableLocalDateColumn("fromDate", I18N.CONSTANTS.fromDate(), 100));
	    configs.add(new EditableLocalDateColumn("toDate", I18N.CONSTANTS.toDate(), 100));
	    
	    gridLockedPeriods = new EditorGrid<LockedPeriodDTO>(
	    		lockedPeriodStore, new ColumnModel(configs));
	    
	    gridLockedPeriods.addListener(Events.OnClick, new Listener<ComponentEvent>() {
			@Override
			public void handleEvent(ComponentEvent be) {
				lockedPeriod = gridLockedPeriods.getSelectionModel().getSelectedItem();
				setDeleteEnabled(gridLockedPeriods.getSelectionModel().getSelectedItem() != null);
			}
		});
	    
	    add(gridLockedPeriods);
	}

	private void initializeComponent() {
		setHeading(I18N.CONSTANTS.lockPanelTitle());
		setLayout(new FitLayout());
	}

	private void createListStore() {
		lockedPeriodStore = new ListStore<LockedPeriodDTO>();
		lockedPeriodStore.addListener(Store.DataChanged, new Listener<StoreEvent>(){
			@Override
			public void handleEvent(StoreEvent be) {
				toolbarActions.setUpdateEnabled(true);
			}
		});
	}

	private void createActionToolbar() {
		toolbarActions = new ActionToolBar(new ActionListener(){
			@Override
			public void onUIAction(String actionId) {
				if (actionId.equals(UIActions.ADD)) {
					eventBus.fireEvent(new StartCreateEvent());
				} else if (actionId.equals(UIActions.DELETE)) {
					eventBus.fireEvent(new RequestDeleteEvent());
				} else if (actionId.equals(UIActions.SAVE)) {
					eventBus.fireEvent(new UpdateEvent());
				} else if (actionId.equals(UIActions.DISCARD_CHANGES)) {
					eventBus.fireEvent(new CancelUpdateEvent());
//				} else if (actionId.equals(UIActions.refresh)) {
//					eventBus.fireEvent(new RefreshEvent());
				}
			}
		});
		toolbarActions.addDeleteButton();
		toolbarActions.addCreateButton();
		toolbarActions.addSaveSplitButton();
//		toolbarActions.addRefreshButton();
		toolbarActions.setDeleteEnabled(false);
		toolbarActions.setUpdateEnabled(false);
		this.setTopComponent(toolbarActions);
	}

	private void createAddLockedPeriodDialog() {
		addLockedPeriod = new AddLockedPeriodDialog();
		addLockedPeriod.addCreateHandler(new CreateHandler() {
			@Override
			public void onCreate(CreateEvent createEvent) {
				lockedPeriod = addLockedPeriod.getValue();
				eventBus.fireEvent(new CreateEvent());
			}
		});
		addLockedPeriod.addCancelCreateHandler(new CancelCreateHandler() {
			@Override
			public void onCancelCreate(CancelCreateEvent createEvent) {
				eventBus.fireEvent(new CancelCreateEvent());
			}
		});
	}

	@Override
	public void create(LockedPeriodDTO item) {
		lockedPeriodStore.add(item);
		addLockedPeriod.cancelCreate();
		if (form != null) {
			form.hide();
		}
	}

	@Override
	public void update(LockedPeriodDTO item) {
		lockedPeriodStore.commitChanges();
	}

	@Override
	public void delete(LockedPeriodDTO item) {
		lockedPeriodStore.remove(item);
	}

	@Override
	public void setCreateEnabled(boolean createEnabled) {
		toolbarActions.setAddEnabled(createEnabled);
	}

	@Override
	public void setUpdateEnabled(boolean updateEnabled) {
		toolbarActions.setUpdateEnabled(updateEnabled);
	}

	@Override
	public final void setDeleteEnabled(boolean deleteEnabled) {
		toolbarActions.setDeleteEnabled(deleteEnabled);
	}

	@Override
	public LockedPeriodDTO getValue() {
		return lockedPeriod;
	}

	@Override
	public HandlerRegistration addCreateHandler(CreateHandler handler) {
		return eventBus.addHandler(CreateEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addUpdateHandler(UpdateHandler handler) {
		return eventBus.addHandler(UpdateEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addConfirmDeleteHandler(ConfirmDeleteHandler handler) {
		return eventBus.addHandler(ConfirmDeleteEvent.TYPE, handler);
	}

	@Override
	public void cancelUpdate(LockedPeriodDTO item) {
		gridLockedPeriods.stopEditing(true);
	}

	@Override
	public void askConfirmDelete(LockedPeriodDTO item) {
		// TODO: i18n
		if (mustConfirmDelete) {
			MessageBox.confirm(I18N.CONSTANTS.deleteLockedPeriodTitle(), I18N.CONSTANTS.deleteLockedPeriodQuestion(), new Listener<MessageBoxEvent>() {
				@Override
				public void handleEvent(MessageBoxEvent be) {
					if (be.isCancelled()) {
						eventBus.fireEvent(new CancelDeleteEvent());
					} else {
						eventBus.fireEvent(new ConfirmDeleteEvent());
					}
				}
			});
		} else {
			eventBus.fireEvent(new ConfirmDeleteEvent());
		}
	}

	@Override
	public void setParent(UserDatabaseDTO parent) {
		addLockedPeriod.setUserDatabase(parent);
	}

	@Override
	public void setItems(List<LockedPeriodDTO> items) {
		lockedPeriodStore.removeAll();
		lockedPeriodStore.add(filterLockedPeriodsByActivity(items));
	}

	private List<LockedPeriodDTO> filterLockedPeriodsByActivity(
			List<LockedPeriodDTO> items) {
		
		if (activityFilter != null) {
			// Remove LockedPeriods which have a different Activity then the activiftyFilter
			List<LockedPeriodDTO> lockedPeriodsFilteredByActivity = new ArrayList<LockedPeriodDTO>();
			for (LockedPeriodDTO lockedPeriod : items) {
				if (lockedPeriod.getParent() != null && lockedPeriod.getParent() instanceof ActivityDTO) {
					// Activity as parent, only add when activity equals filter
					if (lockedPeriod.getParent().getId() == activityFilter.getId()) {
						lockedPeriodsFilteredByActivity.add(lockedPeriod);
					}
				} else {
					// Database or Project, can be added
					lockedPeriodsFilteredByActivity.add(lockedPeriod);
				}
			}
			return lockedPeriodsFilteredByActivity;
		} else {
			// No filter, just return the items
			return items;
		}
	}

	@Override
	public HandlerRegistration addCancelUpdateHandler(
			CancelUpdateHandler handler) {
		return eventBus.addHandler(CancelUpdateEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addRequestDeleteHandler(
			RequestDeleteHandler handler) {
		return eventBus.addHandler(RequestDeleteEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addCancelCreateHandler(CanCreate.CancelCreateHandler handler) {
		return eventBus.addHandler(CancelCreateEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addStartCreateHandler(CanCreate.StartCreateHandler handler) {
		return eventBus.addHandler(StartCreateEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addCancelDeleteHandler(CanDelete.CancelDeleteHandler handler) {
		return eventBus.addHandler(CancelDeleteEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addRequestUpdateHandler(CanUpdate.RequestUpdateHandler handler) {
		return eventBus.addHandler(RequestUpdateEvent.TYPE, handler);
	}
	
	@Override
	public List<LockedPeriodDTO> getUnsavedItems() {
		List<LockedPeriodDTO> unsavedItems = new ArrayList<LockedPeriodDTO>();
		
		List<Record> modifiedRecords = lockedPeriodStore.getModifiedRecords();
		for (Record record : modifiedRecords) {
			unsavedItems.add((LockedPeriodDTO)record.getModel());
		}
		
		return unsavedItems;
	}

	@Override
	public boolean hasChangedItems() {
		return getUnsavedItems().size() > 0;
	}

	@Override
	public boolean hasSingleChangedItem() {
		return getUnsavedItems().size() == 1;
	}

	@Override
	public Map<String, Object> getChanges(LockedPeriodDTO item) {
		for (Record record : lockedPeriodStore.getModifiedRecords()) {
			LockedPeriodDTO lockedPeriod = (LockedPeriodDTO)record.getModel();
			if (lockedPeriod.getId() == item.getId()) {
				Map<String, Object> changes = new HashMap<String, Object>();
				for (String property : record.getPropertyNames()) {
					changes.put(property, lockedPeriod.get(property));
				}
				
				return changes;
			}
		}
		
		return null;
	}

	@Override
	public void cancelUpdateAll() {
		lockedPeriodStore.rejectChanges();
	}
	
	@Override
	public void startCreate() {
		addLockedPeriod.startCreate();
		form = new FormDialogImpl<AddLockedPeriodDialog>(addLockedPeriod);
		form.setHeading(I18N.CONSTANTS.addTimeLock());
		form.setWidth(400);
		form.setHeight(350);
		
		form.show(new FormDialogCallback() {
			@Override
			public void onValidated() {
				super.onValidated();
			}

			@Override
			public void onValidated(FormDialogTether dlg) {
				LockedPeriodGrid.this.lockedPeriod = addLockedPeriod.getValue();
				eventBus.fireEvent(new CreateEvent());
			}

			@Override
			public void onCancelled() {
				eventBus.fireEvent(new CancelCreateEvent());
			}
		});
	}

	@Override
	public void cancelCreate() {
		addLockedPeriod.cancelCreate();
		if (form != null) {
			form.hide();
		}
	}
	@Override
	public AsyncMonitor getLoadingMonitor() {
		return loadingMonitor;
	}

	@Override
	public AsyncMonitor getCreatingMonitor() {
		return form;
	}

	@Override
	public AsyncMonitor getUpdatingMonitor() {
		return updatingMonitor;
	}

	@Override
	public AsyncMonitor getDeletingMonitor() {
		return deletingMonitor;
	}
	
	@Override
	public void filter(Filter<LockedPeriodDTO> filter) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void setMustConfirmDelete(boolean mustConfirmDelete) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setRefreshEnabled(boolean canRefresh) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeFilter() {
		// TODO Auto-generated method stub
	}

	@Override
	public void startUpdate() {
		// TODO Auto-generated method stub
	}

	@Override
	public void cancelDelete() {
		// TODO Auto-generated method stub
	}
	@Override
	public HandlerRegistration addRefreshHandler(RefreshHandler handler) {
		return eventBus.addHandler(RefreshEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addFilterHandler(
			FilterHandler filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
	}

	@Override
	public void setValue(LockedPeriodDTO value) {
		//gridLockedPeriods.getSelectionModel().select(vgetActivityalue, false);
	}

	public void setActivityFilter(ActivityDTO activityFilter) {
		this.activityFilter = activityFilter;
	}

	public ActivityDTO getActivityFilter() {
		return activityFilter;
	}

	public void setReadOnly(boolean isReadOnly) {
		if (isReadOnly) {
			remove(toolbarActions);
		}
	}

	@Override
	public void setTitle(String title) {
		setHeading(title);
	}
}
