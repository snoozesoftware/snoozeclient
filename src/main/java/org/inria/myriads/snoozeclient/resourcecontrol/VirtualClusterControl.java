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
package org.inria.myriads.snoozeclient.resourcecontrol;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.inria.myriads.snoozeclient.configurator.api.ClientConfiguration;
import org.inria.myriads.snoozeclient.util.BootstrapUtilis;
import org.inria.myriads.snoozecommon.communication.NetworkAddress;
import org.inria.myriads.snoozecommon.communication.groupmanager.GroupManagerDescription;
import org.inria.myriads.snoozecommon.communication.rest.CommunicatorFactory;
import org.inria.myriads.snoozecommon.communication.rest.api.BootstrapAPI;
import org.inria.myriads.snoozecommon.communication.rest.api.GroupManagerAPI;
import org.inria.myriads.snoozecommon.communication.virtualcluster.VirtualMachineMetaData;
import org.inria.myriads.snoozecommon.communication.virtualcluster.migration.ClientMigrationRequestSimple;
import org.inria.myriads.snoozecommon.communication.virtualcluster.requests.MetaDataRequest;
import org.inria.myriads.snoozecommon.communication.virtualcluster.submission.VirtualClusterSubmissionRequest;
import org.inria.myriads.snoozecommon.communication.virtualcluster.submission.VirtualClusterSubmissionResponse;
import org.inria.myriads.snoozecommon.communication.virtualcluster.submission.VirtualMachineLocation;
import org.inria.myriads.snoozecommon.communication.virtualmachine.ResizeRequest;
import org.inria.myriads.snoozecommon.guard.Guard;
import org.inria.myriads.snoozecommon.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Virtual machine control logic.
 *  
 * @author Eugen Feller
 */
public final class VirtualClusterControl 
{
    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(VirtualClusterControl.class);
    
    /** Group manager communicator. */
    private GroupManagerAPI groupManagerCommunicator_;

    /** Client parameters. */
    private ClientConfiguration clientConfiguration_;
    
    /**
     * Constructor.
     * 
     * @param clientConfiguration    The client configuration
     * @param groupManagerAddress    The group manager address
     */
    public VirtualClusterControl(ClientConfiguration clientConfiguration, 
                                 NetworkAddress groupManagerAddress) 
    {
        Guard.check(clientConfiguration);
        log_.debug("Initializing virtual cluster control");  
        clientConfiguration_ = clientConfiguration;
        groupManagerCommunicator_ = CommunicatorFactory.newGroupManagerCommunicator(groupManagerAddress);
    }
    
    /**
     * Constructor.
     * 
     * @param clientConfiguration                      The client configuration
     */
    public VirtualClusterControl(ClientConfiguration clientConfiguration) 
    {
        Guard.check(clientConfiguration);
        log_.debug("Initializing virtual cluster control without group manager");  
        clientConfiguration_ = clientConfiguration;
    }
    
    /**  
     * Start a virtual machine.
     *  
     * @param virtualClusterDescription  Virtual cluster description
     * @param networkAddress             The group manager address
     * @return                           The virtual cluster response
     */
    public VirtualClusterSubmissionResponse start(VirtualClusterSubmissionRequest virtualClusterDescription)
    {
        Guard.check(virtualClusterDescription);
        log_.debug("Starting virtual cluster");
        
        BootstrapAPI bootstrapCommunicator  = 
                BootstrapUtilis.getActiveBootstrapCommunicator(clientConfiguration_.getGeneralSettings().getBootstrapNodes());
        GroupManagerDescription groupLeaderDescription = bootstrapCommunicator.getGroupLeaderDescription();
        
        GroupManagerAPI groupLeader = CommunicatorFactory.newGroupManagerCommunicator(groupLeaderDescription.getListenSettings().getControlDataAddress());
        String taskIdentifier = groupLeader.startVirtualCluster(virtualClusterDescription);
        log_.debug(String.format("Virtual cluster received identifier: %s", taskIdentifier));
        
        if (taskIdentifier == null)
        {
            return null;
        }
        
        VirtualClusterSubmissionResponse virtualClusterResponse = null;
        int pollingInterval = clientConfiguration_.getGeneralSettings().getSubmissionPollingInterval();
        while (true)
        {
            try 
            {              
                log_.debug("Waiting for virtual cluster response retrieval");
                Thread.sleep(TimeUtils.convertSecondsToMilliseconds(pollingInterval));
                virtualClusterResponse = groupLeader.getVirtualClusterResponse(taskIdentifier);
                if (virtualClusterResponse != null)
                {
                    log_.debug("Received valid virtual cluster response!");
                    break;
                }
            } 
            catch (InterruptedException exception) 
            {
                log_.error("Interrupted exception", exception);
                break;
            }
        }
        
        return virtualClusterResponse;
    }

    /** 
     * Suspend a virtual machine. 
     *  
     * @param location      The virtual machine location
     * @return              true if everything ok, false otherwise
     */
    public boolean suspend(VirtualMachineLocation location)
    {
        Guard.check(location);
        return groupManagerCommunicator_.suspendVirtualMachine(location);
    }
       
    /** 
     * Resume a virtual machine. 
     *  
     * @param location      The virtual machine location
     * @return              true if everything ok, false otherwise
     */
    public boolean resume(VirtualMachineLocation location)
    {
        Guard.check(location);
        return groupManagerCommunicator_.resumeVirtualMachine(location);
    }
            
    /** 
     * Show information about a virtual machine (status, host, etc.).
     * 
     * @param request     The virtual machine meta data request
     * @return            The virtual machine meta data
     */
    public VirtualMachineMetaData info(MetaDataRequest request)
    {
        Guard.check(request);
        return groupManagerCommunicator_.getVirtualMachineMetaData(request);
    }
    
    /** 
     * Shutdown a virtual machine.
     * 
     * @param location      The virtual machine location
     * @return              true if everything ok, false otherwise
     */
    public boolean shutdown(VirtualMachineLocation location)
    {
        Guard.check(location);
        return groupManagerCommunicator_.shutdownVirtualMachine(location);
    }
    
    /** 
     * Reboot a virtual machine.
     * 
     * @param location      The virtual machine location
     * @return              true if everything ok, false otherwise
     */
    public boolean reboot(VirtualMachineLocation location)
    {
        Guard.check(location);
        return groupManagerCommunicator_.rebootVirtualMachine(location);
    }

    /** 
     * Destroy a virtual machine.
     * 
     * @param location      The virtual machine location
     * @return              true if everything ok, false otherwise
     */
    public boolean destroy(VirtualMachineLocation location)
    {
        Guard.check(location);
        return groupManagerCommunicator_.destroyVirtualMachine(location);
    }
    
    public VirtualMachineMetaData resize(ResizeRequest resizeRequest)
    {
        Guard.check(resizeRequest);
        return groupManagerCommunicator_.resizeVirtualMachine(resizeRequest);
    }

    public boolean migrate(ClientMigrationRequestSimple migrationRequest) 
    {
        BootstrapAPI bootstrapCommunicator  = 
                BootstrapUtilis.getActiveBootstrapCommunicator(clientConfiguration_.getGeneralSettings().getBootstrapNodes());
        
        return bootstrapCommunicator.migrateVirtualMachine(migrationRequest);
    }
}
