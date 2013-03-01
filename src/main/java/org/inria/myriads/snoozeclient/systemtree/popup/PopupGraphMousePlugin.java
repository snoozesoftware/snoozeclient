/**
 * Copyright (C) 2010-2013 Eugen Feller, INRIA <eugen.feller@inria.fr>
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
package org.inria.myriads.snoozeclient.systemtree.popup;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;

import org.inria.myriads.snoozeclient.systemtree.SystemTreeVisualizer;
import org.inria.myriads.snoozeclient.systemtree.vertex.SnoozeVertex;
import org.inria.myriads.snoozecommon.communication.groupmanager.GroupManagerDescription;
import org.inria.myriads.snoozecommon.communication.groupmanager.repository.GroupLeaderRepositoryInformation;
import org.inria.myriads.snoozecommon.communication.localcontroller.LocalControllerDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * PopupGraphMousePlugin.
 * 
 * @author msimonin
 *
 */
public class PopupGraphMousePlugin extends AbstractPopupGraphMousePlugin implements MouseListener 
{
    
        /** Define the logger. */
        private static final Logger log_ = LoggerFactory.getLogger(PopupGraphMousePlugin.class);
    
        /** The hierarchy. */
        private GroupLeaderRepositoryInformation hierarchy_;
        
        /** The system tree visualizer. */
        private SystemTreeVisualizer systemTreeVisualizer_;
        
        
        /**
         * 
         * Constructor.
         * 
         * @param hierarchy                 the hierarchy
         * @param systemTreeVisualizer      the systemTreeVisualizer
         */
        public PopupGraphMousePlugin(GroupLeaderRepositoryInformation hierarchy, 
                                        SystemTreeVisualizer systemTreeVisualizer) 
        {
            this(MouseEvent.BUTTON3_MASK);
            hierarchy_ = hierarchy;
            systemTreeVisualizer_ = systemTreeVisualizer;
        }
        
        
        /**
         * 
         * Constructor.
         * 
         * @param modifiers     modifiers
         */
        public PopupGraphMousePlugin(int modifiers) 
        {
            super(modifiers);
        }
        
        /**
         * 
         * Handle mouse event over vertex.
         * 
         * If this event is over a Vertex, pop up a menu a window with graphs
         * @param e                 MouseEvent.
         */
        @SuppressWarnings("unchecked")
        protected void handlePopup(MouseEvent e)
        {
            final VisualizationViewer<SnoozeVertex, Integer> vv = 
                (VisualizationViewer<SnoozeVertex, Integer>) e.getSource();
            Point2D p = e.getPoint();
            
            GraphElementAccessor<SnoozeVertex, Integer> pickSupport = vv.getPickSupport();
            if (pickSupport != null)
            {
                final SnoozeVertex v = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
                if (v != null) 
                {
                    String id = v.getHostId();
                    //seek on the hierarchy the id
                    boolean isConstructed = constructFrameFromNodeId(id);
                    // arrange the components inside the window
                    
                } 
                else 
                {
                    final Number edge = pickSupport.getEdge(vv.getGraphLayout(), p.getX(), p.getY());
                    if (edge != null)
                    {
                        JPopupMenu popup = new JPopupMenu();
                        popup.add(new AbstractAction(edge.toString()) {
                            public void actionPerformed(ActionEvent e) 
                            {
                                System.err.println("got " + edge);
                            }
                        });
                        popup.show(vv, e.getX(), e.getY());
                       
                    }
                }
            }
        }
        
        
        /**
         * 
         * Construct a popup windows based on the node type of the vertex.
         * 
         * @param hostId        Host Id.
         * @return              True if everything ok, false otherwise.
         */
        private boolean constructFrameFromNodeId(String hostId) 
        {
            for (GroupManagerDescription groupManager : hierarchy_.getGroupManagerDescriptions())
            {
                if (groupManager.getId().equals(hostId))
                {
                    //constructGroupManagerFrame(f, groupManager);
                    log_.debug("Construct new group manager component");
                    PopupComponent popupComponent = 
                            new GroupManagerPopupComponent(groupManager, hostId, systemTreeVisualizer_);
                    popupComponent.display();
                    systemTreeVisualizer_.register(popupComponent);
                    return true;
                }
                for (Map.Entry<String, LocalControllerDescription> entry : 
                            groupManager.getLocalControllers().entrySet())
                {
                    String localControllerId = entry.getKey();
                    LocalControllerDescription localController = entry.getValue();
                    if (localControllerId.equals(hostId))
                    {
                        PopupComponent popupComponent = 
                                new LocalControllerPopupComponent(localController, hostId, systemTreeVisualizer_);
                        popupComponent.display();
                        systemTreeVisualizer_.register(popupComponent);
                    }
                    
                }
            }
            return false;
            
        }
        


}
