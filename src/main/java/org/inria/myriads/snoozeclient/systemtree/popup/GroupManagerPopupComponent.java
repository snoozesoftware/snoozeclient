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

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.inria.myriads.snoozeclient.systemtree.SystemTreeVisualizer;
import org.inria.myriads.snoozecommon.communication.groupmanager.GroupManagerDescription;
import org.inria.myriads.snoozecommon.communication.groupmanager.repository.GroupLeaderRepositoryInformation;
import org.inria.myriads.snoozecommon.communication.groupmanager.summary.GroupManagerSummaryInformation;
import org.inria.myriads.snoozecommon.datastructure.LRUCache;
import org.inria.myriads.snoozecommon.globals.Globals;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *Group Manager Component.
 * 
 * @author msimonin
 *
 */
/**
 * @author msimonin
 *
 */
/**
 * @author msimonin
 *
 */
public class GroupManagerPopupComponent extends PopupComponent 
{
    /** default serial id.*/
    private static final long serialVersionUID = 1L;

    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(GroupManagerPopupComponent.class);

    /** Group Manager Description. */
    private GroupManagerDescription groupManagerDescription_;
   
    

   
   
    
    /**
    * Constructor.
    *  
    * @param groupManager           The group manager description
    * @param hostId                 The host id
    * @param systemTreeVisualizer   The system tree visualizer
    */
    public GroupManagerPopupComponent(GroupManagerDescription groupManager,
                                           String hostId, SystemTreeVisualizer systemTreeVisualizer)
    {
        super(systemTreeVisualizer);
        log_.debug("Creation of the new group manager component");
        groupManagerDescription_ = groupManager;
        
        
        initializeHostPanel();
        
        initializeGroupManagerPanel();
        display();               
        log_.debug("group manager component created with id " + getPopupComponentId());
    }

 
    
    /**
     * Initializes Group Manager Panel. 
     */
    private void initializeGroupManagerPanel() 
    {
       
       GridLayout usedSummaryLayout = new GridLayout(2, 2);
       usedSummaryPanel_.setLayout(usedSummaryLayout);
       initializeSummaryPanel();
   }

   @Override
   public boolean update(GroupLeaderRepositoryInformation hierarchy) 
   {
       updateHostDescription(hierarchy);
       SwingUtilities.invokeLater(new Runnable() 
       {
           public void run() 
           {
               usedSummaryPanel_.removeAll();
               GridLayout usedSummaryLayout = new GridLayout(2, 2);
               usedSummaryPanel_.setLayout(usedSummaryLayout);
               usedSummaryPanel_.setPreferredSize(new Dimension(800, 400));
               initializeSummaryPanel();
               usedSummaryPanel_.revalidate();
               usedSummaryPanel_.repaint();
           }
       });  
       
        
        return true;
   }
   
    
   /**
    * 
    * Update the hierarchy.
    * 
    * @param hierarchy              The new hierarchy
    */
   private void updateHostDescription(GroupLeaderRepositoryInformation hierarchy) 
   {
       log_.debug("Update the groupManager Popup" + getPopupComponentId());
       for (GroupManagerDescription groupManager : hierarchy.getGroupManagerDescriptions())
       {
           if (groupManager.getId().equals(groupManagerDescription_.getId()))
           {
               groupManagerDescription_ = groupManager;
           }
       }
   }



/**
    * 
    * initializes the host panel.
    * 
    * @return          true if everything ok.
    */
    boolean initializeHostPanel() 
    {
        JLabel label = new JLabel();
        label.setText(groupManagerDescription_.getHostname());
        getHostDescriptionPanel().add(label);
        label = new JLabel();
        label.setText(groupManagerDescription_.getListenSettings().getControlDataAddress().getAddress());
        getHostDescriptionPanel().add(label);
        label = new JLabel();
        label.setText("" + groupManagerDescription_.getListenSettings().getControlDataAddress().getPort());        
        getHostDescriptionPanel().add(label);
        
        return true;
    }

    /**
     * Initializes the summary Panel.
     */
    private void initializeSummaryPanel() 
    {
        LRUCache<Long, GroupManagerSummaryInformation> summaryInformation = 
                groupManagerDescription_.getSummaryInformation();
        XYSeries usedCPUCapacity = new XYSeries("Used ");
        XYSeries requestedCPUCapacity = new XYSeries("Requested ");
        
        XYSeries usedMemoryCapacity = new XYSeries("Used ");
        XYSeries requestedMemoryCapacity = new XYSeries("Requested ");
        
        XYSeries usedTxCapacity = new XYSeries("Used ");
        XYSeries requestedTxCapacity = new XYSeries("Requested ");
        
        XYSeries usedRxCapacity = new XYSeries("Used ");
        XYSeries requestedRxCapacity = new XYSeries("Requested ");
        
        int i = 0;
        for (Map.Entry<Long, GroupManagerSummaryInformation> entry : summaryInformation.entrySet()) 
        {
            Long timestamp = entry.getKey();
            GroupManagerSummaryInformation summary = entry.getValue();
            
            //cpu
            usedCPUCapacity.add(i, summary.getUsedCapacity().get(Globals.CPU_UTILIZATION_INDEX));
            requestedCPUCapacity.add(i, summary.getRequestedCapacity().get(Globals.CPU_UTILIZATION_INDEX));
            
            usedMemoryCapacity.add(i, summary.getUsedCapacity().get(Globals.MEMORY_UTILIZATION_INDEX));
            requestedMemoryCapacity.add(i, summary.getRequestedCapacity().get(Globals.MEMORY_UTILIZATION_INDEX));
            
            usedTxCapacity.add(i, summary.getUsedCapacity().get(Globals.NETWORK_TX_UTILIZATION_INDEX));
            requestedTxCapacity.add(i, summary.getRequestedCapacity().get(Globals.NETWORK_TX_UTILIZATION_INDEX));
            
            usedRxCapacity.add(i, summary.getUsedCapacity().get(Globals.NETWORK_RX_UTILIZATION_INDEX));
            requestedRxCapacity.add(i, summary.getRequestedCapacity().get(Globals.NETWORK_RX_UTILIZATION_INDEX));
            
            i++;

        }
        
        
        
        XYSeriesCollection collection = new XYSeriesCollection();
        collection.addSeries(usedCPUCapacity);
        collection.addSeries(requestedCPUCapacity);
        
        
        JFreeChart chart = ChartFactory.createXYLineChart("CPU",
                "x", "y", collection, PlotOrientation.VERTICAL, true, true,
                false);
        ChartPanel cp = new ChartPanel(chart);
        usedSummaryPanel_.add(cp);
         
        NumberAxis rangeAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        collection = new XYSeriesCollection();
        collection.addSeries(usedMemoryCapacity);
        collection.addSeries(requestedMemoryCapacity);
        
        chart = ChartFactory.createXYLineChart("Memory",
                "x", "y", collection, PlotOrientation.VERTICAL, true, true,
                false);
        
        rangeAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        cp = new ChartPanel(chart);
        usedSummaryPanel_.add(cp);
        
        collection = new XYSeriesCollection();
        collection.addSeries(usedTxCapacity);
        collection.addSeries(requestedTxCapacity);
        
        chart = ChartFactory.createXYLineChart("Tx",
                "x", "y", collection, PlotOrientation.VERTICAL, true, true,
                false);
        
        rangeAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        cp = new ChartPanel(chart);
        usedSummaryPanel_.add(cp);
        
        collection = new XYSeriesCollection();
        collection.addSeries(usedRxCapacity);
        collection.addSeries(requestedRxCapacity);
        
        chart = ChartFactory.createXYLineChart("Rx",
                "x", "y", collection, PlotOrientation.VERTICAL, true, true,
                false);
        
        rangeAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        cp = new ChartPanel(chart);
        usedSummaryPanel_.add(cp);
    }


    /**
     * 
     * Gets the description.
     * 
     * @return the groupManagerDescription
     */
    public GroupManagerDescription getGroupManagerDescription() 
    {
        return groupManagerDescription_;
    }

    /**
     * 
     * Sets the description.
     * 
     * @param groupManagerDescription the groupManagerDescription_ to set
     */
    public void setGroupManagerDescription_(
            GroupManagerDescription groupManagerDescription) 
    {
        this.groupManagerDescription_ = groupManagerDescription;
    }
}
