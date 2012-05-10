/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.report.model;



/**
 * Models a data dimension that can be grouped by week, month, quarter, year, etc
 * 
 * @author Alex Bertram
 *
 */
public class DateDimension extends Dimension {
	
	private DateUnit unit;
	private String format;
	
	public DateDimension() {
		
	}
	
	public DateDimension(String name, int id, DateUnit unit, String format) {
		super(DimensionType.Date);
		this.unit = unit;
		this.format = format;
		set("caption", name);
		set("id", "dim_date_" + id);
	}
	
	public DateDimension(DateUnit unit) {
		super(DimensionType.Date);
		this.unit = unit;
	}
	
	public String getId() {
		return get("id");
	}

	public DateUnit getUnit() {
		return unit;
	}

    public void setUnit(DateUnit unit) {
        this.unit = unit;
    }

    @Override
	public boolean equals(Object other) {
		if(this==other) {
            return true;
        }
		if(other==null) {
            return false;
        }
		if(!(other instanceof DateDimension)) {
            return false;
        }

		DateDimension that = (DateDimension)other;
		return this.unit == that.unit;
	}
    
    @Override
	public int hashCode() {
    	return unit.hashCode();
    }

	/**
	 * 
	 * @return The format string that should be applied to category values in this
	 * dimension. See {@link java.text.SimpleDateFormat#applyPattern(String)} for details on
	 * the grammar of this format string.
	 */
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
}
