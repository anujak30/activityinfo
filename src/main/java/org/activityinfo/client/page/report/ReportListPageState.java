/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.report;

import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.app.Section;

import java.util.Arrays;
import java.util.List;

/**
 * Page State object
 *
 * @author Alex Bertram
 */
public class ReportListPageState implements PageState {

    public PageId getPageId() {
        return ReportsPage.PAGE_ID;
    }

    public String serializeAsHistoryToken() {
        return null;
    }

    public List<PageId> getEnclosingFrames() {
        return Arrays.asList(ReportsPage.PAGE_ID);
    }

	@Override
	public Section getSection() {
		return Section.ANALYSIS;
	}

}
