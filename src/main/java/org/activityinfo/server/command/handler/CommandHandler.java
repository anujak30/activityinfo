package org.activityinfo.server.command.handler;

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

import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;

/**
 * Command executors are the server half of {@link Command}s defined in the
 * client package. Each {@link Command} has its corresponding executor which is
 * responsible for carrying out the command on the server.
 * 
 * @author Alex Bertram
 */
public interface CommandHandler<CommandT extends Command> {

    /*
     * TODO: is there anyway the return type can be automatically parameratized
     * with the type parameter of CommandT ? (and without adding a second type
     * parameter to CommandHandler
     */

    /**
     * Execute a command received from the client
     * 
     * @param <T>
     *            Result type
     * @param cmd
     *            Command received from the server
     * @return The result of command if successful. If the command is not
     *         successful, an exception should be thrown.
     * @throws org.activityinfo.shared.exception.CommandException
     * 
     */
    public CommandResult execute(CommandT cmd, User user)
        throws CommandException;

}
