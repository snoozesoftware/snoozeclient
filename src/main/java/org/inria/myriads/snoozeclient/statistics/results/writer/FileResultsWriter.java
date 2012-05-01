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
package org.inria.myriads.snoozeclient.statistics.results.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.inria.myriads.snoozecommon.guard.Guard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * File results writer.
 * 
 * @author Eugen Feller
 */
public class FileResultsWriter 
{
    /** Logger. */
    private final Logger log_ = LoggerFactory.getLogger(FileResultsWriter.class);
   
    /** BufferedWriter objects. */
    private final BufferedWriter bufferedWriter_;
    
    /** FileWriter object. */
    private final FileWriter fileWriter_;
   
    /**
     * Constructor of the writer.
     *  
     * @param fileName      File name to write data to
     * @throws IOException  I/O exception
     */
    public FileResultsWriter(String fileName) 
        throws IOException 
    {
        Guard.check(fileName);
        log_.debug("Initializing the file results writer");
        
        fileWriter_ = new FileWriter(fileName, true);
        bufferedWriter_ = new BufferedWriter(fileWriter_);
    }

    /**
     *  Write a line to the data file.
     *  
     * @param data          The data to write
     * @throws IOException  I/O exception
     */
    public final void writeData(String data)
        throws IOException
    {
        log_.debug(String.format("Writing data %s", data));
        bufferedWriter_.write(data + "\n");
    }
    
    /** 
     * Closes the file.
     *  
     * @throws IOException 
     */
    public final void closeFile()
        throws IOException 
    {
        bufferedWriter_.close();
    }
}
