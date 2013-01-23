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
package org.inria.myriads.snoozeclient.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;

import org.inria.myriads.snoozeclient.exception.BootstrapUtilityException;
import org.inria.myriads.snoozecommon.communication.NetworkAddress;
import org.inria.myriads.snoozecommon.communication.groupmanager.GroupManagerDescription;
import org.inria.myriads.snoozecommon.communication.groupmanager.repository.GroupLeaderRepositoryInformation;
import org.inria.myriads.snoozecommon.communication.rest.CommunicatorFactory;
import org.inria.myriads.snoozecommon.communication.rest.api.BootstrapAPI;
import org.inria.myriads.snoozecommon.guard.Guard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bootstrap utility.
 * 
 * @author Eugen Feller
 */
public final class BootstrapUtilis 
{
    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(BootstrapUtilis.class);
    
    /**
     * Hide the constructor.
     */
    private BootstrapUtilis() 
    {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Checks if the remote host is active.
     * 
     * @param networkAddress    The network address
     * @return                  true if host active, false otherwise
     */
    public static boolean isHostActive(NetworkAddress networkAddress) 
    {
        Guard.check(networkAddress);
        String host = networkAddress.getAddress();
        int port = networkAddress.getPort();
        log_.debug(String.format("Testing connection to: %s, port: %s", host, port));
        
        Socket socket = null;
        try 
        {
            socket = new Socket();
            socket.setReuseAddress(true);
            SocketAddress socketAddress = new InetSocketAddress(host, Integer.valueOf(port));
            socket.connect(socketAddress, Integer.valueOf(port));          
            return true;
        } 
        catch (IOException exception) 
        {
            log_.debug(String.format("I/O exception during test connection: %s", exception.getMessage()));
        } 
        finally
        {
            if (socket != null) 
            {
                try 
                {
                    socket.close();
                } 
                catch (IOException exception) 
                {
                    log_.debug(String.format("Unable to close the socket: %s", exception.getMessage()));
                }
            }
        }
        
        return false;
    }
    
    /**
     * Starts a bootstrap communicator.
     * 
     * @param bootstrapAddresses    The bootstrap addresses
     * @return                      The bootstrap communicator
     */
    private static BootstrapAPI getActiveBootstrapCommunicator(List<NetworkAddress> bootstrapAddresses) 
    {
        Guard.check(bootstrapAddresses);   
        log_.debug("Starting the bootstrap communicator");
        
        for (int i = 0; i < bootstrapAddresses.size(); i++)
        {            
            NetworkAddress bootstrapAddress = bootstrapAddresses.get(i);
            if (isHostActive(bootstrapAddress))
            {
                log_.debug("Online bootstrap node found");
                return CommunicatorFactory.newBootstrapCommunicator(bootstrapAddress);
            }
        }
        
        return null;
    }
    
    /**
     * Returns current group leader description.
     * 
     * @param bootstrapAddresse         The bootstrap addresses
     * @return                          The current group leader description
     * @throws BootstrapUtilityException 
     */
    public static GroupManagerDescription getGroupLeaderDescription(List<NetworkAddress> bootstrapAddresse)
        throws BootstrapUtilityException
    {
        Guard.check(bootstrapAddresse);
        log_.debug("Getting current group leader description");
        
        BootstrapAPI bootstrapCommunicator = getActiveBootstrapCommunicator(bootstrapAddresse);
        if (bootstrapCommunicator == null)
        {
            throw new BootstrapUtilityException("Unable to find any active bootstrap node!");
        }
        
        GroupManagerDescription groupLeaderDescription = bootstrapCommunicator.getGroupLeaderDescription();        
        if (groupLeaderDescription == null)
        {
            throw new BootstrapUtilityException("Group leader description is not available!");
        }
        return groupLeaderDescription;
    }
    
    /**
     * Returns the complete hierarchy.
     * 
     * @param bootstrapAddresse         The bootstrap addresses
     * @return                          The current group leader description
     * @throws BootstrapUtilityException 
     */
    public static GroupLeaderRepositoryInformation getCompleteHierarchy(List<NetworkAddress> bootstrapAddresse)
        throws BootstrapUtilityException
    {
        Guard.check(bootstrapAddresse);
        log_.debug("Getting current group leader description");
        
        BootstrapAPI bootstrapCommunicator = getActiveBootstrapCommunicator(bootstrapAddresse);
        if (bootstrapCommunicator == null)
        {
            throw new BootstrapUtilityException("Unable to find any active bootstrap node!");
        }
        
        GroupLeaderRepositoryInformation hierarchy = bootstrapCommunicator.getCompleteHierarchy();        
        if (hierarchy == null)
        {
            throw new BootstrapUtilityException("Group leader description is not available!");
        }
        return hierarchy;
    }
    
}
