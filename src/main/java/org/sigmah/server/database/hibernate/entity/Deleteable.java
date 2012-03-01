/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.database.hibernate.entity;

/**
 * 
 * @author Alex Bertram
 *
 */
public interface Deleteable {

	public void delete();
	
	public boolean isDeleted();

}
