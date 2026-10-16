package dev.dashaun.telemetry;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration for the telemetry starter.
 *
 * <p>{@link #getAddress() The address} is the endpoint the starter POSTs the
 * telemetry payload to on application startup. When not set it defaults to
 * {@code https://telemetry.dashaun.io}.</p>
 */
@ConfigurationProperties(prefix = "dashaun.telemetry")
public class TelemetryProperties {

	/**
	 * The URL to POST the telemetry payload to.
	 */
	private String address = "https://telemetry.dashaun.io";

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
