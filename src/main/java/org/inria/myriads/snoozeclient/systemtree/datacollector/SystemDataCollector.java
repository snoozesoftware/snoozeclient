/**
 * Copyright (C) 2010-2012 Eugen Feller, INRIA <eugen.feller@inria.fr>
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
package org.inria.myriads.snoozeclient.systemtree.datacollector;

import org.inria.myriads.snoozecommon.communication.NetworkAddress;
import org.inria.myriads.snoozecommon.communication.groupmanager.repository.GroupLeaderRepositoryInformation;
import org.inria.myriads.snoozecommon.communication.groupmanager.repository.GroupManagerRepositoryInformation;
import org.inria.myriads.snoozecommon.communication.rest.CommunicatorFactory;
import org.inria.myriads.snoozecommon.communication.rest.api.GroupManagerAPI;
import org.inria.myriads.snoozecommon.guard.Guard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Collects system data.
 * 
 * @author Eugen Feller
 */
public final class SystemDataCollector 
{
    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(SystemDataCollector.class);    

    /** Hide constructor. */
    private SystemDataCollector()
    {
        throw new UnsupportedOperationException();
    }
    
    
    
    /**
     * Returns the current group leader repository information.
     * 
     * @param groupLeaderAddress        The group leader address
     * @param numberOfBacklogEntries    The number of backlog entries
     * @return                          The group leader repository information
     */
    public static GroupLeaderRepositoryInformation 
        getGroupLeaderRepositoryInformation(NetworkAddress groupLeaderAddress, int numberOfBacklogEntries)
    {
        Guard.check(groupLeaderAddress);
        log_.info(String.format("Getting group leader repository information"));
        GroupManagerAPI groupLeaderCommunicator = 
                CommunicatorFactory.newGroupManagerCommunicator(groupLeaderAddress); 
        GroupLeaderRepositoryInformation information = 
            groupLeaderCommunicator.getGroupLeaderRepositoryInformation(numberOfBacklogEntries);
        return information;
    }
    
    /**
     * Returns the current  group manager repository information.
     * 
     * @param groupManagerAddress       The group manager address
     * @param numberOfBacklogEntries    The number of backlog entries
     * @return                          The group manager repository information
     */
    public static GroupManagerRepositoryInformation 
        getGroupManagerRepositoryInformations(NetworkAddress groupManagerAddress, int numberOfBacklogEntries)
    {
        Guard.check(groupManagerAddress);
        log_.info(String.format("Getting group manager repository informations"));
        
        GroupManagerAPI groupManagerCommunicator = 
                CommunicatorFactory.newGroupManagerCommunicator(groupManagerAddress); 
        GroupManagerRepositoryInformation information = 
                groupManagerCommunicator.getGroupManagerRepositoryInformation(numberOfBacklogEntries);
        return information;        
    }
}
