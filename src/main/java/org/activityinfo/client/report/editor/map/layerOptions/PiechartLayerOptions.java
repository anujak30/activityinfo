package org.activityinfo.client.report.editor.map.layerOptions;

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
import java.util.List;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.common.columns.EditColorColumn;
import org.activityinfo.client.page.common.columns.ReadTextColumn;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.report.model.layers.PiechartMapLayer;
import org.activityinfo.shared.report.model.layers.PiechartMapLayer.Slice;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SliderEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Slider;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.SliderField;
import com.extjs.gxt.ui.client.widget.grid.CellSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Displays a list of options to configure a PiechartMapLayer
 */
public class PiechartLayerOptions extends LayoutContainer implements LayerOptionsWidget<PiechartMapLayer> {
	private Dispatcher service;
	private PiechartMapLayer piechartMapLayer;
	private SchemaDTO schema;
	private EditorGrid<NamedSlice> gridIndicatorOptions;
	private ListStore<NamedSlice> indicatorsStore = new ListStore<NamedSlice>();
	private SliderField sliderfieldMinSize;
	private SliderField sliderfieldMaxSize;
	private Slider sliderMinSize = new Slider();
	private Slider sliderMaxSize = new Slider();
	private Timer timerMinSlider;
	private Timer timerMaxSlider;
	private FormData formData = new FormData("5");
	private FormPanel panel = new FormPanel();

	public PiechartLayerOptions(Dispatcher service) {
		super();
		
		this.service=service;
		
		initializeComponent();
		
		loadData();
		
		createMinMaxSliders();

		setupIndicatorOptionsGrid();
	}

	private void initializeComponent() {
		panel.setHeaderVisible(false);
		add(panel);
	}
	
	private void setSliderDefaults(Slider slider) {
		slider.setMinValue(1);
		slider.setMaxValue(60);
		slider.setIncrement(1);
		slider.setDraggable(true);
		slider.setAutoWidth(true);
	}
	private void createMinMaxSliders() {
		setSliderDefaults(sliderMinSize);
		setSliderDefaults(sliderMaxSize);

		sliderMinSize.setValue(16);
		sliderMaxSize.setValue(48);
		
		sliderfieldMinSize = new SliderField(sliderMinSize);
		sliderfieldMinSize.setFieldLabel(I18N.CONSTANTS.radiusMinimum());
		sliderfieldMaxSize = new SliderField(sliderMaxSize);
		sliderfieldMaxSize.setFieldLabel(I18N.CONSTANTS.radiusMaximum());
		panel.add(sliderfieldMinSize, formData);
		panel.add(sliderfieldMaxSize, formData);
		
		// Ensure min can't be more then max, and max can't be less then min
		sliderMinSize.addListener(Events.Change, new Listener<SliderEvent>() {
			@Override
			public void handleEvent(SliderEvent be) {
				timerMinSlider.cancel();
				timerMinSlider.schedule(250);
		}});

		sliderMaxSize.addListener(Events.Change, new Listener<SliderEvent>() {
			@Override
			public void handleEvent(SliderEvent be) {
				timerMinSlider.cancel();
				timerMaxSlider.schedule(250);
		}});
		timerMinSlider = new Timer() {
			@Override
			public void run() {
				if (sliderMinSize.getValue() > sliderMaxSize.getValue()) {
					sliderMinSize.setValue(sliderMaxSize.getValue());
				}
				piechartMapLayer.setMinRadius(sliderMinSize.getValue());
				ValueChangeEvent.fire(PiechartLayerOptions.this, piechartMapLayer);
			}
		};
		timerMaxSlider = new Timer() {
			@Override
			public void run() {
				if (sliderMaxSize.getValue() < sliderMinSize.getValue()) {
					sliderMaxSize.setValue(sliderMinSize.getValue());
				}
				piechartMapLayer.setMaxRadius(sliderMaxSize.getValue());
				ValueChangeEvent.fire(PiechartLayerOptions.this, piechartMapLayer);
			}
		};
	}

	private void setupIndicatorOptionsGrid() {
		List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
	    
	    columnConfigs.add(new EditColorColumn());
	    columnConfigs.add(new ReadTextColumn("name", I18N.CONSTANTS.indicators(), 50));
	    
		ColumnModel columnmodelIndicators = new ColumnModel(columnConfigs);

		gridIndicatorOptions = new EditorGrid<NamedSlice>(indicatorsStore, columnmodelIndicators);
		gridIndicatorOptions.setBorders(false);
		gridIndicatorOptions.setAutoExpandColumn("name");
		gridIndicatorOptions.setAutoWidth(true);
		gridIndicatorOptions.setHeight(100);
		gridIndicatorOptions.getView().setShowDirtyCells(false);
		gridIndicatorOptions.setSelectionModel(new CellSelectionModel<PiechartLayerOptions.NamedSlice>());
		gridIndicatorOptions.addListener(Events.AfterEdit, new Listener<GridEvent<NamedSlice>>() {
			@Override
			public void handleEvent(GridEvent<NamedSlice> be) {
				be.getModel().getSlice().setColor(be.getModel().getColor());
				ValueChangeEvent.fire(PiechartLayerOptions.this, piechartMapLayer);
			}
		});

		VBoxLayoutData vbld = new VBoxLayoutData();
		vbld.setFlex(1);
		
		panel.add(gridIndicatorOptions);
	}

	private void loadData() {
		service.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(SchemaDTO result) {
				schema=result;
				populateColorPickerWidget();
			}
		});
	}

	private void populateColorPickerWidget() {
		indicatorsStore.removeAll();
		if (piechartMapLayer !=null &&
				piechartMapLayer.getIndicatorIds() != null &&
				piechartMapLayer.getIndicatorIds().size() > 0) {
			for (Slice slice : piechartMapLayer.getSlices()) {
				String name = schema.getIndicatorById(slice.getIndicatorId()).getName();
				indicatorsStore.add(new NamedSlice(slice.getColor(), slice.getIndicatorId(), name, slice));
			}
		}
		layout(true);
	}

	@Override
	public PiechartMapLayer getValue() {
		return piechartMapLayer;
	}

	@Override
	public void setValue(PiechartMapLayer value) {
		this.piechartMapLayer = value;
		updateUI();
	}

	private void updateUI() {
		populateColorPickerWidget();
		sliderMinSize.setValue(piechartMapLayer.getMinRadius(), true);
		sliderMaxSize.setValue(piechartMapLayer.getMaxRadius(), true);
	}

	@Override
	public void setValue(PiechartMapLayer value, boolean fireEvents) {
		setValue(value);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<PiechartMapLayer> handler) {
		return this.addHandler(handler, ValueChangeEvent.getType());
	}
	
	public final static class NamedSlice extends BaseModelData {
		private Slice slice;
		public NamedSlice() {
		}

		public NamedSlice(String color, int indicatorId, String name, Slice slice) {
			super();
			
			setColor(color);
			setIndicatorId(indicatorId);
			setName(name);
			this.slice=slice;
		}
		
		public String getColor() {
			return (String)get("color");
		}
		
		public void setColor(String color) {
			set("color", color);
		}
		
		public int getIndicatorId() {
			return (Integer)get("indicatorId");
		}
		
		public void setIndicatorId(int indicatorId) {
			set("indicatorId" , indicatorId);
		}

		public String getName() {
			return get("name");
		}

		public void setName(String name) {
			set("name" , name);
		}

		public Slice getSlice() {
			return slice;
		}
	}
}
