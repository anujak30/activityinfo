package org.activityinfo.client.offline.capability;

import org.activityinfo.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.gears.client.Factory;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * Utilities for Gears-based offline capability profiles.
 *
 */
public abstract class GearsCapabilityProfile extends OfflineCapabilityProfile {
	

	
	protected final boolean isGearsInstalled() {
		return Factory.getInstance() != null;
	}
	
	@Override
	public final boolean isOfflineModeSupported() {
		return isGearsInstalled();
	}


	@Override
	public final void acquirePermission(AsyncCallback<Void> callback) {
		Log.trace("GearsCapabilityProfile: acquiring permissions...");
		try {
			if(!isGearsInstalled()) {
				callback.onFailure(new UnsupportedOperationException("Gears is not installed"));
			} else if(acquirePermissions()) {
				callback.onSuccess(null);
			} else {
				callback.onFailure(new PermissionRefusedException());
			}
		} catch(Exception e) {
			callback.onFailure(e);
		}
	}


	@Override
	public final boolean hasPermission() {
		if(!isGearsInstalled()) {
			throw new UnsupportedOperationException("Gears is not installed");
		}
		return Factory.getInstance().hasPermission();
	}


	private boolean acquirePermissions() {
		return Factory.getInstance().getPermission("ActivityInfo", 
				GWT.getModuleBaseURL() + "desktopicons/64x64.png");
	}
}
