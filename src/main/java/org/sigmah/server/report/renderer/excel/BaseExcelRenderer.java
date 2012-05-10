/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.excel;

import java.util.List;

import org.activityinfo.shared.report.content.FilterDescription;
import org.activityinfo.shared.report.model.ReportElement;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public abstract class BaseExcelRenderer<ElementT extends ReportElement> {

	protected final ElementT element;
	protected final Workbook book;
	protected final Sheet sheet;
	protected final CreationHelper factory;
	
	protected int rowIndex;

	public BaseExcelRenderer(Workbook book, ElementT element ) {

		this.element = element;
		this.book = book;
		this.factory = book.getCreationHelper();
		this.sheet = book.createSheet(composeSheetName());
		
		/* Create title line */
		
		Row titleRow = sheet.createRow(0);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue(factory.createRichTextString(element.getTitle()));
		
		/* Create filter descriptors */
		
		List<FilterDescription> descs = generateFilterDescriptions();
		
		rowIndex = 2;
		
		for(FilterDescription desc : descs ) {
			
			Row filterRow = sheet.createRow(rowIndex++);
			Cell filterCell = filterRow.createCell(0);
			
			filterCell.setCellValue(factory.createRichTextString(desc.joinLabels(", ")));			
		}
		
		rowIndex ++; 

		generate();
	}

    public abstract List<FilterDescription> generateFilterDescriptions();
	
	
	public String composeSheetName() {
		if(element.getSheetTitle() != null) {
			return element.getSheetTitle();
		} else if(element.getTitle() != null) {
			return element.getTitle();
		} else {
			return "Sheet" + (book.getNumberOfSheets()+1);
		}
	}
	
	
	public void generate() {
		
		
	}
	
	protected Font createBaseFont() {
		Font font = book.createFont();
		
		return font;
	}
	
}
