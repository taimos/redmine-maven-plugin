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

import de.taimos.maven_redmine_plugin.model.Version;

/**
 * Goal which creates changelog file with all closed versions
 * 
 * @goal changelog
 */
public class ChangelogMojo extends AbstractChangelogMojo {
	
	/**
	 * Changelog file
	 * 
	 * @parameter default-value="target/redmine/changelog"
	 * @required
	 */
	private File changelogFile;
	
	/**
	 * Changelog version
	 * 
	 * @parameter expression="${changelogVersion}" default-value="${project.version}"
	 * @required
	 */
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
		try (FileWriter fw = new FileWriter(this.changelogFile)) {
			// write changelog to file
			fw.write(changelog);
		} catch (final Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}
	
	@Override
	protected void prepareExecute() throws MojoExecutionException {
		try {
			this.changelogFile.getParentFile().mkdirs();
		} catch (final Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}
	
	@Override
	protected String getDateFormat() {
		return "MMM dd yyyy";
	}
	
	@Override
	protected String getEmptyVersionString() {
		return "No tickets found";
	}
}
