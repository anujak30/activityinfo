/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator;

import org.sigmah.server.command.DispatcherSync;
import org.sigmah.shared.report.model.ReportElement;

import com.google.inject.Inject;

/**
 * This is the base class for generators of element that
 * take the essential form of a list of sites. For example,
 * we have the table (grid) of sites, narrative description
 * of sites, or a map of sites.
 *
 * @author Alex Bertram
 * @param <ElementT>
 */
public abstract class ListGenerator<ElementT extends ReportElement>
        extends BaseGenerator<ElementT> {


    @Inject
    public ListGenerator(DispatcherSync dispatcher) {
        super(dispatcher);
    }

}
