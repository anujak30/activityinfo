/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.util;

import com.extjs.gxt.ui.client.data.SortInfo;


public final class SortInfoEqualityChecker {

	private SortInfoEqualityChecker() {}
	
    public static boolean equals(SortInfo a, SortInfo b) {
        if(a==null && b==null) {
            return true;
        }
        if(a==null || b==null) {
            return false;
        }
        return a.getSortField().equals(b.getSortField()) &&
               a.getSortDir() == b.getSortDir();
    }
}
