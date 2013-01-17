package org.inria.myriads.snoozeclient.systemtree.transformers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

/**
 * 
 * Vertex Label Transformer (for geographical).
 * @deprecated 
 * @author msimonin
 *
 */
public class VertexLabelTransformer extends ToStringLabeller<String> 
{

    /** Logger. */
    private static final Logger log_ = LoggerFactory.getLogger(VertexLabelTransformer.class);
            
    @Override
    public String transform(String v) 
    {
        String str[]=v.split("/");
        String result = v;
        try
        {
            result = str[2];
        }
        catch(Exception exception)
        {
            log_.error("Wrong label format");
        }
        return "";
    }

    
    
}
