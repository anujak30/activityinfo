package org.activityinfo.shared.report.model;

import java.util.Collections;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import org.activityinfo.shared.report.content.NullContent;

/**
 * 
 * Defines an external, static image to be included in the report
 * 
 */
public class ImageReportElement extends ReportElement<NullContent>{
	
	private String url;

	
	/**
	 * 
	 * @return the URL of the image to include in the report
	 */
	@XmlElement
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public Set<Integer> getIndicators() {
		return Collections.emptySet();
	}
	
	
}
