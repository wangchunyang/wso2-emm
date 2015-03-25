package com.hp.emm;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.wso2.carbon.service.mgt.ServiceAdminStub;

import java.rmi.RemoteException;


/**
 * @author chun-yang.wang@hp.com
 */
public class ServiceAdminClient {
    private final String serviceName = "ServiceAdmin";
    private ServiceAdminStub serviceAdminStub;
    private String endPoint;

    public ServiceAdminClient(String backEndUrl, String sessionCookie) throws AxisFault {
        this.endPoint = backEndUrl + "/services/" + serviceName;
        serviceAdminStub = new ServiceAdminStub(endPoint);
        //Authenticate Your stub from sessionCooke
        ServiceClient serviceClient;
        Options option;

        serviceClient = serviceAdminStub._getServiceClient();
        option = serviceClient.getOptions();
        option.setManageSession(true);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, sessionCookie);
    }

    public void deleteService(String[] serviceGroup) throws RemoteException {
        ServiceAdminStub.DeleteServiceGroups request = new ServiceAdminStub.DeleteServiceGroups();
        request.setServiceGroups(serviceGroup);
        serviceAdminStub.deleteServiceGroups(request);
    }

    public ServiceAdminStub.ServiceMetaDataWrapper listServices() throws RemoteException {
        ServiceAdminStub.ListServices request = new ServiceAdminStub.ListServices();
        request.setPageNumber(0);
        //request.setServiceTypeFilter("ALL");
        //request.setServiceSearchString("*");
        ServiceAdminStub.ListServicesResponse response = serviceAdminStub.listServices(request);
        return response.get_return();
        //return serviceAdminStub.listServices("ALL", "*", 0);
    }
}
