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
package org.inria.myriads.snoozeclient.statistics.util;

import java.io.IOException;
import java.util.List;

import org.inria.myriads.snoozeclient.configurator.statistics.StatisticsOutput;
import org.inria.myriads.snoozeclient.statistics.SubmissionResultsWriterFactory;
import org.inria.myriads.snoozeclient.statistics.impl.SubmissionResultsWriter;
import org.inria.myriads.snoozeclient.statistics.results.SubmissionResults;
import org.inria.myriads.snoozecommon.communication.virtualcluster.VirtualMachineMetaData;
import org.inria.myriads.snoozecommon.communication.virtualcluster.status.VirtualMachineStatus;
import org.inria.myriads.snoozecommon.communication.virtualcluster.submission.VirtualClusterSubmissionResponse;
import org.inria.myriads.snoozecommon.guard.Guard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Submission results utilities.
 * 
 * @author Eugen Feller
 */
public final class SubmissionResultsUtils 
{
    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(SubmissionResultsUtils.class);
    
    /** Hide constructor. */
    private SubmissionResultsUtils()
    {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Generates the submission results.
     * 
     * @param virtualClusterResponse    The virtual cluster response
     * @param numberOfVirtualMachines   The number of virual machines
     * @param startSystemTime           The start system time
     * @param finishSystemTime          The finish system time
     * @return                          The submission results
     */
    public static SubmissionResults generateSubmissionResults(VirtualClusterSubmissionResponse virtualClusterResponse, 
                                                              int numberOfVirtualMachines,
                                                              long startSystemTime,
                                                              long finishSystemTime) 
    {
        Guard.check(virtualClusterResponse, startSystemTime, finishSystemTime);
        log_.debug("Generating the submission results");
        
        long timeToSchedule = finishSystemTime - startSystemTime;
        int numberOfFailedAllocations = computeNumberOfFailedAllocations(virtualClusterResponse);
        SubmissionResults submissionResults = new SubmissionResults(numberOfVirtualMachines, 
                                                                    startSystemTime, 
                                                                    timeToSchedule,
                                                                    numberOfFailedAllocations);     
        return submissionResults;
    }

    /**
     * Computes the number of failed allocations.
     * 
     * @param virtualClusterResponse    The virtual cluster response
     * @return                          The number of failed allocations
     */
    private static int computeNumberOfFailedAllocations(VirtualClusterSubmissionResponse virtualClusterResponse) 
    {
        Guard.check(virtualClusterResponse);
        log_.debug("Computing the number of failed allocations");
        
        List<VirtualMachineMetaData> metaData = virtualClusterResponse.getVirtualMachineMetaData();
        int numberOfFailedAllocations = 0;
        for (VirtualMachineMetaData entry : metaData) 
        {
            if (!entry.getStatus().equals(VirtualMachineStatus.RUNNING))
            {
                numberOfFailedAllocations++;
            }
        }

        return numberOfFailedAllocations;
    }

    /**
     * Writes out submission results.
     * 
     * @param output                    The statistics output
     * @param submissionResults         The virtual cluster response
     * @throws IOException              The I/O exception
     */
    public static void writeSubmissionResults(StatisticsOutput output, 
                                              SubmissionResults submissionResults) 
        throws IOException
    {
        Guard.check(output, submissionResults);
        log_.debug("Starting to write out the submission results");

        SubmissionResultsWriter resultsWriter = 
            SubmissionResultsWriterFactory.newSubmissionResultsWriter(output.getFile(), output.getFormat());
        resultsWriter.writeSubmissionResults(submissionResults);
    }
}
