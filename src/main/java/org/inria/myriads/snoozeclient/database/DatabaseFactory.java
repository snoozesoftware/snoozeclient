/**
 * Copyright (C) 2010-2013 Eugen Feller, INRIA <eugen.feller@inria.fr>
 *
 * This file is part of Snooze, a scalable, autonomic, and
 * energy-aware virtual machine (VM) management framework.
 *
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 */
package org.inria.myriads.snoozeclient.database;

import org.inria.myriads.snoozeclient.database.api.ClientRepository;
import org.inria.myriads.snoozeclient.database.api.impl.ClientXMLRepository;

/**
 * Database factory.
 * 
 * @author Eugen Feller
 */
public final class DatabaseFactory 
{
    /** Hide constructor. */
    private DatabaseFactory()
    {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Creates a new client repository.
     * 
     * @param databaseFileName      The database file name
     * @return                      The client repository
     * @throws Exception            The exception
     */
    public static ClientRepository newClientRepository(String databaseFileName) 
        throws Exception
    {
        return new ClientXMLRepository(databaseFileName);
    }
}
