package org.seasar.remoting.axis2.rest.example;

import org.seasar.remoting.axis2.annotation.RestMethod;
import org.seasar.remoting.axis2.annotation.RestUriTemplate;
import org.seasar.remoting.axis2.annotation.RestUriParam;

@RestUriTemplate("/RestService")
public interface SampleRestService {

    SampleRestDto beanEcho(SampleRestDto dto);

    @RestMethod(name = "echo2", httpMethod = RestMethod.HTTP_METHOD_POST)
    SampleRestDto beanEcho2(SampleRestDto dto);

    String getHello();

    @RestMethod(name = "hello2", httpMethod = RestMethod.HTTP_METHOD_GET)
    String getHello2();

    String postEcho(@RestUriParam("msg")
    String message);

    @RestMethod(name = "echo2", httpMethod = RestMethod.HTTP_METHOD_POST)
    String postEcho2(@RestUriParam("id")
    Integer id, @RestUriParam("msg")
    String message);

    String invalid(@RestUriParam("id")
    Integer id, String message);

    String invalid2(@RestUriParam("id")
    Integer id, SampleRestDto dto);

}
