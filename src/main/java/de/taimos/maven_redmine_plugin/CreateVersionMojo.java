package de.taimos.maven_redmine_plugin;

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
	 * Changelog author
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
