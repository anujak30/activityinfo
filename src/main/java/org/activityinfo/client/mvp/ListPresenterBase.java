package org.activityinfo.client.mvp;

import java.util.List;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.mvp.CanCreate.CancelCreateEvent;
import org.activityinfo.client.mvp.CanCreate.CancelCreateHandler;
import org.activityinfo.client.mvp.CanCreate.CreateEvent;
import org.activityinfo.client.mvp.CanCreate.CreateHandler;
import org.activityinfo.client.mvp.CanCreate.StartCreateEvent;
import org.activityinfo.client.mvp.CanCreate.StartCreateHandler;
import org.activityinfo.client.mvp.CanDelete.CancelDeleteEvent;
import org.activityinfo.client.mvp.CanDelete.CancelDeleteHandler;
import org.activityinfo.client.mvp.CanDelete.ConfirmDeleteEvent;
import org.activityinfo.client.mvp.CanDelete.ConfirmDeleteHandler;
import org.activityinfo.client.mvp.CanDelete.RequestDeleteEvent;
import org.activityinfo.client.mvp.CanDelete.RequestDeleteHandler;
import org.activityinfo.client.mvp.CanFilter.FilterEvent;
import org.activityinfo.client.mvp.CanFilter.FilterHandler;
import org.activityinfo.client.mvp.CanRefresh.RefreshEvent;
import org.activityinfo.client.mvp.CanRefresh.RefreshHandler;
import org.activityinfo.client.mvp.CanUpdate.CancelUpdateEvent;
import org.activityinfo.client.mvp.CanUpdate.CancelUpdateHandler;
import org.activityinfo.client.mvp.CanUpdate.RequestUpdateEvent;
import org.activityinfo.client.mvp.CanUpdate.RequestUpdateHandler;
import org.activityinfo.client.mvp.CanUpdate.UpdateEvent;
import org.activityinfo.client.mvp.CanUpdate.UpdateHandler;
import org.activityinfo.shared.dto.DTO;

public class ListPresenterBase
		<M extends DTO, 
		 L extends List<M>, 
		 P extends DTO, 
		 V extends CrudView<M, P>> 
	extends 
		PresenterBase<V, M>
	implements 
		UpdateHandler,
		CancelUpdateHandler,
		ConfirmDeleteHandler,
		CreateHandler,
		FilterHandler,
		RefreshHandler,
		RequestDeleteHandler, 
		StartCreateHandler, 
		CancelCreateHandler, 
		RequestUpdateHandler, 
		CancelDeleteHandler
	{
	
	protected P parentModel;

	public ListPresenterBase(Dispatcher service, EventBus eventBus, V view) {
		super(service, eventBus, view);
	}

	@Override
	protected void addListeners() {
		// Create
		view.addStartCreateHandler(this);
		view.addCancelCreateHandler(this);
		view.addCreateHandler(this);

		// Update
		view.addRequestUpdateHandler(this);
		view.addCancelUpdateHandler(this);
		view.addUpdateHandler(this);

		// Delete
		view.addRequestDeleteHandler(this);
		view.addCancelDeleteHandler(this);
		view.addConfirmDeleteHandler(this);

		view.addFilterHandler(this);
		view.addRefreshHandler(this);
	}

	@Override
	public void onRequestDelete(RequestDeleteEvent deleteEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRefresh(RefreshEvent refreshEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFilter(FilterEvent filterEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCreate(CreateEvent createEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConfirmDelete(ConfirmDeleteEvent deleteEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancelUpdate(CancelUpdateEvent updateEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate(UpdateEvent updateEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancelDelete(CancelDeleteEvent cancelDeleteEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRequestUpdate(RequestUpdateEvent requestUpdateEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancelCreate(CancelCreateEvent createEvent) {
		view.cancelCreate();
	}

	@Override
	public void onStartCreate(StartCreateEvent createEvent) {
		view.startCreate();
	}
}
