/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.test;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

@RunWith(InjectionSupport.class)
@Modules(MockHibernateModule.class)
public class TestScopeTest {

    @Inject
    private EntityManager em1;


    @Inject
    private EntityManager em2;

    @Test
    public void test() {
        Assert.assertSame(em1, em2);
    }

}
