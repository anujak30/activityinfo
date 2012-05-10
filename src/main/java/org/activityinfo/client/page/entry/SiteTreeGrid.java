package org.activityinfo.client.page.entry;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.page.entry.grouping.AdminGroupingModel;
import org.activityinfo.client.page.entry.grouping.GroupingModel;
import org.activityinfo.client.page.entry.grouping.TimeGroupingModel;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid.ClicksToEdit;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.treegrid.EditorTreeGrid;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

final class SiteTreeGrid extends EditorTreeGrid<ModelData> implements SiteGridPanelView {

	public static final String ADMIN_STATE_ID = "sitetreegrid.admin";
	
	public SiteTreeGrid(Dispatcher dispatcher, GroupingModel groupingModel, Filter filter, ColumnModel columnModel) {
		super(createStore(dispatcher, groupingModel), columnModel);
		setLoadMask(true); 
		setStateful(true);
		setStateId(ADMIN_STATE_ID);
		setClicksToEdit(ClicksToEdit.TWO);

		setIconProvider(new ModelIconProvider<ModelData>() {
			@Override
			public AbstractImagePrototype getIcon(ModelData model) {
				if (model instanceof SiteDTO) {
					SiteDTO site = (SiteDTO)model;
					if(site.hasCoords()) {
						return IconImageBundle.ICONS.mapped();
					} else {
						return IconImageBundle.ICONS.unmapped();
					}
				} else {
					return IconImageBundle.ICONS.folder();
				}
			}
		});
		
		GridSelectionModel<ModelData> sm = new GridSelectionModel<ModelData>();
		sm.setSelectionMode(SelectionMode.SINGLE);
		sm.addSelectionChangedListener(new SelectionChangedListener<ModelData>() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent<ModelData> se) {
				if(se.getSelectedItem() instanceof SiteDTO) {
					fireEvent(Events.SelectionChange, se);
				}
			}
		});
		setSelectionModel(sm);
		
		getLoader().setFilter(filter);
	}

	private static TreeStore<ModelData> createStore(Dispatcher dispatcher, GroupingModel groupingModel) {
		
		SiteTreeLoader loader;
		
		if(groupingModel instanceof AdminGroupingModel) {
			loader = new SiteAdminTreeLoader(dispatcher, (AdminGroupingModel) groupingModel);
		} else if(groupingModel instanceof TimeGroupingModel) {
			loader = new SiteTimeTreeLoader(dispatcher);
		} else {
			throw new IllegalArgumentException("Invalid grouping model " + groupingModel);
		}
		
		TreeStore<ModelData> treeStore = new TreeStore<ModelData>(loader);
		treeStore.setKeyProvider(loader);
		return treeStore;
	}
	

	private SiteTreeLoader getLoader() {
		return (SiteTreeLoader) getTreeStore().getLoader();
	}

	@Override
	public void addSelectionChangeListener(
			SelectionChangedListener<SiteDTO> selectionChangedListener) {
		addListener(Events.SelectionChange, selectionChangedListener);
	}

	@Override
	public void refresh() {
		getTreeStore().removeAll();
		getLoader().load();
	}

	@Override
	public Component asComponent() {
		return this;
	}

	@Override
	public SiteDTO getSelection() {
		ModelData selection = getSelectionModel().getSelectedItem();
		if(selection instanceof SiteDTO) {
			return (SiteDTO)selection;
		} else {
			return null;
		}
	}
}
