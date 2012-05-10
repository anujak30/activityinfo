/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command.handler;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.activityinfo.shared.command.GetCountries;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.CountryResult;
import org.activityinfo.shared.dto.CountryDTO;
import org.activityinfo.shared.exception.CommandException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.Mapper;
import org.sigmah.server.database.hibernate.dao.CountryDAO;
import org.sigmah.server.database.hibernate.entity.Country;
import org.sigmah.server.database.hibernate.entity.User;

import com.google.inject.Inject;

public class GetCountriesHandler implements CommandHandler<GetCountries> {

    private final static Log LOG = LogFactory.getLog(GetCountriesHandler.class);

    private final CountryDAO countryDAO;
    private final Mapper mapper;

    private final EntityManager entityManager;

    @Inject
    public GetCountriesHandler(CountryDAO countryDAO, Mapper mapper, EntityManager entityManager) {
        this.countryDAO = countryDAO;
        this.mapper = mapper;

        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public CommandResult execute(GetCountries cmd, User user) throws CommandException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("[execute] Gets countries command.");
        }

        if (cmd.isContainingProjects()) {

            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] Gets countries with projects ? " + cmd.isContainingProjects());
            }

            return new CountryResult(mapToDtos(entityManager.createQuery(
                    "SELECT c FROM Country c WHERE c IN (SELECT co.id FROM Project p, IN(p.country) co)")
                    .getResultList()));
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("[execute] Gets countries with projects ? " + cmd.isContainingProjects());
        }
        
        return new CountryResult(mapToDtos(countryDAO.queryAllCountriesAlphabetically()));
    }

    private ArrayList<CountryDTO> mapToDtos(List<Country> countries) {
        ArrayList<CountryDTO> dtos = new ArrayList<CountryDTO>();
        for (Country country : countries) {
            dtos.add(mapper.map(country, CountryDTO.class, "countryNameOnly"));
        }
        return dtos;
    }
}
