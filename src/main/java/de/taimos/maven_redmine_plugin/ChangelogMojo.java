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

import de.taimos.maven_redmine_plugin.model.Version;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.IOUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Goal which creates changelog file with all closed versions
 */
@Mojo(name = "changelog")
public class ChangelogMojo extends AbstractChangelogMojo {
	
	/**
	 * Changelog file
	 */
	@Parameter(defaultValue = "${project.build.directory}/redmine/changelog", required = true)
	private File changelogFile;
	
	/**
	 * Changelog version
	 */
	@Parameter(defaultValue = "${project.version}", property = "changelogVersion", required = true)
	private String changelogVersion;
	
	
	@Override
	protected String getVersionHeader(String version, String date) {
		return String.format("Version %s (%s) \n", version, date);
	}
	
	@Override
	protected boolean includeVersion(Version v) throws MojoExecutionException {
		String version = Version.cleanSnapshot(this.changelogVersion);
		return v.getName().equals(Version.createName(this.getProjectVersionPrefix(), version));
	}
	
	@Override
	protected void doChangelog(InputStream changelog) throws MojoExecutionException {
		try (FileOutputStream outputStream = new FileOutputStream(this.changelogFile)) {
			// write changelog to file
			IOUtil.copy(changelog, outputStream);
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		} finally {
			IOUtil.close(changelog);
		}
	}
	
	@Override
	protected void prepareExecute() throws MojoExecutionException {
		try {
			this.changelogFile.getParentFile().mkdirs();
		} catch (Exception e) {
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
