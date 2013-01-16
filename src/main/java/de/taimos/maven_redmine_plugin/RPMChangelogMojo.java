package de.taimos.maven_redmine_plugin;

import java.io.File;
import java.io.FileWriter;

import org.apache.maven.plugin.MojoExecutionException;

import de.taimos.maven_redmine_plugin.model.Version;

/**
 * Goal which creates changelog file with all closed versions
 * 
 * @goal rpm-changelog
 */
public class RPMChangelogMojo extends AbstractChangelogMojo {

	/**
	 * Changelog file
	 * 
	 * @parameter default-value="target/redmine/rpm-changelog"
	 * @required
	 */
	private File rpmChangelogFile;

	/**
	 * Changelog author
	 * 
	 * @parameter expression="${rpmChangelogAuthor}"
	 * @required
	 */
	private String rpmChangelogAuthor;

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
		return v.getProjectPrefix().equals(this.getProjectVersionPrefix()) && v.getStatus().equals("closed");
	}

}
