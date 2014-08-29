package _81._70._129._61.GeneralWs.services.DkfServices;

public class DkfServicesProxy implements _81._70._129._61.GeneralWs.services.DkfServices.DkfServices {
  private String _endpoint = null;
  private _81._70._129._61.GeneralWs.services.DkfServices.DkfServices dkfServices = null;
  
  public DkfServicesProxy() {
    _initDkfServicesProxy();
  }
  
  public DkfServicesProxy(String endpoint) {
    _endpoint = endpoint;
    _initDkfServicesProxy();
  }
  
  private void _initDkfServicesProxy() {
    try {
      dkfServices = (new _81._70._129._61.GeneralWs.services.DkfServices.DkfServicesServiceLocator()).getDkfServices();
      if (dkfServices != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)dkfServices)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)dkfServices)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (dkfServices != null)
      ((javax.xml.rpc.Stub)dkfServices)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public _81._70._129._61.GeneralWs.services.DkfServices.DkfServices getDkfServices() {
    if (dkfServices == null)
      _initDkfServicesProxy();
    return dkfServices;
  }
  
  public java.lang.String sendSMS(java.lang.String userString, java.lang.String passwordString, java.lang.String[] mobiles, java.lang.String content, java.lang.String planTime, java.lang.String filename) throws java.rmi.RemoteException{
    if (dkfServices == null)
      _initDkfServicesProxy();
    return dkfServices.sendSMS(userString, passwordString, mobiles, content, planTime, filename);
  }
  
  public java.lang.String sendSMSOneOne(java.lang.String userString, java.lang.String passwordString, java.lang.String[] mobiles, java.lang.String[] content, java.lang.String planTime, java.lang.String filename) throws java.rmi.RemoteException{
    if (dkfServices == null)
      _initDkfServicesProxy();
    return dkfServices.sendSMSOneOne(userString, passwordString, mobiles, content, planTime, filename);
  }
  
  public java.lang.String getUserSmsCount(java.lang.String userString, java.lang.String passwordString) throws java.rmi.RemoteException{
    if (dkfServices == null)
      _initDkfServicesProxy();
    return dkfServices.getUserSmsCount(userString, passwordString);
  }
  
  public java.lang.Object[] getMO(java.lang.String userString, java.lang.String passwordString) throws java.rmi.RemoteException{
    if (dkfServices == null)
      _initDkfServicesProxy();
    return dkfServices.getMO(userString, passwordString);
  }
  
  public java.lang.String getMT(java.lang.String userString, java.lang.String passwordString, java.lang.String startTime, java.lang.String endTime, java.lang.String mobile, java.lang.String msgid) throws java.rmi.RemoteException{
    if (dkfServices == null)
      _initDkfServicesProxy();
    return dkfServices.getMT(userString, passwordString, startTime, endTime, mobile, msgid);
  }
  
  public java.lang.String sendSMSFormatModel(java.lang.String userString, java.lang.String passwordString, java.lang.String[] mobiles, java.lang.String modelNumber, java.lang.String[][] content, java.lang.String planTime, java.lang.String filename) throws java.rmi.RemoteException{
    if (dkfServices == null)
      _initDkfServicesProxy();
    return dkfServices.sendSMSFormatModel(userString, passwordString, mobiles, modelNumber, content, planTime, filename);
  }
  
  
}