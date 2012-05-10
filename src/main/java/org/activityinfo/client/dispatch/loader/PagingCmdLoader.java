/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.dispatch.loader;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.shared.command.PagingGetCommand;
import org.activityinfo.shared.command.result.PagingResult;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoader;

/**
 * Command-pattern-based implementation of the GXT {@link com.extjs.gxt.ui.client.data.PagingLoader}
 *
 * @deprecated Use standard GXT loader with custom {@link com.extjs.gxt.ui.client.data.DataProxy}
 * @param <ResultT>
 */
public class PagingCmdLoader<ResultT extends PagingResult<?>>
        extends AbstractListCmdLoader<ResultT, PagingGetCommand<ResultT>>
        implements PagingLoader<ResultT> {

    public PagingCmdLoader(Dispatcher service) {
        super(service);
    }

    private int offset = 0;
    private int limit = -1;
    private int totalCount;


    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public int getTotalCount() {
        return totalCount;
    }

    @Override
    public void load(int offset, int pageSize) {
        this.offset = offset;
        this.limit = pageSize;

        load(null);
    }

    @Override
    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public boolean load(Object loadConfig) {
        if (loadConfig instanceof PagingLoadConfig) {
            PagingLoadConfig plConfig = (PagingLoadConfig) loadConfig;

            setOffset(plConfig.getOffset());
            setLimit(plConfig.getLimit());

        }

        return super.load(loadConfig);
    }

    @Override
    protected void prepareCommand(PagingGetCommand<ResultT> cmd) {
        super.prepareCommand(cmd);
        cmd.setLimit(limit);
        cmd.setOffset(offset);
    }

}
