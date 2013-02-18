/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command.handler.sync;

import java.sql.Timestamp;
import java.util.Date;

public class TimestampHelper {

    public static String toString(long timestamp) {
        return Long.toString(timestamp);
    }

    public static long fromString(String s) {
        if(s == null) {
            return 0;
        }
        String[] parts = s.split("\\.");
        return Long.parseLong(parts[0]);
    }
    
    public static Timestamp fromDate(Date d) {
    	if(d instanceof Timestamp) {
    		return (Timestamp)d;
    	}
    	return new Timestamp(d.getTime());
    }

    public static String toString(Date date) {
        if(date instanceof Timestamp) {
            return toString(date);
        } else {
            return Long.toString(date.getTime());
        }

    }

    static boolean isAfter(Date date1, Date date2) {
        if(date1.after(date2)) {
            return true;
        }
        if(date1.before(date2)) {
            return false;
        }

        if(date1 instanceof Timestamp && date2 instanceof Timestamp) {
            int nano1 = ((Timestamp) date1).getNanos();
            int nano2 = ((Timestamp) date2).getNanos();
            return nano1 > nano2;
        }
        return false;
    }
}
