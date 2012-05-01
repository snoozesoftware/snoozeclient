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
package org.inria.myriads.snoozeclient.parser.api.impl.commands;

import com.beust.jcommander.Parameter;

/**
 * Base command.
 * 
 * @author Eugen Feller
 */
abstract class HelpCommandBase 
    implements Command
{
    /** Help. */
    @Parameter(names = {"--help", "-help", "-?", "-h" }, description = "Get help for the particular command.")
    private boolean help_;

    /**
     * Checks if help is needed.
     * 
     * @return  true if needed, false otherwise
     */
    @Override
    public boolean isHelp() 
    {
        return help_;
    }
}
