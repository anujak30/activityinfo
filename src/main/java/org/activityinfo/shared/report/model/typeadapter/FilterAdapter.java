/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.report.model.typeadapter;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.report.model.DateRange;
import org.activityinfo.shared.report.model.DimensionType;

/**
 * @author Alex Bertram
 */
public class FilterAdapter extends XmlAdapter<
        FilterAdapter.FilterElement,
        Filter> {

    public static class Restriction {
        @XmlAttribute
        private String dimension;
        
        @XmlElement(name="category")
        private List<String> categories = new ArrayList<String>(0);
    }

    public static class FilterElement {
        @XmlElement(name="restriction")
        private List<Restriction> restrictions = new ArrayList<Restriction>(0);

        @XmlElement
        private DateRange dateRange;
    }

    @Override
    public Filter unmarshal(FilterElement element) throws Exception {
        Filter filter = new Filter();
        filter.setDateRange(element.dateRange);

        for(Restriction r : element.restrictions) {
            for(String s : r.categories) {
                filter.addRestriction(findDimType(r.dimension), Integer.parseInt(s));
            }
        }
        return filter;
    }

    private DimensionType findDimType(String name) {
        String nameLowered = name.toLowerCase();
        for(DimensionType type : DimensionType.values()) {
            if(type.toString().toLowerCase().equals(nameLowered)) {
                return type;
            }
        }
        throw new RuntimeException("No DimensionType could be find to match '" + name + "'");
    }

    @Override
    public FilterElement marshal(Filter filter) throws Exception {
        FilterElement element = new FilterElement();
        element.dateRange = filter.getDateRange();
        for(DimensionType t : filter.getRestrictedDimensions()) {
            Restriction r = new Restriction();
            r.dimension = t.toString().toLowerCase();
            for(Integer id : filter.getRestrictions(t) ) {
                r.categories.add(id.toString());
            }
            element.restrictions.add(r);
        }
        return element;
    }
}
