package sample.rs;

import org.apache.commons.lang3.concurrent.EventCountCircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.HelloEndPoint;
import sample.HelloEndPointService;
import sample.breaker.Breaker;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.xml.ws.BindingProvider;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class SoapClient {

    private static final Logger LOG = LoggerFactory.getLogger(SoapClient.class);
    private static final String DEFAULT_RESPONSE = "duke hello world!";
    private HelloEndPoint helloEndPoint;
//    private EventCountCircuitBreaker breaker;

    @PostConstruct
    public void init() {
        helloEndPoint = new HelloEndPointService().getHelloEndPointPort();
        ((BindingProvider) helloEndPoint).getRequestContext().put("javax.xml.ws.client.connectionTimeout", "5000");
        ((BindingProvider) helloEndPoint).getRequestContext().put("javax.xml.ws.client.receiveTimeout", "5000");

//        breaker = new EventCountCircuitBreaker(5, 1, TimeUnit.MINUTES);
    }

    @Breaker(fallbackMethod = "fallback")
    public String helloWorld(String name) {
        return helloEndPoint.helloWorld(name);
//        if (breaker.checkState()) {
//            return helloEndPoint.helloWorld(name);
//        }
//
//        LOG.warn("breaker is open");
//        return DEFAULT_RESPONSE;
    }

    String fallback(String name) {
        return DEFAULT_RESPONSE;
    }
}
