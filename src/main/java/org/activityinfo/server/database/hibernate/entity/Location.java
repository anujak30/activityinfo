package org.activityinfo.server.database.hibernate.entity;

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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * 
 * @author Alex Bertram
 * 
 */
@Entity
public class Location implements java.io.Serializable {

    private int id;
    private LocationType locationType;
    private String locationGuid;
    private Double x;
    private Double y;
    private String name;
    private String axe;
    private Set<Site> sites = new HashSet<Site>(0);
    private Set<AdminEntity> adminEntities = new HashSet<AdminEntity>(0);
    private long timeEdited;

    public Location() {
    }

    @Id
    @Column(name = "LocationID", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LocationTypeID", nullable = false)
    public LocationType getLocationType() {
        return this.locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    @Column(name = "LocationGuid", length = 36)
    public String getLocationGuid() {
        return this.locationGuid;
    }

    public void setLocationGuid(String locationGuid) {
        this.locationGuid = locationGuid;
    }

    @Column(name = "X", precision = 7, scale = 0)
    public Double getX() {
        return this.x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    @Column(name = "Y", precision = 7, scale = 0)
    public Double getY() {
        return this.y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    @Column(name = "Name", nullable = false, length = 50)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "Axe", length = 50)
    public String getAxe() {
        return this.axe;
    }

    public void setAxe(String axe) {
        this.axe = axe;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "location")
    public Set<Site> getSites() {
        return this.sites;
    }

    public void setSites(Set<Site> sites) {
        this.sites = sites;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "LocationAdminLink",
        joinColumns = {
            @JoinColumn(name = "LocationId", nullable = false, updatable = false) },
        inverseJoinColumns = {
            @JoinColumn(name = "AdminEntityId", nullable = false, updatable = false) })
    public Set<AdminEntity> getAdminEntities() {
        return this.adminEntities;
    }

    public void setAdminEntities(Set<AdminEntity> adminEntities) {
        this.adminEntities = adminEntities;
    }

    public void setAdminEntity(int levelId, AdminEntity newEntity) {

        for (AdminEntity entity : getAdminEntities()) {
            if (entity.getLevel().getId() == levelId) {

                if (newEntity == null) {
                    getAdminEntities().remove(entity);
                } else if (newEntity.getId() != entity.getId()) {
                    getAdminEntities().remove(entity);
                    getAdminEntities().add(newEntity);
                }

                return;
            }
        }

        if (newEntity != null) {
            getAdminEntities().add(newEntity);
        }
    }

    public long getTimeEdited() {
        return timeEdited;
    }

    public void setTimeEdited(long timeEdited) {
        this.timeEdited = timeEdited;
    }

    public void setTimeEdited(Date date) {
        this.timeEdited = date.getTime();
    }

    @PrePersist
    public void onCreate() {
        Date now = new Date();
        setTimeEdited(now.getTime());
    }

    @PreUpdate
    public void onUpdate() {
        Date now = new Date();
        setTimeEdited(now.getTime());
    }
}
