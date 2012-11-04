package de.taimos.maven_redmine_plugin;

import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;

import de.taimos.maven_redmine_plugin.model.Version;

/**
 * Goal which closes the given version
 * 
 * @goal close-version
 */
public class CloseVersionMojo extends RedmineMojo {

	/**
	 * Changelog author
	 * 
	 * @parameter expression="${version}" default-value="${project.version}"
	 * @required
	 */
	private String version;

	@Override
	protected void doExecute() throws MojoExecutionException {
		final List<Version> versions = this.redmine.getVersions(this.getProjectIdentifier());
		for (final Version v : versions) {
			if (v.getName().equals(this.version)) {
				this.redmine.closeVersion(v);
				return;
			}
		}
		throw new MojoExecutionException(String.format("No version %s found for project %s.", this.version, this.getProjectIdentifier()));
	}

}
