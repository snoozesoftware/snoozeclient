package org.inria.myriads.snoozeclient.systemtree.transformers;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

import org.apache.commons.collections15.Transformer;
import org.inria.myriads.snoozeclient.systemtree.enums.NodeType;
import org.inria.myriads.snoozeclient.systemtree.vertex.SnoozeVertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Vertex transformer.
 * 
 * @author msimonin
 *
 */
public class GeographicalVertexShapeTransformer implements Transformer<SnoozeVertex, Shape> {

    
    
    /** Logger. */
    private static final Logger log_ = LoggerFactory.getLogger(GeographicalVertexShapeTransformer.class);
            
    /** Shapes. */
        private Shape[] shapes_ = 
        {
            new Rectangle(-24, -16, 48, 24), 
            new Ellipse2D.Double(-20, -8, 40, 16),
            new Arc2D.Double(-24, -12, 48, 24, 24, 24, Arc2D.PIE) 
        };

        /**
         * Transforms.
         * 
         * @param index     The index
         * @return          The color
         */
        public Shape transform(SnoozeVertex vertex)
        {
            Shape shape;
            NodeType nodeType = vertex.getNodeType();
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
