

package org.activityinfo.test;

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

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class AssertUtils {

    public static <T> void assertSorted(String name, Collection<T> collection,
                                        Comparator<T> comparator) {
        if (collection.size() <= 1) {
            return;
        }

        Iterator<T> it = collection.iterator();
        T last = it.next();

        while (it.hasNext()) {
            T next = it.next();
            if (comparator.compare(last, next) > 0) {
                throw new AssertionError("The collection '" + name + "' is not sorted, " +
                        last.toString() + " > " + next.toString());
            }
            last = next;
        }
    }

}
