package com.hp.emm;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.wso2.carbon.core.services.authentication.AuthenticationAdminAuthenticationExceptionException;
import org.wso2.carbon.core.services.authentication.AuthenticationAdminStub;

import java.rmi.RemoteException;

/**
 * @author chun-yang.wang@hp.com
 */

public class LoginAdminServiceClient {
    private final String serviceName = "AuthenticationAdmin";
    private AuthenticationAdminStub authenticationAdminStub;
    private String endPoint;

    public static void main(String[] args) throws RemoteException, AuthenticationAdminAuthenticationExceptionException {

        /**
         * trust store path.  this must contains server's  certificate or Server's CA chain
         */
//        String trustStore = System.getProperty("user.dir") + File.separator +
//                "src" + File.separator + "main" + File.separator +
//                "resources" + File.separator + "wso2carbon.jks";
        String trustStore = "C:\\code\\github\\wangchunyang\\wso2-emm\\eem-test\\src\\main\\resources\\wso2carbon.jks";
        /**
         * Call to https://localhost:9443/services/   uses HTTPS protocol.
         * Therefore we to validate the server certificate or CA chain. The server certificate is looked up in the
         * trust store.
         * Following code sets what trust-store to look for and its JKs password.
         */

        System.setProperty("javax.net.ssl.trustStore", trustStore);

        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");


        LoginAdminServiceClient client = new LoginAdminServiceClient("https://localhost:9443");
        String result = client.authenticate("admin", "admin");
        System.out.println(result);
    }

    public LoginAdminServiceClient(String backEndUrl) throws AxisFault {
        this.endPoint = backEndUrl + "/services/" + serviceName;
        authenticationAdminStub = new AuthenticationAdminStub(endPoint);
    }

    public String authenticate(String userName, String password) throws RemoteException, AuthenticationAdminAuthenticationExceptionException {

        String sessionCookie = null;

        //if (authenticationAdminStub.login(userName, password, "localhost")) {
        AuthenticationAdminStub.Login login = new AuthenticationAdminStub.Login();
        login.setUsername(userName);
        login.setPassword(password);
        login.setRemoteAddress("localhost");
        AuthenticationAdminStub.LoginResponse loginResponse = authenticationAdminStub.login(login);
        if (loginResponse.get_return()) {
            System.out.println("Login Successful");

            ServiceContext serviceContext = authenticationAdminStub.
                    _getServiceClient().getLastOperationContext().getServiceContext();
            sessionCookie = (String) serviceContext.getProperty(HTTPConstants.COOKIE_STRING);
            System.out.println(sessionCookie);
        }

        return sessionCookie;
    }

    public void logOut() throws RemoteException, AuthenticationAdminAuthenticationExceptionException {
        AuthenticationAdminStub.Logout logout = new AuthenticationAdminStub.Logout();
        authenticationAdminStub.logout(logout);
    }
}