package org.activityinfo.client.page.config;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.common.GalleryView;
import org.activityinfo.client.page.config.design.DesignPresenter;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.google.inject.Inject;

public class DbConfigPresenter implements DbPage {

    private final GalleryView view;
    private final Dispatcher dispatcher;

    public static final PageId PAGE_ID = new PageId("db");

    @Inject
    public DbConfigPresenter(GalleryView view, Dispatcher dispatcher) {
        this.view = view;
        this.dispatcher = dispatcher;
    }

    @Override
    public void go(UserDatabaseDTO db) {
        view.setHeading(db.getFullName() == null ? db.getName() : db
            .getFullName());

        if (db.isDesignAllowed()) {
            view.add(I18N.CONSTANTS.design(),
                I18N.CONSTANTS.designDescription(),
                "db-design.png",
                new DbPageState(DesignPresenter.PAGE_ID, db.getId()));
        }
        if (db.isManageAllUsersAllowed()) {
            view.add(I18N.CONSTANTS.partner(),
                I18N.CONSTANTS.partnerEditorDescription(),
                "db-partners.png",
                new DbPageState(DbPartnerEditor.PAGE_ID, db.getId()));
        }
        if (db.isManageUsersAllowed()) {
            view.add(I18N.CONSTANTS.users(),
                I18N.CONSTANTS.userManagerDescription(),
                "db-users.png",
                new DbPageState(DbUserEditor.PAGE_ID, db.getId()));
        }
        if (db.isDesignAllowed()) {
            view.add(I18N.CONSTANTS.timeLocks(), I18N.CONSTANTS
                .lockPeriodsDescription(),
                "db-lockedperiods.png", new DbPageState(
                    LockedPeriodsPresenter.PAGE_ID, db.getId()));
        }
        if (db.isDesignAllowed()) {
            view.add(I18N.CONSTANTS.project(),
                I18N.CONSTANTS.projectManagerDescription(),
                "db-projects.png",
                new DbPageState(DbProjectEditor.PAGE_ID, db.getId()));
        }
        if (db.isDesignAllowed()) {
            view.add(I18N.CONSTANTS.target(),
                I18N.CONSTANTS.targetDescription(),
                "db-targets.png",
                new DbPageState(DbTargetEditor.PAGE_ID, db.getId()));
        }

        // view.add("Cibles", "Définer les cibles pour les indicateurs.",
        // "db-targets",
        // new DbPageState(Pages.DatabaseTargets, db.getId()));
    }

    @Override
    public PageId getPageId() {
        return PAGE_ID;
    }

    @Override
    public Object getWidget() {
        return view;
    }

    @Override
    public void requestToNavigateAway(PageState place,
        NavigationCallback callback) {
        callback.onDecided(true);
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    @Override
    public boolean navigate(PageState place) {
        return false;
    }

    @Override
    public void shutdown() {

    }
}
