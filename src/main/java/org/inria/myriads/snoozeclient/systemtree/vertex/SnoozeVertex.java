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
package org.inria.myriads.snoozeclient.systemtree.vertex;

import org.inria.myriads.snoozeclient.systemtree.enums.NodeType;

/**
 * 
 * Vertex class.
 * 
 * @author msimonin
 *
 */
public class SnoozeVertex 
{

    /** Node Type.*/
    private NodeType nodeType_;
    
    /** Host Id.*/
    private String hostId_;
    
    /** Host Name.*/
    private String hostName_;

    /**
     * 
     * Constructor.
     * 
     * @param nodeType      The node type.
     * @param hostId        The host id.
     * @param hostName      The host name.
     */
    public SnoozeVertex(NodeType nodeType, String hostId, String hostName)
    {
        nodeType_ = nodeType;
        hostId_ = hostId;
        hostName_ = hostName;
    
    }


    /**
     * 
     * Gets the type.
     * 
     * @return the nodeType_
     */
    public NodeType getNodeType() 
    {
        return nodeType_;
    }

    /**
     * 
     * Sets the type.
     * 
     * @param nodeType      the nodeType to set
     */
    public void setNodeType(NodeType nodeType) 
    {
        nodeType_ = nodeType;
    }

    /**
     * Gets the host id.
     * 
     * @return the hostId
     */
    public String getHostId() 
    {
        return hostId_;
    }

    /**
     * 
     * Sets the host id.
     * 
     * @param hostId the hostId to set
     */
    public void setHostId(String hostId) 
    {
        hostId_ = hostId;
    }


    @Override
    public boolean equals(Object obj)
    {
        return ((SnoozeVertex) obj).getHostId().equals(this.getHostId());
    }

    
    @Override
    public String toString() 
    {
            return String.valueOf(this.getNodeType()); 
    }
    
    @Override
    public int hashCode()
    {
        return hostId_.hashCode();
    }


    /**
     * @return the hostName
     */
    public String getHostName() 
    {
        return hostName_;
    }


    /**
     * @param hostName the hostName to set
     */
    public void setHostName_(String hostName) 
    {
        hostName_ = hostName;
    }

}
