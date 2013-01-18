package org.inria.myriads.snoozeclient.systemtree.popup;

import java.awt.Dimension;
import java.awt.GridLayout;
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


   /** Define the logger. */
   private static final Logger log_ = LoggerFactory.getLogger(GroupManagerPopupComponent.class);

   /** LocalControllerDescription.*/
   private LocalControllerDescription localController_;
      
   /** virtual machines panel.*/
   private JPanel virtualMachineSummaryPanel_;
        
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
        GridLayout usedSummaryLayout = new GridLayout(2, 2);
        virtualMachineSummaryPanel_ = new JPanel();
        virtualMachineSummaryPanel_.setLayout(usedSummaryLayout);
        virtualMachineSummaryPanel_.setPreferredSize(new Dimension(800, 400));
        initializevirtualMachineSummaryPanel();
        getContentPane().add(virtualMachineSummaryPanel_);
    }

    /**
     * 
     * Initializes virtual machine summary panel.
     * 
     */
    private void initializevirtualMachineSummaryPanel()
    {
        
        for (Map.Entry<String, VirtualMachineMetaData> entry : localController_.getVirtualMachineMetaData().entrySet())
        {
            String virtualMachineId = entry.getKey();
            VirtualMachineMetaData virtualMachine = entry.getValue();
            XYSeries usedCPUCapacity = new XYSeries("Used ");
            XYSeries totalCPUCapacity = new XYSeries("Requested ");
            
            XYSeries usedMemoryCapacity = new XYSeries("Used ");
            XYSeries totalMemoryCapacity = new XYSeries("Requested ");
            
            XYSeries usedTxCapacity = new XYSeries("Used ");
            XYSeries totalTxCapacity = new XYSeries("Requested ");
            
            XYSeries usedRxCapacity = new XYSeries("Used ");
            XYSeries totalRxCapacity = new XYSeries("Requested ");
            
            int i = 0;
            for (Map.Entry<Long , VirtualMachineMonitoringData> monitoringEntry :
                        virtualMachine.getUsedCapacity().entrySet()) 
            {
                Long timestamp = monitoringEntry.getKey();
                VirtualMachineMonitoringData summary = monitoringEntry.getValue();
                usedCPUCapacity.add(i, summary.getUsedCapacity().get(0));
                totalCPUCapacity.add(i, localController_.getTotalCapacity().get(0));
                
                usedMemoryCapacity.add(i, summary.getUsedCapacity().get(1));
                totalMemoryCapacity.add(i, localController_.getTotalCapacity().get(1));
                
                usedTxCapacity.add(i, summary.getUsedCapacity().get(2));
                totalTxCapacity.add(i, localController_.getTotalCapacity().get(2));
                
                usedRxCapacity.add(i, summary.getUsedCapacity().get(3));
                totalRxCapacity.add(i, localController_.getTotalCapacity().get(3));
                i++;
            }
            
            //CPU
            XYSeriesCollection collection = new XYSeriesCollection();
            collection.addSeries(usedCPUCapacity);
            collection.addSeries(totalCPUCapacity);
            JFreeChart chart = ChartFactory.createXYLineChart("CPU",
                    "x", "y", collection, PlotOrientation.VERTICAL, true, true,
                    false);
            NumberAxis rangeAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            ChartPanel cp = new ChartPanel(chart);
            virtualMachineSummaryPanel_.add(cp);
            
            //Memory
            collection = new XYSeriesCollection();
            collection.addSeries(usedMemoryCapacity);
            collection.addSeries(totalMemoryCapacity);
            chart = ChartFactory.createXYLineChart("Memory",
                    "x", "y", collection, PlotOrientation.VERTICAL, true, true,
                    false);
            rangeAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            cp = new ChartPanel(chart);
            virtualMachineSummaryPanel_.add(cp);
            
            //Tx
            collection = new XYSeriesCollection();
            collection.addSeries(usedTxCapacity);
            collection.addSeries(totalRxCapacity);
            chart = ChartFactory.createXYLineChart("Tx",
                    "x", "y", collection, PlotOrientation.VERTICAL, true, true,
                    false);
            rangeAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            cp = new ChartPanel(chart);
            virtualMachineSummaryPanel_.add(cp);
            
            //Rx
            collection = new XYSeriesCollection();
            collection.addSeries(usedTxCapacity);
            collection.addSeries(totalRxCapacity);
            chart = ChartFactory.createXYLineChart("Rx",
                    "x", "y", collection, PlotOrientation.VERTICAL, true, true,
                    false);
            rangeAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            cp = new ChartPanel(chart);
            virtualMachineSummaryPanel_.add(cp);
            
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
                virtualMachineSummaryPanel_.removeAll();
                GridLayout usedSummaryLayout = new GridLayout(2, 2);
                virtualMachineSummaryPanel_.setLayout(usedSummaryLayout);
                virtualMachineSummaryPanel_.setPreferredSize(new Dimension(800, 400));
                initializevirtualMachineSummaryPanel();
                virtualMachineSummaryPanel_.revalidate();
                virtualMachineSummaryPanel_.repaint();
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
