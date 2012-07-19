/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.dispatch.remote;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;
import static org.junit.internal.matchers.IsCollectionContaining.hasItem;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.activityinfo.client.dispatch.remote.CommandRequest;
import org.activityinfo.shared.command.GetSchema;
import org.junit.Test;

import com.bedatadriven.rebar.async.NullCallback;

public class CommandRequestTest {

    @Test
    public void equalCommandsShouldBeMerged() {

        assumeThat(new GetSchema(), is(equalTo(new GetSchema())));

        CommandRequest firstCommand = new CommandRequest(new GetSchema(),
                new NullCallback());
        List<CommandRequest> pending = Collections.singletonList(firstCommand);

        CommandRequest secondRequest = new CommandRequest(new GetSchema(), new NullCallback());

        boolean merged = secondRequest.mergeSuccessfulInto(pending);

        assertThat("merged", merged, is(true));
        assertThat(firstCommand.getCallbacks(), hasItem(first(secondRequest.getCallbacks())));

    }

    private <T> T first(Collection<T> items) {
        return items.iterator().next();
    }

}
