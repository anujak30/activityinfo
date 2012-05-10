package org.activityinfo.client.page.report;

import java.util.Arrays;
import java.util.List;

import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.PageStateParser;
import org.activityinfo.client.page.app.Section;

public class ReportDesignPageState implements PageState {

	private int reportId;

	public ReportDesignPageState() {

	}

	public ReportDesignPageState(int reportId) {
		this.reportId = reportId;
	}

	@Override
	public PageId getPageId() {
		return ReportDesignPage.PAGE_ID;
	}

	@Override
	public String serializeAsHistoryToken() {
		return Integer.toString(reportId);
	}

	@Override
	public List<PageId> getEnclosingFrames() {
		return Arrays.asList(ReportDesignPage.PAGE_ID);
	}

	public int getReportId() {
		return reportId;
	}

	public static class Parser implements PageStateParser {
		@Override
		public PageState parse(String token) {
			return new ReportDesignPageState(Integer.parseInt(token));
		}
	}

	@Override
	public Section getSection() {
		return Section.ANALYSIS;
	}
}
