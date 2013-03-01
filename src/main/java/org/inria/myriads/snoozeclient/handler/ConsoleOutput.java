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
package org.inria.myriads.snoozeclient.handler;

/**
 * Information output.
 * 
 * @author Eugen Feller
 */
public final class ConsoleOutput 
{
    /** CPU utilization. */
    private String cpuUtilization_;
    
    /** Memory utilization. */
    private String memoryUtilization_;
    
    /** Network utilizatipn. */
    private String networkUtilization_;
    
    /** Final status. */
    private String finalStatus_;
    
    /**
     * Information output.
     * 
     * @param cpuUtilization        The CPU utilization
     * @param memoryUtilization     The memory utilization
     * @param networkUtilization    The network utilization
     * @param finalStatus           The final status
     */
    public ConsoleOutput(String cpuUtilization, 
                             String memoryUtilization, 
                             String networkUtilization, 
                             String finalStatus) 
    {
        cpuUtilization_ = cpuUtilization;
        memoryUtilization_ = memoryUtilization;
        networkUtilization_ = networkUtilization;
        finalStatus_ = finalStatus;
    }

    /**
     * Returns the CPU utilization.
     * 
     * @return  The CPU utilization
     */
    public String getCpuUtilization() 
    {
        return cpuUtilization_;
    }

    /**
     * Returns the network utilization.
     * 
     * @return  The memory utilization
     */
    public String getMemoryUtilization() 
    {
        return memoryUtilization_;
    }

    /**
     * Returns the network utilization.
     * 
     * @return  The network utilization
     */
    public String getNetworkUtilization() 
    {
        return networkUtilization_;
    }

    /**
     * Returns the final status.
     * 
     * @return  The final status
     */
    public String getFinalStatus() 
    {
        return finalStatus_;
    }

}
