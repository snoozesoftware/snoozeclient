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

import org.inria.myriads.snoozeclient.configurator.api.ClientConfiguration;
import org.inria.myriads.snoozeclient.configurator.general.GeneralSettings;
import org.inria.myriads.snoozeclient.configurator.statistics.StatisticsSettings;
import org.inria.myriads.snoozecommon.guard.Guard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utils class. Contains some helper functions.
 * 
 * @author Eugen Feller
 */
public final class OutputUtils
{           
    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(OutputUtils.class);
    
    /**
     * Hide the constructor.
     */
    private OutputUtils() 
    {
        throw new UnsupportedOperationException();
    }
                    
    /** 
     * Prints the client configuration.
     * 
     * @param clientConfiguration  The client configuration
     */
    public static void printConfiguration(ClientConfiguration clientConfiguration) 
    {
        Guard.check(clientConfiguration);
        GeneralSettings generalSettings = clientConfiguration.getGeneralSettings();
        StatisticsSettings statisticsSettings = clientConfiguration.getStatisticsSettings();
        log_.debug("------------------- Client configuration ---------------");
        log_.debug("--------------------");
        log_.debug("General settings:");
        log_.debug("--------------------");
        log_.debug(String.format("Bootstrap nodes: %s", generalSettings.getBootstrapNodes()));   
        log_.debug(String.format("Submission polling interval: %s", generalSettings.getSubmissionPollingInterval()));
        log_.debug(String.format("Number of monitoring entries: %d", generalSettings.getNumberOfMonitoringEntries()));
        log_.debug(String.format("Dump output file: %s", generalSettings.getDumpOutputFile()));
        log_.debug("--------------------");
        log_.debug("Statistics settings:");
        log_.debug("--------------------");
        log_.debug(String.format("Statistics enabled: %s", statisticsSettings.isEnabled()));
        log_.debug(String.format("Output format: %s", statisticsSettings.getOutput().getFormat()));
        log_.debug(String.format("Output file: %s", statisticsSettings.getOutput().getFile()));
        log_.debug("-------------------");
    }
}
