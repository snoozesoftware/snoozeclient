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
package org.inria.myriads.snoozeclient.configurator.statistics;

/**
 * Output settings.
 * 
 * @author Eugen Feller
 */
public class StatisticsOutput 
{
    /** Format. */
    private StatisticsFormat format_;

    /** File name. */
    private String fileName_;

    /**
     * Sets the format.
     * 
     * @param format    The format
     */
    public void setFormat(StatisticsFormat format) 
    {
        format_ = format;
    }

    /**
     * Sets the file name.
     * 
     * @param fileName  The file name
     */
    public void setFile(String fileName) 
    {
        fileName_ = fileName;
    }
        
    /**
     * Returns the output format.
     * 
     * @return  The statistics output format
     */
    public StatisticsFormat getFormat()
    {
        return format_;
    }
    
    /**
     * Returns the file name.
     * 
     * @return  The file name
     */
    public String getFile()
    {
        return fileName_;
    }
}
