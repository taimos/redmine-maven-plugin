package de.taimos.maven_redmine_plugin;

import org.apache.maven.plugin.MojoExecutionException;

import de.taimos.maven_redmine_plugin.model.Version;

/**
 * Goal which creates changelog file with all closed versions
 * 
 * @goal printchangelog
 */
public class PrintChangelogMojo extends AbstractChangelogMojo {

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
		this.getLog().info(changelog);
	}
}
