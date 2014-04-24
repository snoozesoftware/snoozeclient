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
package org.inria.myriads.snoozeclient.database.api.impl;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.inria.myriads.snoozeclient.database.api.AttributeType;
import org.inria.myriads.snoozeclient.database.api.ClientRepository;
import org.inria.myriads.snoozeclient.templates.TemplateReaderFactory;
import org.inria.myriads.snoozeclient.templates.api.TemplateReader;
import org.inria.myriads.snoozecommon.communication.NetworkAddress;
import org.inria.myriads.snoozecommon.communication.virtualcluster.VirtualMachineMetaData;
import org.inria.myriads.snoozecommon.communication.virtualcluster.monitoring.NetworkDemand;
import org.inria.myriads.snoozecommon.communication.virtualcluster.status.VirtualMachineStatus;
import org.inria.myriads.snoozecommon.communication.virtualcluster.submission.VirtualClusterSubmissionRequest;
import org.inria.myriads.snoozecommon.communication.virtualcluster.submission.VirtualClusterSubmissionResponse;
import org.inria.myriads.snoozecommon.communication.virtualcluster.submission.VirtualMachineTemplate;
import org.inria.myriads.snoozecommon.globals.Globals;
import org.inria.myriads.snoozecommon.guard.Guard;
import org.inria.myriads.snoozecommon.util.NetworkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * Client data repository based on XML.
 * 
 * @author Eugen Feller
 */
public final class ClientXMLRepository 
    implements ClientRepository
{
    /** Define the logger. */
    private static final Logger log_ = LoggerFactory.getLogger(ClientXMLRepository.class);
        
    /** Root element of xml file. */
    private static final String ROOT_ELEMENT_NAME = "clusters";
    
    /** Keeps XML in memory. */
    private Document document_;
    
    /** Client repository file. */
    private String repositoryFile_;
    
    /**
     * Constructor.
     * 
     * @param repositoryFile   Repository file name
     * @throws Exception       The exception
     */
    public ClientXMLRepository(String repositoryFile) 
        throws Exception
    {
        Guard.check(repositoryFile);
        repositoryFile_ = repositoryFile;
        
        log_.debug("Initializing the client xml repository");              
        boolean fileExists = fileExists(repositoryFile); 
        if (fileExists)
        {
            log_.debug("XML file exists! Creating document from it!");
            document_ = createDocumentFromFile(repositoryFile);
            return;
        } 
       
        log_.debug("Snooze XML file is missing! Creating empty document!");
        document_ = createDocument();
    }

    /**
     * Creates a document object model from file.
     * 
     * @param fileName      The file name
     * @return              The document
     * @throws Exception 
     */
    private Document createDocumentFromFile(String fileName) 
        throws Exception
    {
        Guard.check(fileName);
        
        File file = new File(fileName);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();
        
        return document;
    }
    
    /**
     * Checks if a file exists.
     * 
     * @param fileName  The file name
     * @return          true if exists, false otherwise
     */
    private boolean fileExists(String fileName)
    {
        Guard.check(fileName);
        
        File file = new File(fileName);
        return file.exists();
    }
    
    /**
     * Write document to file.
     * 
     * @throws Exception       The exception
     */
    private void writeXmlFile() 
        throws Exception
    {
        log_.debug("Writing XML output to stable storage");
        
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
                
        Source source = new DOMSource(document_);
        File file = new File(repositoryFile_);
        Result result = new StreamResult(file);
        
        transformer.transform(source, result);
    } 
    
    /**
     * Defines cluster(s).
     * 
     * @param virtualClusterName    The virtual cluster name
     * @return                      true if defined, false otherwise
     * @throws Exception 
     */
    @Override
    public boolean defineVirtualCluster(String virtualClusterName) 
        throws Exception
    {    
        Guard.check(virtualClusterName);
        log_.debug(String.format("Defining cluster: %s", virtualClusterName));
        
        boolean isDefined = createVirtualClusterDefinition(virtualClusterName);
        if (isDefined)
        {
            log_.debug("Cluster is defined! Writing xml file!");
            writeXmlFile();
        }
        
        return isDefined;
    }

    /**
     * Removes cluster(s).
     * 
     * @param virtualClusterName    The virtual cluster name
     * @return                      true if undefined, false otherwise
     * @throws Exception 
     */
    @Override
    public boolean undefineVirtualCluster(String virtualClusterName) 
        throws Exception
    {    
        Guard.check(virtualClusterName);
        log_.debug(String.format("Undefining clusters: %s", virtualClusterName));
        
        boolean isRemoved = removeClusterDefinition(virtualClusterName);
        if (isRemoved)
        {
            log_.debug("Cluster removed");
            writeXmlFile();
        }
        
        return isRemoved;
    }

    /**
     * Creates a new cluster defintion.
     * 
     * @param virtualClusterName    The virtual cluster name
     * @return                      true if created, false otherwise
     * @throws Exception 
     */
    private boolean createVirtualClusterDefinition(String virtualClusterName)
        throws Exception
    {
        Guard.check(virtualClusterName);
        log_.debug(String.format("Creating cluster definition for: %s", virtualClusterName)); 
        Element root = document_.getDocumentElement();
        
        if (hasAttribute(AttributeType.cluster, virtualClusterName))
        {
            log_.debug("Such cluster already exists!");
            return false;
        }
        
        Element virtualCluster = document_.createElement("cluster");
        virtualCluster.setAttribute("name", virtualClusterName);      
        root.appendChild(virtualCluster);   
        
        return true;
    }

    /**
     * Delete a cluster definition.
     * 
     * @param virtualClusterName    The virtual cluster name
     * @return                      true if removed, false otherwise
     * @throws Exception 
     */
    private boolean removeClusterDefinition(String virtualClusterName) 
        throws Exception
    {
        Guard.check(virtualClusterName);
        log_.debug(String.format("Removing cluster definition: %s", virtualClusterName));
        
        Node node = getElementByAttribute(AttributeType.cluster, virtualClusterName);
        if (node == null)
        {
            log_.debug(String.format("Unable to such node cluster: %s", virtualClusterName));
            return false;
        }
        
        Node root = document_.getDocumentElement();
        root.removeChild(node);
        
        return true;
    }
    
    /**
     * Find element by attribute.
     * 
     * @param attributeType     The attribute type
     * @param name              The name
     * @return                  The node
     * @throws Exception        The exception
     */
    private Node getElementByAttribute(AttributeType attributeType, String name) 
        throws Exception
    {
        Guard.check(attributeType, name);
        log_.debug(String.format("Finding element by attribute: %s", name));
        
        XPath xpath = XPathFactory.newInstance().newXPath();
        String searchExpression = "//" + attributeType + "[@name = '" + name + "']";        
        log_.debug("Search expression: " + searchExpression);
        
        XPathExpression expression = xpath.compile(searchExpression);
        Object result = expression.evaluate(document_, XPathConstants.NODESET);
        
        NodeList nodes = (NodeList) result;
        Node node = nodes.item(0);
        
        return node;
    }
    
    /**
     * Create new document.
     * 
     * @return              The document
     * @throws Exception 
     */
    private Document createDocument() 
        throws Exception 
    {
        log_.debug("Creating document");
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        Document document = docBuilder.newDocument();

        Element root = document.createElement(ROOT_ELEMENT_NAME);
        document.appendChild(root);
        
        return document;
    }

    /**
     * Creates element with content.
     * 
     * @param name          The name
     * @param content       The content
     * @return              The element
     */
    private Element createElementWithContent(String name, String content)
    {
        Guard.check(name, content);
        
        Element newElement = document_.createElement(name);    
        Text elementContent = document_.createTextNode(content);
        newElement.appendChild(elementContent);      
        return newElement;
    }
    
    /**
     * Returns virtual machine identifier from libvirt template.
     * 
     * @param libVirtTemplate       The libvirt template
     * @return                      The identifier
     * @throws Exception            The exception
     */
    private String getVirtualMachineIdFromTemplate(String libVirtTemplate) 
        throws Exception
    {
        Guard.check(libVirtTemplate);
        Element templateRoot = createDocumentFromFile(libVirtTemplate).getDocumentElement();
        String virtualMachineId = getDataFromElement(templateRoot, "name");
        return virtualMachineId;
    }
    
    /**
     * Checks if such attribute exists.
     * 
     * @param attributeType     The attribute type
     * @param name              The name
     * @return                  true if exists, false otherwise
     * @throws Exception        The exception
     */
    private boolean hasAttribute(AttributeType attributeType, String name) 
        throws Exception
    {
        Guard.check(name);
        Node node = null;
        switch (attributeType)
        {
            case vm:
                node = getElementByAttribute(AttributeType.vm, name);
                break;
                
            case cluster:
                node = getElementByAttribute(AttributeType.cluster, name);
                break;
            default:
                log_.error(String.format("Unknown attribute type: %s", attributeType));
                break;
        }
        
        if (node != null)
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * Add virtual machine description to a given cluster.
     * 
     * @param description            The virtual machine description
     * @param virtualClusterName     The virtual cluster name
     * @return                       true if added, false otherwise
     * @throws Exception 
     */
    @Override
    public boolean addVirtualMachineTemplate(VirtualMachineTemplate description, String virtualClusterName) 
        throws Exception 
    {
        Guard.check(description, virtualClusterName);
        log_.debug("Adding virutal machine description");
                
        Node cluster = getElementByAttribute(AttributeType.cluster, virtualClusterName);
        if (cluster == null)
        {
            log_.debug("Unable to find the cluster");
            return false;
        }
        String virtualMachineId = getVirtualMachineId(description);
        
        boolean doesExist = hasAttribute(AttributeType.vm, virtualMachineId);
        if (doesExist)
        {
            log_.debug("Such virtual machine already exists!");
            return false;
        }
        
        Element virtualMachine = createVirtualMachineNode(virtualMachineId);
        
        // template based
        if (description.getLibVirtTemplate() != null)
        {
            Element libVirtTemplate = createLibVirtTemplateElement(description.getLibVirtTemplate());
            virtualMachine.appendChild(libVirtTemplate);
        }
            
        // flavor based
        if (description.getLibVirtDescription() == null)
        {
            Element name =  createNameElement(description.getName());
            virtualMachine.appendChild(name);
            
            Element image =  createImageElement(description.getImageId());
            virtualMachine.appendChild(image);
            
            Element vcpusDemand =  createVcpusDemandElement(description.getVcpus());
            virtualMachine.appendChild(vcpusDemand);
            
            Element memoryDemand =  createMemoryDemandElement(description.getMemory());
            virtualMachine.appendChild(memoryDemand);
            
            Element hostIdDemand =  createHostIdDemandElement(description.getHostId());
            virtualMachine.appendChild(hostIdDemand);
        }
        
        // common
        Element networkDemand = createNetworkCapacityElement(description.getNetworkCapacityDemand());
        virtualMachine.appendChild(networkDemand);
        
        cluster.appendChild(virtualMachine);
        writeXmlFile();
        
        return true;
    }
    
    private Element createHostIdDemandElement(String hostId) {
        Element element = createElementWithContent("hostId", String.valueOf(hostId));
        return element;
    }

    private Element createNameElement(String name) 
    {
        Element element = createElementWithContent("name", String.valueOf(name));
        return element;
    }

    private Element createMemoryDemandElement(long memory) 
    {
        Element element = createElementWithContent("memory", String.valueOf(memory));
        return element;
    }

    private Element createVcpusDemandElement(int vcpus) 
    {
        Element element = createElementWithContent("vcpus", String.valueOf(vcpus));
        return element;
    }

    private Element createImageElement(String imageId) 
    {
        Element element = createElementWithContent("imageId", imageId);
        return element;
    }

    private String getVirtualMachineId(VirtualMachineTemplate description) throws Exception 
    {
        log_.debug("Getting the name from the command line");
        if (description.getLibVirtTemplate() == null)
        {
            log_.debug("no template given, search name in the cli");
            return description.getName();
        }
            
        log_.debug("extract the name from the template");
        return getVirtualMachineIdFromTemplate(description.getLibVirtTemplate());
    }

    /**
     * Creates a virtual machine node.
     * 
     * @param virtualMachineId      The virtual machine identifier
     * @return                      The node
     */
    private Element createVirtualMachineNode(String virtualMachineId)
    {
        Element virtualMachine = document_.createElement("vm");
        virtualMachine.setAttribute("name", virtualMachineId);
        return virtualMachine;
    }
    
    /**
     * Creates a libvirt template node.
     * 
     * @param libVirtTemplate   The libvirt template
     * @return                  The node
     */
    private Element createLibVirtTemplateElement(String libVirtTemplate)
    {
        Element element = createElementWithContent("template", libVirtTemplate);
        return element;
    }
    
    /**
     * Returns information from tag.
     * 
     * @param root      The root element
     * @param tag       The tag
     * @return          The information
     */
    private String getDataFromElement(Element root, String tag) 
    {
        Guard.check(root, tag);
        NodeList nodes = root.getElementsByTagName(tag);       
        Element element = (Element) nodes.item(0);
        String information = getDataFromElement(element);
        return information;
    }
    
    /**
     * Retrieves data from element.
     * 
     * @param element   The element 
     * @return          The data
     */
    private String getDataFromElement(Element element) 
    {
        Guard.check(element);
        Node child = element.getFirstChild();

        if (child instanceof CharacterData) 
        {
            CharacterData characterData = (CharacterData) child;
            return characterData.getData();
        }
        
        return null;
    }

    /**
     * Removes virtual machine description.
     * 
     * @param virtualMachineId      The virtual machine identifier
     * @param virtualClusterName    The virtual cluster name
     * @return                      true if removed, false otherwise
     * @throws Exception            The exception
     */
    @Override
    public boolean removeVirtualMachineDescription(String virtualMachineId, String virtualClusterName)
        throws Exception 
    {
        Guard.check(virtualMachineId, virtualClusterName);
        log_.debug("Removing virtual machine description");
                
        Node virtualCluster =  getElementByAttribute(AttributeType.cluster, virtualClusterName);
        if (virtualCluster == null)
        {
            log_.debug("Unable to find the cluster");
            return false;
        }
        
        Node virtualMachine = getElementByAttribute(AttributeType.vm, virtualMachineId);
        if (virtualMachine == null)
        {
            log_.debug("Unable to find this virtual machine entry");
            return false;
        }
        
        virtualCluster.removeChild(virtualMachine);
        writeXmlFile();
        return true;
    }

    /**
     * Creates a virtual machine template from node.
     * 
     * @param virtualMachine    The virtual machine node
     * @return                  The virtual machine template
     * @throws Exception 
     */
    private VirtualMachineTemplate createVirtualMachineTemplate(Node virtualMachine) 
        throws Exception
    {
        Guard.check(virtualMachine);
        String virtualMachineTemplate = getVirtualMachineTemplateFromNode(virtualMachine);
        String imageId = getValueFromNode(virtualMachine, "imageId");
        
        if (virtualMachineTemplate == null && imageId == null)
        {
            log_.debug("No template nor image id could be found for this virtual machine!");
            return null;
        }
        
        VirtualMachineTemplate template = new VirtualMachineTemplate();
        if (virtualMachineTemplate != null)
        {
            TemplateReader templateReader = TemplateReaderFactory.newTemplateReader();
            String templateContent = templateReader.readTemplateDescription(virtualMachineTemplate);
            log_.debug(String.format("Virtual machine template: %s, content: %s",
                                     virtualMachineTemplate, templateContent));
            template.setLibVirtTemplate(templateContent); 
        }
        else
        {
            // maybe we should rely on default values of Snooze.
            String vcpus = getValueFromNode(virtualMachine, "vcpus");
            if (vcpus == null)
            {
                return null;
            }
            String memory = getValueFromNode(virtualMachine, "memory");
            if (memory == null)
            {
                return null;
            }
            String name = getValueFromNode(virtualMachine, "name");
            if (name == null)
            {
                return null;
            }
            String hostId = getValueFromNode(virtualMachine, "hostId");
            if (hostId != null)
            {
                template.setHostId(hostId);
            }
            template.setVcpus(Integer.valueOf(vcpus));
            template.setMemory(Long.valueOf(memory));
            template.setName(name);
            template.setImageId(imageId);
            
        }
        
        NetworkDemand networkCapacity = getNetworkCapacityRequirementsFromNode(virtualMachine);
        if (networkCapacity == null)
        {
            log_.debug("Network capacity is NULL!");
            return null;
        }

        template.setNetworkCapacityDemand(networkCapacity);
        
        return template;
    }
    
    private String getValueFromNode(Node node, String tagName)
    {
        if (node.getNodeType() == Node.ELEMENT_NODE) 
        {
            Element vmElement = (Element) node;
            log_.debug("Returning text content");
            NodeList list = vmElement.getElementsByTagName(tagName);
            if (list == null || list.getLength() < 1)
            {
                return null;
            }
            return vmElement.getElementsByTagName(tagName).item(0).getTextContent();
        }
        return null;   
    }
    
    /**
     * Creates virtual cluster templates.
     * 
     * @param virtualClusterNode   The virtual cluster node
     * @return                     The virtual machine templates
     * @throws Exception 
     */
    private ArrayList<VirtualMachineTemplate> createVirtualClusterTemplates(Node virtualClusterNode)
        throws Exception
    {
        Guard.check(virtualClusterNode);

        NodeList childList = virtualClusterNode.getChildNodes();
        if (childList.getLength() == 0)
        {
            throw new ParseException("Unable to detect any cluster entries! Add some virtual machines?", 0);
        }
        
        ArrayList<VirtualMachineTemplate> templates = new ArrayList<VirtualMachineTemplate>();
        for (int i = 0; i < childList.getLength(); i++) 
        {
            Node virtualMachine = childList.item(i);        
            if (virtualMachine.getNodeType() == Node.ELEMENT_NODE) 
            {
                if (virtualMachine.getNodeName().equals("vm"))
                {
                    VirtualMachineTemplate template = createVirtualMachineTemplate(virtualMachine);
                    if (template == null)
                    {
                        log_.debug("Error during virtual machine template generation!");
                        continue;
                    }
                    templates.add(template);
                } else
                {
                    throw new ParseException("Huh? Unknown element in the XML file!! BAD!", 0);
                } 
            }
        }
        
        return templates;        
    }
    
    /**
     * Create virtual cluster submission request.
     * 
     * @param typeIdentifier    The type identifier
     * @param attributeType     The attribute type
     * @return                  The virtual cluster submission request
     * @throws Exception 
     */
    @Override
    public VirtualClusterSubmissionRequest createVirtualClusterSubmissionRequest(String typeIdentifier, 
                                                                                 AttributeType attributeType) 
        throws Exception 
    {
        Guard.check(typeIdentifier, attributeType);
        log_.debug(String.format("Generating virtual cluster submission request for: %s", typeIdentifier));
                                       
        Node node = getElementByAttribute(attributeType, typeIdentifier);
        if (node == null)
        {
            throw new ParseException("Unable to find the specified virtual machine! Define it first?", 0);
        }
        
        ArrayList<VirtualMachineTemplate> templates = new ArrayList<VirtualMachineTemplate>();
        switch (attributeType)
        {
            case vm :      
                VirtualMachineTemplate template = createVirtualMachineTemplate(node);
                if (template == null)
                {
                    throw new ParseException("Error during virtual machine template generation!", 0);
                }
                templates.add(template);
                break;
                
            case cluster :
                templates = createVirtualClusterTemplates(node);
                if (templates == null)
                {
                    throw new ParseException("Error during virtual machine templates generation!", 0);
                }
                break;
                
            default:
                throw new ParseException(String.format("Invalid attribute type selected: %s", attributeType), 0);
        }
        
        VirtualClusterSubmissionRequest virtualCluster = new VirtualClusterSubmissionRequest();    
        virtualCluster.setVirtualMachineTemplates(templates);
        return virtualCluster;
    }
    
    /**
     * Create virtual cluster mapping for a given cluster.
     * 
     * @param virtualClusterName    The virtual cluster name
     * @return                      The list of identifiers
     * @throws Exception            The exception
     */
    @Override
    public List<String> getVirtualMachineIds(String virtualClusterName) 
        throws Exception
    {
        Guard.check(virtualClusterName);
        log_.debug(String.format("Generating virtual cluster mapping for cluster: %s", virtualClusterName));
                          
        Node node = getElementByAttribute(AttributeType.cluster, virtualClusterName);
        if (node == null)
        {
            throw new ParseException("Unable to find the cluster! Define it first?", 0);
        }
        
        NodeList childList = node.getChildNodes();
        if (childList.getLength() == 0)
        {
            throw new ParseException("Unable to detect any cluster entries! Add some VM?", 0);
        }
        
        List<String> virtualMachineNames = new ArrayList<String>();
        for (int i = 0; i < childList.getLength(); i++) 
        {
          Node tmpNode = childList.item(i);        
          if (tmpNode.getNodeType() == Node.ELEMENT_NODE) 
          {
              if (tmpNode.getNodeName().equals("vm"))
              {
                  String virtualMachineId = tmpNode.getAttributes().getNamedItem("name").getNodeValue();
                  log_.debug(String.format("Adding virtual machine %s to list", virtualMachineId));
                  virtualMachineNames.add(virtualMachineId);
              } else
              {
                  throw new ParseException("Huh? Unknown element in the XML file!! BAD!", 0);
              } 
          }

        }
        
        return virtualMachineNames;
    }
    
    /**
     * Gets the virtual machine template from VM node.
     * 
     * @param virtualMachine    The virtual machine node
     * @return                  The virtual machine template
     * @throws ParseException 
     */
    private String getVirtualMachineTemplateFromNode(Node virtualMachine) 
        throws ParseException 
    {
        Guard.check(virtualMachine);
        log_.debug("Getting virtual machine template");
        
        NodeList childList = virtualMachine.getChildNodes();  
        if (childList == null)
        {
            log_.debug("The child list is NULL!");
            return null;
        }
        
        String virtualMachineTemplate = getContentFromNodeList("template", childList);
        return virtualMachineTemplate;
    }

    /**
     * Print information about a cluster.
     * 
     * @param virtualClusterName    The virtual cluster name
     * @throws Exception 
     */
    @Override
    public void printVirtualCluster(String virtualClusterName)
        throws Exception
    {
        Guard.check(virtualClusterName);
        log_.debug(String.format("Printing content of cluster: %s", virtualClusterName));
        
        Node virtualClusterNode = getElementByAttribute(AttributeType.cluster, virtualClusterName);
        if (virtualClusterNode == null)
        {
            throw new ParseException("Unable to find the cluster! Define it first?", 0);
        }
        
        printClusterVirtualNodeContent(virtualClusterNode);
    }

    /**
     * Prints the content of a single cluster node.
     * 
     * @param virtualClusterNode   The virtual cluster node
     * @throws ParseException 
     */
    private void printClusterVirtualNodeContent(Node virtualClusterNode)
        throws ParseException
    {
        Guard.check(virtualClusterNode);
        
        NodeList childList = virtualClusterNode.getChildNodes();
        if (childList.getLength() == 0)
        {
            throw new ParseException("Unable to detect any cluster entries! No VMs in this cluster?", 0);
        }
                
        log_.info(String.format("%20s \t %20s", "NAME", "TEMPLATE"));
        
        for (int i = 0; i < childList.getLength(); i++) 
        {
          Node tmpNode = childList.item(i);          
          if (tmpNode.getNodeType() == Node.ELEMENT_NODE) 
          {
              if (tmpNode.getNodeName().equals("vm"))
              {
                  Element element = (Element) tmpNode;
                  String virtualMachineId = element.getAttribute("name");
                  String virtualMachineTemplate = getVirtualMachineTemplateFromNode(tmpNode);
                  log_.info(String.format("%20s \t %20s", virtualMachineId, virtualMachineTemplate));
              } else
              {
                  throw new ParseException("Huh? Unknown element in the XML file!! BAD!", 0);
              } 
          }
        }
    }
    
    /**
     * Print available clusters.
     * 
     * @throws Exception 
     */
    @Override
    public void printVirtualClusters()
        throws Exception
    {
        log_.debug("Printing all clusters");
        
        Element root = document_.getDocumentElement();
        NodeList clusterNodes = root.getElementsByTagName("cluster");
        if (clusterNodes.getLength() == 0)
        {
            throw new ParseException("No virtual clusters defined!", 0);
        }
        
        log_.info("Virtual cluster(s):");
        for (int i = 0; i < clusterNodes.getLength(); i++)
        {
            Node node = clusterNodes.item(i);
            Element element = (Element) node;
            String virtualClusterName = element.getAttribute("name");
            log_.info(virtualClusterName);
        }
    }
    
    /**
     * Returns virtual machine meta data.
     * 
     * @param virtualMachineId      The virtual machine identifier
     * @return                      The virtual machine meta data
     * @throws Exception 
     */
    @Override
    public VirtualMachineMetaData getVirtualMachineMetaData(String virtualMachineId)
        throws Exception 
    {              
        Guard.check(virtualMachineId);
        log_.debug("Getting virtual machine meta data");
        
        Node virtualMachine = getElementByAttribute(AttributeType.vm, virtualMachineId);
        if (virtualMachine == null)
        {
            log_.debug("No such virtual machine avalable! Define it first!");
            return null;
        }
                                
        NetworkAddress controlDataAddress = getGroupManagerControlDataAddressFromNode(virtualMachine);
        String localControllerId = getLocalControllerIdFromNode(virtualMachine);
                
        VirtualMachineMetaData metaData = new VirtualMachineMetaData();
        metaData.setGroupManagerControlDataAddress(controlDataAddress);
        metaData.getVirtualMachineLocation().setVirtualMachineId(virtualMachineId);
        metaData.getVirtualMachineLocation().setLocalControllerId(localControllerId);
        
        return metaData;
    }

    /**
     * Returns content from node list.
     * 
     * @param tag           The tag
     * @param nodeList      The node list
     * @return              The content
     */
    private String getContentFromNodeList(String tag, NodeList nodeList)
    {          
        Guard.check(tag, nodeList);
        log_.debug(String.format("Getting data for tag: %s", tag));
        
        for (int i = 0; i < nodeList.getLength(); i++) 
        {
          Node node = nodeList.item(i);         
          if (node.getNodeType() == Node.ELEMENT_NODE) 
          {
              if (node.getNodeName().equals(tag)) 
              {
                  if (node.getChildNodes().getLength()>0)
                  {
                      String content = node.getChildNodes().item(0).getNodeValue();
                      log_.debug(String.format("Content: %s", content));
                      return content;
                  }
                  
              } 
          }
        }
        
        return null;     
    }

    /**
     * Returns network capacity requirements from node.
     * 
     * @param virtualMachine    The virtual machine node
     * @return                  The network capacity requirements
     * @throws ParseException 
     */
    private NetworkDemand getNetworkCapacityRequirementsFromNode(Node virtualMachine) 
        throws ParseException 
    {        
        Guard.check(virtualMachine);
        log_.debug("Getting network capacity requirements");
        
        Node networkCapacityNode = getNodeByName("network_capacity", virtualMachine);
        if (networkCapacityNode == null)
        {
            log_.debug("No network capacity data available");
            return null;
        }
        
        NodeList childNodes = networkCapacityNode.getChildNodes();  
        if (childNodes == null)
        {
            log_.debug("The child list is NULL!");
            return null;
        }
             
        double networkRxCapacity = Double.valueOf(getContentFromNodeList("rx_capacity", childNodes));
        double networkTxCapacity = Double.valueOf(getContentFromNodeList("tx_capacity", childNodes));
        NetworkDemand networkDemand = new NetworkDemand(networkRxCapacity, networkTxCapacity);
        return networkDemand;
    }
    
    

    /**
     * 
     * Return hostId from node.
     * 
     * @param virtualMachine
     * @return
     */
    private String getHostIdFromNode(Node virtualMachine) {
        Guard.check(virtualMachine);
        log_.debug("Getting host id from xml template");
        
        NodeList childList = virtualMachine.getChildNodes();  
        if (childList == null)
        {
            log_.debug("The child list is NULL!");
            return null;
        }
        String hostId = getContentFromNodeList("hostId", childList);
        
        return hostId;
    }
    /**
     * Returns the group manager control data address.
     * 
     * @param virtualMachine      The virtual machine node
     * @return                    The group manager control data address
     * @throws ParseException 
     */
    private NetworkAddress getGroupManagerControlDataAddressFromNode(Node virtualMachine) 
        throws ParseException 
    {        
        Guard.check(virtualMachine);
        
        log_.debug("Getting group manager control data address from node");
        NetworkAddress address = new NetworkAddress();
        
        Node groupManagerNode = getNodeByName("group_manager", virtualMachine);
        if (groupManagerNode == null)
        {
            log_.debug("No group manager information available!");
            return address;
        }
        
        NodeList childNodes = groupManagerNode.getChildNodes();  
        if (childNodes == null)
        {
            log_.debug("The child list is NULL!");
            return address;
        }
        
        String groupManagerAddress = getContentFromNodeList("listen_address", childNodes);
        String groupManagerControlDataPort = getContentFromNodeList("control_data_port", childNodes);
                        
        if (groupManagerAddress == null ||
            groupManagerControlDataPort == null)
        {
            log_.debug("Something is wrong with the XML file!");
            return address;
        }
            
        address = 
            NetworkUtils.createNetworkAddress(groupManagerAddress, Integer.valueOf(groupManagerControlDataPort));
        return address;
    }

    /**
     * Returns the local controller identifier.
     * 
     * @param virtualMachine    The virtual machine node
     * @return                  The local controller identifier
     * @throws ParseException 
     */
    private String getLocalControllerIdFromNode(Node virtualMachine) 
        throws ParseException 
    {        
        Guard.check(virtualMachine);
        log_.debug("Getting local controller identifier from node");
        
        Node localController = getNodeByName("local_controller", virtualMachine);
        if (localController == null)
        {
            log_.debug("No local controller data available!");
            return null;
        }
        
        NodeList childNodes = localController.getChildNodes();  
        if (childNodes == null)
        {
            log_.debug("The child list is NULL!");
            return null;
        }
        
        String localControllerId = getContentFromNodeList("id", childNodes);        
        if (localControllerId == null)
        {
            log_.debug("Something is wrong with the XML file! Local controller identifier is NULL!");
            return null;
        }
        
        return localControllerId;
    }
    
    /**
     * Updates the virtual cluster mapping.
     * 
     * @param virtualClusterResponse    The virtual cluster response
     * @throws Exception 
     */
    @Override
    public void addVirtualClusterResponse(VirtualClusterSubmissionResponse virtualClusterResponse) 
        throws Exception 
    {
        Guard.check(virtualClusterResponse);
        log_.debug("Updating virtual cluster mapping");
                
        List<VirtualMachineMetaData> metaData = virtualClusterResponse.getVirtualMachineMetaData();
        for (VirtualMachineMetaData entry : metaData)
        {
            String virtualMachineId = entry.getVirtualMachineLocation().getVirtualMachineId();
            if (entry.getStatus().equals(VirtualMachineStatus.ERROR))
            {
                log_.debug("Virtual machine is in ERROR state! Not updating!");
                continue;
            }
            
            updateVirtualMachineMetaData(virtualMachineId, 
                                         entry.getVirtualMachineLocation().getLocalControllerId(),
                                         entry.getGroupManagerControlDataAddress());
        }
    }

    /**
     * Updates virtual machine mapping.
     * 
     * @param virtualMachineId      The virtual machine identifier
     * @param localControllerId     The local controller identifier
     * @param groupManagerAddress   The group manager address
     * @throws Exception            The exception
     */
    @Override
    public void updateVirtualMachineMetaData(String virtualMachineId, 
                                             String localControllerId,
                                             NetworkAddress groupManagerAddress) 
        throws Exception
    {
        Guard.check(virtualMachineId, localControllerId, groupManagerAddress);
        log_.debug(String.format("Updating mapping of virtual machine: %s", virtualMachineId));   
        
        updateGroupManagerAddress(virtualMachineId, groupManagerAddress);     
        updateLocalControllerId(virtualMachineId, localControllerId);      
        writeXmlFile();
    }
        
    /**
     * Updates the local controller identifier.
     * 
     * @param virtualMachineId      The virtual machine identifier
     * @param localControllerId     The local controller identifier
     * @throws Exception            The exception
     */
    private void updateLocalControllerId(String virtualMachineId, String localControllerId)
        throws Exception
    {
        Guard.check(virtualMachineId, localControllerId);
        Node virtualMachine = getElementByAttribute(AttributeType.vm, virtualMachineId);
        Node localControllerElement = getNodeByName("local_controller", virtualMachine);
        if (localControllerElement == null)
        {
            log_.debug("No local controller identifier available for this virtual machine");

            Element groupManager = createLocalControllerIdNode(localControllerId);
            virtualMachine.appendChild(groupManager);     
        } else
        {
            log_.debug("Local controller identifier already exists! Updating!");
            
            virtualMachine.removeChild(localControllerElement);
            Element groupManager = createLocalControllerIdNode(localControllerId);
            virtualMachine.appendChild(groupManager); 
        }        
    }
    
    /**
     * Creates network capacity node.
     * 
     * @param networkDemand     The network demand
     * @return                  The element
     */
    private Element createNetworkCapacityElement(NetworkDemand networkDemand)
    {
        Guard.check(networkDemand);
        log_.debug("Creating network capacity node");
        
        Element networkCapacity = document_.createElement("network_capacity");
        Element rxCapacity = createElementWithContent("rx_capacity", String.valueOf(networkDemand.getRxBytes()));
        Element txCapacity = createElementWithContent("tx_capacity", String.valueOf(networkDemand.getTxBytes()));
        networkCapacity.appendChild(rxCapacity);
        networkCapacity.appendChild(txCapacity);      
        return networkCapacity;
    }
    
    /**
     * Creates a group manager address node.
     * 
     * @param groupManagerAddress     The group manager address
     * @return                        The element
     */
    private Element createGroupManagerAddressElement(NetworkAddress groupManagerAddress)
    {
        Guard.check(groupManagerAddress);
        log_.debug("Creating group manager node");
        
        Element groupManager = document_.createElement("group_manager");
        Element listenAddress = createElementWithContent("listen_address", groupManagerAddress.getAddress());
        Element controlDataPort = createElementWithContent("control_data_port", 
                                                           String.valueOf(groupManagerAddress.getPort()));
        groupManager.appendChild(listenAddress);
        groupManager.appendChild(controlDataPort);   
        return groupManager;
    }
 
    /**
     * Creates a local controller identifier node.
     * 
     * @param localControllerId     The local controller identifier
     * @return                      The node
     */
    private Element createLocalControllerIdNode(String localControllerId)
    {
        Guard.check(localControllerId);
        log_.debug("Creating local controller node");
        
        Element localControllerElement = document_.createElement("local_controller");
        Element localController = createElementWithContent("id", localControllerId);
        localControllerElement.appendChild(localController);      
        return localControllerElement;
    }
    
    /**
     * Checks if the virtual machine node has a group manager.
     * 
     * @param name      The name
     * @param node      The node
     * @return          The node
     */
    private Node getNodeByName(String name, Node node)
    {
        Guard.check(name, node);
        log_.debug(String.format("Checking if node has children with name: %s", name));
        
        NodeList childList = node.getChildNodes();       
        for (int i = 0; i < childList.getLength(); i++) 
        {
            Node firstNode = childList.item(i);           
            if (firstNode.getNodeType() == Node.ELEMENT_NODE) 
            {
                if (firstNode.getNodeName().equals(name)) 
                {
                    log_.debug(String.format("Found one child with name: %s", name));
                    return firstNode;
                }
            }
        }  
        
        return null;
    }
 
    /**
     * Updates the group manager address.
     * 
     * @param virtualMachineId      The virtual machine identifier
     * @param address               The group manager address  
     * @throws Exception            The exception
     */
    private void updateGroupManagerAddress(String virtualMachineId, NetworkAddress address) 
        throws Exception 
    {
        Guard.check(virtualMachineId, address);
        Node virtualMachine = getElementByAttribute(AttributeType.vm, virtualMachineId);
        Node groupManager = getNodeByName("group_manager", virtualMachine);
        if (groupManager == null)
        {
            log_.debug("No group manager address exists for this virtual machine");
            Element groupManagerAddress = createGroupManagerAddressElement(address);
            virtualMachine.appendChild(groupManagerAddress);     
        } else
        {
            log_.debug("Group manager address is already exists! Updating!");
            virtualMachine.removeChild(groupManager);
            Element groupManagerAddressElement = createGroupManagerAddressElement(address);
            virtualMachine.appendChild(groupManagerAddressElement); 
        }   
    }

    @Override
    public String getVirtualMachineTemplateContent(String virtualMachineId) throws Exception 
    {
        Node virtualMachine = getElementByAttribute(AttributeType.vm, virtualMachineId);
        if (virtualMachine == null)
        {
            log_.debug("No such virtual machine available! Define it first!");
            return null;
        }
        String virtualMachineTemplate = getVirtualMachineTemplateFromNode(virtualMachine);
        TemplateReader templateReader = TemplateReaderFactory.newTemplateReader();
        String templateContent = templateReader.readTemplateDescription(virtualMachineTemplate);
        log_.debug(String.format("Virtual machine template: %s, content: %s",
                                 virtualMachineTemplate, templateContent));
   
        return templateContent;
    }

    @Override
    public String getVirtualMachineTemplate(String virtualMachineId) throws Exception 
    {
        Node virtualMachine = getElementByAttribute(AttributeType.vm, virtualMachineId);
        if (virtualMachine == null)
        {
            log_.debug("No such virtual machine available! Define it first!");
            return null;
        }
        return getVirtualMachineTemplateFromNode(virtualMachine);
    }

    @Override
    public void updateNetworkCapacityDemand(String virtualMachineId, NetworkDemand networkDemand) throws Exception 
    {
        Node virtualMachine = getElementByAttribute(AttributeType.vm, virtualMachineId);
        if (virtualMachine == null)
        {
            log_.debug("No such virtual machine available! Define it first!");
            throw new Exception("No such virtual machine available! Define it first!");
        }
        Node networkDemandNode = getNodeByName("network_capacity", virtualMachine);
        if (networkDemandNode == null)
        {
            log_.debug("No network demand exist for this virtual machine");
            Element networkElement = createNetworkCapacityElement(networkDemand);
            virtualMachine.appendChild(networkElement); 
        }
        else
        {
            NetworkDemand oldNetworkDemand = getNetworkCapacityRequirementsFromNode(virtualMachine);
            NetworkDemand newNetworkDemand = new NetworkDemand();
            newNetworkDemand.setRxBytes(networkDemand.getRxBytes());
            newNetworkDemand.setTxBytes(networkDemand.getTxBytes());
            if (networkDemand.getRxBytes() == 0)
                newNetworkDemand.setRxBytes(oldNetworkDemand.getRxBytes());
            if (networkDemand.getTxBytes() == 0)
                newNetworkDemand.setTxBytes(oldNetworkDemand.getTxBytes());
            
            log_.debug("Updating the network demand");
            virtualMachine.removeChild(networkDemandNode);
            Element networkElement = createNetworkCapacityElement(newNetworkDemand);
            virtualMachine.appendChild(networkElement); 
        } 
        writeXmlFile();
    }
}
