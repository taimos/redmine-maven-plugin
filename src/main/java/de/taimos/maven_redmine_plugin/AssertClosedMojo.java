package de.taimos.maven_redmine_plugin;

/*
 * #%L
 * redmine-maven-plugin Maven Mojo
 * %%
 * Copyright (C) 2012 - 2013 Taimos GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;

import de.taimos.maven_redmine_plugin.model.Ticket;
import de.taimos.maven_redmine_plugin.model.Version;

/**
 * Goal which asserts that all issues for given version are closed
 * 
 * @goal assert-closed
 */
public class AssertClosedMojo extends RedmineMojo {

	/**
	 * assert version
	 * 
	 * @parameter expression="${assertVersion}" default-value="${project.version}"
	 * @required
	 */
	private String assertVersion;

	@Override
	protected void doExecute() throws MojoExecutionException {
		final List<Version> versions = this.redmine.getVersions(this.getProjectIdentifier());

		final Integer versionId = this.getVersionId(versions);
		if (versionId == null) {
			final String verString = Version.createName(this.getProjectVersionPrefix(), Version.cleanSnapshot(this.assertVersion));
			throw new MojoExecutionException("No version found: " + verString);
		}

		final List<Ticket> tickets = this.redmine.getOpenTickets(this.getProjectIdentifier(), versionId);
		if (!tickets.isEmpty()) {
			final String errorText = "Found " + tickets.size() + " open tickets.";
			this.getLog().error(errorText);
			for (final Ticket ticket : tickets) {
				this.getLog().warn("- " + ticket.toString());
			}
			throw new MojoExecutionException(errorText);
		}
	}

	private Integer getVersionId(final List<Version> versions) {
		final String verString = Version.createName(this.getProjectVersionPrefix(), Version.cleanSnapshot(this.assertVersion));
		for (final Version v : versions) {
			if (v.getName().equals(verString)) {
				return v.getId();
			}
		}
		return null;
	}

}