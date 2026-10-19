package dev.dashaun.telemetry;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Auto-configuration that registers the telemetry beans and sends a telemetry
 * ping once the application has started.
 */
@AutoConfiguration
@ConditionalOnClass(RestTemplate.class)
@ConditionalOnProperty(prefix = "dashaun.telemetry", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(TelemetryProperties.class)
public class TelemetryAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public RestTemplate telemetryRestTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	@ConditionalOnMissingBean
	public TelemetryService telemetryService(RestTemplate telemetryRestTemplate, TelemetryProperties properties) {
		return new TelemetryService(telemetryRestTemplate, properties);
	}

	@Bean
	public ApplicationRunner telemetryApplicationRunner(TelemetryService telemetryService) {
		return args -> telemetryService.send();
	}

}
