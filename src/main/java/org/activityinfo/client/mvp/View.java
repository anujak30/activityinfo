package org.activityinfo.client.mvp;

import org.activityinfo.client.dispatch.AsyncMonitor;

import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.IsWidget;

/*
 * The view always has a primary domain object to display. The view receives calls 
 * from the Presenter (the Presenter having an instance of the View), and the View 
 * throws events the Presenter subscribes to. The View does not have an instance of
 * the Presenter. The View only has 'dumb' methods: the Presenter acts as a proxy 
 * between the model and the view.
 */
public interface View<M> extends TakesValue<M>, IsWidget {
	/*
	 * Presenters have an async process of fetching data. Only after the data has been
	 * fetched successfully and the data set on the View, the Presenter calls this method.
	 * This is foremost important for data which is listed, e.g. the choices of a combobox.
	 * The constructor can construct the UI, when the presenter has this data fetched, it 
	 * can be set on the view. After this, the initialize method can be called.  
	 *  
	 */
	public void initialize();
	
	/*
	 * The UI displaying loading, network status (retry/error/complete)
	 * Usually some standard Async monitor UI view, such as NullAsyncMonitor,
	 * MaskingAsyncMonitor etc
	 * 
	 */
	public AsyncMonitor getLoadingMonitor();
	
}
