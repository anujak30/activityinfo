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

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * @author Alex Bertram
 * 
 */
@Embeddable
public class AttributeValueId implements java.io.Serializable {

    private int siteId;
    private int attributeId;

    public AttributeValueId() {
    }

    public AttributeValueId(int siteId, int attributeId) {
        this.siteId = siteId;
        this.attributeId = attributeId;
    }

    @Column(name = "SiteId", nullable = false)
    public int getSiteId() {
        return this.siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    @Column(name = "AttributeId", nullable = false)
    public int getAttributeId() {
        return this.attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    @Override
    public boolean equals(Object other) {
        if ((this == other)) {
            return true;
        }
        if ((other == null)) {
            return false;
        }
        if (!(other instanceof AttributeValueId)) {
            return false;
        }
        AttributeValueId castOther = (AttributeValueId) other;

        return (this.getSiteId() == castOther.getSiteId())
            && (this.getAttributeId() == castOther.getAttributeId());
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getSiteId();
        result = 37 * result + this.getAttributeId();
        return result;
    }

}