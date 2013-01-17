package org.inria.myriads.snoozeclient.systemtree.layout;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import org.inria.myriads.snoozeclient.systemtree.enums.NodeType;
import org.inria.myriads.snoozeclient.systemtree.util.DumpUtil;
import org.inria.myriads.snoozeclient.systemtree.vertex.SnoozeVertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.Forest;

/**
 * @author msimonin
 */
public class GeographicalLayout extends TreeLayout<SnoozeVertex, Integer> implements Layout<SnoozeVertex, Integer>{
    
     
    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(GeographicalLayout.class);
    
    /** Geographical roots*/
    Map<String,Point2D> roots_ = new HashMap<String, Point2D>();
    
    
    /** Geographical roots*/
    Map<String,int[]> nodesPerLevel_ = new HashMap<String, int[]>();

    private boolean initialized = false;
    /**
     * @param g
     * @param distx
     * @param disty
     */
    public GeographicalLayout(Forest<SnoozeVertex, Integer> g, int distx, int disty) 
    {
        super(g, distx, disty);
        initializeRoots();
    }

    /**
     * @param g
     * @param distx
     */
    public GeographicalLayout(Forest<SnoozeVertex, Integer> g, int distx) 
    {
        super(g, distx);
        initializeRoots();
    }

    /**
     * @param g
     */
    public GeographicalLayout(Forest<SnoozeVertex, Integer> g) 
    {
        super(g);
        initializeRoots();
    }
    
    
    
    private void initializeRoots() 
    {
        log_.error("initialize Root");
        roots_ = new HashMap<String, Point2D>();
        nodesPerLevel_ = new HashMap<String, int[]>();
        roots_.put("rennes", new Point2D.Double(50,50));
        nodesPerLevel_.put("rennes", new int[3]);
        roots_.put("nancy", new Point2D.Double(500,50));
        nodesPerLevel_.put("nancy", new int[3]);
        roots_.put("sophia", new Point2D.Double(600,400));
        nodesPerLevel_.put("sophia", new int[3]);
        roots_.put("mafalda", new Point2D.Double(500,50));
        nodesPerLevel_.put("mafalda", new int[3]);
        
        initialized=true;
    }

    protected void setCurrentPositionFor(SnoozeVertex vertex) {
        int x = m_currentPoint.x;
        int y = m_currentPoint.y;
        if(x < 0) size.width -= x;

        if(x > size.width-distX)
                size.width = x + distX;

        if(y < 0) size.height -= y;
        if(y > size.height-distY)
                size.height = y + distY;
        String hostName = vertex.getHostName() ;
        String site = "";
        try
        {
             site = getSite(hostName);    
        }
        catch(Exception e)
        {
            site = "mafalda";
        }
        

        
        //hmm this is needed
        if (!initialized)
        {
            initializeRoots();
        
        }
        Point2D root ;
        root = roots_.get(site);
        double xRoot = root.getX();
        double yRoot = root.getY();
        
        log_.debug("root = "+xRoot+","+yRoot);
        
        
        int n = 0;
        int y1 = 0;
        int x1 = 0;
                
        switch(vertex.getNodeType()){
            case GL :
                locations.get(vertex).setLocation(xRoot,yRoot);
                break;
            case GM : 
                n = nodesPerLevel_.get(site)[0];
                y1 = n/4;
                x1 = n%4;
                locations.get(vertex).setLocation(new Point2D.Double(xRoot+x1*60,yRoot+100+60*y1));
                nodesPerLevel_.get(site)[0]++;
                break;
            case LC :
            case LC_PASSIVE:
                n = nodesPerLevel_.get(site)[1];
                y1 = n/4;
                x1 = n%4;
                locations.get(vertex).setLocation(new Point2D.Double(xRoot+x1*60,yRoot+200+60*y1));
                nodesPerLevel_.get(site)[1]++;
                break;
            case VM :
                n = nodesPerLevel_.get(site)[2];
                y1 = n/4;
                x1 = n%4;
                locations.get(vertex).setLocation(new Point2D.Double(xRoot+x1*60,yRoot+300+60*y1));
                nodesPerLevel_.get(site)[2]++;
                break;
        }
        
    }
    
    private String getSite(String hostname) {
        String[] splitted = hostname.split("\\.");
        try{
            log_.debug("splitted hostname : " + hostname);
            log_.debug(splitted[0]);           
            return splitted[1];
        }
        catch(Exception e)
        {
            log_.debug("default hostname used");
            hostname="mafalda";
            return hostname;
        }
    }

    private int calculateDimensionX(SnoozeVertex v)
    {
        int size = 0;
        int childrenNum = graph.getSuccessors(v).size();
    
        if (childrenNum != 0) {
            for (SnoozeVertex element : graph.getSuccessors(v)) {
                size += calculateDimensionX(element) + distX;
            }
        }
        size = Math.max(0, size - distX);
        basePositions.put(v, size);
    
        return size;
    }
}
