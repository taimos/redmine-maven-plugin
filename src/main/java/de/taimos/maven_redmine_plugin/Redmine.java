package de.taimos.maven_redmine_plugin;

/*
 * #%L redmine-maven-plugin Maven Mojo %% Copyright (C) 2012 - 2013 Taimos GmbH %% Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License. #L%
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import de.taimos.httputils.HTTPRequest;
import de.taimos.httputils.WS;
import de.taimos.httputils.WSConstants;
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
	 * @param redmineUrl the URL of the Redmine server
	 * @param redmineKey the API KEy to connect to Redmine
	 */
	public Redmine(final String redmineUrl, final String redmineKey) {
		this.redmineUrl = redmineUrl;
		this.redmineKey = redmineKey;
		
		this.mapper = new ObjectMapper();
		this.mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
	}
	
	/**
	 * @param project
	 * @param version
	 * @return list of {@link Ticket}
	 */
	public List<Ticket> getClosedTickets(final String project, final Integer version) {
		return this.getTickets(project, version, "closed");
	}
	
	/**
	 * @param project
	 * @param version
	 * @return list of {@link Ticket}
	 */
	public List<Ticket> getOpenTickets(final String project, final Integer version) {
		return this.getTickets(project, version, "open");
	}
	
	/**
	 * @param project
	 * @param version
	 * @param status
	 * @return list of {@link Ticket}
	 */
	public List<Ticket> getTickets(final String project, final Integer version, final String status) {
		// http://redmine/issues.json?project_id=<project>&fixed_version_id=<version>&status_id=<status>
		final List<Ticket> tickets = new ArrayList<>();
		
		int offset = 0;
		int count = Integer.MAX_VALUE;
		while ((tickets.size() < count) && (offset < count)) {
			final String url = "/issues.json?project_id=" + project + "&fixed_version_id=" + version + "&status_id=" + status + "&offset=" + offset;
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
		if (object != null) {
			for (final Version v : object) {
				if (v.getName().equals(version)) {
					return v;
				}
			}
		}
		return null;
	}
	
	/**
	 * @param project
	 * @return array of {@link Version}
	 */
	public List<Version> getVersions(final String project) {
		final HashMap<String, Object> map = this.getResponseAsMap("/projects/" + project + "/versions.json");
		final List<HashMap<String, Object>> object = (List<HashMap<String, Object>>) map.get("versions");
		final Version[] versions = this.mapper.convertValue(object, Version[].class);
		if ((versions != null) && (versions.length != 0)) {
			return Arrays.asList(versions);
		}
		return new ArrayList<>();
	}
	
	private HashMap<String, Object> getResponseAsMap(final String url) {
		try {
			final HttpResponse response = this.createRequest(url).get();
			final String responseAsString = WS.getResponseAsString(response);
			return this.mapper.readValue(responseAsString, HashMap.class);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return new HashMap<>();
	}
	
	private HTTPRequest createRequest(final String url) {
		return WS.url(this.redmineUrl + url).header("X-Redmine-API-Key", this.redmineKey);
	}
	
	/**
	 * @param version the version to close
	 */
	public void closeVersion(final Version version) {
		try {
			final String body;
			final String due = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			final String bodyString = "{\"version\":{\"name\":\"%s\",\"status\":\"closed\",\"due_date\":\"%s\"}}";
			body = String.format(bodyString, version.getName(), due);
			final HTTPRequest req = this.createRequest("/versions/" + version.getId() + ".json");
			req.header(WSConstants.HEADER_CONTENT_TYPE, "application/json");
			final HttpResponse put = req.body(body).put();
			if (!WS.isStatusOK(put)) {
				System.out.println(WS.getResponseAsString(put));
				throw new RuntimeException("Status change failed");
			}
		} catch (final Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Status change failed");
		}
	}
	
	/**
	 * @param version the version to close
	 * @param newName the new version name
	 */
	public void renameVersion(final Version version, final String newName) {
		try {
			final String body = String.format("{\"version\":{\"name\":\"%s\"}}", newName);
			final HTTPRequest req = this.createRequest("/versions/" + version.getId() + ".json");
			req.header(WSConstants.HEADER_CONTENT_TYPE, "application/json");
			final HttpResponse put = req.body(body).put();
			if (!WS.isStatusOK(put)) {
				System.out.println(WS.getResponseAsString(put));
				throw new RuntimeException("Status change failed");
			}
		} catch (final Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Status change failed");
		}
	}
	
	/**
	 * @param project the project identifier
	 * @param name the version name
	 */
	public void createVersion(final String project, final String name) {
		try {
			final String body = String.format("{\"version\":{\"name\":\"%s\",\"status\":\"open\"}}", name);
			final HTTPRequest req = this.createRequest("/projects/" + project + "/versions.json");
			req.header(WSConstants.HEADER_CONTENT_TYPE, "application/json");
			final HttpResponse put = req.body(body).post();
			if (!WS.isStatusOK(put)) {
				System.out.println(WS.getResponseAsString(put));
				throw new RuntimeException("Status change failed");
			}
		} catch (final Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Status change failed");
		}
	}
}
