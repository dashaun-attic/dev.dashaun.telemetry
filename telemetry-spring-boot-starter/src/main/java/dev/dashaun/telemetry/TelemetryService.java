package dev.dashaun.telemetry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

/**
 * Sends a telemetry payload to a configurable address using {@link RestClient}.
 *
 * <p>The call is best effort: any failure is logged at {@code debug} level and
 * swallowed, so the client application starts and runs regardless of whether the
 * telemetry endpoint is reachable.</p>
 */
public class TelemetryService {

	private static final Logger log = LoggerFactory.getLogger(TelemetryService.class);

	private static final String PAYLOAD = "Hello, world!";

	private final RestClient restClient;

	private final TelemetryProperties properties;

	public TelemetryService(RestClient restClient, TelemetryProperties properties) {
		this.restClient = restClient;
		this.properties = properties;
	}

	/**
	 * POST the payload to the configured address. Never throws.
	 */
	public void send() {
		String address = properties.getAddress();
		try {
			restClient.post()
					.uri(address)
					.contentType(MediaType.TEXT_PLAIN)
					.body(PAYLOAD)
					.retrieve()
					.toBodilessEntity();
			log.debug("Telemetry sent to {}", address);
		}
		catch (Exception ex) {
			log.debug("Telemetry failed to send to {}: {}", address, ex.getMessage());
		}
	}

}
