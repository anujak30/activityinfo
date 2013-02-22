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

// Generated Apr 9, 2009 7:58:20 AM by Hibernate Tools 3.2.2.GA

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * @author Alex Bertram
 */
@Entity
public class Authentication implements java.io.Serializable {

    private String id;
    private User user;
    private Date dateCreated;
    private Date dateLastActive;

    public Authentication() {

    }

    /**
     * Creates a new session object for the given user, with a secure session id
     * and starting at the current time
     * 
     * @param user
     */
    public Authentication(User user) {
        // setId(SecureTokenGenerator.generate());
        this.user = user;
        this.dateCreated = new Date();
        this.dateLastActive = new Date();
    }

    /**
     * Gets the secure id of this Authentication, which is a 128-bit random
     * number represented as a 32-character hexadecimal string.
     * 
     * @return the id of this authentication
     */
    @Id
    @Column(name = "AuthToken", unique = true, nullable = false, length = 32)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SecureSequenceGenerator")
    @org.hibernate.annotations.GenericGenerator(name = "SecureSequenceGenerator",
        strategy = "org.activityinfo.server.authentication.SecureSequenceGenerator")
    public String getId() {
        return this.id;
    }

    public void setId(String sessionId) {
        this.id = sessionId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserId", nullable = false)
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    public Date getDateCreated() {
        return this.dateCreated;
    }

    private void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    public Date getDateLastActive() {
        return this.dateLastActive;
    }

    public void setDateLastActive(Date dateLastActive) {
        this.dateLastActive = dateLastActive;
    }

    public long minutesSinceLastActivity() {
        return ((new Date()).getTime() - getDateLastActive().getTime()) / 1000 / 60;
    }

    @Transient
    public boolean isExpired() {
        // TODO: when do we invalidate tokens?
        // return minutesSinceLastActivity() > 30;
        return false;
    }

    public void setDateLastActive() {
        setDateLastActive(new Date());
    }
}
