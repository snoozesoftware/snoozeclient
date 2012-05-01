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

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

import org.apache.commons.collections15.Transformer;
import org.inria.myriads.snoozeclient.systemtree.enums.NodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Vertex shape transformer.
 * 
 * @author Eugen Feller
 */
public final class VertexShapeTransformer 
    implements Transformer<String, Shape>
{
    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(VertexShapeTransformer.class);
    
    /** Shapes. */
    private final Shape[] shapes_ = 
    {
        new Rectangle(-30, -20, 50, 30), 
        new Ellipse2D.Double(-25, -10, 50, 20),
        new Arc2D.Double(-30, -15, 60, 30, 30, 30, Arc2D.PIE) 
    };
    
    /**
     * Transforms.
     * 
     * @param index     The index
     * @return          The color
     */
    public Shape transform(String index)
    {
        Shape shape;
        NodeType nodeType = NodeType.valueOf(index.substring(0, 2));
        switch (nodeType)
        {
            case GL :
                shape = shapes_[0];
                break;
                
            case GM :
                shape = shapes_[1];
                break;
                
            case LC :
                shape = shapes_[1];
                break;
                
            case VM :
                shape = shapes_[1];
                break;
                
            default :
                log_.error(String.format("Unknown node type selected: %d", nodeType));
                shape = shapes_[2];
                break;
        }
        
        return shape;
    }
}
