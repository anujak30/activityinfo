/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Observable;

/**
 * The EventBus is a publish/subscribe API that allows
 * objects to communicate with each other without having to refer to each other.
 */
public interface EventBus extends Observable {

    public static class NamedEventType extends EventType {
        private final String name;

        public NamedEventType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public boolean fireEvent(EventType type);
    
    public boolean fireEvent(BaseEvent event);
}
