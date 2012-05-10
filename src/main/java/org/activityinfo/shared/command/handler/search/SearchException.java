package org.activityinfo.shared.command.handler.search;

import org.activityinfo.shared.exception.CommandException;

public class SearchException extends CommandException {

	public SearchException(String message) {
		super(message);
	}

	public SearchException(Throwable e) {
		super(e);
	}
	
}
