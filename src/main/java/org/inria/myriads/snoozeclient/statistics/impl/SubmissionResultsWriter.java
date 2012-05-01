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
package org.inria.myriads.snoozeclient.statistics.impl;

import java.io.IOException;

import org.inria.myriads.snoozeclient.configurator.statistics.StatisticsFormat;
import org.inria.myriads.snoozeclient.statistics.results.SubmissionResults;
import org.inria.myriads.snoozeclient.statistics.results.writer.FileResultsWriter;
import org.inria.myriads.snoozecommon.guard.Guard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Submission results writer.
 * 
 * @author Eugen Feller
 */
public final class SubmissionResultsWriter 
{
    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(SubmissionResultsWriter.class);
    
    /** File results writer. */
    private FileResultsWriter fileResultsWriter_;

    /** Statistics output format. */
    private StatisticsFormat statisticsOutputFormat_;
    
    /**
     * Constructor.
     * 
     * @param fileName                  The file name
     * @param statisticsOutputFormat    The output format
     * @throws IOException 
     */
    public SubmissionResultsWriter(String fileName, StatisticsFormat statisticsOutputFormat)
        throws IOException 
    {
        Guard.check(fileName);
        log_.debug("Initializing the submission results writer");
        fileResultsWriter_ = new FileResultsWriter(fileName);
        statisticsOutputFormat_ = statisticsOutputFormat;
    }
    
    /** 
     * Will write the submission results in GNUPlot format.
     *  
     * @param submissionResults  Submission results result
     * @throws IOException 
     */
    public void writeSubmissionResults(SubmissionResults submissionResults)
        throws IOException 
    {
        Guard.check(submissionResults);
        log_.debug(String.format("Writing submission results in format: %s", statisticsOutputFormat_));
        
        switch (statisticsOutputFormat_)
        {
            case gnuplot :
                writeGNUPlotInFormat(submissionResults);
                break;
                
            default:
                log_.error("Unknown statistics output format selected!");
        }
    }

    /**
     * Writes the results in GNUPlot format.
     * 
     * @param schedulingResults     The scheduling results
     * @throws IOException 
     */
    private void writeGNUPlotInFormat(SubmissionResults schedulingResults) 
        throws IOException
    {
        Guard.check(schedulingResults);
        String separator = " ";
        String result = "";
        
        /** 
         * Create the resulting simulation line 
         * 
         * Constrained: submission time, number of virtual machines, number of successfull allocations, 
         *              number of failed allocations, time to schedule, 
         */
        String submissionTime = String.valueOf(schedulingResults.getSubmissionTime());
        String numberOfVirtualMachines = String.valueOf(schedulingResults.getNumberOfVirtualMachines());
        String numberOfSuccessfullAllocations = String.valueOf(schedulingResults.getNumberOfSuccessfullAllocations());
        String numberOfFailedAllocations = String.valueOf(schedulingResults.getNumberOfFailedAllocations());
        String timeToSchedule = String.valueOf((double) schedulingResults.getTimeToSchedule() / 1000);
        
        result = submissionTime + separator + numberOfVirtualMachines + separator + numberOfSuccessfullAllocations + 
                 separator + numberOfFailedAllocations + separator + timeToSchedule;
        
        log_.debug(String.format("Writing line: %s", result));
        
        fileResultsWriter_.writeData(result);
        fileResultsWriter_.closeFile(); 
    }
}
