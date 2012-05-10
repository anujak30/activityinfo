/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.report.content;

public class YearCategory implements DimensionCategory {

    private int year;

    /**
     * Required for GWT Serialization
     */
    private YearCategory() {

    }

    public YearCategory(int year) {
        this.year = year;
    }

    @Override
    public Comparable getSortKey() {
        return year;
    }

    public int getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        YearCategory that = (YearCategory) o;

        if (year != that.year) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return year;
    }

    @Override
    public String toString() {
        return "YearCategory{" + year + '}';
    }

	@Override
	public String getLabel() {
		return Integer.toString(year);
	}
}
