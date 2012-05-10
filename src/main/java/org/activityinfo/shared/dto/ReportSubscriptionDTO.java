/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * Projection DTO for the {@link org.activityinfo.server.database.hibernate.entity.ReportSubscription ReportSubscription}
 * domain class.
 * 
 * A row in a list of users who can be invited to view a report.
 * Models a projection of <code>UserPermission</code>, <code>ReportTemplate</code>,
 * and <code>ReportProjection</code>.
 *
 * @author Alex Bertram
 */
public final class ReportSubscriptionDTO extends BaseModelData {

    public ReportSubscriptionDTO() {
    }

    public void setUserId(int userId) {
        set("userId", userId);
    }

    public int getUserId() {
        return (Integer) get("userId");
    }

    public String getUserEmail() {
        return get("userEmail");
    }

    public void setUserEmail(String email) {
        set("userEmail", email);
    }

    public void setUserName(String name) {
        set("userName", name);
    }

    public String getUserName() {
        return get("userName");
    }

    public Boolean isSubscribed() {
        return get("subscribed");
    }

    public void setSubscribed(Boolean subscribed) {
        set("subscribed", subscribed);
    }
    
}
