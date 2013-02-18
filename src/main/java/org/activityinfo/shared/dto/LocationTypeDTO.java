/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;


/**
 * One-to-one DTO of the {@link org.activityinfo.server.database.hibernate.entity.LocationType LocationType}
 * domain object.
 *
 * @author Alex Bertram
 */
public final class LocationTypeDTO extends BaseModelData implements DTO {

	public LocationTypeDTO() {
	}

    public LocationTypeDTO(int id, String name) {
        setId(id);
        setName(name);
    }

    public void setId(int id) {
		set("id", id);
	}
	
	public int getId() {
		return (Integer)get("id");
	}
	
	public void setName(String value) {
		set("name", value);
	}
	
	public String getName() { 
		return get("name");
	}
	
	public Integer getBoundAdminLevelId() {
		return get("boundAdminLevelId");
	}
	
	public void setBoundAdminLevelId(Integer id) {
		set("boundAdminLevelId", id);
	}

	public boolean isAdminLevel() {
		return getBoundAdminLevelId() != null;
	}

}
