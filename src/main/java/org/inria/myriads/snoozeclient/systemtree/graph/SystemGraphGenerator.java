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

import org.inria.myriads.snoozeclient.exception.SystemTreeGeneratorException;
import org.inria.myriads.snoozeclient.systemtree.datacollector.SystemDataCollector;
import org.inria.myriads.snoozeclient.systemtree.enums.NodeType;
import org.inria.myriads.snoozeclient.systemtree.factory.EdgeFactory;
import org.inria.myriads.snoozecommon.communication.NetworkAddress;
import org.inria.myriads.snoozecommon.communication.groupmanager.GroupManagerDescription;
import org.inria.myriads.snoozecommon.communication.groupmanager.repository.GroupLeaderRepositoryInformation;
import org.inria.myriads.snoozecommon.communication.groupmanager.repository.GroupManagerRepositoryInformation;
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
    public Forest<String, Integer> generateGraph(GroupManagerDescription groupLeader) 
        throws SystemTreeGeneratorException 
    {        
        log_.debug("Starting graph generation");
                
        NetworkAddress groupLeaderAddress = groupLeader.getListenSettings().getControlDataAddress();
        GroupLeaderRepositoryInformation groupLeaderInformation =
            SystemDataCollector.getGroupLeaderRepositoryInformation(groupLeaderAddress,
                                                                    numberOfBacklogEntries_);
        if (groupLeaderInformation == null)
        {
            throw new SystemTreeGeneratorException("Group leader repository information is not available!");
        }
        
        String groupLeaderLabel = createNodeLabel(NodeType.GL, groupLeaderAddress);
        log_.info(String.format("Adding group leader node: %s", groupLeaderLabel));
        
        Forest<String, Integer> graph = new DelegateForest<String, Integer>();  
        graph.addVertex(groupLeaderLabel);
        
        List<GroupManagerDescription> groupManagers = groupLeaderInformation.getGroupManagerDescriptions();
        for (GroupManagerDescription groupManager : groupManagers) 
        {
            NetworkAddress address = groupManager.getListenSettings().getControlDataAddress();
            String label = createNodeLabel(NodeType.GM, address);           
            log_.info(String.format("Adding group manager: %s", label));
            graph.addEdge(edgeFactory_.create(), groupLeaderLabel, label);
            
            GroupManagerRepositoryInformation information = 
                SystemDataCollector.getGroupManagerRepositoryInformations(address,
                                                                          numberOfBacklogEntries_);
            if (information == null)
            {
                throw new SystemTreeGeneratorException("Group manager repository information is not available!");
            }
            
            addLocalControllerBranch(graph, label, information);
        }
        
        return graph;
    }
    
    /**
     * Adds a local controller branch.
     * 
     * @param graph                         The graph
     * @param groupManagerLabel             The group manager label
     * @param groupManagerInformation       The group manager information
     */
    private void addLocalControllerBranch(Forest<String, Integer> graph,   
                                          String groupManagerLabel,
                                          GroupManagerRepositoryInformation groupManagerInformation) 
    {        
        Guard.check(graph, groupManagerLabel, groupManagerInformation);
        log_.info(String.format("Adding %d local controller to %s", 
                                 groupManagerInformation.getLocalControllerDescriptions().size(),
                                 groupManagerLabel));
                  
        for (LocalControllerDescription localController : 
                 groupManagerInformation.getLocalControllerDescriptions()) 
        {            
            if (localController.getStatus().equals(LocalControllerStatus.PASSIVE))
            {
                log_.info("Ignoring local controller in PASSIVE mode!");
                continue;
            }
            
            String localControllerLabel = createNodeLabel(NodeType.LC, localController.getControlDataAddress());
            log_.info(String.format("Adding local controller: %s", localControllerLabel));
            graph.addEdge(edgeFactory_.create(), groupManagerLabel, localControllerLabel);        
            addVirtualMachineBranch(graph,
                                    localControllerLabel, 
                                    localController.getVirtualMachineMetaData());
        }
    }
    
    /**
     * Adds a virtual machine branch.
     * 
     * @param graph                     The graph
     * @param localControllerLabel      The local controller label
     * @param virtualMachines           The virtual machines
     */
    private void addVirtualMachineBranch(Forest<String, Integer> graph,
                                         String localControllerLabel,
                                         HashMap<String, VirtualMachineMetaData> virtualMachines)
    {
        Guard.check(graph, localControllerLabel, virtualMachines);
        log_.info(String.format("Adding virtual machine branch to: %s", localControllerLabel));
        
        for (Map.Entry<String, VirtualMachineMetaData> entry : virtualMachines.entrySet()) 
        {
            String virtualMachineLabel = NodeType.VM + "/" + entry.getKey();
            log_.info(String.format("Adding virtual machine: %s", virtualMachineLabel));
            graph.addEdge(edgeFactory_.create(), localControllerLabel, virtualMachineLabel);
        }       
    }
    
    /**
     * Creates the node label.
     * 
     * @param nodeType         The node type
     * @param networkAddress   The network address
     * @return                 The node label
     */
    private String createNodeLabel(NodeType nodeType, NetworkAddress networkAddress)
    {
        Guard.check(networkAddress);
        log_.debug(String.format("Creating node label for: %s", nodeType));
                
        String identifier = nodeType + "/" + networkAddress.getAddress() + ":" + networkAddress.getPort();
        return identifier;
    }
}
