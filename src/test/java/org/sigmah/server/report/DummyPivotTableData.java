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
import org.sigmah.shared.report.content.YearCategory;
import org.sigmah.shared.report.model.AdminDimension;
import org.sigmah.shared.report.model.DateDimension;
import org.sigmah.shared.report.model.DateUnit;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.PivotTableReportElement;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class DummyPivotTableData {



    public Dimension partnerDim = new Dimension(DimensionType.Partner);
    public Dimension provinceDim = new AdminDimension(1);
    public List<Dimension> rowDims = new ArrayList<Dimension>();

    public Dimension yearDim = new DateDimension(DateUnit.YEAR);
    public Dimension indicatorDim = new DateDimension(DateUnit.YEAR);
    public List<Dimension> colDims = new ArrayList<Dimension>();


    public PivotTableData.Axis[] leafRows = new PivotTableData.Axis[4];
    public PivotTableData.Axis[] leafCols = new PivotTableData.Axis[5];
    public PivotTableData table = new PivotTableData();
    public PivotTableData.Axis row1 = table.getRootRow().addChild(partnerDim, new EntityCategory(1, "AVSI"), "AVSI", null);
    public PivotTableData.Axis row2 = table.getRootRow().addChild(partnerDim, new EntityCategory(1, "NRC"), "NRC", null);
    public PivotTableData.Axis col1 = table.getRootColumn().addChild(yearDim, new YearCategory(2007), "2007", null );
    public PivotTableData.Axis col2 = table.getRootColumn().addChild(yearDim, new YearCategory(2009), "2009", null );

    public DummyPivotTableData() {

        rowDims.add(partnerDim);
        rowDims.add(provinceDim);

        colDims.add(yearDim);
        colDims.add(indicatorDim);

        leafRows[0] = row1.addChild(provinceDim, new EntityCategory(61, "Nord Kivu"), "Nord", null);
        leafRows[1] = row1.addChild(provinceDim, new EntityCategory(62, "Sud Kivu"), "Sud Kivu", null);

        leafRows[2] = row2.addChild(provinceDim, new EntityCategory(61, "Nord Kivu"), "Nord", null);
        leafRows[3] = row2.addChild(provinceDim, new EntityCategory(62, "Sud Kivu"), "Sud Kivu", null);

        leafCols[0] = col1.addChild(indicatorDim, new EntityCategory(201, "NFI"), "NFI", null );
        leafCols[1] = col1.addChild(indicatorDim, new EntityCategory(202, "Bache"), "Bache", null );

        leafCols[2] = col2.addChild(indicatorDim, new EntityCategory(201, "NFI"), "NFI", null );
        leafCols[3] = col2.addChild(indicatorDim, new EntityCategory(202, "Bache"), "Bache", null );
        leafCols[4] = col2.addChild(indicatorDim, new EntityCategory(203, "Abri"), "Abri", null );

        for(int i=0; i!= leafRows.length; ++i) {
            for(int j=0; j!= leafCols.length; ++j) {
                leafRows[i].setValue(leafCols[j], (double)(i * (j+9) * 100));
            }
        }

    }


    public PivotTableReportElement Foobar1612Element() {
        PivotTableReportElement element = new PivotTableReportElement();
        element.setTitle("Foobar 1612");
        element.setRowDimensions(rowDims);
        element.setColumnDimensions(colDims);
        element.setContent(new PivotContent(table, new ArrayList<FilterDescription>()));
       
        return element;
    }


}
