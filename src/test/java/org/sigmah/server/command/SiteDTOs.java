package org.sigmah.server.command;

import java.util.GregorianCalendar;

import org.activityinfo.client.offline.command.handler.KeyGenerator;
import org.activityinfo.shared.dto.PartnerDTO;
import org.activityinfo.shared.dto.ProjectDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.junit.Assert;

public class SiteDTOs {

	public static SiteDTO newSite() {
		SiteDTO newSite = new SiteDTO();
	
		newSite.setId(new KeyGenerator().generateInt());
	    newSite.setActivityId(1);
	    newSite.setLocationId(1);
	    newSite.setPartner(new PartnerDTO(1, "Foobar"));
	    newSite.setReportingPeriodId(new KeyGenerator().generateInt());
	    newSite.setDate1((new GregorianCalendar(2008, 12, 1)).getTime());
	    newSite.setDate2((new GregorianCalendar(2009, 1, 3)).getTime());
	    newSite.setIndicatorValue(1, 996.0);
	    newSite.setIndicatorValue(2, null);
	    newSite.setAttributeValue(1, true);
	    newSite.setAttributeValue(2, false);
	    newSite.setComments("huba huba");
	    newSite.setProject(new ProjectDTO(1, "WoeiProject"));
		return newSite;
	}

	public static void validateNewSite(SiteDTO secondRead) {
		SiteDTO newSite = newSite();
		Assert.assertEquals("site.location.name", LocationDTOs.newLocation().getName(), secondRead.getLocationName());
		Assert.assertEquals("site.location.axe", LocationDTOs.newLocation().getAxe(), secondRead.getLocationAxe());

	    Assert.assertEquals("site.attribute[1]", true, secondRead.getAttributeValue(1));
	    Assert.assertEquals("site.reportingPeriod[0].indicatorValue[0]", 996.0, secondRead.getIndicatorValue(1), 0.1);
	    Assert.assertEquals("site.comments", newSite.getComments(), secondRead.getComments());
	    Assert.assertEquals("site.partner", newSite.getPartner().getId(), secondRead.getPartner().getId());
	}

}
