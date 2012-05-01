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
package org.inria.myriads.snoozeclient.configurator.api;

import org.inria.myriads.snoozeclient.configurator.general.GeneralSettings;
import org.inria.myriads.snoozeclient.configurator.statistics.StatisticsSettings;

/**
 * Configuration configuration.
 * 
 * @author Eugen Feller
 */
public final class ClientConfiguration 
{        
    /** General settings. */
    private GeneralSettings generalSettings_;
    
    /** Statistics settings. */
    private StatisticsSettings statisticsSettings_;
    
    /**
     * Sets the general settings.
     * 
     * @param generalSettings     The general settings
     */
    public void setGeneralSettings(GeneralSettings generalSettings) 
    {
        generalSettings_ = generalSettings;
    }
    
    /**
     * Returns the general settings.
     * 
     * @return  The general settings
     */
    public GeneralSettings getGeneralSettings() 
    {
        return generalSettings_;
    }
    
    /**
     * Sets statistics settings.
     * 
     * @param statisticsSettings    The statistics settings
     */
    public void setStatisticsSettings(StatisticsSettings statisticsSettings) 
    {
        statisticsSettings_ = statisticsSettings;
    }
    
    /**
     * Returns the statistics settings.
     * 
     * @return  The statistics settings
     */
    public StatisticsSettings getStatisticsSettings() 
    {
        return statisticsSettings_;
    }
}
