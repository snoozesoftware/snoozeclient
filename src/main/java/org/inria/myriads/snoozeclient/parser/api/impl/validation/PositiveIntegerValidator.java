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
package org.inria.myriads.snoozeclient.parser.api.impl.validation;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * Positive integer validation.
 * 
 * @author Eugen Feller
 */
public class PositiveIntegerValidator 
    implements IParameterValidator 
{
    /**
     * Valides the input.
     *
     * @param name                  The name
     * @param value                 The value
     * @throws ParameterException   The parameter exception
     */
    public void validate(String name, String value) 
        throws ParameterException 
    {
        int n = Integer.parseInt(value);
        if (n < 0) 
        {
            throw new ParameterException("Parameter " + name + " should be positive (found " + value + ")");
        }
    }
}
