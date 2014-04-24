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
package org.inria.myriads.snoozeclient.parser.api.impl.commands;

import java.util.UUID;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import org.inria.myriads.snoozeclient.parser.api.impl.validation.PositiveIntegerValidator;
import org.inria.myriads.snoozeclient.parser.api.impl.validation.PositiveLongValidator;

/**
 * Add command.
 * 
 * @author Eugen Feller
 */
/**
 * @author msimonin
 *
 */
@Parameters(separators = "=", commandDescription = "Add virtual machine to the cluster")
public final class AddCommand extends HelpCommandBase
{        
    /** Virtual cluster name. */
    @Parameter(names = {"-vcn", "--virtualClusterName" }, description = "Virtual cluster name", 
               required = true)
    private String virtualClusterName_;
    
    /** Virtual machine template path. */
    @Parameter(names = {"-vmt", "--virtualMachineTemplate" }, description = "Virtual machine template path")
    private String virtualMachineTemplate_;

    /** name. */
    @Parameter(names = {"-name", "--name" }, description = "Virtual machine name")
    private String name_;
    
    /** image. */
    @Parameter(names = {"-iid", "--image" }, description = "Virtual machine image")
    private String image_;
    
    /** vcpus capacity. */
    @Parameter(names = {"-vcpus", "--vcpus" }, description = "Virtual machine virtual cores requirements",
               validateWith = PositiveIntegerValidator.class)
    private int vcpus_;
    
    /** memory capacity. */
    @Parameter(names = {"-mem", "--memory" }, description = "Virtual machine virtual memory requirements (MB)",
               validateWith = PositiveLongValidator.class)
    private long memory_;
    
    /** Network Rx capacity. */
    @Parameter(names = {"-rx", "--networkRxCapacity" }, description = "Virtual machine network Rx requirement (KB)",
               validateWith = PositiveIntegerValidator.class)
    private int networkRxCapacity_;
    
    /** Network Tx capacity. */
    @Parameter(names = {"-tx", "--networkTxCapacity" }, description = "Virtual machine network Tx requirement (KB)",
               validateWith = PositiveIntegerValidator.class)
    private int networkTxCapaciy_;
    
    /** hostId. */
    @Parameter(names = {"-hid", "--hostId"}, description = "Hosts id on which the vm will attempt to start")
    private String hostId_;
    
    /** Constructor. */
    public AddCommand()
    {
        networkRxCapacity_ = 12800;
        networkTxCapaciy_ = 12800;
        vcpus_ = 1;
        memory_ = 512*1024;
        name_ = UUID.randomUUID().toString();
        hostId_ = ""; 
    }
    
    /**
     * Returns the virtual cluster name.
     * 
     * @return      The virtual cluster name
     */
    public String getVirtualClusterName() 
    {
        return virtualClusterName_;
    }  
    
    /**
     * Returns the network rx capacity.
     * 
     * @return  The network rx capacity
     */
    public int getNetworkRxCapacity()
    {
        return networkRxCapacity_;
    }

    /**
     * Returns the network tx capacity.
     * 
     * @return  The network tx capacity
     */
    public int getNetworkTxCapaciy() 
    {
        return networkTxCapaciy_;
    }
    
    /**
     * Returns the virtual machine template.
     * 
     * @return  The virtual machine template
     */
    public String getVirtualMachineTemplate() 
    {
        return virtualMachineTemplate_;
    }


    /**
     * @param hostId the hostId to set
     */
    public void setHostId(String hostId) 
    {
        hostId_ = hostId;
    }

    /*
     * @return the name
     */
    public String getName() 
    {
        return name_;
    }

    /**
     * @return the vcpus
     */
    public int getVcpus() 
    {
        return vcpus_;
    }

    /**
     * @return the memory
     */
    public long getMemory() 
    {
        return memory_;
    }

    /**
     * @return the image
     */
    public String getImage()
    {
        return image_;
    }

    /**
     * @return the hostId
     */
    public String getHostId() 
    {
        return hostId_;
    }
}
