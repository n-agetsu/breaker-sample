package sample.rs;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/test")
@ApplicationScoped
public class SoapClientResource {

    @Inject
    private SoapClient soapClient;

    @GET
    public String get() {
        return soapClient.helloWorld("norito");
    }
}
