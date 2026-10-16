package dev.dashaun.telemetry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * Sends a telemetry payload to a configurable address using {@link RestTemplate}.
 *
 * <p>The call is best effort: any failure is logged at {@code debug} level and
 * swallowed, so the client application starts and runs regardless of whether the
 * telemetry endpoint is reachable.</p>
 */
public class TelemetryService {

	private static final Logger log = LoggerFactory.getLogger(TelemetryService.class);

	private static final String PAYLOAD = "Hello, world!";

	private final RestTemplate restTemplate;

	private final TelemetryProperties properties;

	public TelemetryService(RestTemplate restTemplate, TelemetryProperties properties) {
		this.restTemplate = restTemplate;
		this.properties = properties;
	}

	/**
	 * POST the payload to the configured address. Never throws.
	 */
	public void send() {
		String address = properties.getAddress();
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.TEXT_PLAIN);
			HttpEntity<String> request = new HttpEntity<>(PAYLOAD, headers);
			restTemplate.postForEntity(address, request, String.class);
			log.debug("Telemetry sent to {}", address);
		}
		catch (Exception ex) {
			log.debug("Telemetry failed to send to {}: {}", address, ex.getMessage());
		}
	}

}
