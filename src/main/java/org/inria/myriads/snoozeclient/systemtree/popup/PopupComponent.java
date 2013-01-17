package org.inria.myriads.snoozeclient.systemtree.popup;

import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.inria.myriads.snoozeclient.systemtree.SystemTreeVisualizer;
import org.inria.myriads.snoozecommon.communication.groupmanager.repository.GroupLeaderRepositoryInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * PopupComponent.
 * 
 * @author msimonin
 *
 */
/**
 * @author msimonin
 *
 */
public abstract class PopupComponent extends JFrame 
{
    
    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(GroupManagerPopupComponent.class);

    /** The Component id. */
    protected String popupComponentId_;
    
    /** The system tree visualizer. */
    private SystemTreeVisualizer systemTreeVisualizer_;
    
    /** Main panel. */
    protected JPanel hostDescriptionPanel_;
    
    /**
     * 
     * Constructor.
     * 
     * @param systemTreeVisualizer      The system Tree Visualizer.
     * @throws HeadlessException        exception
     */
    public PopupComponent(SystemTreeVisualizer systemTreeVisualizer) throws HeadlessException
    {
        super("Host Description");        
        systemTreeVisualizer_ = systemTreeVisualizer;
        initializeGui();
        popupComponentId_ = UUID.randomUUID().toString();
    }



    /**
     * 
     * Constructor.
     * 
     * @param title                 The title.
     * @throws HeadlessException    Exception
     */
    public PopupComponent(String title) throws HeadlessException 
    {
        super(title);
    }



    /**
     * 
     * Initializes GUI.
     * 
     */
    public void initializeGui()
    {
        
        setResizable(false);
        setDefaultLookAndFeelDecorated(false);
        GridBagLayout hostLayout = new GridBagLayout();       
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(hostLayout);
        
        hostDescriptionPanel_ = new JPanel();
        hostDescriptionPanel_.setBorder(BorderFactory.createTitledBorder("Host description"));
        
        add(hostDescriptionPanel_);
        
        addWindowListener(new WindowListener() {
            
            public void windowActivated(WindowEvent arg0) 
            {
            }
            public void windowClosing(WindowEvent arg0) 
            {
                log_.debug("===============");
                log_.debug("Closing Windows");
                systemTreeVisualizer_.remove(popupComponentId_);
                dispose();
            }
            public void windowDeactivated(WindowEvent arg0) 
            {
            }
            public void windowDeiconified(WindowEvent arg0) 
            {
            }
            public void windowIconified(WindowEvent arg0) 
            {
            }
            public void windowOpened(WindowEvent arg0) 
            {
            }
            public void windowClosed(WindowEvent e) 
            {
            }
        });       
    }

    /**
     * 
     * Display the popup.
     * 
     */
    public void display() 
    {
         pack();
         setVisible(true);
    }

    /**
     * @return the popupComponentId_
     */
    public String getPopupComponentId() 
    {
        return popupComponentId_;
    }



    /**
     * 
     * Sets the popup component id.
     * 
     * @param popupComponentId the popupComponentId to set
     */
    public void setPopupComponentId(String popupComponentId) 
    {
        popupComponentId_ = popupComponentId;
    }
    
    /**
     * 
     * Update the popup component.
     * 
     * @param hierarchy         The hierarchy.
     * @return                  true if everything ok.
     */
    public abstract boolean update(GroupLeaderRepositoryInformation hierarchy);
    
    /**
     * 
     * initializes the host panel.
     * 
     * @return          true if everything ok.
     */
    abstract boolean initializeHostPanel();




}
