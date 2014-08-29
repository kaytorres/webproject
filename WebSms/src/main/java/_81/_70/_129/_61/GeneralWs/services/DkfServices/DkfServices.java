/**
 * DkfServices.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package _81._70._129._61.GeneralWs.services.DkfServices;

public interface DkfServices extends java.rmi.Remote {
    public java.lang.String sendSMS(java.lang.String userString, java.lang.String passwordString, java.lang.String[] mobiles, java.lang.String content, java.lang.String planTime, java.lang.String filename) throws java.rmi.RemoteException;
    public java.lang.String sendSMSOneOne(java.lang.String userString, java.lang.String passwordString, java.lang.String[] mobiles, java.lang.String[] content, java.lang.String planTime, java.lang.String filename) throws java.rmi.RemoteException;
    public java.lang.String getUserSmsCount(java.lang.String userString, java.lang.String passwordString) throws java.rmi.RemoteException;
    public java.lang.Object[] getMO(java.lang.String userString, java.lang.String passwordString) throws java.rmi.RemoteException;
    public java.lang.String getMT(java.lang.String userString, java.lang.String passwordString, java.lang.String startTime, java.lang.String endTime, java.lang.String mobile, java.lang.String msgid) throws java.rmi.RemoteException;
    public java.lang.String sendSMSFormatModel(java.lang.String userString, java.lang.String passwordString, java.lang.String[] mobiles, java.lang.String modelNumber, java.lang.String[][] content, java.lang.String planTime, java.lang.String filename) throws java.rmi.RemoteException;
}
