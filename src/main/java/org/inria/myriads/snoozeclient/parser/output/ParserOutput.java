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
package org.inria.myriads.snoozeclient.parser.output;

import org.inria.myriads.snoozeclient.parser.commands.ClientCommand;
import org.inria.myriads.snoozecommon.communication.virtualcluster.monitoring.NetworkDemand;

/**
 * Parse output holder.
 * 
 * @author Eugen Feller
 */
public final class ParserOutput 
{
    /** Virtual cluster command. */
    private ClientCommand clientCommand_;
    
    /** Virtual cluster names. */
    private String virtualClusterName_;
    
    /** Virtual machine template. */
    private String virtualMachineTemplate_;
    
    /** Virtual machine name. */
    private String virtualMachineName_;
    
    /** Network capacity. */
    private NetworkDemand networkCapacity_;

    /** Visualization. */
    private boolean isVisualize_;
    
    /** Dump. */
    private boolean isDump_;
    
    /** Constructor. */
    public ParserOutput()
    {
        networkCapacity_ = new NetworkDemand();
    }
    
    /**
     * Sets the client command.
     * 
     * @param clientCommand    The client command
     */
    public void setClientCommand(ClientCommand clientCommand) 
    {
        clientCommand_ = clientCommand;
    }

    /**
     * Returns the VM command.
     * 
     * @return     The virtual cluster command
     */
    public ClientCommand getClientCommand() 
    {
        return clientCommand_;
    }

    /** 
     * Cluster names.
     * 
     * @param clusterName  The cluster name
     */
    public void setClusterName(String clusterName)
    {
        virtualClusterName_ = clusterName;
    }

    /** 
     * Returns the cluster names.
     * 
     * @return  The cluster name
     */
    public String getVirtualClusterName()
    {
        return virtualClusterName_;
    }
    
    /**
     * Sets the virtual machine description.
     * 
     * @param vmTemplate    The virtual machine template
     */
    public void setVirtualMachineTemplate(String vmTemplate) 
    {
        virtualMachineTemplate_ = vmTemplate;
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
     * Sets the virtual machine name.
     * 
     * @param vitualMachineName    The virtual machine name
     */
    public void setVirtualMachineName(String vitualMachineName)
    {
        virtualMachineName_ = vitualMachineName;
    }
    
    /**
     * Gets the virtual machine name.
     * 
     * @return  The virtual machine name
     */
    public String getVirtualMachineName() 
    {
        return virtualMachineName_;
    }

    /**
     * Sets the network capacity.
     * 
     * @param networkCapacity   The network capacity
     */
    public void setNetworkCapacity(NetworkDemand networkCapacity) 
    {
        networkCapacity_ = networkCapacity;
    }

    /**
     * Returns the network capacity.
     * 
     * @return  The network capacity
     */
    public NetworkDemand getNetworkCapacity() 
    {
        return networkCapacity_;
    }

    /**
     * Sets the visualization flag.
     * 
     * @param isVisualize   The visualization flag
     */
    public void setVisualize(boolean isVisualize) 
    {
        isVisualize_ = isVisualize;
    }

    /**
     * Returns the visualization flag.
     * 
     * @return  The visualization flag
     */
    public boolean isVisualize() 
    {
        return isVisualize_;
    }

    /**
     * Sets the dump flag.
     * 
     * @param isDump    The dump flag
     */
    public void setDump(boolean isDump) 
    {
        isDump_ = isDump;
    }

    /**
     * Returns the dump flag.
     * 
     * @return  The dump flag
     */
    public boolean isDump() 
    {
        return isDump_;
    }
}
