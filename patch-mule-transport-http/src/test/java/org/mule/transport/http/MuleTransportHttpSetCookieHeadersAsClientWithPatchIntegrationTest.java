package org.mule.transport.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.mule.api.MuleMessage;

import se.skltp.patch.mule.transport.http.headers.MuleTransportHttpSetCookieHeadersAsClientIntegrationTest;

public class MuleTransportHttpSetCookieHeadersAsClientWithPatchIntegrationTest
		extends MuleTransportHttpSetCookieHeadersAsClientIntegrationTest {

	@Test
	@Override
	public void testHttpSetCookieHeadersWithDifferentCaseInResponse_issue_MULE_7680()
			throws Exception {
		try {
			MuleMessage responseMsg = muleClient.send(URL_TEST_PRODUCER,
					"hello", requestMsgProps);
			String payloadStr = IOUtils.toString((InputStream) responseMsg
					.getPayload());
			assertEquals("hello from server", payloadStr);

			assertTrue(responseMsg.getInboundProperty("Set-Cookie") instanceof Cookie[]);
			Cookie[] cookie = responseMsg.getInboundProperty("Set-Cookie");
			assertEquals(2, cookie.length);
			assertEquals("header-name-is-uppercase", cookie[0].getName());
			assertEquals("SET-COOKIE-UPPERCASE-1", cookie[0].getValue());
			assertEquals("header-name-is-camelcase", cookie[1].getName());
			assertEquals("Set-Cookie-CamelCase-1", cookie[1].getValue());

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
