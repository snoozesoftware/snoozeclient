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
package org.inria.myriads.snoozeclient.parser;

import org.inria.myriads.snoozeclient.parser.api.CommandLineParser;
import org.inria.myriads.snoozeclient.parser.api.impl.JCommanderCLI;

/**
 * Command line parser factory.
 * 
 * @author Eugen Feller
 */
public final class CommandLineParserFactory
{
    /** Hide the constructor. */
    private CommandLineParserFactory() 
    {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Creates a new parser instance.
     * 
     * @return      Command line parser instance
     */
    public static CommandLineParser newParser() 
    {
        return new JCommanderCLI();
    }
}

