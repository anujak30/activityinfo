/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.event;

import org.activityinfo.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;

/**
 * Encapsulates details for {@link org.activityinfo.server.database.hibernate.entity.Site} related events.
 *
 * @author Alex Bertram
 */
public class SiteEvent extends BaseEvent {
    private int siteId;
    private SiteDTO site;

    /**
     *
     * @param type
     * @param source the component which fired the event
     * @param site
     */
    public SiteEvent(EventType type, Object source, SiteDTO site) {
        super(type);
        this.setSource(source);
        this.site = site;
        this.siteId = site.getId();
    }

    public SiteEvent(EventType type, Object source, int siteId) {
        super(type);
        this.setSource(source);
        this.siteId = siteId;
    }

    public SiteDTO getSite() {
        return site;
    }

    public int getSiteId() {
        return siteId;
    }
}
