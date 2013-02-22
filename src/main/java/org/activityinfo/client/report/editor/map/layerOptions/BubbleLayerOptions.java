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

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.widget.ColorField;
import org.activityinfo.shared.report.model.layers.BubbleMapLayer;

import com.extjs.gxt.ui.client.event.ColorPaletteEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SliderEvent;
import com.extjs.gxt.ui.client.widget.ColorPalette;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Slider;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;

/*
 * Displays a list of options the user can choose to configure a BubbleMapLayer
 * TODO: replace the two min/max sliders with a RangeSlider (Slider with 2 knobs)
 */
public class BubbleLayerOptions extends LayoutContainer implements LayerOptionsWidget<BubbleMapLayer> {
	private BubbleMapLayer bubbleMapLayer;
	private ColorPalette colorPicker = new ColorPalette();
	private Slider sliderMinSize = new Slider();
	private Slider sliderMaxSize = new Slider();
	private Timer timerMinSlider;
	private Timer timerMaxSlider;

	public BubbleLayerOptions() {
		super();
		setStyleAttribute("padding", "5px");
		createColorPicker();
		createMinMaxSliders();
	}

	private void createColorPicker() {
		colorPicker.setValue("000000");
		
		// Set the selected color to the maplayer
		colorPicker.addListener(Events.Select, new Listener<ColorPaletteEvent>() {
			@Override
			public void handleEvent(ColorPaletteEvent be) {
				bubbleMapLayer.setBubbleColor(colorPicker.getValue());
				ValueChangeEvent.fire(BubbleLayerOptions.this, bubbleMapLayer);
		}});

		LabelField labelColor = new LabelField(I18N.CONSTANTS.color());
		add(labelColor);
		add(colorPicker);
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
		
		LabelField labelMin = new LabelField(I18N.CONSTANTS.radiusMinimum());
		LabelField labelMax = new LabelField(I18N.CONSTANTS.radiusMaximum());
		
		add(labelMin);
		add(sliderMinSize);
		add(labelMax);
		add(sliderMaxSize);
		
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
				timerMaxSlider.cancel();
				timerMaxSlider.schedule(250);
			}
		});
		timerMinSlider = new Timer() {
			@Override
			public void run() {
				if (sliderMinSize.getValue() > sliderMaxSize.getValue()) {
					sliderMinSize.setValue(sliderMaxSize.getValue());
				}
				bubbleMapLayer.setMinRadius(sliderMinSize.getValue());
				ValueChangeEvent.fire(BubbleLayerOptions.this, bubbleMapLayer);
			}
		};
		timerMaxSlider = new Timer() {
			@Override
			public void run() {
				if (sliderMaxSize.getValue() < sliderMinSize.getValue()) {
					sliderMaxSize.setValue(sliderMinSize.getValue());
				}
				bubbleMapLayer.setMaxRadius(sliderMaxSize.getValue());
				ValueChangeEvent.fire(BubbleLayerOptions.this, bubbleMapLayer);
			}
		};
	}

	@Override
	public BubbleMapLayer getValue() {
		return bubbleMapLayer;
	}

	private void updateUI() {
		sliderMinSize.setValue(bubbleMapLayer.getMinRadius(), true);
		sliderMaxSize.setValue(bubbleMapLayer.getMaxRadius(), true);
		colorPicker.setValue(bubbleMapLayer.getBubbleColor());
	}
	
	// TODO: fireevent
	@Override
	public void setValue(BubbleMapLayer value, boolean fireEvents) {
		setValue(value);
	}
	
	@Override
	public void setValue(BubbleMapLayer value) {
		this.bubbleMapLayer=value;
		updateUI();
	}
	
	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<BubbleMapLayer> handler) {
		return this.addHandler(handler, ValueChangeEvent.getType());
	}
}
