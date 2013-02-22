

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


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


/**
 * 
 * @author Alex Bertram
 *
 */
@Entity
@org.hibernate.annotations.Filter(
		name="hideDeleted",
		condition="(IndicatorId not in (select i.IndicatorId from Indicator i where i.dateDeleted is not null))"
	)
public class IndicatorValue implements java.io.Serializable {

	private IndicatorValueId id;
	private Indicator indicator;
	private ReportingPeriod reportingPeriod;
	private Double value;

	public IndicatorValue() {
	}

	public IndicatorValue(IndicatorValueId id, Indicator indicator,
			ReportingPeriod reportingPeriod) {
		this.id = id;
		this.indicator = indicator;
		this.reportingPeriod = reportingPeriod;
	}

	public IndicatorValue(IndicatorValueId id, Indicator indicator,
			ReportingPeriod reportingPeriod, Double value) {
		this.id = id;
		this.indicator = indicator;
		this.reportingPeriod = reportingPeriod;
		this.value = value;
	}

	public IndicatorValue(ReportingPeriod period, Indicator indicator,
			double value) {
		
		this.id = new IndicatorValueId(period.getId(), indicator.getId());
		this.indicator = indicator;
		this.reportingPeriod = period;
		this.value = value;
	}


    @EmbeddedId
	@AttributeOverrides( {
		@AttributeOverride(name = "reportingPeriodId", column = @Column(name = "ReportingPeriodId", nullable = false)),
		@AttributeOverride(name = "indicatorId", column = @Column(name = "IndicatorId", nullable = false)) })
		public IndicatorValueId getId() {
		return this.id;
	}

	public void setId(IndicatorValueId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IndicatorId", nullable = false, insertable = false, updatable = false)
	public Indicator getIndicator() {
		return this.indicator;
	}

	public void setIndicator(Indicator indicator) {
		this.indicator = indicator;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ReportingPeriodId", nullable = false, insertable = false, updatable = false)
	public ReportingPeriod getReportingPeriod() {
		return this.reportingPeriod;
	}

	public void setReportingPeriod(ReportingPeriod reportingPeriod) {
		this.reportingPeriod = reportingPeriod;
	}

	@Column(name = "Value", precision = 15, scale = 0, nullable=false)
	public Double getValue() {
		return this.value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object other) {
		if(this == other) {
            return true;
        }
		if(other == null) {
            return false;
        }
		if(!(other instanceof IndicatorValue)) {
            return false;
        }

		IndicatorValue that = (IndicatorValue)other;
		return this.getId().equals(that.getId());
	}

	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}

}