package dev.dashaun.telemetry;

import org.junit.jupiter.api.Test;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link TelemetryAutoConfiguration}. These run the auto-configuration
 * in isolation and verify it registers the telemetry beans and honors the
 * {@code dashaun.telemetry.enabled} flag.
 */
class TelemetryAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withUserConfiguration(TestConfiguration.class)
			.withConfiguration(AutoConfigurations.of(TelemetryAutoConfiguration.class));

	@Test
	void registersTelemetryBeansByDefault() {
		contextRunner.run(context -> {
			assertThat(context).hasSingleBean(RestTemplate.class);
			assertThat(context).hasSingleBean(TelemetryService.class);
			assertThat(context).hasSingleBean(ApplicationRunner.class);
		});
	}

	@Test
	void usesConfiguredAddress() {
		contextRunner
				.withPropertyValues("dashaun.telemetry.address=https://example.com/telemetry")
				.run(context -> {
					TelemetryProperties properties = context.getBean(TelemetryProperties.class);
					assertThat(properties.getAddress()).isEqualTo("https://example.com/telemetry");
				});
	}

	@Test
	void noTelemetryBeansWhenDisabled() {
		contextRunner
				.withPropertyValues("dashaun.telemetry.enabled=false")
				.run(context -> {
					assertThat(context).doesNotHaveBean(RestTemplate.class);
					assertThat(context).doesNotHaveBean(TelemetryService.class);
					assertThat(context).doesNotHaveBean(ApplicationRunner.class);
				});
	}

	@Configuration(proxyBeanMethods = false)
	static class TestConfiguration {

		@Bean
		RestTemplateBuilder restTemplateBuilder() {
			return new RestTemplateBuilder();
		}
	}

}
