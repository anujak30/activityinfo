package org.activityinfo.shared.command.handler;

import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CommandHandlerAsync<C extends Command<R>, R extends CommandResult> {

    /*
     * TODO: is there anyway the return type can be automatically parameratized
     * with the type parameter of CommandT ? (and without adding a second type
     * parameter to CommandHandler
     */


   /**
    * Execute a Command asynchronously
    *
    * @param <T> Result type
    * @param command Command to be executed
    * @param callback Callback to receive the command result or an exception
    *
    */
   void execute(C command, ExecutionContext context, AsyncCallback<R> callback);

	
}
