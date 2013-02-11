package org.activityinfo.client.local.ui;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.local.capability.LocalCapabilityProfile;
import org.activityinfo.client.local.capability.ProfileResources;

import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;

public class BasePromptDialog extends Dialog {

	static {
		ProfileResources.INSTANCE.style().ensureInjected();
	}
	
	protected final static LocalCapabilityProfile capabilityProfile = GWT.create(LocalCapabilityProfile.class);
		
	public BasePromptDialog(String html) {
		
		setWidth(500);
		setHeight(350);
		setHeading(I18N.CONSTANTS.installOffline());
		setModal(true);
		setLayout(new FitLayout());
		
		Html bodyHtml = new Html(html);
		bodyHtml.addStyleName(ProfileResources.INSTANCE.style().startupDialogBody());
        
		add(bodyHtml);
	}
}
