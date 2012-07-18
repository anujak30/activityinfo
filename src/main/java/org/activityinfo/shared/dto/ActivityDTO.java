/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activityinfo.shared.dto.LockedPeriodDTO.HasLockedPeriod;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.common.collect.Sets;

/**
 * One-to-one DTO for the Activity table.
 *
 * @author Alex Bertram
 */
public final class ActivityDTO 
	extends 
		BaseModelData 
	implements 
		EntityDTO,
		HasLockedPeriod,
		ProvidesKey {

    public static final String ENTITY_NAME = "Activity";

    public static final int REPORT_ONCE = 0;
    public static final int REPORT_MONTHLY = 1;

    private UserDatabaseDTO database;

    private List<IndicatorDTO> indicators = new ArrayList<IndicatorDTO>(0);
    private List<AttributeGroupDTO> attributeGroups = new ArrayList<AttributeGroupDTO>(0);
    private Set<LockedPeriodDTO> lockedPeriods = new HashSet<LockedPeriodDTO>(0);
    
    // to ensure serializer
    private Published _published;

    public ActivityDTO() {
        setAssessment(false);
        setReportingFrequency(REPORT_ONCE);
    }

    /**
     * Constructs a DTO with the given properties
     */
    public ActivityDTO(Map<String,Object> properties) {
        super(properties);  
    }

    /**
     * Creates a shallow clone
     *
     * @param model
     */
    public ActivityDTO(ActivityDTO model) {
        super(model.getProperties());
        this.database = model.getDatabase();
        this.setIndicators(model.getIndicators());
        this.setAttributeGroups(model.getAttributeGroups());
    }

    /**
     *
     * @param id the Activity's id
     * @param name the Activity's name
     */
    public ActivityDTO(int id, String name) {
        this();
        setId(id);
        setName(name);
    }

    /**
     *
     * @param db the UserDatabaseDTO to which this Activity belongs
     */
    public ActivityDTO(UserDatabaseDTO db) {
        setDatabase(db);
    }

    /**
     *
     * @return this Activity's id
     */
    @Override
	public int getId() {
        return (Integer)get("id");
    }

    /**
     * Sets this Activity's id
     */
    public void setId(int id) {
        set("id", id);
    }


    /**
     * Sets this Activity's name
     */
    public void setName(String value) {
        set("name", value);
    }

    /**
     * @return this Activity's name
     */
    @Override
	public String getName(){
        return get("name");
    }

    /**
     * @return the database to which this Activity belongs
     */
    public UserDatabaseDTO getDatabase() {
        return database;
    }

    /**
     * Sets the database to which this Activity belongs
     */
    public void setDatabase(UserDatabaseDTO database) {
        this.database = database;
    }
    
    public int getPublished() {
    	return (Integer) get("published");
    }

    public void setPublished(int published) {
    	set("published",published);
    }
    
    /**
     * @return a list of this Activity's indicators
     */
    public List<IndicatorDTO> getIndicators() {
        return indicators;
    }

    /**
     * @param indicatorIds the ids of the indicators for which to search
     * @return true if this activity contains any of the provided indicators
     */
    public boolean containsAny(Set<Integer> indicatorIds) {
		for(IndicatorDTO indicator : getIndicators()) {
			if(indicatorIds.contains(indicator.getId())) {
				return true;
			}
		}
		return false;
    }

    /**
     * Sets this Activity's Indicator
     */
    public void setIndicators(List<IndicatorDTO> indicators) {
        this.indicators = indicators;
    }

    public List<AttributeGroupDTO> getAttributeGroups() {
        return attributeGroups;
    }

    public void setAttributeGroups(List<AttributeGroupDTO> attributes) {
        this.attributeGroups = attributes;
    }

    public void setAssessment(boolean value) {
        set("assessment", value);
    }

    public boolean isAssessment() {
        return (Boolean)get("assessment");
    }

    /**
     * Sets the ReportingFrequency of this Activity, either
     * <code>REPORT_ONCE</code> or <code>REPORT_MONTHLY</code>
     */
    public void setReportingFrequency(int frequency) {
        set("reportingFrequency", frequency);
    }

    /**
     * @return the ReportingFrequency of this Activity, either
     * <code>REPORT_ONCE</code> or <code>REPORT_MONTHLY</code>
     */
    public int getReportingFrequency() {
        return (Integer)get("reportingFrequency");
    }

    /**
     * Sets the id of the LocationType of the Location to which this Site
     * belongs.
     */
    public void setLocationTypeId(int locationId) {
        set("locationTypeId", locationId);

    }

    /**
     * @return  the id of the LocationType of the Location to which this Site belongs
     */
    public int getLocationTypeId() {
        return (Integer)get("locationTypeId");
    }

    /**
     *
     * @return the
     */
    public LocationTypeDTO getLocationType() {
        return getDatabase().getCountry().getLocationTypeById(getLocationTypeId());
    }


    /**
     * Searches the list of Attributes for the AttributeDTO with the given id
     *
     * @return the AttributeDTO matching the given id, or null if no such AttributeDTO was found.
     */
    public AttributeDTO getAttributeById(int id) {
        for(AttributeGroupDTO group : attributeGroups) {
            AttributeDTO attribute = SchemaDTO.getById(group.getAttributes(), id);
            if(attribute!=null) {
                return attribute;
            }
        }
        return null;
    }

    /**
     * Searches this Activity's list of Indicators for the IndicatorDTO with the given id.
     *
     * @return the matching IndicatorDTO or null if nothing was found
     */
    public IndicatorDTO getIndicatorById(int indicatorId) {
        for(IndicatorDTO indicator : indicators) {
            if(indicator.getId() == indicatorId) {
                return indicator;
            }
        }
        return null;
    }

    /**
     *
     * @return this Activity's category
     */
    public String getCategory() {
        return get("category");
    }

    /**
     * Sets this Activity's category
     */
    public void setCategory(String category) {
        set("category", category);
    }

    /**
     * Convenience method that creates a list of IndicatorGroups from this Activity's list
     * of Indicators, based on the Indicator's category property.
     *
     */
    public List<IndicatorGroup> groupIndicators() {
        List<IndicatorGroup> groups = new ArrayList<IndicatorGroup>();
        Map<String, IndicatorGroup> map = new HashMap<String, IndicatorGroup>();

        for(IndicatorDTO indicator : indicators) {
            String category = indicator.getCategory();
            IndicatorGroup group = map.get(category);
            if(group == null) {
                group = new IndicatorGroup(category);
                group.setActivityId(this.getId());
                map.put(category, group);
                groups.add(group);
            }
            group.addIndicator(indicator);
        }
        return groups;
    }

    /**
     *
     * @return the id of the MapIcon associated with this Activity
     */
    public String getMapIcon() {
        return get("mapIcon");
    }

    /**
     * Sets the id of the MapIcon to be associated with this Activity
     */
    public void setMapIcon(String mapIcon) {
        set("mapIcon", mapIcon);
    }

    @Override
    public String getEntityName() {
    	return ENTITY_NAME;
    }

    
    /**
     *
     * @return The list of admin levels that can be set for this 
     * Activity's LocationType.
     */
    public List<AdminLevelDTO> getAdminLevels() {
        if(getLocationType().isAdminLevel()) {

            // if this activity is bound to an administrative
            // level, then we need only as far down as this goes

            return getDatabase().getCountry().getAdminLevelAncestors(
                    getLocationType().getBoundAdminLevelId());
        } else {

            // all admin levels

            return getDatabase().getCountry().getAdminLevels();
        }
    }


    /**
     * Searches this Activity's list of AttributeGroups for an AttributeGroupDTO with
     * the given id

     * @return  the matching AttributeGroupDTO or null if there are no matches
     */
    public AttributeGroupDTO getAttributeGroupById(int id) {
        for(AttributeGroupDTO group : attributeGroups) {
            if(group.getId()==id) {
                return group;
            }
        }
        return null;
    }

	@Override
	public String getKey() {
		return "act" + getId();
	}

	public void setLockedPeriods(Set<LockedPeriodDTO> lockedPeriods) {
		this.lockedPeriods = lockedPeriods;
	}

	@Override
	public Set<LockedPeriodDTO> getLockedPeriods() {
		return lockedPeriods;
	}

	@Override
	public Set<LockedPeriodDTO> getEnabledLockedPeriods() {
	    Set<LockedPeriodDTO> enabled = Sets.newHashSet();

	    for (LockedPeriodDTO lockedPeriod : getLockedPeriods()) {
	    	if (lockedPeriod.isEnabled()) {
	    		enabled.add(lockedPeriod);
	    	}
	    }
	    
	    return enabled;
	}
}
