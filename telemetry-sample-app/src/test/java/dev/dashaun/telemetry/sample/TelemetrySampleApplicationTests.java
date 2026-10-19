package dev.dashaun.telemetry.sample;

import dev.dashaun.telemetry.TelemetryService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * Integration test that boots the real sample application and verifies the
 * telemetry starter auto-configuration is active: the telemetry beans are
 * present in the context and the startup {@link ApplicationRunner} invoked
 * {@link TelemetryService#send()}.
 *
 * <p>{@link TelemetryService} is mocked so the test never makes a real network
 * call to the configured telemetry endpoint.</p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class TelemetrySampleApplicationTests {

	@MockitoBean
	private TelemetryService telemetryService;

	@Autowired
	private ApplicationContext context;

	@Test
	void telemetryStarterIsActiveInTheSampleApp() {
		// The auto-configured ApplicationRunner pinged telemetry on startup
		verify(telemetryService, atLeastOnce()).send();

		// The starter's auto-configured beans are present in the sample app context
		assertThat(context.getBean(RestClient.class)).isNotNull();
		assertThat(context.getBean(TelemetryService.class)).isSameAs(telemetryService);
		assertThat(context.getBeansOfType(ApplicationRunner.class)).isNotEmpty();
	}

}
