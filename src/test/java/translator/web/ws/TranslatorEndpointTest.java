package translator.web.ws;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ClassUtils;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.beans.factory.annotation.Value;

import translator.Application;
import translator.web.ws.schema.GetTranslationRequest;
import translator.web.ws.schema.GetTranslationResponse;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
public class TranslatorEndpointTest {

  private Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

  @LocalServerPort
  private int port;

  @Value("${wss4j.user}")
  private String user;

  @Value("${wss4j.password}")
  private String password;

  public Wss4jSecurityInterceptor securityInterceptor(){
      Wss4jSecurityInterceptor wss4jSecurityInterceptor = new Wss4jSecurityInterceptor();
      wss4jSecurityInterceptor.setSecurementActions("Timestamp UsernameToken");
      wss4jSecurityInterceptor.setSecurementUsername(user);
      wss4jSecurityInterceptor.setSecurementPassword(password);
      return wss4jSecurityInterceptor;
  }

  @Before
  public void init() throws Exception {
    marshaller.setPackagesToScan(ClassUtils.getPackageName(GetTranslationRequest.class));
    marshaller.afterPropertiesSet();
  }

  @Test
  public void testSendAndReceive() {
    GetTranslationRequest request = new GetTranslationRequest();
    request.setLangFrom("en");
    request.setLangTo("es");
    request.setText("This is a test of translation service");
    ClientInterceptor[] interceptors = new ClientInterceptor[] {securityInterceptor()};
    WebServiceTemplate template = new WebServiceTemplate(marshaller);
    template.setInterceptors(interceptors);
    Object response = template.marshalSendAndReceive("http://localhost:"
            + port + "/ws", request);
    assertNotNull(response);
    assertThat(response, instanceOf(GetTranslationResponse.class));
    GetTranslationResponse translation = (GetTranslationResponse) response;
    assertThat(translation.getTranslation(), is("Esto es una prueba de servicio de traducción"));
  }
}
