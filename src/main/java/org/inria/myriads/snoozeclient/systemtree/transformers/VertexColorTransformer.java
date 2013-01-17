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
package org.inria.myriads.snoozeclient.systemtree.transformers;

import java.awt.Color;
import java.awt.Paint;

import org.apache.commons.collections15.Transformer;
import org.inria.myriads.snoozeclient.systemtree.enums.NodeType;
import org.inria.myriads.snoozeclient.systemtree.vertex.SnoozeVertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Vertex color transformer.
 * 
 * @author Eugen Feller
 */
public final class VertexColorTransformer 
    implements Transformer<SnoozeVertex, Paint>
{
    /** Logger. */
    private static final Logger log_ = LoggerFactory.getLogger(VertexColorTransformer.class);
    
    /** Colors. */
    private final Color[] colors_ = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.BLACK, Color.LIGHT_GRAY}; 

    /**
     * Transforms.
     * @param vertex           The vertex to transform.
     * @return          The color
     */
    public Paint transform(SnoozeVertex vertex)
    {
        Color color;
        //NodeType nodeType = NodeType.valueOf(index.substring(0, 2));
        NodeType nodeType = vertex.getNodeType();
        switch (nodeType)
        {
            case GL :
                color = colors_[0];
                break;
                
            case GM :
                color = colors_[1];
                break;
                
            case LC :
                color = colors_[2];
                break;
            case LC_PASSIVE :
                color = colors_[5];
                break;
            case VM :
                color = colors_[3];
                break;
                
            default :
                log_.error(String.format("Unknown node type selected: %d", nodeType));
                color = colors_[4];
                break;
        }
        
        return color;
    }
}
