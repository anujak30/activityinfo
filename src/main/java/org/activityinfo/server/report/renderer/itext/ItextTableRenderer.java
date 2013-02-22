package org.activityinfo.server.report.renderer.itext;

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

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.content.TableData;
import org.activityinfo.shared.report.model.TableColumn;
import org.activityinfo.shared.report.model.TableElement;

import com.google.inject.Inject;
import com.lowagie.text.Cell;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;

/**
 * Renders a {@link org.activityinfo.shared.report.model.TableElement} to an
 * iText Document.
 * 
 */
public class ItextTableRenderer implements ItextRenderer<TableElement> {

    private final ItextMapRenderer mapRenderer;

    @Inject
    public ItextTableRenderer(ItextMapRenderer mapRenderer) {
        this.mapRenderer = mapRenderer;
    }

    @Override
    public void render(DocWriter writer, Document document, TableElement element)
        throws DocumentException {
        document.add(ThemeHelper.elementTitle(element.getTitle()));
        ItextRendererHelper.addFilterDescription(document, element.getContent()
            .getFilterDescriptions());
        ItextRendererHelper.addDateFilterDescription(document, element
            .getFilter().getDateRange());
        TableData data = element.getContent().getData();

        if (data.isEmpty()) {
            renderEmptyText(document);

        } else {
            if (element.getMap() != null) {
                mapRenderer.renderMap(writer, element.getMap(), document);
            }
            renderTable(document, data);
        }
    }

    private void renderEmptyText(Document document) throws DocumentException {
        document.add(new Paragraph("Aucune Données")); // TODO: i18n
    }

    private void renderTable(Document document, TableData data)
        throws DocumentException {
        int colDepth = data.getRootColumn().getDepth();
        List<TableColumn> colLeaves = data.getRootColumn().getLeaves();
        int colBreadth = colLeaves.size();

        Table table = new Table(colBreadth, 1);
        table.setUseVariableBorders(true);
        table.setWidth(100.0f);
        table.setBorderWidth(0);

        // first write the column headers

        for (int depth = 1; depth <= colDepth; ++depth) {
            List<TableColumn> columns = data.getRootColumn()
                .getDescendantsAtDepth(depth);
            for (TableColumn column : columns) {
                Cell cell = ThemeHelper.columnHeaderCell(column.getLabel(),
                    column.isLeaf(),
                    computeHAlign(column));
                cell.setColspan(Math.max(1, column.getChildren().size()));
                cell.setRowspan(colDepth - depth - column.getDepth() + 1);
                table.addCell(cell);
            }
        }
        table.endHeaders();

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        numberFormat.setGroupingUsed(true);

        for (SiteDTO row : data.getRows()) {
            for (TableColumn column : colLeaves) {

                Object value = row.get(column.getSitePropertyName());

                String label = "";
                if (value instanceof Date) {
                    label = dateFormat.format(value);
                } else if (value instanceof Number) {
                    label = numberFormat.format(value);
                } else if (value != null) {
                    label = value.toString();
                }

                table.addCell(ThemeHelper.bodyCell(label, false, 0, true,
                    computeHAlign(column)));
            }
        }
        document.add(table);
    }

    protected int computeHAlign(TableColumn column) {
        if (!column.isLeaf()) {
            return Cell.ALIGN_CENTER;
        } else if ("indicator".equals(column.getProperty())) {
            return Cell.ALIGN_RIGHT;
        } else if ("map".equals(column.getProperty())) {
            return Cell.ALIGN_CENTER;
        } else {
            return Cell.ALIGN_LEFT;
        }
    }
}
