package org.activityinfo.client.page.entry.form;

import java.util.List;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.offline.command.handler.KeyGenerator;
import org.activityinfo.client.page.entry.form.resources.SiteFormResources;
import org.activityinfo.shared.command.CreateSite;
import org.activityinfo.shared.command.UpdateSite;
import org.activityinfo.shared.command.exception.NotAuthorizedException;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.LocationDTO;
import org.activityinfo.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.CardLayout;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SiteDialog extends Window {
	
	public static final int HEIGHT = 450;
	public static final int WIDTH = 500;
	private FormNavigationListView navigationListView;
	private LayoutContainer sectionContainer;
	
	private List<FormSection<SiteDTO>> sections = Lists.newArrayList();
	private LocationFormSection locationForm;
	
	private final Dispatcher dispatcher;
	private final ActivityDTO activity;
	
	private SiteDialogCallback callback;
	
	private SiteDTO site = null;
	
	/**
	 * True if this is a brand new site
	 */
	private boolean newSite;
		
	public SiteDialog(Dispatcher dispatcher, ActivityDTO activity) {
		this.dispatcher = dispatcher;
		this.activity = activity;
		
		setHeading(I18N.MESSAGES.addNewSiteForActivity(activity.getName()));
		setWidth(WIDTH);
		setHeight(HEIGHT);
	
		setLayout(new BorderLayout());
		
		navigationListView = new FormNavigationListView();
		BorderLayoutData navigationLayout = new BorderLayoutData(LayoutRegion.WEST);
		navigationLayout.setSize(150);
		add(navigationListView, navigationLayout);
		
		sectionContainer = new LayoutContainer();
		final CardLayout sectionLayout = new CardLayout();
		sectionContainer.setLayout(sectionLayout);
		
		add(sectionContainer, new BorderLayoutData(LayoutRegion.CENTER));

		if(activity.getLocationType().isAdminLevel()) {
			locationForm = new BoundLocationSection(dispatcher, activity);
		} else {
			locationForm = new LocationSection(dispatcher, activity);
		}
		
		addSection(FormSectionModel.forComponent(new ActivitySection(activity))
				.withHeader(I18N.CONSTANTS.siteDialogIntervention())
				.withDescription(I18N.CONSTANTS.siteDialogInterventionDesc()));
		
		addSection(FormSectionModel.forComponent(locationForm)
				.withHeader(I18N.CONSTANTS.location())
				.withDescription(I18N.CONSTANTS.siteDialogSiteDesc()));
		
		if(!activity.getAttributeGroups().isEmpty()) {
	
			addSection(FormSectionModel.forComponent(new AttributeSection(activity))
					.withHeader(I18N.CONSTANTS.attributes())
					.withDescription(I18N.CONSTANTS.siteDialogAttributes()));
			
		}
		
		if(activity.getReportingFrequency() == ActivityDTO.REPORT_ONCE &&
				!activity.getIndicators().isEmpty()) {
		
			addSection(FormSectionModel.forComponent(new IndicatorSection(activity))
					.withHeader(I18N.CONSTANTS.indicators())
					.withDescription(I18N.CONSTANTS.siteDialogIndicators()));
			
		}
		
		addSection(FormSectionModel.forComponent(new CommentSection())
				.withHeader(I18N.CONSTANTS.comments())
				.withDescription(I18N.CONSTANTS.siteDialogComments()));
		
			
		SiteFormResources.INSTANCE.style().ensureInjected();
		
		navigationListView.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<FormSectionModel>() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent<FormSectionModel> se) {
				if(!se.getSelection().isEmpty()) {
					sectionLayout.setActiveItem(se.getSelectedItem().getComponent());
				}
			}
		});
		
		Button finishButton = new Button(I18N.CONSTANTS.save(), IconImageBundle.ICONS.save(), new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				onSaveClicked();
			}
		});

		getButtonBar().add(finishButton);
	}
	
	public void showNew(SiteDTO site, LocationDTO location, boolean locationIsNew, SiteDialogCallback callback) {
		this.newSite = true;
		this.callback = callback;
		locationForm.updateForm(location, locationIsNew);
		updateForms(site);
		show();
	}
	
	public void showExisting(SiteDTO site, SiteDialogCallback callback) {
		this.newSite = false;
		this.site = site;
		this.callback = callback;
		LocationDTO location = site.getLocation();
		location.setLocationTypeId(activity.getLocationTypeId());
		
		locationForm.updateForm(location, false);
		updateForms(site);
		show();
	}
	
	private void updateForms(SiteDTO site) {
		for(FormSectionModel<SiteDTO> section : navigationListView.getStore().getModels()) {
			section.getSection().updateForm(site);
		}
	}
	
	private void updateModel(final SiteDTO newSite) {
		for(FormSectionModel<SiteDTO> section : navigationListView.getStore().getModels()) {
			section.getSection().updateModel(newSite);
		}
	}
		
	private void addSection(FormSectionModel<SiteDTO> model) {
		navigationListView.addSection(model);
		sectionContainer.add(model.getComponent());
		sections.add(model.getSection());
	}
	
	private void onSaveClicked() {
		if(validateSections()) {
			saveLocation();
		}
	}

	private boolean validateSections() {
		for(FormSectionModel<SiteDTO> section : navigationListView.getStore().getModels()) {
			if(!section.getSection().validate()) {
				navigationListView.getSelectionModel().select(section, false);
				MessageBox.alert(getHeading(), I18N.CONSTANTS.pleaseCompleteForm(), null);
				return false;
			}
		}
		return true;
	}

	private void saveLocation() {
		locationForm.save(new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				saveSite();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert(I18N.CONSTANTS.saving(), I18N.CONSTANTS.serverError(), null);
			}
		});
	}


	private void saveSite() {
		if(newSite) {
			saveNewSite();
		} else {
			updateSite();
		}
	}
	


	private void saveNewSite() {
		final SiteDTO newSite = new SiteDTO();
		newSite.setId(new KeyGenerator().generateInt());
		newSite.setActivityId(activity.getId());
		
		if(activity.getReportingFrequency() == ActivityDTO.REPORT_ONCE) {
			newSite.setReportingPeriodId(new KeyGenerator().generateInt());
		}
		
		updateModel(newSite);
		
		dispatcher.execute(new CreateSite(newSite), new AsyncCallback<CreateResult>() {

			@Override
			public void onFailure(Throwable caught) {
				showError(caught);
			}

			@Override
			public void onSuccess(CreateResult result) {
				hide();
				callback.onSaved(newSite);
			}
		});
	}

	
	private void updateSite() {
		
		final SiteDTO updated = new SiteDTO(site);
		updateModel(updated);
		
		dispatcher.execute(new UpdateSite(site, updated), new AsyncCallback<VoidResult>() {

			@Override
			public void onFailure(Throwable caught) {
				showError(caught);
			}

			@Override
			public void onSuccess(VoidResult result) {
				hide();
				callback.onSaved(updated);
			}
		});
	}
	

	private void showError(Throwable caught) {
		if(caught instanceof NotAuthorizedException) {
			MessageBox.alert(I18N.CONSTANTS.dataEntry(), I18N.CONSTANTS.notAuthorized(), null);	
		} else {
			MessageBox.alert(I18N.CONSTANTS.dataEntry(), I18N.CONSTANTS.serverError(), null);
		}
	}
}
