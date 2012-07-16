package org.activityinfo.client.offline.sync;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.offline.command.CommandQueue;
import org.activityinfo.client.offline.command.CommandQueue.QueueEntry;
import org.activityinfo.client.offline.sync.pipeline.AsyncCommand;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Sends updates to the remote database. 
 * 
 */
@Singleton
public class UpdateSynchronizer implements AsyncCommand {

	private CommandQueue commandQueue;
	private Dispatcher dispatcher;
	
	private AsyncCallback<Void> callback;
	private QueueEntry currentEntry = null;
	
	@Inject
	public UpdateSynchronizer(CommandQueue commandQueue, SynchronizerDispatcher dispatcher) {
		super();
		this.commandQueue = commandQueue;
		this.dispatcher = dispatcher;
	}
	
	
	public void execute(AsyncCallback<Void> callback) {
		this.callback = callback;
		nextCommand();
	}
	
	private void nextCommand() {
		commandQueue.peek(new AsyncCallback<CommandQueue.QueueEntry>() {
			
			@Override
			public void onSuccess(QueueEntry entry) {
				currentEntry = entry;
				if(currentEntry == null) {
					callback.onSuccess(null);
				} else {
					sendToServer();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}


	private void sendToServer() {
		dispatcher.execute(currentEntry.getCommand(), new AsyncCallback() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(Object result) {
				removeFromQueue();
			}
		});
		
	}
	
	private void removeFromQueue() {
		commandQueue.remove(currentEntry.getId(), new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean removed) {
				nextCommand();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}
}
