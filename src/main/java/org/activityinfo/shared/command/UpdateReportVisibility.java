package org.activityinfo.shared.command;

import java.util.List;

import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.ReportVisibilityDTO;

public class UpdateReportVisibility implements MutatingCommand<VoidResult> {
	private int reportId;
	private List<ReportVisibilityDTO> list;
	
	public UpdateReportVisibility() {
	}

	public UpdateReportVisibility(int reportId, List<ReportVisibilityDTO> list) {
		super();
		this.reportId = reportId;
		this.list = list;
	}

	public int getReportId() {
		return reportId;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

	public List<ReportVisibilityDTO> getList() {
		return list;
	}

	public void setList(List<ReportVisibilityDTO> list) {
		this.list = list;
	}
	
	
}
