/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.schedule;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.sigmah.server.database.hibernate.entity.ReportDefinition;
import org.sigmah.server.database.hibernate.entity.ReportSubscription;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.shared.report.model.DateRange;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.EmailDelivery;

/**
 * @author Alex Bertram
 */
public class ReportMailerTest {


    @Test
    public void testWeeklyCheck() {

        Report report = new Report();
        report.setFrequency(EmailDelivery.WEEKLY);
        report.setDay(0); // Sunday

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2009);
        cal.set(Calendar.MONTH, 9);
        cal.set(Calendar.DATE, 4);

        Assert.assertTrue("Sunday report goes out on Sunday", ReportMailerHelper.mailToday(cal.getTime(), report));

        cal.set(Calendar.DATE, 5);

        Assert.assertFalse("Sunday report does not out on Monday", ReportMailerHelper.mailToday(cal.getTime(), report));

        report.setDay(1);

        Assert.assertTrue("Monday report goes out on Monday", ReportMailerHelper.mailToday(cal.getTime(), report));
    }

    @Test
    public void testMonthly() {

        Report report = new Report();
        report.setFrequency(EmailDelivery.MONTHLY);
        report.setDay(11);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2009);
        cal.set(Calendar.MONTH, 3);
        cal.set(Calendar.DATE, 11);

        Assert.assertTrue("Monthly report scheduled for each the 11th goes out on the 11th",
                ReportMailerHelper.mailToday(cal.getTime(), report));


        cal.set(Calendar.DATE, 30);
    }

    @Test
    public void testLastDayOfMonth() {

        Report report = new Report();
        report.setFrequency(EmailDelivery.MONTHLY);
        report.setDay(Report.LAST_DAY_OF_MONTH);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2009);
        cal.set(Calendar.MONTH, 3);
        cal.set(Calendar.DATE, 30);

        Assert.assertTrue("Report goes out on 4-April",
                ReportMailerHelper.mailToday(cal.getTime(), report));

        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DATE, 31);

        Assert.assertTrue("Report goes out on 31-Jan",
                ReportMailerHelper.mailToday(cal.getTime(), report));

    }

    @Test
    public void testDateParameters() {

        Report report = new Report();
        report.setFrequency(EmailDelivery.MONTHLY);


        Calendar today = Calendar.getInstance();
        today.set(Calendar.DATE, 5);
        today.set(Calendar.MONTH, 0);
        today.set(Calendar.YEAR, 2009);

        DateRange dateRange = ReportMailerHelper.computeDateRange(report, today.getTime());

        Calendar month = Calendar.getInstance();
        month.setTime(dateRange.getMaxDate());

        Assert.assertEquals(11, month.get(Calendar.MONTH));
        Assert.assertEquals(31, month.get(Calendar.DATE));
        Assert.assertEquals(2008, month.get(Calendar.YEAR));

    }

    @Test
    public void testTextEmail() {

        User user = new User();
        user.setEmail("akbertram@gmail.com");
        user.setName("alex");
        user.setLocale("fr");

        ReportSubscription sub = new ReportSubscription();
        sub.setTemplate(new ReportDefinition());
        sub.getTemplate().setId(1);
        sub.setUser(user);

        Report report = new Report();
        report.setTitle("Rapport RRM Mensuelle");
        report.setFrequency(EmailDelivery.WEEKLY);
        report.setDay(1);

        String text = ReportMailerHelper.composeTextEmail(sub, report);

        System.out.println(text);

        Assert.assertTrue("user name is present", text.contains(user.getName()));

    }

}
