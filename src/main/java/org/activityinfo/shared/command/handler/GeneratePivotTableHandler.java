package org.activityinfo.shared.command.handler;

import org.activityinfo.shared.command.GeneratePivotTable;
import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.command.PivotSites.PivotResult;
import org.activityinfo.shared.command.handler.pivot.PivotTableDataBuilder;
import org.activityinfo.shared.report.content.PivotContent;
import org.activityinfo.shared.report.content.PivotTableData;
import org.activityinfo.shared.report.model.PivotTableReportElement;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class GeneratePivotTableHandler implements CommandHandlerAsync<GeneratePivotTable, PivotContent> {



	@Override
	public void execute(final GeneratePivotTable command, ExecutionContext context,
			final AsyncCallback<PivotContent> callback) {
		
		context.execute(new PivotSites(command.getModel().allDimensions(), command.getModel().getFilter()), new AsyncCallback<PivotSites.PivotResult>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(PivotResult result) {
				callback.onSuccess(buildResult(command.getModel(), result));
			}
		});
	}

	protected PivotContent buildResult(PivotTableReportElement model,
			PivotResult result) {
	
		PivotTableDataBuilder builder = new PivotTableDataBuilder();
		PivotTableData data = builder.build(model, model.getRowDimensions(), model.getColumnDimensions(), result.getBuckets());

        PivotContent content = new PivotContent();
        content.setEffectiveFilter(model.getFilter());
		content.setData(data);

		return content;
	}
}
