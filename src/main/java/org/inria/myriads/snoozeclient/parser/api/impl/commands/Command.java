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

/**
 * Command interface.
 *
 * Based on the excellent Karl Heinz Marbaise tutorial:
 *   http://blog.soebes.de/index.php?/archives/241-Kommandozeile-und-Java.html
 *   https://github.com/khmarbaise/cli-test/tree/master/src/main/java/com/soebes/cli/cli_test
 *  
 * @author Eugen Feller
 */
public interface Command 
{
    /** 
     * Checks if help is needed.
     * 
     * @return  true if needed, false otherwise
     */
    boolean isHelp();
}
