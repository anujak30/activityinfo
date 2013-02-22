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
public class IndicatorValueId implements java.io.Serializable {

    private int reportingPeriodId;
    private int indicatorId;

    public IndicatorValueId() {
    }

    public IndicatorValueId(int reportingPeriodId, int indicatorId) {
        this.reportingPeriodId = reportingPeriodId;
        this.indicatorId = indicatorId;
    }

    @Column(name = "ReportingPeriodId", nullable = false)
    public int getReportingPeriodId() {
        return this.reportingPeriodId;
    }

    public void setReportingPeriodId(int reportingPeriodId) {
        this.reportingPeriodId = reportingPeriodId;
    }

    @Column(name = "IndicatorId", nullable = false)
    public int getIndicatorId() {
        return this.indicatorId;
    }

    public void setIndicatorId(int indicatorId) {
        this.indicatorId = indicatorId;
    }

    @Override
    public boolean equals(Object other) {
        if ((this == other)) {
            return true;
        }
        if ((other == null)) {
            return false;
        }
        if (!(other instanceof IndicatorValueId)) {
            return false;
        }
        IndicatorValueId castOther = (IndicatorValueId) other;

        return (this.getReportingPeriodId() == castOther.getReportingPeriodId())
            && (this.getIndicatorId() == castOther.getIndicatorId());
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getReportingPeriodId();
        result = 37 * result + this.getIndicatorId();
        return result;
    }

}