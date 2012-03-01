/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.util.beanMapping;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class BeanMappingModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    Mapper provideMapper() {
        List<String> mappingFiles = new ArrayList<String>();
        mappingFiles.add("dozer-localdate.xml");
        mappingFiles.add("dozer-admin-mapping.xml");
        mappingFiles.add("dozer-schema-mapping.xml");

        return new DozerBeanMapper(mappingFiles);
    }

    public static Mapper getMapper() {
        BeanMappingModule mod = new BeanMappingModule();
        return mod.provideMapper();
    }
}
