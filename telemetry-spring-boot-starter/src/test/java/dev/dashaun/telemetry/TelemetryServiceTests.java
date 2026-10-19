package dev.dashaun.telemetry;

import org.junit.jupiter.api.Test;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class TelemetryServiceTests {

	@Test
	void sendPostsToConfiguredAddress() {
		RestClient.Builder builder = RestClient.builder();
		MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
		RestClient restClient = builder.build();

		TelemetryProperties properties = new TelemetryProperties();
		properties.setAddress("https://example.com/telemetry");

		server.expect(once(), requestTo("https://example.com/telemetry"))
				.andExpect(method(HttpMethod.POST))
				.andExpect(content().contentType(MediaType.TEXT_PLAIN))
				.andExpect(content().string("Hello, world!"))
				.andRespond(withSuccess());

		new TelemetryService(restClient, properties).send();

		server.verify();
	}

	@Test
	void sendDefaultsToTelemetryEndpoint() {
		RestClient.Builder builder = RestClient.builder();
		MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
		RestClient restClient = builder.build();

		TelemetryProperties properties = new TelemetryProperties();

		server.expect(once(), requestTo("https://telemetry.dashaun.io"))
				.andExpect(method(HttpMethod.POST))
				.andRespond(withSuccess());

		new TelemetryService(restClient, properties).send();

		server.verify();
	}

	@Test
	void sendDoesNotThrowWhenEndpointUnavailable() {
		RestClient.Builder builder = RestClient.builder();
		MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
		RestClient restClient = builder.build();

		TelemetryProperties properties = new TelemetryProperties();

		server.expect(once(), requestTo("https://telemetry.dashaun.io"))
				.andRespond(withSuccess());

		TelemetryService service = new TelemetryService(restClient, properties);

		assertThatCode(service::send).doesNotThrowAnyException();

		server.verify();
	}

}
