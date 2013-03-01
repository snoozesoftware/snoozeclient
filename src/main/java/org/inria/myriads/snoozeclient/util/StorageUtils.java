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
package org.inria.myriads.snoozeclient.util;

import java.io.File;

import org.inria.myriads.snoozeclient.globals.Globals;
import org.inria.myriads.snoozecommon.guard.Guard;

/**
 * Storage utility.
 * 
 * @author Eugen Feller
 */
public final class StorageUtils 
{        
    /**
     * Hide the constructor.
     */
    private StorageUtils() 
    {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Creates a directory.
     * 
     * @param directoryName     The directory name
     * @return                  true if directory was created, false otherwise
     */
    private static boolean createDirectory(String directoryName)
    {
        Guard.check(directoryName);
        File file = new File(directoryName);
        return file.mkdir();
    }
    
    /**
     * Creates the repository storage directory.
     * 
     * @return  true if created, false otherwise
     */
    public static boolean createRepositoryDirectory() 
    {
        boolean isCreated = createDirectory(Globals.REPOSITORY_STORAGE_DIRECTORY);           
        return isCreated;
    }
    
    /**
     * Returns the database file name.
     * 
     * @return  The database file name
     */
    public static String getDatabaseFileName()
    {
        String fileName = Globals.REPOSITORY_STORAGE_DIRECTORY + Globals.REPOSITORT_FILE_NAME;
        return fileName;
    }
}
