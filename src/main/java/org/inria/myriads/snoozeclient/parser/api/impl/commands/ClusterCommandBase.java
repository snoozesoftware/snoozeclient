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
package org.inria.myriads.snoozeclient.parser.api.impl.commands;

import com.beust.jcommander.Parameter;

/**
 * Cluster command.
 * 
 * @author Eugen Feller
 */
public class ClusterCommandBase extends HelpCommandBase
{
    /** Virtual cluster name. */
    @Parameter(names = {"-vcn", "--virtualClusterName" }, description = "Virtual cluster name", required = true)
    private String virtualClusterName_;
    
    /** Virtual machine name. */
    @Parameter(names = {"-vmn", "--virtualMachineName" }, description = "Virtual machine name")
    private String virtualMachineName_;
    
    /**
     * Returns the virtual machine name.
     * 
     * @return  The virtual machine name
     */
    public String getVirtualMachineName() 
    {
        return virtualMachineName_;
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
}
