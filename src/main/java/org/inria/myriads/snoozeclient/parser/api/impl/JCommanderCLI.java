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
package org.inria.myriads.snoozeclient.parser.api.impl;

import java.util.HashMap;
import java.util.Map;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import org.inria.myriads.snoozeclient.parser.api.CommandLineParser;
import org.inria.myriads.snoozeclient.parser.api.impl.commands.AddCommand;
import org.inria.myriads.snoozeclient.parser.api.impl.commands.ClusterCommandBase;
import org.inria.myriads.snoozeclient.parser.api.impl.commands.Command;
import org.inria.myriads.snoozeclient.parser.api.impl.commands.DefineCommand;
import org.inria.myriads.snoozeclient.parser.api.impl.commands.DestroyCommand;
import org.inria.myriads.snoozeclient.parser.api.impl.commands.DumpCommand;
import org.inria.myriads.snoozeclient.parser.api.impl.commands.InfoCommand;
import org.inria.myriads.snoozeclient.parser.api.impl.commands.ListCommand;
import org.inria.myriads.snoozeclient.parser.api.impl.commands.MainCommand;
import org.inria.myriads.snoozeclient.parser.api.impl.commands.RebootCommand;
import org.inria.myriads.snoozeclient.parser.api.impl.commands.RemoveCommand;
import org.inria.myriads.snoozeclient.parser.api.impl.commands.ResizeCommand;
import org.inria.myriads.snoozeclient.parser.api.impl.commands.ResumeCommand;
import org.inria.myriads.snoozeclient.parser.api.impl.commands.ShutdownCommand;
import org.inria.myriads.snoozeclient.parser.api.impl.commands.StartCommand;
import org.inria.myriads.snoozeclient.parser.api.impl.commands.SuspendCommand;
import org.inria.myriads.snoozeclient.parser.api.impl.commands.UndefineCommand;
import org.inria.myriads.snoozeclient.parser.api.impl.commands.VisualizeCommand;
import org.inria.myriads.snoozeclient.parser.commands.ClientCommand;
import org.inria.myriads.snoozeclient.parser.output.ParserOutput;
import org.inria.myriads.snoozecommon.communication.virtualcluster.monitoring.NetworkDemand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the JCommander based CLI.
 * 
 * Based on the excellent Karl Heinz Marbaise tutorial:
 *  http://blog.soebes.de/index.php?/archives/241-Kommandozeile-und-Java.html
 *  https://github.com/khmarbaise/cli-test/tree/master/src/main/java/com/soebes/cli/cli_test
 * 
 * @author Eugen Feller
 */
public final class JCommanderCLI
    implements CommandLineParser 
{
    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(JCommanderCLI.class);
   
    /** Programm name. */
    private static final String PROGRAMM_NAME = "snoozeclient";
    
    /** Main command. */
    private MainCommand mainCommand_;
    
    /** Command list. */
    private Map<ClientCommand, Command> commands_;
    
    /** JCommander. */
    private JCommander commander_;
    
    /** Constructor. */
    public JCommanderCLI()
    {
        mainCommand_ = new MainCommand();
        commander_ = new JCommander(mainCommand_);
        commander_.setProgramName(PROGRAMM_NAME); 
        
        commands_ = new HashMap<ClientCommand, Command>();
        commands_.put(ClientCommand.DEFINE, new DefineCommand());
        commands_.put(ClientCommand.UNDEFINE, new UndefineCommand());
        commands_.put(ClientCommand.ADD, new AddCommand());
        commands_.put(ClientCommand.REMOVE, new RemoveCommand());
        commands_.put(ClientCommand.START, new StartCommand());
        commands_.put(ClientCommand.SHUTDOWN, new ShutdownCommand());
        commands_.put(ClientCommand.DESTROY, new DestroyCommand());
        commands_.put(ClientCommand.SUSPEND, new SuspendCommand());
        commands_.put(ClientCommand.RESUME, new ResumeCommand());
        commands_.put(ClientCommand.INFO, new InfoCommand());
        commands_.put(ClientCommand.LIST, new ListCommand());
        commands_.put(ClientCommand.VISUALIZE, new VisualizeCommand());
        commands_.put(ClientCommand.DUMP, new DumpCommand());
        commands_.put(ClientCommand.REBOOT, new RebootCommand());
        commands_.put(ClientCommand.RESIZE, new ResizeCommand());
        
        for (ClientCommand command : ClientCommand.values()) 
        {
            commander_.addCommand(command.getCommandName(), commands_.get(command));
        }
    }
    
    /**
     * Shows a warning.
     * 
     * @param warning   The warning to show
     */
    private void showWarning(String warning)
    {
        log_.warn("--------------------------------------------------------------------------------------");
        log_.warn("You have entered a wrong command or used wrong options or a combination of this.");
        log_.warn("");
        log_.warn(String.format("Message: %s",  warning));
        log_.warn("");
        log_.warn("Get help for all commands by entering: \"snoozeclient --help\"");
        log_.warn("Get help for a particular command by entering: \"snoozeclient command --help\"");
        log_.warn("--------------------------------------------------------------------------------------"); 
    }
    
    /** 
     * Helper function to generate a ParserOutput object from the JCInputOptions content.
     *  
     * @param args                  Arguments to parse
     * @return                      Parser output
     */
    @Override
    public ParserOutput parse(String[] args) 
    {
        log_.debug("Parsing the user input");
        
        ClientCommand command;
        String parameterException = null;
        try 
        {
            commander_.parse(args);
        } 
        catch (ParameterException exception) 
        {
            parameterException = exception.getMessage();
        } 
        finally
        {
            command = getCommand();        
            if (command == null || 
                    (args == null) || (args.length == 0) || 
                                       mainCommand_.isHelp()) 
            {
                commander_.usage();
                return null;
            }
            
            if (isHelpForCommand()) 
            {
                commander_.usage(command.getCommandName());
                return null;
            }
            
            if (parameterException != null)
            {
                showWarning(parameterException);
                return null;
            }
        }

        ParserOutput output = processCommand(command);
        if (output != null)
        {
            output.setClientCommand(command);
        }
        
        return output;
    }
    
    /**
     * Processes the client command.
     *  
     * @param command   The command
     * @return          The parser output
     */
    private ParserOutput processCommand(ClientCommand command)
    {
        ParserOutput output = new ParserOutput();
        switch (command)
        {                
            case DEFINE :
                output.setClusterName(getDefineCommand().getVirtualClusterName());
                break;
            
            case UNDEFINE :
                output.setClusterName(getUndefineCommand().getVirtualClusterName());
                break;
                
            case LIST :
                output.setClusterName(getListCommand().getVirtualClusterName());
                break;
                
            case ADD :
                output = addCommand(getAddCommand());
                break;
                
            case REMOVE :
                clusterCommand(getRemoveCommand(), output);
                break;
                
            case SUSPEND :
                clusterCommand(getSuspendCommand(), output);
                break;
           
            case RESUME :
                clusterCommand(getResumeCommand(), output);
                break;
                
            case START :
                clusterCommand(getStartCommand(), output);
                break;
                
            case SHUTDOWN :
                clusterCommand(getShutdownCommand(), output);
                break;
            
            case REBOOT :
                clusterCommand(getRebootCommand(), output);
                break;
                
            case DESTROY :
                clusterCommand(getDestroyCommand(), output);
                break;
            
            case INFO :
                clusterCommand(getInfoCommand(), output);
                break;
                
            case RESIZE :
                resizeCommand(getResizeCommand(), output);
                break;
            case VISUALIZE :
                output.setVisualize(true);
                break;
             
            case DUMP :
                output.setDump(true);
                break;
                
            default :
                log_.error(String.format("Unknown command specified: %s", command));
        }
        
        return output;
    }
    
    /**
     * 
     * Resize Command.
     * 
     * @param resizeCommand     The resize command
     * @param output            The parser output
     */
    private void resizeCommand(ResizeCommand resizeCommand, ParserOutput output)
    {
        output.setClusterName(resizeCommand.getVirtualClusterName());
        output.setVirtualMachineName(resizeCommand.getVirtualMachineName());
        output.setVcpu(resizeCommand.getVcpu());
        output.setMemory(resizeCommand.getMemory());
        output.setNetworkCapacity(new NetworkDemand(resizeCommand.getNetworkRxCapacity(), 
                resizeCommand.getNetworkTxCapacity()));
    }

    /**
     * Checks if help is specified.
     * 
     * @return  true if specified, false otherwise
     */ 
    private boolean isHelpForCommand() 
    {
        boolean result = false;
        ClientCommand command = getCommand();
        Command baseCommand = commands_.get(command);
        if (baseCommand == null) 
        {
            result = false;
        } else
        {
            result = baseCommand.isHelp();
        }
        
        return result;
    }
    
    /**
     * Returns the parsed command.
     * 
     * @return  The command
     */
    public ClientCommand getCommand() 
    {
        ClientCommand command = null;    
        for (ClientCommand item : ClientCommand.values()) 
        {
            if (item.getCommandName().equalsIgnoreCase(commander_.getParsedCommand())) 
            {
                command = item;
            }
        }
        
        return command;
    }
         
    /**
     * Add command.
     * 
     * @param addCommand        The add command
     * @return                  The parser output
     */
    private ParserOutput addCommand(AddCommand addCommand)
    {
        ParserOutput output = new ParserOutput();
        output.setClusterName(addCommand.getVirtualClusterName());
        output.setVirtualMachineTemplate(addCommand.getVirtualMachineTemplate());
        output.getNetworkCapacity().setRxBytes(addCommand.getNetworkRxCapacity());
        output.getNetworkCapacity().setTxBytes(addCommand.getNetworkTxCapaciy());
        return output;
    }
    
    /**
     * Cluster command.
     * 
     * @param clusterCommand    The cluster command
     * @param output            The parser output
     */
    private void clusterCommand(ClusterCommandBase clusterCommand, ParserOutput output)
    {
        output.setClusterName(clusterCommand.getVirtualClusterName());
        output.setVirtualMachineName(clusterCommand.getVirtualMachineName());
    }
    
    /**
     * Returns the add command.
     * 
     * @return   The add command
     */
    public AddCommand getAddCommand()
    {
        return (AddCommand) commands_.get(ClientCommand.ADD);
    }
    
    /**
     * Returns the remove command.
     * 
     * @return   The remove command
     */
    public RemoveCommand getRemoveCommand()
    {
        return (RemoveCommand) commands_.get(ClientCommand.REMOVE);
    }

    /**
     * Returns the define command.
     * 
     * @return   The define command
     */
    public DefineCommand getDefineCommand()
    {
        return (DefineCommand) commands_.get(ClientCommand.DEFINE);
    }

    /**
     * Returns the undefine command.
     * 
     * @return   The undefine command
     */
    public UndefineCommand getUndefineCommand()
    {
        return (UndefineCommand) commands_.get(ClientCommand.UNDEFINE);
    }
    
    /**
     * Returns the start command.
     * 
     * @return   The destroy command
     */
    public StartCommand getStartCommand()
    {
        return (StartCommand) commands_.get(ClientCommand.START);
    }
    
    /**
     * Returns the shutdown command.
     * 
     * @return   The shutdown command
     */
    public ShutdownCommand getShutdownCommand()
    {
        return (ShutdownCommand) commands_.get(ClientCommand.SHUTDOWN);
    }
    
    /**
     * Returns the reboot command.
     * 
     * @return   The reboot command
     */
    public RebootCommand getRebootCommand()
    {
        return (RebootCommand) commands_.get(ClientCommand.REBOOT);
    }
    
    /**
     * Returns the resize command.
     * 
     * @return   The reboot command
     */
    public ResizeCommand getResizeCommand()
    {
        return (ResizeCommand) commands_.get(ClientCommand.RESIZE);
    }
    
    /**
     * Returns the destroy command.
     * 
     * @return   The destroy command
     */
    public DestroyCommand getDestroyCommand()
    {
        return (DestroyCommand) commands_.get(ClientCommand.DESTROY);
    }
    
    /**
     * Returns the suspend command.
     * 
     * @return   The suspend command
     */
    public SuspendCommand getSuspendCommand()
    {
        return (SuspendCommand) commands_.get(ClientCommand.SUSPEND);
    }
    
    /**
     * Returns the resume command.
     * 
     * @return   The resume command
     */
    public ResumeCommand getResumeCommand()
    {
        return (ResumeCommand) commands_.get(ClientCommand.RESUME);
    }
    
    /**
     * Returns the info command.
     * 
     * @return   The info command
     */
    public InfoCommand getInfoCommand()
    {
        return (InfoCommand) commands_.get(ClientCommand.INFO);
    }
    
    /**
     * Returns the list command.
     * 
     * @return   The list command
     */
    public ListCommand getListCommand()
    {
        return (ListCommand) commands_.get(ClientCommand.LIST);
    }
}
