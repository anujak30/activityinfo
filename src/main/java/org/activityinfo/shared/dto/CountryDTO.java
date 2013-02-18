/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.dto;


import java.util.ArrayList;
import java.util.List;

import org.activityinfo.shared.util.mapping.Extents;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * One-to-one DTO for {@link org.activityinfo.server.database.hibernate.entity.Country} domain objects.
 */
public final class CountryDTO extends BaseModelData implements DTO {

    private static final long serialVersionUID = 3189552164304073119L;
    
    private List<AdminLevelDTO> adminLevels = new ArrayList<AdminLevelDTO>(0);
	private List<LocationTypeDTO> locationTypes = new ArrayList<LocationTypeDTO>(0);
    private Extents bounds;

	public CountryDTO() {
	}

    public CountryDTO(int id, String name) {
        setId(id);
        setName(name);
    }

    public void setId(int id) {
		set("id",id);
	}
	
	public int getId() {
		return (Integer)get("id");
	}
	
	public String getName()
	{
		return get("name");		
	}
	
	public void setName(String value) {
		set("name", value);
	}

	public List<AdminLevelDTO> getAdminLevels() {
		return this.adminLevels;
	}
	
	public void setAdminLevels(List<AdminLevelDTO> levels) {
		this.adminLevels = levels;
	}
	
	public List<LocationTypeDTO> getLocationTypes() {
		return this.locationTypes;
	}
	
	public void setLocationTypes(List<LocationTypeDTO> types) {
		this.locationTypes = types;
	}

    public Extents getBounds() {
        return bounds;
    }

    public void setBounds(Extents bounds) {
        this.bounds = bounds;
    }
    
    public String getCodeISO() {
        return get("codeISO");
    }
    
    public void setCodeISO(String codeISO) {
        set("codeISO", codeISO);
        set("completeName", getName() + " (" + codeISO + ")");
    }
    
    /**
     * Gets the name of this country with the code appended at the end.
     * @return The name and the code of this country.
     */
    public String getCompleteName() {
        return getName() + " (" + getCodeISO() + ")";
    }

    /**
     * Finds an AdminEntity by id
     *
     * @param levelId the id of the AdminEntity to return
     * @return the AdminEntity with corresponding id or null if no such AdminEntity is found in the list
     */
	public AdminLevelDTO getAdminLevelById(int levelId) {
		for(AdminLevelDTO level : this.adminLevels) {
			if(level.getId() == levelId) {
				return level;
			}
		}
		return null;
	}

    /**
     * Returns a list of <code>AdminLevelDTO</code>s that are ancestors of the
     * the AdminLevel with an id of <code>levelId</code>  in order descending from the root.
     *
     * @param levelId the id of AdminLevel
     * @return a list of AdminLevelDTOs in <code>adminLevels</code>  which are ancestors of
     * the AdminLevel with the id of <code>levelId</code>, or null if no AdminLevelDTO with the
     * given id or exists or if the indicated AdminLevel is a root level.
     */
	public List<AdminLevelDTO> getAdminLevelAncestors(int levelId) {
		List<AdminLevelDTO> ancestors = new ArrayList<AdminLevelDTO>();
		
		AdminLevelDTO level = getAdminLevelById(levelId);
		
		if(level == null) {
            return null;
        }
		
		while(true) {
			ancestors.add(0, level);
			
			if(level.isRoot()) {
                return ancestors;
            } else {
                level = getAdminLevelById(level.getParentLevelId());
            }
		}
	}

    /**
     * Returns the <code>LocationTypeDTO</code> with the given <code>locationTypeId</code>
     * @param locationTypeId the id of a <code>LocationTypeDTO</code> in <code>locationTypes</code>
     * @return the <code>LocationTypeDTO</code> in <code>locationTypes</code> with the id <code>locationTypeId</code>
     */
    public LocationTypeDTO getLocationTypeById(int locationTypeId) {
        for(LocationTypeDTO type : getLocationTypes()) {
            if(type.getId()==locationTypeId) {
                return type;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CountryDTO");
        sb.append("\nname:");
        sb.append(this.getName());
        sb.append("\niso2:");
        sb.append(this.getCodeISO());
        sb.append("\nbounds:");
        sb.append(this.getBounds());
        return sb.toString();
    }

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(obj.getClass() != getClass()) {
			return false;
		}
		CountryDTO other = (CountryDTO)obj;
		return getId() == other.getId();
	}

	@Override
	public int hashCode() {
		return getId();
	}
    
    
}
