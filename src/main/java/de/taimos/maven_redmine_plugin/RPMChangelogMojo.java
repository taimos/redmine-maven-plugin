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

import java.io.File;
import java.io.FileWriter;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import de.taimos.maven_redmine_plugin.model.Version;

/**
 * Goal which creates changelog file with all closed versions
 */
@Mojo(name = "rpm-changelog")
public class RPMChangelogMojo extends AbstractChangelogMojo {
	
	/**
	 * Changelog file
	 */
	@Parameter(defaultValue = "${project.build.directory}/redmine/rpm-changelog", required = true)
	private File rpmChangelogFile;
	
	/**
	 * Changelog author
	 */
	@Parameter(property = "rpmChangelogAuthor", required = true)
	private String rpmChangelogAuthor;
	
	/**
	 * minimal changelog version
	 */
	@Parameter(defaultValue = "0.0.0", property = "rpmMinimalVersion")
	private String rpmMinimalVersion;
	
	
	@Override
	protected String getVersionHeader(final String version, final String date) {
		return String.format("* %s %s %s \n", date, this.rpmChangelogAuthor, version + "-1");
	}
	
	@Override
	protected void doChangelog(final String changelog) throws MojoExecutionException {
		try (FileWriter fw = new FileWriter(this.rpmChangelogFile)) {
			// write changelog to file
			fw.write(changelog);
		} catch (final Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}
	
	@Override
	protected void prepareExecute() throws MojoExecutionException {
		try {
			this.rpmChangelogFile.getParentFile().mkdirs();
		} catch (final Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}
	
	@Override
	protected String getDateFormat() {
		return "EEE MMM dd yyyy";
	}
	
	@Override
	protected boolean includeVersion(final Version v) {
		final boolean include = v.getProjectPrefix().equals(this.getProjectVersionPrefix()) && v.getStatus().equals("closed");
		if (include) {
			return v.compareToVersion(this.rpmMinimalVersion) >= 0;
		}
		return false;
	}
	
	@Override
	protected String getEmptyVersionString() {
		return "No tickets found";
	}
	
}
