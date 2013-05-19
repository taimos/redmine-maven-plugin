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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import de.taimos.maven_redmine_plugin.model.Version;

/**
 * Goal which prints changelog of current version
 */
@Mojo(name = "print")
public class PrintChangelogMojo extends AbstractChangelogMojo {
	
	/**
	 * Changelog version
	 */
	@Parameter(defaultValue = "${project.version}", property = "changelogVersion", required = true)
	private String changelogVersion;
	
	
	@Override
	protected String getVersionHeader(final String version, final String date) {
		return String.format("Version %s (%s) \n", version, date);
	}
	
	@Override
	protected boolean includeVersion(final Version v) throws MojoExecutionException {
		final String version = Version.cleanSnapshot(this.changelogVersion);
		return v.getName().equals(Version.createName(this.getProjectVersionPrefix(), version));
	}
	
	@Override
	protected void doChangelog(final String changelog) throws MojoExecutionException {
		this.getLog().info(changelog);
	}
	
	@Override
	protected String getEmptyVersionString() {
		return "No tickets found";
	}
}
