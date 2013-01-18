package org.inria.myriads.snoozeclient.systemtree.transformers;

import org.apache.commons.collections15.Transformer;
import org.inria.myriads.snoozeclient.systemtree.vertex.SnoozeVertex;

/**
 * 
 * Tooltip transfomer.
 * Tooltip for vertex hovering.
 * 
 * @author msimonin
 *
 */
public class VertexToolTipTransformer implements Transformer<SnoozeVertex, String> 
{

    @Override
    public String transform(SnoozeVertex vertex) 
    {
        return vertex.getHostName();
     }

}
