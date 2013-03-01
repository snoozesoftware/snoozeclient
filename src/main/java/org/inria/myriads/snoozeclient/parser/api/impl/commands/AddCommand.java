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

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import org.inria.myriads.snoozeclient.parser.api.impl.validation.PositiveIntegerValidator;

/**
 * Add command.
 * 
 * @author Eugen Feller
 */
@Parameters(separators = "=", commandDescription = "Add virtual machine to the cluster")
public final class AddCommand extends HelpCommandBase
{        
    /** Virtual cluster name. */
    @Parameter(names = {"-vcn", "--virtualClusterName" }, description = "Virtual cluster name", 
               required = true)
    private String virtualClusterName_;
    
    /** Virtual machine template path. */
    @Parameter(names = {"-vmt", "--virtualMachineTemplate" }, description = "Virtual machine template path", 
               required = true)
    private String virtualMachineTemplate_;

    /** Network Rx capacity. */
    @Parameter(names = {"-rx", "--networkRxCapacity" }, description = "Virtual machine network Rx requirement (KB)",
               validateWith = PositiveIntegerValidator.class)
    private int networkRxCapacity_;
    
    /** Network Tx capacity. */
    @Parameter(names = {"-tx", "--networkTxCapacity" }, description = "Virtual machine network Tx requirement (KB)",
               validateWith = PositiveIntegerValidator.class)
    private int networkTxCapaciy_;  
    
    /** Constructor. */
    public AddCommand()
    {
        networkRxCapacity_ = 12800;
        networkTxCapaciy_ = 12800;
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
}
