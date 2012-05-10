package org.activityinfo.server.command.handler;

import java.util.Date;

import javax.persistence.EntityManager;

import org.activityinfo.server.database.hibernate.entity.Project;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.database.hibernate.entity.UserDatabase;
import org.activityinfo.shared.command.AddProject;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.ProjectDTO;
import org.activityinfo.shared.exception.CommandException;

import com.google.inject.Inject;


/*
 * Adds given Project to the database
 */
public class AddProjectHandler implements CommandHandler<AddProject> {

    private final EntityManager em;

    @Inject
	public AddProjectHandler(EntityManager em) {
		this.em = em;
	}

	@Override
	public CommandResult execute(AddProject cmd, User user)
			throws CommandException {

        UserDatabase db = em.find(UserDatabase.class, cmd.getDatabaseId());

        ProjectDTO from = cmd.getProjectDTO();
        Project project = new Project();
        project.setName(from.getName());
        project.setDescription(from.getDescription());
        project.setUserDatabase(db);

        db.setLastSchemaUpdate(new Date());
        
        em.persist(project);
        em.persist(db);
        db.getProjects().add(project);

        return new CreateResult(project.getId());
	}
}