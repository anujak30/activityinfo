

package org.activityinfo.shared.report.model.typeadapter;

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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.activityinfo.shared.report.content.DimensionCategory;
import org.activityinfo.shared.report.content.EntityCategory;

/**
 * @author Alex Bertram
 */
public class CategoryAdapter extends XmlAdapter<CategoryAdapter.Category, DimensionCategory> {

    public static class Category {

        @XmlAttribute
        private Integer id;
    }

    @Override
    public DimensionCategory unmarshal(Category category) throws Exception {
        if(category.id != null) {
            return new EntityCategory(category.id);
        }
        return null;
    }

    @Override
    public Category marshal(DimensionCategory v) throws Exception {
        return null;
    }
}
