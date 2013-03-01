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
package org.inria.myriads.snoozeclient.discovery;

import org.inria.myriads.snoozecommon.communication.NetworkAddress;
import org.inria.myriads.snoozecommon.communication.rest.CommunicatorFactory;
import org.inria.myriads.snoozecommon.communication.rest.api.GroupManagerAPI;
import org.inria.myriads.snoozecommon.communication.virtualcluster.discovery.VirtualMachineDiscoveryResponse;
import org.inria.myriads.snoozecommon.communication.virtualcluster.submission.VirtualMachineLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Virtual machine discovery logic.
 * 
 * @author Eugen Feller
 */
public final class VirtualMachineDiscovery 
{
    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(VirtualMachineDiscovery.class);
 
    /** Hide constructor. */
    private VirtualMachineDiscovery()
    {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Checks if a group manager hosts the virtual machine.
     * 
     * @param location          The virtual machine location
     * @param networkAddress    The network address
     * @return                  true if yes, false otherwise
     */
    public static boolean hasVirtualMachine(VirtualMachineLocation location,
                                            NetworkAddress networkAddress)      
    {
        log_.debug(String.format("Sending hasVirtualMachine request to group manager for: %s on %s", 
                                 location.getVirtualMachineId(), 
                                 location.getLocalControllerId()));
        
        GroupManagerAPI groupManagerCommunicator = CommunicatorFactory.newGroupManagerCommunicator(networkAddress);
        boolean hasVirtualMachine = groupManagerCommunicator.hasVirtualMachine(location);
        return hasVirtualMachine;       
    }
    
    /**
     * Starts virtual machine discovery procedure.
     * 
     * @param virtualMachineId          The virtual machine identifier
     * @param networkAddress            The group leader description
     * @return                          The corresponding group manager
     */
    public static VirtualMachineDiscoveryResponse discoverVirtualMachine(String virtualMachineId, 
                                                                         NetworkAddress networkAddress)
    {
        log_.debug(String.format("Sending virtual machine discovery request for: %s", 
                                 virtualMachineId));
        
        GroupManagerAPI groupLeaderCommunicator = CommunicatorFactory.newGroupManagerCommunicator(networkAddress);
        VirtualMachineDiscoveryResponse response = groupLeaderCommunicator.discoverVirtualMachine(virtualMachineId);
        return response;       
    }
}
