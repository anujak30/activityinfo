package org.activityinfo.client.page.entry.location;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.entry.form.resources.SiteFormResources;
import org.activityinfo.shared.command.result.LocationResult;

import org.activityinfo.client.Log;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.widget.Html;

public class SearchStatusView extends Html {

	public SearchStatusView(LocationSearchPresenter presenter) {
		addStyleName(SiteFormResources.INSTANCE.style().locationDialogHelp());
		presenter.getStore().getLoader().addLoadListener(new LoadListener() {

			
			@Override
			public void loaderBeforeLoad(LoadEvent le) {
				setHtml("");
				addStyleName(SiteFormResources.INSTANCE.style().locationDialogHelp());
			}

			@Override
			public void loaderLoadException(LoadEvent le) {
				Log.error("Search load exception", le.exception);
				setHtml(I18N.CONSTANTS.connectionProblem());
			}

			@Override
			public void loaderLoad(LoadEvent le) {
				LocationResult data = le.getData();
				if(data.getTotalLength() == 0) {
					setHtml(I18N.CONSTANTS.locationSearchNoResults());
				} else if(data.getTotalLength() > data.getData().size()) {
					setHtml(I18N.MESSAGES.matchingLocations(data.getTotalLength()) + 
							"<br>" + 
							I18N.CONSTANTS.tooManyLocationsToDisplay());
				} 
			}
		});
	}
	
}
