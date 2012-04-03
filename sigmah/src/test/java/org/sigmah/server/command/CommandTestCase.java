/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command;


import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;

import org.sigmah.server.authentication.AuthenticationModuleStub;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.endpoint.gwtrpc.CommandServlet;
import org.sigmah.server.endpoint.gwtrpc.GwtRpcModule;
import org.sigmah.server.util.TemplateModule;
import org.sigmah.server.util.beanMapping.BeanMappingModule;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.test.MockHibernateModule;
import org.sigmah.test.Modules;

import com.google.inject.Inject;
import com.google.inject.Injector;

@Modules({
        MockHibernateModule.class,
        TemplateModule.class,
        BeanMappingModule.class,
        GwtRpcModule.class,
        AuthenticationModuleStub.class
})
public abstract class CommandTestCase {

    @Inject
    protected CommandServlet servlet;
    @Inject
    protected EntityManager em;

    @Inject
    protected Injector injector;


    protected void setUser(int userId) {
    	AuthenticationModuleStub.setUserId(userId);
    }

    protected <T extends CommandResult> T execute(Command<T> command) throws CommandException {
        User user = em.find(User.class, AuthenticationModuleStub.getCurrentUser().getUserId());
        assert user != null : "cannot find user id " + 
        	AuthenticationModuleStub.getCurrentUser().getUserId() + " in the database, have you " +
        	" called execute() without a @OnDataset annotation?";
        Locale.setDefault(Locale.ENGLISH);

        List<CommandResult> results = servlet.handleCommands(Collections.<Command>singletonList(command));

        // normally each request and so each handleCommand() gets its own
        // EntityManager, but here successive requests in the same test
        // will share an EntityManager, which can be bad if there are collections
        // still living in the first-level cache
        //
        // I think these command tests should ultimately become real end-to-end
        // tests and so would go through the actual servlet process, but for the moment,
        // we'll just add this work aroudn that clears the cache after each command.
        em.clear();


        CommandResult result = results.get(0);
        if (result instanceof CommandException) {
            throw (CommandException) result;
        }

        return (T) result;
    }
}