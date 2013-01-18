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
package org.inria.myriads.snoozeclient.systemtree.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;

import org.inria.myriads.snoozeclient.systemtree.enums.NodeType;
import org.inria.myriads.snoozeclient.systemtree.factory.EdgeFactory;
import org.inria.myriads.snoozeclient.systemtree.vertex.SnoozeVertex;
import org.inria.myriads.snoozecommon.communication.groupmanager.GroupManagerDescription;
import org.inria.myriads.snoozecommon.communication.groupmanager.repository.GroupLeaderRepositoryInformation;
import org.inria.myriads.snoozecommon.communication.localcontroller.LocalControllerDescription;
import org.inria.myriads.snoozecommon.communication.localcontroller.LocalControllerStatus;
import org.inria.myriads.snoozecommon.communication.virtualcluster.VirtualMachineMetaData;
import org.inria.myriads.snoozecommon.guard.Guard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Graph generator.
 * 
 * @author Eugen Feller
 */
public final class SystemGraphGenerator 
{
    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(SystemGraphGenerator.class);
    
    /** Edge factory. */    
    private EdgeFactory edgeFactory_;

    /** Number of backlog entries. */
    private int numberOfBacklogEntries_;
    
    /**
     * Constructor.
     * 
     * @param numberOfBacklogEntries    The number of backlog entries
     */
    public SystemGraphGenerator(int numberOfBacklogEntries) 
    {
        log_.debug("Initializing the system  graph generator");
        numberOfBacklogEntries_ = numberOfBacklogEntries;
        edgeFactory_ = new EdgeFactory();
    }
    
    /**
     * Generates the graph.
     *
     * @param groupLeader                        The group leader description
     * @return                                   The graph
     * @throws SystemTreeGeneratorException 
     */

   
    /**
     * Adds a virtual machine branch.
     * 
     * @param graph                     The graph
     * @param localControllerVertex      The local controller vertex
     * @param virtualMachines           The virtual machines
     */
    private void addVirtualMachineBranch(Forest<SnoozeVertex, Integer> graph,
                                         SnoozeVertex localControllerVertex,
                                         HashMap<String, VirtualMachineMetaData> virtualMachines)
    {
        Guard.check(graph, localControllerVertex, virtualMachines);
        log_.info(String.format("Adding virtual machine branch to: %s", localControllerVertex.getHostId()));
        
        for (Map.Entry<String, VirtualMachineMetaData> entry : virtualMachines.entrySet()) 
        {
            String virtualMachineId = entry.getKey();
            String virtualMachineName = virtualMachineId;
            SnoozeVertex virtualMachineVertex = new SnoozeVertex(NodeType.VM, virtualMachineId, virtualMachineName);
            log_.info(String.format("Adding virtual machine: %s", virtualMachineVertex.getHostId()));
            graph.addEdge(edgeFactory_.create(), localControllerVertex, virtualMachineVertex);
        }       
    }
    
    

    /**
     * 
     * Generates the graph according to the hierarchy.
     * 
     * @param hierarchy     The hierarchy.
     * @return              The graph.
     */
    public Forest<SnoozeVertex, Integer> generateGraph(GroupLeaderRepositoryInformation hierarchy) 
    {
        log_.debug("Starting graph generation");
        
        Forest<SnoozeVertex, Integer> graph = new DelegateForest<SnoozeVertex, Integer>();
        SnoozeVertex groupLeaderVertex = new SnoozeVertex(NodeType.GL, "0", "");
        log_.info(String.format("Adding group leader node: %s", groupLeaderVertex.getHostId()));
        graph.addVertex(groupLeaderVertex);
        
        List<GroupManagerDescription> groupManagers = hierarchy.getGroupManagerDescriptions();
        for (GroupManagerDescription groupManager : groupManagers) 
        {
            //String groupManagerLabel = createNodeLabel(NodeType.GM, groupManager.getId());
            SnoozeVertex groupManagerVertex = new SnoozeVertex(NodeType.GM, 
                                                                groupManager.getId(),
                                                                groupManager.getHostname());
            log_.info(String.format("Adding group manager: %s", groupManagerVertex.getHostId()));
            graph.addEdge(edgeFactory_.create(), groupLeaderVertex, groupManagerVertex);
            
            if (groupManager.getLocalControllers() != null)
                addLocalControllerBranch(graph, groupManagerVertex, groupManager.getLocalControllers());
        }
        
        return graph;
    }

    /**
     * 
     * Add a local controller branch to the graph.
     * 
     * @param graph                     the graph under construction.
     * @param groupManagerVertex        the vertex
     * @param localControllers          the local controllers
     */
    private void addLocalControllerBranch(Forest<SnoozeVertex, Integer> graph,
            SnoozeVertex groupManagerVertex,
            HashMap<String, LocalControllerDescription> localControllers) 
    {

        Guard.check(graph, groupManagerVertex, localControllers);
        log_.info(String.format("Adding %d local controller to %s", 
                                 localControllers.size(),
                                 groupManagerVertex));
               
        
        for (Map.Entry<String, LocalControllerDescription> entry : localControllers.entrySet()) 
        {
            LocalControllerDescription localController = entry.getValue();
            NodeType nodeType = NodeType.LC;
            if (localController.getStatus().equals(LocalControllerStatus.PASSIVE))
            {
                log_.info("local controller in PASSIVE mode!");
                nodeType = NodeType.LC_PASSIVE;
            }

            SnoozeVertex localControllerVertex = new SnoozeVertex(nodeType,
                                                                  localController.getId(),
                                                                  localController.getHostname());
            log_.info(String.format("Adding local controller: %s", localControllerVertex));
            graph.addEdge(edgeFactory_.create(), groupManagerVertex, localControllerVertex);        
            addVirtualMachineBranch(graph,
                                    localControllerVertex, 
                                    localController.getVirtualMachineMetaData());
        }
        
    }

}
