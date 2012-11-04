package de.taimos.maven_redmine_plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import de.taimos.httputils.WS;
import de.taimos.maven_redmine_plugin.model.Ticket;
import de.taimos.maven_redmine_plugin.model.Version;

/**
 * @author hoegertn
 * 
 */
public class Redmine {

	private final ObjectMapper mapper;

	private final String redmineUrl;
	private final String redmineKey;

	/**
	 * @param redmineUrl
	 *            the URL of the Redmine server
	 * @param redmineKey
	 *            the API KEy to connect to Redmine
	 */
	public Redmine(final String redmineUrl, final String redmineKey) {
		this.redmineUrl = redmineUrl;
		this.redmineKey = redmineKey;

		this.mapper = new ObjectMapper();
		this.mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * @param project
	 * @param version
	 * @return list of {@link Ticket}
	 */
	@SuppressWarnings("unchecked")
	public List<Ticket> getClosedTickets(final String project, final Integer version) {
		// http://redmine/issues.json?project_id=<project>&fixed_version_id=<version>&status_id=closed
		final List<Ticket> tickets = new ArrayList<>();

		int offset = 0;
		int count = Integer.MAX_VALUE;
		while ((tickets.size() < count) && (offset < count)) {
			final String url = "/issues.json?project_id=" + project + "&fixed_version_id=" + version + "&status_id=closed&offset=" + offset;
			final HashMap<String, Object> map = this.getResponseAsMap(url);
			final List<HashMap<String, Object>> issues = (List<HashMap<String, Object>>) map.get("issues");
			count = (int) map.get("total_count");
			offset += 25;

			for (final HashMap<String, Object> hashMap : issues) {
				tickets.add(this.mapper.convertValue(hashMap, Ticket.class));
			}
		}
		return tickets;
	}

	/**
	 * @param project
	 * @param version
	 * @return the {@link Version}
	 */
	public Version getVersion(final String project, final String version) {
		final List<Version> object = this.getVersions(project);
		for (final Version v : object) {
			if (v.getName().equals(version)) {
				return v;
			}
		}
		return null;
	}

	/**
	 * @param project
	 * @return array of {@link Version}
	 */
	@SuppressWarnings("unchecked")
	public List<Version> getVersions(final String project) {
		final HashMap<String, Object> map = this.getResponseAsMap("/projects/" + project + "/versions.json");
		final List<HashMap<String, Object>> object = (List<HashMap<String, Object>>) map.get("versions");
		final Version[] versions = this.mapper.convertValue(object, Version[].class);
		return Arrays.asList(versions);
	}

	@SuppressWarnings("unchecked")
	private HashMap<String, Object> getResponseAsMap(final String url) {
		try {
			final HttpResponse response = WS.url(this.redmineUrl + url).header("X-Redmine-API-Key", this.redmineKey).get();
			final String responseAsString = WS.getResponseAsString(response);
			return this.mapper.readValue(responseAsString, HashMap.class);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return new HashMap<>();
	}
}
