/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.report.model;

import java.util.HashMap;
import java.util.Map;

import org.activityinfo.shared.report.content.DimensionCategory;

@Deprecated
public class MapSymbol {

    private Map<Dimension, DimensionCategory> categories = new HashMap<Dimension, DimensionCategory>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MapSymbol that = (MapSymbol) o;

        if (categories != null ? !categories.equals(that.categories) : that.categories != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return categories != null ? categories.hashCode() : 0;
    }

    public void put(Dimension dimension, DimensionCategory category) {
        categories.put(dimension, category);
    }

    public DimensionCategory get(Dimension dimension) {
        return categories.get(dimension);
    }
}
