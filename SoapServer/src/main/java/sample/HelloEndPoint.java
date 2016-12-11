package sample;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by agetsuma on 2016/12/08.
 */
@WebService
public class HelloEndPoint {

    @WebMethod
    public String helloWorld(String name) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "hello " + name;
    }
}
