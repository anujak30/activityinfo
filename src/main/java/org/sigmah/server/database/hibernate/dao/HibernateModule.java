/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.database.hibernate.dao;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.ejb.HibernateEntityManager;
import org.sigmah.server.util.config.DeploymentConfiguration;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.RequestScoped;

/**
 * Gui
import org.sigmah.server.util.config.ConfigProperties;
ce module that provides Hibernate-based implementations for the DAO-layer interfaces.
 *
 * @author Alex Bertram
 */
public class HibernateModule extends AbstractModule {

    @Override
    protected void configure() {
        configureEmf();
        configureEm();
        configureDAOs();
        install(new TransactionModule());
    }

    protected void configureEmf() {
        bind(EntityManagerFactory.class).toProvider(EntityManagerFactoryProvider.class).in(Singleton.class);
    }

    protected void configureEm() {
        bind(EntityManager.class).toProvider(EntityManagerProvider.class).in(RequestScoped.class);
    }
    protected void configureDAOs() {
    	bind(AdminDAO.class).to(AdminHibernateDAO.class);
        bindDAOProxy(ActivityDAO.class);
        bindDAOProxy(AuthenticationDAO.class);
        bindDAOProxy(CountryDAO.class);
        bindDAOProxy(IndicatorDAO.class);
        bindDAOProxy(ReportDefinitionDAO.class);
        bindDAOProxy(PartnerDAO.class);
        bindDAOProxy(UserDatabaseDAO.class);
        bindDAOProxy(UserPermissionDAO.class);
        bind(UserDAO.class).to(UserDAOImpl.class);
    }

    private <T extends DAO> void bindDAOProxy(Class<T> daoClass) {
        HibernateDAOProvider<T> provider = new HibernateDAOProvider<T>(daoClass);
        requestInjection(provider);

        bind(daoClass).toProvider(provider);
    }


    protected static class EntityManagerFactoryProvider implements Provider<EntityManagerFactory> {
        private org.sigmah.server.util.config.DeploymentConfiguration configProperties;

        @Inject
        public EntityManagerFactoryProvider(DeploymentConfiguration configProperties) {
            this.configProperties = configProperties;
        }

        @Override
        public EntityManagerFactory get() {
        	// ensure that hibernate does do schema updating--liquibase is in charge
        	Properties config = configProperties.asProperties();
        	config.setProperty("hibernate.hbm2ddl.auto", "");
        	
            return Persistence.createEntityManagerFactory("activityInfo", config);
        }
    }

    protected static class EntityManagerProvider implements Provider<EntityManager> {
        private EntityManagerFactory emf;

        @Inject
        public EntityManagerProvider(EntityManagerFactory emf) {
            this.emf = emf;
        }

        @Override
        public EntityManager get() {
            return emf.createEntityManager();
        }
    }

    @Provides
    protected HibernateEntityManager provideHibernateEntityManager(EntityManager entityManager) {
        return (HibernateEntityManager)entityManager;
    }
   
}
