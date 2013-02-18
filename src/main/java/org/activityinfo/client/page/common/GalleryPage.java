/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.common;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.event.NavigationEvent;
import org.activityinfo.client.page.NavigationHandler;
import org.activityinfo.client.page.PageState;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class GalleryPage extends LayoutContainer implements GalleryView {

    private ListStore<GalleryModel> store;
    private Html heading;
    private Html introPara;

    public static class GalleryModel extends BaseModelData {

        private PageState place;

        public GalleryModel(String name, String desc, String path, PageState place) {
            set("name", name);
            set("path", path);
            set("desc", desc);

            this.place = place;
        }

        public PageState getPlace() {
            return place;
        }
    }

    @Inject
    public GalleryPage(final EventBus eventBus) {

        this.setStyleName("gallery");
        this.setScrollMode(Style.Scroll.AUTOY);
        this.setStyleAttribute("background", "white");

        setLayout(new FlowLayout());

        heading = new Html();
        heading.setTagName("h3");
        add(heading);

        introPara = new Html();
        introPara.setTagName("p");
        introPara.setStyleName("gallery-intro");
        add(introPara);

        store = new ListStore<GalleryModel>();

        ListView<GalleryModel> view = new ListView<GalleryModel>();
        view.setTemplate(getTemplate(GWT.getModuleBaseURL() + "image/"));
        view.setBorders(false);
        view.setStore(store);
        view.setItemSelector("dd");
        view.setOverStyle("over");

        view.addListener(Events.Select,
            new Listener<ListViewEvent<GalleryModel>>() {

                @Override
				public void handleEvent(ListViewEvent<GalleryModel> event) {
                    eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested,
                            event.getModel().getPlace()));
                }
            });
        add(view);
    }

    @Override
	public void setHeading(String html) {
        heading.setHtml(html);
    }

    @Override
	public void setIntro(String html) {
        introPara.setHtml(html);
    }

    @Override
    public void add(String name, String desc, String path, PageState place) {

        store.add(new GalleryModel(name, desc, path, place));
    }

    private native String getTemplate(String base) /*-{
              return ['<dl><tpl for=".">',
              '<dd>',
              '<img src="' + base + 'thumbs/{path}" title="{name}">',
              '<div>',
              '<h4>{name}</h4><p>{desc}</p></div>',
              '</tpl>',
              '<div style="clear:left;"></div></dl>'].join("");

              }-*/;

}
