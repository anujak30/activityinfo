/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.authentication;

import java.util.Collection;

import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.exception.InvalidAuthTokenException;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Cookies;
import com.google.inject.Provider;

/**
 * Supplies user <code>Authentication</code> information from
 * the <code>authToken</code> and <code>email</code> cookies, or
 * failing that, from the <code>UserInfo</code> dictionary.
 *
 * @author Alex Bertram
 */
public class ClientSideAuthProvider implements Provider<AuthenticatedUser> {

    public AuthenticatedUser get() {

    	String authToken = Cookies.getCookie(AuthenticatedUser.AUTH_TOKEN_COOKIE);
    	String userId = Cookies.getCookie(AuthenticatedUser.USER_ID_COOKIE) ;
    	String email = Cookies.getCookie(AuthenticatedUser.EMAIL_COOKIE);
    	
        if (authToken != null && userId != null && email != null) {

            return new AuthenticatedUser(
            		authToken,
            		Integer.parseInt(userId),
            		email.replaceAll("\"", ""),
                    currentLocale());

        }

       	throw new InvalidAuthTokenException("Request is not authenticated");
    }
    
    private String currentLocale() {
    	return LocaleInfo.getCurrentLocale().getLocaleName();
    }
}
