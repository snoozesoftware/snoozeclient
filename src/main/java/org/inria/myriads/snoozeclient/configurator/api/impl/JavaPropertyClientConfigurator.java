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
package org.inria.myriads.snoozeclient.configurator.api.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.inria.myriads.snoozeclient.configurator.api.ClientConfiguration;
import org.inria.myriads.snoozeclient.configurator.api.ClientConfigurator;
import org.inria.myriads.snoozeclient.configurator.general.GeneralSettings;
import org.inria.myriads.snoozeclient.configurator.statistics.StatisticsFormat;
import org.inria.myriads.snoozeclient.configurator.statistics.StatisticsSettings;
import org.inria.myriads.snoozeclient.exception.ClientConfiguratorException;
import org.inria.myriads.snoozecommon.communication.NetworkAddress;
import org.inria.myriads.snoozecommon.guard.Guard;
import org.inria.myriads.snoozecommon.util.NetworkUtils;
import org.inria.myriads.snoozecommon.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client configurator class.
 * 
 * @author Eugen Feller
 */
public final class JavaPropertyClientConfigurator 
    implements ClientConfigurator
{  
    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(JavaPropertyClientConfigurator.class);
            
    /** Client configuration. */
    private ClientConfiguration clientConfiguration_;
     
    /** Properties field. */
    private Properties properties_;
    
    /** 
     * Initialize client configuration.
     * 
     * @param configurationFile             The configuration file
     * @throws IOException 
     * @throws ClientConfiguratorException 
     */
    public JavaPropertyClientConfigurator(String configurationFile)
        throws IOException, ClientConfiguratorException 
    {
        Guard.check(configurationFile);
        log_.debug("Initializing java properties based client configurator");
        clientConfiguration_ = new ClientConfiguration();    
        properties_ = new Properties();
        properties_.load(new FileInputStream(configurationFile));
        
        setGeneralSettings();
        setStatisticsSettings();
    }

    /**
     * Sets the general settings.
     * 
     * @throws ClientConfiguratorException  The client configuration exception
     */
    private void setGeneralSettings() 
        throws ClientConfiguratorException
    {          
        GeneralSettings generalSettings = new GeneralSettings();
        String bootstrapNodes = getPropertyContent("general.bootstrapNodes");
        List<String> bootstrapAddresses = StringUtils.convertStringToStringArray(bootstrapNodes, ",");
        List<NetworkAddress> networkAddresses = generateListOfNetworkAddresses(bootstrapAddresses);
        generalSettings.setBootstrapNodes(networkAddresses);
        
        String submissionPollingInterval = getPropertyContent("general.submissionPollingInterval");
        generalSettings.setSubmissionPollingInterval(Integer.valueOf(submissionPollingInterval));
        
        String dumpOutputFile = getPropertyContent("general.dumpOutputFile");
        generalSettings.setDumpOutputFile(dumpOutputFile);     
        
        String numberOfMonitoringEntries = getPropertyContent("general.numberOfMonitoringEntries");
        generalSettings.setNumberOfMonitoringEntries(Integer.valueOf(numberOfMonitoringEntries));        
        clientConfiguration_.setGeneralSettings(generalSettings);
    }
        
    /** 
     * Returns the client configuration.
     *  
     * @return  The client configuration
     */
    @Override
    public ClientConfiguration getConfiguration() 
    {
        return clientConfiguration_;
    }
    
    /**
     * Returns the property content.
     * 
     * @param property                      The property
     * @return                              The content string
     * @throws ClientConfiguratorException  The client configuration exception
     */
    private String getPropertyContent(String property) 
        throws ClientConfiguratorException
    {
        String content = properties_.getProperty(property);
        if (content == null) 
        {
            throw new ClientConfiguratorException(String.format("%s entry is missing", property));
        }
        
        content = content.trim();
        return content;             
    }
    
    /**
     * Sets the statistics parameters.
     * 
     * @throws ClientConfiguratorException  The client configuration exception
     */
    private void setStatisticsSettings() 
        throws ClientConfiguratorException
    {         
        StatisticsSettings statisticsSettings = new StatisticsSettings();
        String isStatistics = getPropertyContent("statistics.enabled");
        statisticsSettings.setEnabled(Boolean.valueOf(isStatistics));

        String outputFormat = getPropertyContent("statistics.output.format");
        statisticsSettings.getOutput().setFormat(StatisticsFormat.valueOf(outputFormat));
        
        String outputFile = getPropertyContent("statistics.output.file");
        statisticsSettings.getOutput().setFile(outputFile);
        clientConfiguration_.setStatisticsSettings(statisticsSettings);
    }
    
    /**
     * Generates a list of network addresses.
     * 
     * @param bootstrapAddresses    The bootstrap addresses
     * @return                      The list of bootstrap addresses
     */
    private List<NetworkAddress> generateListOfNetworkAddresses(List<String> bootstrapAddresses)
    {
        List<NetworkAddress> networkAddresses = new ArrayList<NetworkAddress>();
        for (int i = 0; i < bootstrapAddresses.size(); i++)
        {
            String[] bootstrapEntry = bootstrapAddresses.get(i).split(":");
            String ipAddress = bootstrapEntry[0];
            int port = Integer.valueOf(bootstrapEntry[1]);
            NetworkAddress networkAddress = NetworkUtils.createNetworkAddress(ipAddress, port);
            networkAddresses.add(networkAddress);
        }     
        return networkAddresses;
    }
}
