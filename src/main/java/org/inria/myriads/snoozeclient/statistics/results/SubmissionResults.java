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
package org.inria.myriads.snoozeclient.statistics.results;

import org.inria.myriads.snoozecommon.guard.Guard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains the scheduling results.
 * 
 * @author Eugen Feller
 */
public final class SubmissionResults 
{
    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(SubmissionResults.class);
    
    /** Number of submitted virtual machines. */
    private int numberOfVirtualMachines_;
    
    /** Submission time. */
    private long submissionTime_;
    
    /** Time to schedule. */
    private long timeToSchedule_;
    
    /** Number of failed allocations. */
    private int numberOfFailedAllocations_;

    /**
     * The scheduling results.
     * 
     * @param numberOfVirtualMachines           The number of submitted virtual machines
     * @param submissionTime                    The submission time
     * @param timeToSchedule                    The time to schedule
     * @param numberofFailedAllocations         The number of failed allocations
     */
    public SubmissionResults(int numberOfVirtualMachines,
                             long submissionTime,
                             long timeToSchedule,
                             int numberofFailedAllocations)
    {
        Guard.check(numberOfVirtualMachines, submissionTime, timeToSchedule, numberofFailedAllocations);
        log_.debug("Initializing the scheduling results");
        
        numberOfVirtualMachines_ = numberOfVirtualMachines;
        submissionTime_ = submissionTime;
        timeToSchedule_ = timeToSchedule;
        numberOfFailedAllocations_ = numberofFailedAllocations;
    }
    
    /**
     * Returns the number of submitted virtual machines.
     * 
     * @return  The number of submitted virtual machines
     */
    public int getNumberOfVirtualMachines() 
    {
        return numberOfVirtualMachines_;
    }

    /**
     * Returns the submission time.
     * 
     * @return  The submission time
     */
    public long getSubmissionTime() 
    {
        return submissionTime_;
    }

    /**
     * Returns the time to schedule.
     * 
     * @return  The time to schedule
     */
    public double getTimeToSchedule() 
    {
        return timeToSchedule_;
    }

    /**
     * Returns the number of successfull allocations.
     * 
     * @return  The number of succesfull allocations
     */
    public int getNumberOfSuccessfullAllocations() 
    {
        return numberOfVirtualMachines_ - numberOfFailedAllocations_;
    }

    /**
     * Returns the number of failed allocations.
     * 
     * @return  The number of failed allocations
     */
    public int getNumberOfFailedAllocations() 
    {
        return numberOfFailedAllocations_;
    }
}
