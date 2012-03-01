/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.report.content.EntityCategory;
import org.sigmah.shared.report.content.FilterDescription;
import org.sigmah.shared.report.content.PivotContent;
import org.sigmah.shared.report.content.PivotTableData;
import org.sigmah.shared.report.model.AdminDimension;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.PivotTableReportElement;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class DummyPivotTableData2 {



    public Dimension partnerDim = new Dimension(DimensionType.Partner);
    public Dimension provinceDim = new AdminDimension(1);
    public List<Dimension> rowDims = new ArrayList<Dimension>();
    public List<Dimension> colDims = new ArrayList<Dimension>();


    public PivotTableData.Axis[] leafRows = new PivotTableData.Axis[4];
    public PivotTableData table = new PivotTableData();
    public PivotTableData.Axis row1 = table.getRootRow().addChild(partnerDim, new EntityCategory(1, "AVSI"), "AVSI", null);
    public PivotTableData.Axis row2 = table.getRootRow().addChild(partnerDim, new EntityCategory(1, "NRC"), "NRC", null);

    public DummyPivotTableData2() {

        rowDims.add(partnerDim);
        rowDims.add(provinceDim);

        leafRows[0] = row1.addChild(provinceDim, new EntityCategory(61, "Nord Kivu"), "Nord", null);
        leafRows[1] = row1.addChild(provinceDim, new EntityCategory(62, "Sud Kivu"), "Sud Kivu", null);

        leafRows[2] = row2.addChild(provinceDim, new EntityCategory(61, "Nord Kivu"), "Nord", null);
        leafRows[3] = row2.addChild(provinceDim, new EntityCategory(62, "Sud Kivu"), "Sud Kivu", null);

        for(int i=0; i!= leafRows.length; ++i) {

            leafRows[i].setValue(table.getRootColumn(), (double)((i+1) * 100));

        }

    }


    public PivotTableReportElement testElement() {
        PivotTableReportElement element = new PivotTableReportElement();
        element.setTitle("Foobar 1612");
        element.setRowDimensions(rowDims);
        element.setColumnDimensions(colDims);
        element.setContent(new PivotContent(table, new ArrayList<FilterDescription>()));

        return element;
    }


}