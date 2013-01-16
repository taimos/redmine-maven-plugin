package de.taimos.maven_redmine_plugin;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;

import de.taimos.maven_redmine_plugin.model.Ticket;
import de.taimos.maven_redmine_plugin.model.Version;

/**
 * 
 */
public abstract class AbstractChangelogMojo extends RedmineMojo {

	@Override
	protected void doExecute() throws MojoExecutionException {
		this.prepareExecute();

		final List<Version> versions = this.redmine.getVersions(this.getProjectIdentifier());

		final SimpleDateFormat sdf = new SimpleDateFormat(this.getDateFormat());
		// Sort versions
		Collections.sort(versions);
		// Newest first
		Collections.reverse(versions);

		final StringBuilder changelogText = new StringBuilder();

		for (final Version v : versions) {
			if (this.includeVersion(v)) {
				final String date = sdf.format(v.getUpdated_on());
				changelogText.append(this.getVersionHeader(v.toVersionString(), date));
				final List<Ticket> tickets = this.redmine.getClosedTickets(this.getProjectIdentifier(), v.getId());
				if (tickets.isEmpty()) {
					this.getLog().warn("No tickets found for version: " + v.toVersionString());
				} else {
					Collections.sort(tickets);
					for (final Ticket ticket : tickets) {
						changelogText.append("- ");
						changelogText.append(ticket.toString());
						changelogText.append('\n');
					}
					changelogText.append("\n");
				}
			}
		}

		this.doChangelog(changelogText.toString());
	}

	protected abstract void doChangelog(String changelog) throws MojoExecutionException;

	/**
	 * @return the version header as String-format. Parameters are version and date
	 */
	protected abstract String getVersionHeader(String version, String date);

	protected abstract boolean includeVersion(Version v) throws MojoExecutionException;

	protected String getDateFormat() {
		return "MMM dd yyyy";
	}

	protected void prepareExecute() throws MojoExecutionException {
		//
	}

}