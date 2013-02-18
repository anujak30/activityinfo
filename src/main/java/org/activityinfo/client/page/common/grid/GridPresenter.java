/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.common.grid;

import org.activityinfo.client.page.common.toolbar.ActionListener;
import org.activityinfo.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.Record;

public interface GridPresenter<T extends ModelData> extends ActionListener {
    public void onSelectionChanged(ModelData selectedItem);
    public int getPageSize();
    public void onDirtyFlagChanged(boolean isDirty);
    public boolean beforeEdit(Record record, String property);
    
    public interface SiteGridPresenter extends GridPresenter<SiteDTO> {
    	
    }
}
