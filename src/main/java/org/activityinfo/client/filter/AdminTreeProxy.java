/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.filter;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.command.result.AdminEntityResult;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.CountryDTO;

import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.google.gwt.user.client.rpc.AsyncCallback;

class AdminTreeProxy implements DataProxy {

    private final Dispatcher service;

    private CountryDTO country;
    private Filter filter;

    public AdminTreeProxy(Dispatcher service) {
        this.service = service;
    }

    public void setCountry(CountryDTO country) {
    	this.country = country;
    }

    public void load(DataReader dataReader, Object parent, final AsyncCallback callback) {
    	
    	if(country == null) {
    		callback.onSuccess(Collections.emptyList());
    		return;
    	}
    	
    	GetAdminEntities request = new GetAdminEntities();
    	request.setCountryId(country.getId());
    	request.setFilter(filter);
    	
        if (parent != null) {
            assert parent instanceof AdminEntityDTO : "expecting AdminEntityDTO";

            AdminEntityDTO parentEntity = (AdminEntityDTO) parent;
            request.setParentId(parentEntity.getId());
        }

        service.execute(request, new AsyncCallback<AdminEntityResult>() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess(AdminEntityResult result) {
            	prepareData(result.getData());
                callback.onSuccess(result.getData());
            }

        });

    }

	public void setFilter(Filter filter) {
		this.filter = filter;
		
	}

	private void prepareData(List<AdminEntityDTO> list) {
		if(!sameLevel(list)) {
			for(AdminEntityDTO entity : list) {
				AdminLevelDTO level = country.getAdminLevelById(entity.getLevelId());
				entity.setName( entity.getName() + " [" + level.getName() + "]");
			}
		}
	}
	
	private boolean sameLevel(List<AdminEntityDTO> entities) {
		Iterator<AdminEntityDTO> it = entities.iterator();
		if(it.hasNext()) {
			int levelId = it.next().getLevelId();
			while(it.hasNext()) {
				if(it.next().getLevelId() != levelId) {
					return false;
				}
			}
		}
		return true;
	}
}

