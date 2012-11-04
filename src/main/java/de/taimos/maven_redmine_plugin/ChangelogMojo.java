package de.taimos.maven_redmine_plugin;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;

import de.taimos.maven_redmine_plugin.model.Ticket;
import de.taimos.maven_redmine_plugin.model.Version;

/**
 * Goal which creates changelog file with all closed versions
 * 
 * @goal changelog
 */
public class ChangelogMojo extends RedmineMojo {

	/**
	 * Changelog file
	 * 
	 * @parameter default-value="target/redmine/changelog"
	 * @required
	 */
	private File changelogFile;

	/**
	 * Changelog author
	 * 
	 * @parameter expression="${changelogAuthor}"
	 * @required
	 */
	private String changelogAuthor;

	@Override
	protected void doExecute() throws MojoExecutionException {
		try {
			this.changelogFile.getParentFile().mkdirs();
		} catch (final Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}

		final List<Version> versions = this.redmine.getVersions(this.getProjectIdentifier());

		final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy");
		// Sort versions
		Collections.sort(versions);
		// Newest first
		Collections.reverse(versions);

		final StringBuilder changelogText = new StringBuilder();

		for (final Version v : versions) {
			if (v.getStatus().equals("closed")) {
				final String date = sdf.format(v.getUpdated_on());
				final String versionString = v.getName() + "-1";
				changelogText.append(String.format("* %s %s %s \n", date, this.changelogAuthor, versionString));
				final List<Ticket> tickets = this.redmine.getClosedTickets(this.getProjectIdentifier(), v.getId());
				Collections.sort(tickets);
				for (final Ticket ticket : tickets) {
					changelogText.append("- ");
					changelogText.append(ticket.toString());
					changelogText.append('\n');
				}
				changelogText.append("\n");
			}
		}

		try (FileWriter fw = new FileWriter(this.changelogFile)) {
			// write changelog to file
			fw.write(changelogText.toString());
		} catch (final Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

}
