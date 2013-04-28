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

import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;

import de.taimos.maven_redmine_plugin.model.Version;

/**
 * Goal which closes the given version
 * 
 * @goal create-version
 */
public class CreateVersionMojo extends RedmineMojo {
	
	/**
	 * the version to create
	 * 
	 * @parameter expression="${createVersion}" default-value="${project.version}"
	 * @required
	 */
	private String createVersion;
	
	
	@Override
	protected void doExecute() throws MojoExecutionException {
		final String cleanSnapshot = Version.cleanSnapshot(this.createVersion);
		final String createName = Version.createName(this.getProjectVersionPrefix(), cleanSnapshot);
		
		final List<Version> versions = this.redmine.getVersions(this.getProjectIdentifier());
		for (final Version v : versions) {
			if (v.getName().equals(createName)) {
				this.getLog().info(String.format("Version %s already exists.", createName));
				return;
			}
		}
		this.redmine.createVersion(this.getProjectIdentifier(), createName);
	}
	
}
