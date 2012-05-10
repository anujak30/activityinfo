package org.activityinfo.client.page.common.columns;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.LockedPeriodDTO;
import org.activityinfo.shared.dto.ProjectDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;

/*
 * A column displaying an icon of the parent type of a LockedPeriod;
 * this can be a database, activity or a project
 */
public class ReadLockedPeriodTypeColumn extends ColumnConfig {

	public ReadLockedPeriodTypeColumn() {
		super();
		
	    GridCellRenderer<LockedPeriodDTO> iconRenderer = new GridCellRenderer<LockedPeriodDTO>() {
			@Override
			public Object render(LockedPeriodDTO model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<LockedPeriodDTO> store, Grid<LockedPeriodDTO> grid) {
				
				if (model.getParent() instanceof ActivityDTO) {
					return IconImageBundle.ICONS.activity().getHTML();
				}
				
				if (model.getParent() instanceof UserDatabaseDTO) {
					return IconImageBundle.ICONS.database().getHTML();
				}
				
				if (model.getParent() instanceof ProjectDTO) {
					return IconImageBundle.ICONS.project().getHTML();
				}
				
				return null;
			}
		}; 
		setHeader(I18N.CONSTANTS.type());  
	    setWidth(48);
	    setRowHeader(true);
	    setRenderer(iconRenderer);	}
}
