package se.skltp.patch.mule.transport.http.headers;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;

public class MuleTransportHttpSetCookieHeadersAsClientIntegrationTest extends
		FunctionalTestCase {
	protected static final String URL_TEST_PRODUCER = "http://localhost:8082/test/headers";
	protected MuleClient muleClient;
	protected Map<String, Object> requestMsgProps;
	protected LittleProxyWebServerForTesting webServer;

	@Override
	protected String getConfigResources() {
		return "patch-mule-http-transport-test.xml";
	}

	@Override
	protected void doSetUp() throws Exception {
		super.doSetUp();

		webServer = new LittleProxyWebServerForTesting();
		webServer.createAndRunWebServer();

		muleClient = new MuleClient(muleContext);
		requestMsgProps = new HashMap<>();
		requestMsgProps.put("Content-Type", "text/xml");
		requestMsgProps.put("http.method", "POST");
	}

	@Test
	public void testHttpSetCookieHeadersWithDifferentCaseInResponse_issue_MULE_7680()
			throws Exception {
		try {
			MuleMessage responseMsg = muleClient.send(URL_TEST_PRODUCER,
					"hello", requestMsgProps);
			fail("expected exception");
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			// System.out.println(sw);
			assertTrue(sw
					.toString()
					.contains(
							"Caused by: java.lang.IllegalArgumentException: Invalid cookiesObject. Only class org.apache.commons.httpclient.Cookie[] and interface java.util.Map are supported:"));
		}
	}

}
