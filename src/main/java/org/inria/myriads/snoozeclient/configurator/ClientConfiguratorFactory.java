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
package org.inria.myriads.snoozeclient.configurator;

import java.io.IOException;

import org.inria.myriads.snoozeclient.configurator.api.ClientConfigurator;
import org.inria.myriads.snoozeclient.configurator.api.impl.JavaPropertyClientConfigurator;
import org.inria.myriads.snoozeclient.exception.ClientConfiguratorException;

/**
 * Client configurator factory.
 * 
 * @author Eugen Feller
 */
public final class ClientConfiguratorFactory 
{
    /** Hide constructor. */
    private ClientConfiguratorFactory()
    {
        throw new UnsupportedOperationException();
    }
    /**
     * Creates a new client configurator.
     * 
     * @param configurationFile             The configuration file
     * @return                              The client configurator
     * @throws IOException                  The I/O exception
     * @throws ClientConfiguratorException  The client configurator exception
     */
    public static ClientConfigurator newClientConfigurator(String configurationFile) 
        throws IOException, ClientConfiguratorException
    {
        return new JavaPropertyClientConfigurator(configurationFile);
    }
}
