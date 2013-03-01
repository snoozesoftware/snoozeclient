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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.inria.myriads.snoozeclient.systemtree.SystemTreeVisualizer;
import org.inria.myriads.snoozecommon.communication.groupmanager.GroupManagerDescription;
import org.inria.myriads.snoozecommon.communication.groupmanager.repository.GroupLeaderRepositoryInformation;
import org.inria.myriads.snoozecommon.communication.localcontroller.LocalControllerDescription;
import org.inria.myriads.snoozecommon.communication.virtualcluster.VirtualMachineMetaData;
import org.inria.myriads.snoozecommon.communication.virtualcluster.monitoring.VirtualMachineMonitoringData;
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
 * Local Controller Component.
 * 
 * @author msimonin
 *
 */
public class LocalControllerPopupComponent  extends PopupComponent 
{
    
    /** default serial id.*/
   private static final long serialVersionUID = 1L;

   /** Monitoring values to take into account. */
   private static int MONITORING_VALUES = 1;
   
   /** Define the logger. */
   private static final Logger log_ = LoggerFactory.getLogger(GroupManagerPopupComponent.class);

   /** LocalControllerDescription.*/
   private LocalControllerDescription localController_;
      
   /** Resources Panel.*/
   private JPanel resourcesPanel_;
   
   /** Graph Panel.*/
   private JPanel graphPanel_;
   
   /** Cache values. */
   private LRUCache<Long, ArrayList<Double>> usedAverageCapacity_;
        
    /**
     * 
     * Constructor.
     * 
     * @param localController               Local controller description    
     * @param hostId                        Host id
     * @param systemTreeVisualizer          System Tree Visualizer to register to
     */
    public LocalControllerPopupComponent(LocalControllerDescription localController, 
                                            String hostId, SystemTreeVisualizer systemTreeVisualizer)
    {
        super(systemTreeVisualizer);
        usedAverageCapacity_ = new LRUCache<Long, ArrayList<Double>>(10); 
        log_.debug("Creation of the new local controller component");
        localController_ = localController;
        initializeHostPanel();
        initializeLocalControllerPanel();
        display();    
        log_.debug("local controller component created with id " + getPopupComponentId());
    }

    /**
     * 
     * Initializes the local controller panel.
     * 
     */
    private void initializeLocalControllerPanel() 
    {
        GridBagLayout usedSummaryLayout = new GridBagLayout();
        usedSummaryPanel_.setLayout(usedSummaryLayout);
        
        initializevirtualMachineSummaryPanel();
        
    }

    /**
     * 
     * Initializes virtual machine summary panel.
     * 
     */
    private void initializevirtualMachineSummaryPanel()
    {
     
        //Should display only the load 
        // CPU load / Treshold / Total for example
        double cpuUsed = 0; 
        double memUsed = 0;
        double rxUsed = 0;
        double txUsed = 0;
        
        resourcesPanel_ = new JPanel();
        resourcesPanel_.setLayout(new GridLayout(4, 2));
        
        
        graphPanel_ = new JPanel();
        graphPanel_.setLayout(new GridLayout(2, 2));
        
        int numberOfVirtualMachines = 0;
        for (Map.Entry<String, VirtualMachineMetaData> entry : localController_.getVirtualMachineMetaData().entrySet())
        {
            double cpuAverage = 0;
            double memAverage = 0;
            double rxAverage = 0;
            double txAverage = 0;
            
            String virtualMachineId = entry.getKey();
            VirtualMachineMetaData virtualMachine = entry.getValue();
            
            int i = 1;
            int numberOfEntry = virtualMachine.getUsedCapacity().size(); 
            for (Map.Entry<Long , VirtualMachineMonitoringData> monitoringEntry :
                        virtualMachine.getUsedCapacity().entrySet()) 
            {
                if (i > MONITORING_VALUES)
                {
                    break;
                }
                Long timestamp = monitoringEntry.getKey();
                VirtualMachineMonitoringData summary = monitoringEntry.getValue();
                      
                cpuAverage += summary.getUsedCapacity().get(Globals.CPU_UTILIZATION_INDEX);
                memAverage += summary.getUsedCapacity().get(Globals.MEMORY_UTILIZATION_INDEX);
                rxAverage += summary.getUsedCapacity().get(Globals.NETWORK_RX_UTILIZATION_INDEX);
                txAverage += summary.getUsedCapacity().get(Globals.NETWORK_TX_UTILIZATION_INDEX);
                
                
                i++;
            }
            
            if (i != 0)
            {
                cpuAverage = cpuAverage / i;
                memAverage = memAverage / i;
                rxAverage = rxAverage / i;
                txAverage = txAverage / i;
            }
            
            cpuUsed += cpuAverage;
            memUsed += memAverage;
            rxUsed += rxAverage;
            txUsed += txAverage;
            
        }
        

        
        DecimalFormat format = new DecimalFormat("#.##"); 
        JLabel cpuDemandLabel = new JLabel("Cpu Used : " + format.format(cpuUsed));
        double cpuTotal = localController_.getTotalCapacity().get(Globals.CPU_UTILIZATION_INDEX);
        JLabel cpuTotalLabel = new JLabel("Cpu Total : " + format.format(cpuTotal));
        
        JLabel memDemandLabel = new JLabel("Mem Used : " + format.format(memUsed));
        double memTotal = localController_.getTotalCapacity().get(Globals.MEMORY_UTILIZATION_INDEX);
        JLabel memTotalLabel = new JLabel("Mem Total : " + format.format(memTotal));
        
        JLabel rxDemandLabel = new JLabel("Rx Used : " + format.format(rxUsed));
        double rxTotal = localController_.getTotalCapacity().get(Globals.NETWORK_RX_UTILIZATION_INDEX);
        JLabel rxTotalLabel = new JLabel("Rx Total : " + format.format(rxTotal));
       
        JLabel txDemandLabel = new JLabel("Tx Used : " + format.format(txUsed));
        double txTotal = localController_.getTotalCapacity().get(Globals.NETWORK_TX_UTILIZATION_INDEX);
        JLabel txTotalLabel = new JLabel("Tx Total : " + format.format(txTotal));
        
        resourcesPanel_.add(cpuDemandLabel);
        resourcesPanel_.add(cpuTotalLabel);
        resourcesPanel_.add(memDemandLabel);
        resourcesPanel_.add(memTotalLabel);
        resourcesPanel_.add(rxDemandLabel);
        resourcesPanel_.add(rxTotalLabel);
        resourcesPanel_.add(txDemandLabel);
        resourcesPanel_.add(txTotalLabel);
        

        
        ArrayList<Double> currentAverage = new ArrayList<Double>();
        currentAverage.add(cpuUsed);
        currentAverage.add(memUsed);
        currentAverage.add(rxUsed);
        currentAverage.add(txUsed);
        Long timeStamp = new Timestamp(System.currentTimeMillis()).getTime();
        usedAverageCapacity_.put(timeStamp, currentAverage);
        
        

        XYSeries usedCPUCapacity = new XYSeries("Used ");
        XYSeries requestedCPUCapacity = new XYSeries("Total ");
        
        XYSeries usedMemoryCapacity = new XYSeries("Used ");
        XYSeries requestedMemoryCapacity = new XYSeries("Total ");
        
        XYSeries usedTxCapacity = new XYSeries("Used ");
        XYSeries requestedTxCapacity = new XYSeries("Total ");
        
        XYSeries usedRxCapacity = new XYSeries("Used ");
        XYSeries requestedRxCapacity = new XYSeries("Total ");
        int i = 0;
        for (Map.Entry<Long, ArrayList<Double>> entry : usedAverageCapacity_.entrySet())
        {
            
            Long timestamp = entry.getKey();
            ArrayList<Double> summary = entry.getValue();
            //cpu
            usedCPUCapacity.add(i, summary.get(Globals.CPU_UTILIZATION_INDEX));
            requestedCPUCapacity.add(i, cpuTotal);
            
            usedMemoryCapacity.add(i, summary.get(Globals.MEMORY_UTILIZATION_INDEX));
            requestedMemoryCapacity.add(i, memTotal);
            
            usedTxCapacity.add(i, summary.get(Globals.NETWORK_TX_UTILIZATION_INDEX));
            requestedTxCapacity.add(i, txTotal);
            
            usedRxCapacity.add(i, summary.get(Globals.NETWORK_RX_UTILIZATION_INDEX));
            requestedRxCapacity.add(i, rxTotal);
            
            i++;
        }
        
        XYSeriesCollection collection = new XYSeriesCollection();
        collection.addSeries(usedCPUCapacity);
        collection.addSeries(requestedCPUCapacity);
        
        
        JFreeChart chart = ChartFactory.createXYLineChart("CPU",
                "x", "y", collection, PlotOrientation.VERTICAL, true, true,
                false);
        ChartPanel cp = new ChartPanel(chart);
        graphPanel_.add(cp);
         
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
        graphPanel_.add(cp);
        
        collection = new XYSeriesCollection();
        collection.addSeries(usedTxCapacity);
        collection.addSeries(requestedTxCapacity);
        
        chart = ChartFactory.createXYLineChart("Tx",
                "x", "y", collection, PlotOrientation.VERTICAL, true, true,
                false);
        
        rangeAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        cp = new ChartPanel(chart);
        graphPanel_.add(cp);
        
        collection = new XYSeriesCollection();
        collection.addSeries(usedRxCapacity);
        collection.addSeries(requestedRxCapacity);
        
        chart = ChartFactory.createXYLineChart("Rx",
                "x", "y", collection, PlotOrientation.VERTICAL, true, true,
                false);
        
        rangeAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        cp = new ChartPanel(chart);
        graphPanel_.add(cp); 
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; 
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = 10; 
        c.weighty = 10;
        usedSummaryPanel_.add(resourcesPanel_, c);

        c.gridy = 1;
        c.weightx = 90;
        c.weighty = 90;
        c.insets = new Insets(2, 2, 2, 2);
        usedSummaryPanel_.add(graphPanel_, c);
        
        pack();
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
        label.setText(localController_.getHostname());
        getHostDescriptionPanel().add(label);
        label = new JLabel();
        label.setText(localController_.getControlDataAddress().getAddress());
        getHostDescriptionPanel().add(label);
        label = new JLabel();
        label.setText("" + localController_.getControlDataAddress().getPort());        
        getHostDescriptionPanel().add(label);
        return true;
    }
    
    /**
     * 
     * Update the popup component.
     * 
     * @param hierarchy         The hierarchy.
     * @return                  true if everything ok.
     */
    public boolean update(GroupLeaderRepositoryInformation hierarchy) 
    {
        updateHostDescription(hierarchy);
        SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
                usedSummaryPanel_.removeAll();
                //GridLayout usedSummaryLayout = new GridLayout(2, 2);

                //usedSummaryPanel_.setPreferredSize(new Dimension(800, 400));
                initializevirtualMachineSummaryPanel();
                
                usedSummaryPanel_.revalidate();
                usedSummaryPanel_.repaint();
            }
        });  
        
         
         return true;
    }

    /**
     * 
     * Updates the popup with the new hierarchy.
     * 
     * @param hierarchy             The new hierarchy
     */
    private void updateHostDescription(
            GroupLeaderRepositoryInformation hierarchy) 
    {
        log_.debug("Update the local Controller Popup" + getPopupComponentId());
        
        for (GroupManagerDescription groupManager : hierarchy.getGroupManagerDescriptions())
        {
            for (Map.Entry<String, LocalControllerDescription> entry : groupManager.getLocalControllers().entrySet())
            {
                if (entry.getKey().equals(localController_.getId()))
                {
                    localController_ = entry.getValue();
                    break;
                }
                    
            }
        }
    }

    
    
}
