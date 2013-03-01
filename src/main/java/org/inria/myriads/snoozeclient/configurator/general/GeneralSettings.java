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
package org.inria.myriads.snoozeclient.configurator.general;

import java.util.List;

import org.inria.myriads.snoozecommon.communication.NetworkAddress;

/**
 * General settings.
 * 
 * @author Eugen Feller
 */
public class GeneralSettings 
{
    /** Bootstrap addresses. */
    private List<NetworkAddress> bootstrapNodes_;
    
    /** Submission polling interval. */
    private int submissionPollingInterval_;

    /** Number of monitoring entries. */
    private int numberOfMonitoringEntries_;
    
    /** Graph output file name. */
    private String dumpOutputFile_;
    
    /** Visualization polling interval. */
    private int graphPollingInterval_;
    
    /**
     * Sets the output file.
     * 
     * @param dumpOutputFile     The dump output file
     */
    public void setDumpOutputFile(String dumpOutputFile) 
    {
        dumpOutputFile_ = dumpOutputFile;
    }

    /**
     * Returns the dump output file.
     * 
     * @return  The dumpt output file
     */
    public String getDumpOutputFile() 
    {
        return dumpOutputFile_;
    }

    /**
     * Sets the number of monitoring entries.
     * 
     * @param numberOfMonitoringEntries    The number of monitoring entries
     */
    public void setNumberOfMonitoringEntries(int numberOfMonitoringEntries) 
    {
        numberOfMonitoringEntries_ = numberOfMonitoringEntries;
    }

    /**
     * Returns the number of monitoring entries.
     * 
     * @return  The number of monitoring entries
     */
    public int getNumberOfMonitoringEntries() 
    {
        return numberOfMonitoringEntries_;
    }
    
    /**
     * Sets the polling interval.
     * 
     * @param pollingInterval   The polling interval
     */
    public void setSubmissionPollingInterval(int pollingInterval) 
    {
        submissionPollingInterval_ = pollingInterval;
    }
    
    /**
     * Returns the polling interval.
     * 
     * @return  The polling interval
     */
    public int getSubmissionPollingInterval()
    {
        return submissionPollingInterval_;
    }
    
    /**
     * Sets the bootstrap nodes.
     * 
     * @param bootstrapNodes    The bootstrap nodes
     */
    public final void setBootstrapNodes(List<NetworkAddress> bootstrapNodes) 
    {
        bootstrapNodes_ = bootstrapNodes;
    }
    
    /**
     * Returns the bootstrap nodes.
     * 
     * @return     The bootstrap nodes
     */
    public final List<NetworkAddress> getBootstrapNodes() 
    {
        return bootstrapNodes_;
    }

    /***
     * 
     * Gets the polling interval.
     * 
     * 
     * @return the graphPollingInterval
     */
    public int getGraphPollingInterval() 
    {
        return graphPollingInterval_;
    }

    /**
     * 
     * Sets the polling interval.
     * 
     * @param graphPollingInterval the graphPollingInterval to set
     */
    public void setGraphPollingInterval(int graphPollingInterval) 
    {
        graphPollingInterval_ = graphPollingInterval;
    }
}
