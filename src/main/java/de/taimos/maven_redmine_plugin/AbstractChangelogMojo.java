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

import de.taimos.maven_redmine_plugin.model.Ticket;
import de.taimos.maven_redmine_plugin.model.Version;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.StringUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 */
public abstract class AbstractChangelogMojo extends RedmineMojo {

	/**
	 * Changelog templates
	 */
	@Parameter(defaultValue = "", property = "changelogTemplate", required = false)
	private String changelogTemplate;
	
	@Override
	protected void doExecute() throws MojoExecutionException {
		this.prepareExecute();

		final List<Version> versions = this.redmine.getVersions(this.getProjectIdentifier());
		
		// Sort versions
		Collections.sort(versions);
		// Newest first
		Collections.reverse(versions);
		// Build input stream
		final InputStream stream = getInputStream(versions);

		try {
			this.doChangelog(stream);
		} finally {
			IOUtil.close(stream);
		}
	}

	private InputStream getInputStream(final List<Version> versions) throws MojoExecutionException {
		Map<Version, List<Ticket>> ticketsMap = buildTicketsMap(versions);
		String template = getChangelogTemplate();
		try {
			FileReader reader = new FileReader(template);
			return template.isEmpty() ? buildBasicString(ticketsMap) : buildTemplate(ticketsMap, template, reader);
		} catch (FileNotFoundException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	private Map<Version, List<Ticket>> buildTicketsMap(final List<Version> versions) throws MojoExecutionException {
		final Map<Version, List<Ticket>> ticketsMap = new LinkedHashMap<>();

		for (Version v : versions) {
			if (this.includeVersion(v)) {
				List<Ticket> tickets = this.redmine.getClosedTickets(this.getProjectIdentifier(), v.getId());
				if (!tickets.isEmpty()) {
					Collections.sort(tickets);
				}

				ticketsMap.put(v, tickets);
			}
		}

		return ticketsMap;
	}

	protected InputStream buildTemplate(final Map<Version, List<Ticket>> ticketsMap, final String templateName, final Reader reader) throws MojoExecutionException {
		Configuration cfg = new Configuration();

		cfg.setDefaultEncoding("UTF-8");
		cfg.setLocale(Locale.US);
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		BeansWrapper wrapper = new BeansWrapper();
		wrapper.setSimpleMapWrapper(true);
		cfg.setObjectWrapper(wrapper);

		Map<Object, Object> model = buildModel(ticketsMap);

		try {
			Template template = new Template(templateName, reader, cfg);
			StringWriter writer = new StringWriter();
			template.process(model, writer);
			return new ByteArrayInputStream(writer.toString().getBytes());
		} catch (TemplateException | IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	private Map<Object, Object> buildModel(final Map<Version, List<Ticket>> ticketsMap) {
		Map<Object, Object> model = new HashMap<>();
		model.put("versions", ticketsMap.keySet());

		// freemarker can't work with non string keys
		Map<String, List<Ticket>> stringKeyMap = new HashMap<>();
		for (Version version : ticketsMap.keySet()) {
			stringKeyMap.put(version.toString(), ticketsMap.get(version));
		}
		model.put("tickets", stringKeyMap);

		return model;
	}

	private InputStream buildBasicString(final Map<Version, List<Ticket>> ticketsMap) throws MojoExecutionException {
		final Set<Version> versions = ticketsMap.keySet();
		final StringBuilder changelogText = new StringBuilder();
		final SimpleDateFormat sdf = new SimpleDateFormat(this.getDateFormat(), Locale.US);

		for (Version v : versions) {
			if (this.includeVersion(v)) {
				String date = sdf.format(v.getUpdated_on());
				changelogText.append(this.getVersionHeader(v.toVersionString(), date));
				List<Ticket> tickets = ticketsMap.get(v);

				if (tickets.isEmpty()) {
					this.getLog().warn("No tickets found for version: " + v.toVersionString());
					String empty = this.getEmptyVersionString();
					if (empty != null) {
						changelogText.append(empty);
					}
				} else {
					for (Ticket ticket : tickets) {
						changelogText.append("- ");
						changelogText.append(ticket.toString());
						changelogText.append('\n');
					}
					changelogText.append("\n");
				}
			}
		}

		return new ByteArrayInputStream(changelogText.toString().getBytes());
	}

	protected abstract String getEmptyVersionString();
	
	protected abstract void doChangelog(final InputStream changelog) throws MojoExecutionException;
	
	/**
	 * @return the version header as String-format. Parameters are version and date
	 */
	protected abstract String getVersionHeader(final String version, final String date);
	
	protected abstract boolean includeVersion(final Version v) throws MojoExecutionException;
	
	protected String getDateFormat() {
		return "MMM dd yyyy";
	}
	
	@SuppressWarnings("unused")
	protected void prepareExecute() throws MojoExecutionException {
		//
	}

	protected String getChangelogTemplate() {
		return StringUtils.isNotEmpty(changelogTemplate) ? changelogTemplate : null;
	}
}