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
package org.inria.myriads.snoozeclient.parser.commands;

/**
 * Client commands.
 * 
 * @author Eugen Feller
 */
public enum ClientCommand 
{
    /** Define. */
    DEFINE("define"),
    /** Undefine. */
    UNDEFINE("undefine"),
    /** Add. */
    ADD("add"),
    /** Remove. */
    REMOVE("remove"),
    /** Start. */
    START("start"),
    /** Suspend. */
    SUSPEND("suspend"),
    /** Resume. */
    RESUME("resume"),
    /** Shutdown. */
    SHUTDOWN("shutdown"),
    /** Destroy. */
    DESTROY("destroy"),
    /** Info. */
    INFO("info"),
    /** List. */
    LIST("list"),
    /** Visualize. */
    VISUALIZE("visualize"),
    /** Dump. */
    DUMP("dump"),
    /** Reboot. */
    REBOOT("reboot");
    
    /** Command name. */
    private String commandName_;
    
    /**
     * Constructor.
     * 
     * @param commandName   The command name
     */
    private ClientCommand(String commandName) 
    {
        commandName_ = commandName;
    }
    
    /**
     * Returns the command name.
     * 
     * @return    The command name
     */
    public String getCommandName() 
    {
        return commandName_;
    }
}
