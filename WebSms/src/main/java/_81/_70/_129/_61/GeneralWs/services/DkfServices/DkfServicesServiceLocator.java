/**
 * DkfServicesServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package _81._70._129._61.GeneralWs.services.DkfServices;

public class DkfServicesServiceLocator extends org.apache.axis.client.Service implements _81._70._129._61.GeneralWs.services.DkfServices.DkfServicesService {

    public DkfServicesServiceLocator() {
    }


    public DkfServicesServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public DkfServicesServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for DkfServices
    private java.lang.String DkfServices_address = "http://61.129.70.81:8689/GeneralWs/services/DkfServices";

    public java.lang.String getDkfServicesAddress() {
        return DkfServices_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String DkfServicesWSDDServiceName = "DkfServices";

    public java.lang.String getDkfServicesWSDDServiceName() {
        return DkfServicesWSDDServiceName;
    }

    public void setDkfServicesWSDDServiceName(java.lang.String name) {
        DkfServicesWSDDServiceName = name;
    }

    public _81._70._129._61.GeneralWs.services.DkfServices.DkfServices getDkfServices() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(DkfServices_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getDkfServices(endpoint);
    }

    public _81._70._129._61.GeneralWs.services.DkfServices.DkfServices getDkfServices(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            _81._70._129._61.GeneralWs.services.DkfServices.DkfServicesSoapBindingStub _stub = new _81._70._129._61.GeneralWs.services.DkfServices.DkfServicesSoapBindingStub(portAddress, this);
            _stub.setPortName(getDkfServicesWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setDkfServicesEndpointAddress(java.lang.String address) {
        DkfServices_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (_81._70._129._61.GeneralWs.services.DkfServices.DkfServices.class.isAssignableFrom(serviceEndpointInterface)) {
                _81._70._129._61.GeneralWs.services.DkfServices.DkfServicesSoapBindingStub _stub = new _81._70._129._61.GeneralWs.services.DkfServices.DkfServicesSoapBindingStub(new java.net.URL(DkfServices_address), this);
                _stub.setPortName(getDkfServicesWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("DkfServices".equals(inputPortName)) {
            return getDkfServices();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://61.129.70.81:8689/GeneralWs/services/DkfServices", "DkfServicesService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://61.129.70.81:8689/GeneralWs/services/DkfServices", "DkfServices"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("DkfServices".equals(portName)) {
            setDkfServicesEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
