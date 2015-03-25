package com.hp.emm;

import org.wso2.carbon.core.services.authentication.AuthenticationAdminAuthenticationExceptionException;
import org.wso2.carbon.service.mgt.ServiceAdminStub;

import java.rmi.RemoteException;

/**
 * @author chun-yang.wang@hp.com
 */
public class ListServices {
    public static void main(String[] args) throws RemoteException, AuthenticationAdminAuthenticationExceptionException {
        String trustStore = "C:\\code\\github\\wangchunyang\\wso2-emm\\eem-test\\src\\main\\resources\\wso2carbon.jks";
        System.setProperty("javax.net.ssl.trustStore", trustStore);
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        String backEndUrl = "https://localhost:9443";

        LoginAdminServiceClient login = new LoginAdminServiceClient(backEndUrl);
        String session = login.authenticate("admin", "admin");
        ServiceAdminClient serviceAdminClient = new ServiceAdminClient(backEndUrl, session);
        ServiceAdminStub.ServiceMetaDataWrapper serviceList = serviceAdminClient.listServices();
        System.out.println("Service Names:");
        for (ServiceAdminStub.ServiceMetaData serviceData : serviceList.getServices()) {
            System.out.println(serviceData.getName());
        }

        login.logOut();
    }
}
