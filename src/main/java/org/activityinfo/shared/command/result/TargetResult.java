package org.activityinfo.shared.command.result;

import java.util.Arrays;
import java.util.List;

import org.activityinfo.shared.dto.TargetDTO;

public class TargetResult extends ListResult<TargetDTO> implements
		CommandResult {

	public TargetResult() {

	}

	public TargetResult(List<TargetDTO> targets) {
		super(targets);
	}

	public TargetResult(TargetDTO... targets) {
		super(Arrays.asList(targets));
	}
}
