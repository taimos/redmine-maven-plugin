package de.taimos.maven_redmine_plugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
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
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Abstract Mojo Class for Redmine
 */
public abstract class RedmineMojo extends AbstractMojo {

	/**
	 * Redmine url.
	 * 
	 * @parameter default-value="${project.issueManagement.url}"
	 * @required
	 */
	private String redmineUrl;

	/**
	 * API key used to log in redmine.
	 * 
	 * @parameter expression="${redmineKey}"
	 * @required
	 */
	private String redmineKey;

	/**
	 * Project identifier.
	 * 
	 * @parameter default-value="${project.artifactId}" expression="${projectIdentifier}"
	 * @required
	 */
	private String projectIdentifier;

	/**
	 * Project version prefix.
	 * 
	 * @parameter default-value="" expression="${projectVersionPrefix}"
	 * @required
	 */
	private String projectVersionPrefix;

	protected Redmine redmine;

	protected final String getProjectIdentifier() {
		return this.projectIdentifier;
	}

	protected String getProjectVersionPrefix() {
		return this.projectVersionPrefix;
	}

	private String getRedmineURL() {
		if (this.redmineUrl.matches(".*/projects/.*")) {
			return this.redmineUrl.substring(0, this.redmineUrl.indexOf("/projects/"));
		}
		return this.redmineUrl;
	}

	@Override
	public void execute() throws MojoExecutionException {
		this.redmine = new Redmine(this.getRedmineURL(), this.redmineKey);

		this.doExecute();
	}

	protected abstract void doExecute() throws MojoExecutionException;

}
