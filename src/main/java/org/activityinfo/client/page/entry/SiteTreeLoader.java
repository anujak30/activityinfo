package org.activityinfo.client.page.entry;

import org.activityinfo.shared.command.Filter;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.data.TreeLoader;

public interface SiteTreeLoader extends TreeLoader<ModelData>, ModelKeyProvider<ModelData> {

	void setFilter(Filter filter);
}
