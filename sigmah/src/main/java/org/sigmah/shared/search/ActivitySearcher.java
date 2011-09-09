package org.sigmah.shared.search;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.domain.Activity;
import org.sigmah.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ActivitySearcher implements Searcher<Activity> {


	@Override
	public void search(List<String> searchTerms, SqlTransaction tx, final AsyncCallback<List<Integer>> callback) {
		SqlQuery.select("ActivityId")
				.from("Activity")
				.onlyWhere("Name")
				.likeMany(searchTerms)
				.or()
				.onlyWhere("Category")
				.likeMany(searchTerms)
				
				.execute(tx, new SqlResultCallback() {
					final List<Integer> activityIds = new ArrayList<Integer>();
					
					@Override
					public void onSuccess(SqlTransaction tx, SqlResultSet results) {
						for (SqlResultSetRow result : results.getRows()) {
							activityIds.add(result.getInt("ActivityId"));
						}
						callback.onSuccess(activityIds);
					}
				});
	}

	@Override
	public DimensionType getDimensionType() {
		return DimensionType.Activity;
	}
}