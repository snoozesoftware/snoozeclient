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
package org.inria.myriads.snoozeclient.main;

import java.io.IOException;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;

import com.beust.jcommander.ParameterException;
import org.inria.myriads.snoozeclient.configurator.ClientConfiguratorFactory;
import org.inria.myriads.snoozeclient.configurator.api.ClientConfiguration;
import org.inria.myriads.snoozeclient.configurator.api.ClientConfigurator;
import org.inria.myriads.snoozeclient.database.DatabaseFactory;
import org.inria.myriads.snoozeclient.database.api.ClientRepository;
import org.inria.myriads.snoozeclient.exception.BootstrapUtilityException;
import org.inria.myriads.snoozeclient.exception.ClientConfiguratorException;
import org.inria.myriads.snoozeclient.exception.CommandHandlerException;
import org.inria.myriads.snoozeclient.exception.SystemTreeGeneratorException;
import org.inria.myriads.snoozeclient.globals.Globals;
import org.inria.myriads.snoozeclient.handler.CommandHandler;
import org.inria.myriads.snoozeclient.parser.CommandLineParserFactory;
import org.inria.myriads.snoozeclient.parser.api.CommandLineParser;
import org.inria.myriads.snoozeclient.parser.output.ParserOutput;
import org.inria.myriads.snoozeclient.util.OutputUtils;
import org.inria.myriads.snoozeclient.util.StorageUtils;
import org.inria.myriads.snoozecommon.util.ErrorUtils;
import org.inria.myriads.snoozecommon.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXParseException;

/**
 * Main client class.
 * 
 * @author Eugen Feller
 */
public final class Main
{           
    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(Main.class);
        
    /** Hide constructor. */
    private Main()
    {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Returns the client configuration.
     * 
     * @return                               The client configuration
     * @throws IOException                   The I/O exception
     * @throws ClientConfiguratorException   The client configurator exception
     */
    private static ClientConfiguration getClientConfiguration() 
        throws IOException, ClientConfiguratorException
    {
        log_.debug("Getting the client parameters");
        
        ClientConfigurator clientConfigurator = ClientConfiguratorFactory.newClientConfigurator(Globals.CLIENT_CONFIG);
        ClientConfiguration clientConfiguration = clientConfigurator.getConfiguration();
        OutputUtils.printConfiguration(clientConfiguration);
        return clientConfiguration;
    }
    
    /**
     * Returns the client repository.
     * 
     * @return              The client repository
     * @throws Exception    The exception
     */
    private static ClientRepository getClientRepository() 
        throws Exception
    {
        log_.debug("Getting the client repository");   
        String databaseFileName = StorageUtils.getDatabaseFileName();
        ClientRepository clientRepository = DatabaseFactory.newClientRepository(databaseFileName);
        return clientRepository;
    }
    
    /**
     * Main method.
     * 
     * @param args  Arguments
     */
    public static void main(String[] args) 
    {          
        LoggerUtils.configureLogger(Globals.LOG4J_CONFIG);
        
        log_.debug("Starting Snooze client");   
        
        CommandLineParser parser = CommandLineParserFactory.newParser();
        try 
        {            
            ParserOutput parserOutput = parser.parse(args);     
            if (parserOutput == null)
            {
                log_.debug("No parser output available!");
                return;
            }
                    
            boolean isCreated = StorageUtils.createRepositoryDirectory();
            if (isCreated) 
            {
                log_.debug("Repository directory created successfully!");
            } else
            {
                log_.debug("Repository directory seems to exist! Good!");
            }
            
            ClientRepository clientRepository = getClientRepository();
            ClientConfiguration clientConfiguration = getClientConfiguration();
            CommandHandler commandHandler = new CommandHandler(clientConfiguration, clientRepository, parserOutput);
            commandHandler.dispatchCommand();
        }
        catch (ParseException exception)
        {
            log_.warn(String.format("%s", exception.getMessage()));
        }
        catch (CommandHandlerException exception)
        {
            log_.warn(String.format("%s", exception.getMessage()));
        }
        catch (BootstrapUtilityException exception)
        {
            log_.warn(String.format("%s", exception.getMessage()));
        }
        catch (ParameterException exception)
        {
            ErrorUtils.processError(String.format("Command line interface processing error: %s", 
                                                  exception.getMessage()));
        }
        catch (SystemTreeGeneratorException exception)
        {
            ErrorUtils.processError(String.format("System tree generation error: %s", exception.getMessage()));      
        }
        catch (ClientConfiguratorException exception)
        {
            ErrorUtils.processError(String.format("Client configuration error: %s", exception.getMessage()));
        } 
        catch (SAXParseException exception)
        {
            ErrorUtils.processError(String.format("Repository parsing error: %s", exception.getMessage()));
        }
        catch (IOException exception)
        {
            ErrorUtils.processError(String.format("I/O error: %s", exception.getMessage()));
        }
        catch (ParserConfigurationException exception)
        {
            ErrorUtils.processError(String.format("XML parser configuration error: %s", 
                                                  ErrorUtils.getStackTrace(exception)));
        }
        catch (IllegalArgumentException exception)
        {
            ErrorUtils.processError(String.format("Illegal argument: %s", ErrorUtils.getStackTrace(exception)));
        }
        catch (Exception exception)
        {
            ErrorUtils.processError(String.format("Exception: %s", ErrorUtils.getStackTrace(exception)));
        }   
    }   
}
