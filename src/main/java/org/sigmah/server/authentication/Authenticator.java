/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.authentication;

import org.sigmah.server.database.hibernate.entity.User;

/**
 * Service interface which provides validation of user passwords.
 * The only current implementation checks the password against the
 * database but this is intended to be an extensibility point for other
 * methods.
 */
public interface Authenticator {
    boolean check(User user, String plaintextPassword);
    
    
}
