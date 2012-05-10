/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.activityinfo.shared.report.model.DateRange;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.typeadapter.FilterAdapter;

import com.google.common.collect.Sets;

/**
 * Defines a filter of activity data as a date range and a set of restrictions on
 * <code>Dimensions</code>.
 *
 */
@XmlJavaTypeAdapter(FilterAdapter.class)
public class Filter implements Serializable {

    // TODO: should be restrictions on DIMENSIONS and not DimensionTypes!!

	private Map<DimensionType, Set<Integer>> restrictions = new HashMap<DimensionType, Set<Integer>>();

 	private DateRange dateRange = new DateRange();

    /**
     * Constructs a <code>Filter</code> with no restrictions. All data visible to the user
     * will be included.
     */
	public Filter() {

	}

    /**
     * Constructs a copy of the given <code>filter</code>
     *
     * @param filter The filter which to copy.
     */
    public Filter(Filter filter) {

        for(Map.Entry<DimensionType, Set<Integer>> entry : filter.restrictions.entrySet()) {
            this.restrictions.put(entry.getKey(), new HashSet<Integer>(entry.getValue()));
        }
        this.dateRange = filter.dateRange;
    }


    /**
     * Constructs a <code>Filter</code> as the intersection between two <code>Filter</code>s.
     *
     * @param a The first filter
     * @param b The second filter
     */
	public Filter(Filter a, Filter b) {

		Set<DimensionType> types = new HashSet<DimensionType>();
		types.addAll(a.restrictions.keySet());
		types.addAll(b.restrictions.keySet());
		
		for(DimensionType type : types) {
			this.restrictions.put(type, intersect(
					a.getRestrictionSet(type, false),
					b.getRestrictionSet(type, false)));
			
		}
        this.dateRange = DateRange.intersection(a.getDateRange(), b.getDateRange());

	}
	
	private Set<Integer> intersect(Set<Integer> a, Set<Integer> b) {
		if(a.size() == 0) {
            return new HashSet<Integer>(b);
        }
		if(b.size() == 0) {
            return new HashSet<Integer>(a);
        }
		
		Set<Integer> intersection = new HashSet<Integer>(a);
		intersection.retainAll(b);
		
		return intersection;
	}
	
	public Set<Integer> getRestrictions(DimensionType type) { 
		return getRestrictionSet(type, false);
	}
	
	private Set<Integer> getRestrictionSet(DimensionType type, boolean create) {
		Set<Integer> set = restrictions.get(type);
		
		if(set == null) {
			if(!create) {
				return Collections.emptySet();
			}
			set = new HashSet<Integer>();
			restrictions.put(type, set);
		}
		
		return set;
	}
	
	public void addRestriction(DimensionType type, int categoryId) {
		Set<Integer> set = getRestrictionSet(type, true);
		set.add(categoryId);
	}
	
	public void addRestriction(DimensionType type, Collection<Integer> categoryIds) {
		if(!categoryIds.isEmpty()) {
			Set<Integer> set = getRestrictionSet(type, true);
			set.addAll(categoryIds);
		}
	}
	
	public void clearRestrictions(DimensionType type) {
		restrictions.remove(type);
	}
	
	public boolean isRestricted(DimensionType type) {
		Set<Integer> set = restrictions.get(type);
		return set != null && !set.isEmpty();
	}
	
	public boolean isNull() {
		return restrictions.isEmpty() && !isDateRestricted();
	}
	
	public boolean hasRestrictions() {
		return !restrictions.isEmpty();
	}
	
	public boolean isDateRestricted() {
		if(dateRange == null){
			return false;
		}
		return dateRange.getMinDate()!=null || dateRange.getMaxDate()!=null;
	}

    public Set<DimensionType> getRestrictedDimensions() {
    	Set<DimensionType> dims = Sets.newHashSet();
    	for(Entry<DimensionType, Set<Integer>> entries : restrictions.entrySet()) {
    		if(!entries.getValue().isEmpty()) {
    			dims.add(entries.getKey());
    		}
    	}
        return dims;
    }

    public Map<DimensionType, Set<Integer>> getRestrictions() {
        return restrictions;
    }

    @XmlTransient
    public Date getMinDate() {
		return getDateRange().getMinDate();
	}

	public void setMinDate(Date minDate) {
		getDateRange().setMinDate(minDate);
	}

    @XmlTransient
	public Date getMaxDate() {
		return getDateRange().getMaxDate();
	}

	public void setMaxDate(Date maxDate) {
		getDateRange().setMaxDate(maxDate);
	}

    public void setDateRange(DateRange range) {
        this.dateRange = range;
    }

	public boolean isDimensionRestrictedToSingleCategory(DimensionType type) {
		return getRestrictions(type).size() == 1;
	}
	
	/**
	 * 
	 * @param activity
	 * @return
	 * @throws UnsupportedOperationException if the dimension is not restricted to exactly one category
	 */
	public int getRestrictedCategory(DimensionType type) {
		Set<Integer> ids = getRestrictions(type);
		if(ids.size() != 1) {
			throw new UnsupportedOperationException("Cannot return a unique category, the dimension " + type + " is restricted to " +
					ids.size()  + " categories");
		}
		return ids.iterator().next();
	}

	
	@XmlElement
    public DateRange getDateRange() {
        if(dateRange == null) {
            dateRange = new DateRange();
        }
        return dateRange;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(DimensionType type : getRestrictedDimensions()) {
            if(sb.length()!=0) {
                sb.append(", ");
            }
            sb.append(type.toString()).append("={");
            for(Integer id : getRestrictions(type)) {
                sb.append(' ').append(id);
            }
            sb.append(" }");
        }
        if(dateRange.getMinDate()!=null || dateRange.getMaxDate()!=null) {
            if(sb.length()!=0) {
                sb.append(", ");
            }
            sb.append("date=[");
            if(dateRange.getMinDate()!=null) {
                sb.append(dateRange.getMinDate());
            }
            sb.append(",");
            if(dateRange.getMaxDate()!=null) {
                sb.append(dateRange.getMaxDate()).append("]");
            }
        }
        return sb.toString();
    }

    public Filter onActivity(int activityId) {
        addRestriction(DimensionType.Activity, activityId);
        return this;
    }

    public Filter onSite(int siteId) {
        addRestriction(DimensionType.Site, siteId);
        return this;
    }
    
    public Filter onDatabase(int databaseId) {
        addRestriction(DimensionType.Database, databaseId);
        return this;
    }
    
    public static Filter filter() {
        return new Filter();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateRange == null) ? 0 : dateRange.hashCode());
		result = prime * result
				+ ((restrictions == null) ? 0 : restrictions.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Filter other = (Filter) obj;
		return getDateRange().equals(other.getDateRange()) &&
				getRestrictions().equals(other.getRestrictions());
	}

}
