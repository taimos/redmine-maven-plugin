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
	 * @parameter expression="${closeVersion}" default-value="${project.version}"
	 * @required
	 */
	private String closeVersion;

	@Override
	protected void doExecute() throws MojoExecutionException {
		final List<Version> versions = this.redmine.getVersions(this.getProjectIdentifier());
		for (final Version v : versions) {
			if (this.checkVersion(v)) {
				this.redmine.closeVersion(v);
				return;
			}
		}
		throw new MojoExecutionException(String.format("No version %s-%s found for project %s.", this.getProjectVersionPrefix(),
				Version.cleanSnapshot(this.closeVersion), this.getProjectIdentifier()));
	}

	private boolean checkVersion(final Version v) {
		return v.getName().equals(Version.createName(this.getProjectVersionPrefix(), Version.cleanSnapshot(this.closeVersion)));
	}
}
