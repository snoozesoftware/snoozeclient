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
package org.inria.myriads.snoozeclient.systemtree.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.io.GraphMLWriter;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.TransformerUtils;
import org.inria.myriads.snoozecommon.guard.Guard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * System tree utilities.
 * 
 * @author Eugen Feller
 */
public final class DumpUtil 
{
    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(DumpUtil.class);
    
    /** Hide constructor. */
    private DumpUtil()
    {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Writes a graph to file in GraphML format.
     * 
     * @param graph         The graph
     * @param fileName      The file name
     * @throws IOException  The exception
     */
    @SuppressWarnings("unchecked")
    public static void writeGraph(Graph<String, Integer> graph, String fileName) 
        throws IOException
    {
        Guard.check(graph, fileName);
        log_.debug("Writing grahp in GraphML format to disk");
                        
        Transformer<String, String> vertexData = TransformerUtils.nopTransformer();
        GraphMLWriter<String, Integer> graphWriter = new GraphMLWriter<String, Integer>();
        graphWriter.addVertexData("data", null, null, vertexData);
        
        Transformer<String, String> vertexId = new Transformer<String, String>() 
        { 
            public String transform(String vertexLabel) 
            { 
                return String.valueOf(Math.abs(vertexLabel.hashCode()));
            } 
        };     
        graphWriter.setVertexIDs(vertexId);
        
        PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));      
        graphWriter.save(graph, printWriter);
    }
}
