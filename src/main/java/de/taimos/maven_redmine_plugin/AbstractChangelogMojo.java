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

import de.taimos.maven_redmine_plugin.model.Ticket;
import de.taimos.maven_redmine_plugin.model.Version;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 */
public abstract class AbstractChangelogMojo extends RedmineMojo {
	
	@Override
	protected void doExecute() throws MojoExecutionException {
		this.prepareExecute();
		
		List<Version> versions = this.redmine.getVersions(this.getProjectIdentifier());
		
		// Sort versions
		Collections.sort(versions);
		// Newest first
		Collections.reverse(versions);
		// Build input stream
		InputStream stream = getInputStream(versions);

		this.doChangelog(stream);
	}

	private InputStream getInputStream(List<Version> versions) throws MojoExecutionException {
		Map<Version, List<Ticket>> ticketsMap = buildTicketsMap(versions);
		return buildBasicString(ticketsMap);
	}

	private Map<Version, List<Ticket>> buildTicketsMap(List<Version> versions) throws MojoExecutionException {
		Map<Version, List<Ticket>> ticketsMap = new LinkedHashMap<>();

		for (Version v : versions) {
			if (this.includeVersion(v)) {
				List<Ticket> tickets = this.redmine.getClosedTickets(this.getProjectIdentifier(), v.getId());
				if (!tickets.isEmpty()) {
					Collections.sort(tickets);
				}

				ticketsMap.put(v, tickets);
			}
		}

		return ticketsMap;
	}

	private InputStream buildBasicString(Map<Version, List<Ticket>> ticketsMap) throws MojoExecutionException {
		Set<Version> versions = ticketsMap.keySet();
		StringBuilder changelogText = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat(this.getDateFormat(), Locale.US);


		for (Version v : versions) {
			if (this.includeVersion(v)) {
				String date = sdf.format(v.getUpdated_on());
				changelogText.append(this.getVersionHeader(v.toVersionString(), date));
				List<Ticket> tickets = ticketsMap.get(v);

				if (tickets.isEmpty()) {
					this.getLog().warn("No tickets found for version: " + v.toVersionString());
					String empty = this.getEmptyVersionString();
					if (empty != null) {
						changelogText.append(empty);
					}
				} else {
					for (Ticket ticket : tickets) {
						changelogText.append("- ");
						changelogText.append(ticket.toString());
						changelogText.append('\n');
					}
					changelogText.append("\n");
				}
			}
		}

		return new ByteArrayInputStream(changelogText.toString().getBytes());
	}

	protected abstract String getEmptyVersionString();
	
	protected abstract void doChangelog(InputStream changelog) throws MojoExecutionException;
	
	/**
	 * @return the version header as String-format. Parameters are version and date
	 */
	protected abstract String getVersionHeader(String version, String date);
	
	protected abstract boolean includeVersion(Version v) throws MojoExecutionException;
	
	protected String getDateFormat() {
		return "MMM dd yyyy";
	}
	
	@SuppressWarnings("unused")
	protected void prepareExecute() throws MojoExecutionException {
		//
	}
	
}