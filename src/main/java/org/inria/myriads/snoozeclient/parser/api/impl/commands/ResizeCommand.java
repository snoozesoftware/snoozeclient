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
 * Resume command.
 * 
 * @author Matthieu Simonin
 */
@Parameters(separators = "=", commandDescription = "Resize virtual machine or virtual cluster")
public final class ResizeCommand extends ClusterCommandBase 
{    
    
    /** vcpu. */
    @Parameter(names = {"-vcpu", "--vcpu" }, description = "VCPU requirement",
               validateWith = PositiveIntegerValidator.class)
    private int vcpu_;
    
    /** mem. */
    @Parameter(names = {"-mem", "--memory" }, description = "Memory requirement",
               validateWith = PositiveIntegerValidator.class)
    private int memory_;
    
    /** Network Rx capacity. */
    @Parameter(names = {"-rx", "--networkRxCapacity" }, description = "Virtual machine network Rx requirement (KB)",
               validateWith = PositiveIntegerValidator.class)
    private int networkRxCapacity_;
    
    /** Network Tx capacity. */
    @Parameter(names = {"-tx", "--networkTxCapacity" }, description = "Virtual machine network Tx requirement (KB)",
               validateWith = PositiveIntegerValidator.class)
    private int networkTxCapacity_;

    /**
     * @return the vcpu_
     */
    public int getVcpu() 
    {
        return vcpu_;
    }

    /**
     * @param vcpu the vcpu to set
     */
    public void setVcpu(int vcpu) 
    {
        vcpu_ = vcpu;
    }

    /**
     * @return the memory
     */
    public int getMemory() 
    {
        return memory_;
    }

    /**
     * @param memory the memory to set
     */
    public void setMemory(int memory) 
    {
        memory_ = memory;
    }

    /**
     * @return the networkRxCapacity_
     */
    public int getNetworkRxCapacity() 
    {
        return networkRxCapacity_;
    }

    /**
     * @param networkRxCapacity the networkRxCapacity to set
     */
    public void setNetworkRxCapacity(int networkRxCapacity) 
    {
        this.networkRxCapacity_ = networkRxCapacity;
    }

    /**
     * @return the networkTxCapaciy
     */
    public int getNetworkTxCapacity() 
    {
        return networkTxCapacity_;
    }

    /**
     * @param networkTxCapacity the networkTxCapacity to set
     */
    public void setNetworkTxCapacity_(int networkTxCapacity) 
    {
        networkTxCapacity_ = networkTxCapacity;
    }  
}
