package de.taimos.maven_redmine_plugin.model;

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

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author hoegertn
 * 
 */
public class Version implements Comparable<Version> {
	
	@JsonDeserialize(using = DateDeserializer.class)
	private Date created_on;
	
	private String description;
	
	private Integer id;
	
	private String name;
	
	private String status;
	
	@JsonDeserialize(using = DateDeserializer.class)
	private Date updated_on;
	
	@JsonDeserialize(using = DateDeserializer.class)
	private Date due_date;
	
	
	/**
	 * @return the created_on
	 */
	public Date getCreated_on() {
		return this.created_on;
	}
	
	/**
	 * @param created_on the created_on to set
	 */
	public void setCreated_on(final Date created_on) {
		this.created_on = created_on;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return this.id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(final Integer id) {
		this.id = id;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}
	
	/**
	 * @return the status
	 */
	public String getStatus() {
		return this.status;
	}
	
	/**
	 * @param status the status to set
	 */
	public void setStatus(final String status) {
		this.status = status;
	}
	
	/**
	 * @return the updated_on
	 */
	public Date getUpdated_on() {
		return this.updated_on;
	}
	
	/**
	 * @param updated_on the updated_on to set
	 */
	public void setUpdated_on(final Date updated_on) {
		this.updated_on = updated_on;
	}
	
	/**
	 * @return the due_date
	 */
	public Date getDue_date() {
		return this.due_date;
	}
	
	/**
	 * @param due_date the due_date to set
	 */
	public void setDue_date(final Date due_date) {
		this.due_date = due_date;
	}
	
	/**
	 * @return the projectPrefix
	 */
	@JsonIgnore
	public String getProjectPrefix() {
		final int pos = this.name.indexOf("-");
		if (pos == -1) {
			return "";
		}
		return this.name.substring(0, pos);
	}
	
	/**
	 * @return the numeric parts
	 */
	@JsonIgnore
	public int[] getNumericParts() {
		final int pos = this.name.indexOf("-");
		if (pos == -1) {
			return Version.splitVersion(this.name);
		}
		return Version.splitVersion(this.name.substring(pos + 1));
	}
	
	/**
	 * @return the version as x.y.z without eventual prefix
	 */
	public String toVersionString() {
		final int pos = this.name.indexOf("-");
		if (pos == -1) {
			return this.name;
		}
		return this.name.substring(pos + 1);
	}
	
	@Override
	public int compareTo(final Version o) {
		int comp = this.getProjectPrefix().compareTo(o.getProjectPrefix());
		if (comp == 0) {
			comp = Version.compareVersions(this.getNumericParts(), o.getNumericParts());
		}
		return comp;
	}
	
	public int compareToVersion(final String o) {
		return Version.compareVersions(this.getNumericParts(), Version.splitVersion(o));
	}
	
	public static int compareVersions(final String me, final String other) {
		return Version.compareVersions(Version.splitVersion(me), Version.splitVersion(other));
	}
	
	private static int compareVersions(final int[] me, final int[] other) {
		if (me[0] == other[0]) {
			if (me[1] == other[1]) {
				return me[2] - other[2];
			}
			return me[1] - other[1];
		}
		return me[0] - other[0];
	}
	
	private static int[] splitVersion(final String version) {
		if (!version.matches("\\d+(\\.\\d){0,2}")) {
			return new int[3];
		}
		final String[] split = version.split("\\.");
		if (split.length > 3) {
			throw new RedmineException("Illegal version name");
		}
		final int[] res = new int[3];
		switch (split.length) {
		case 3:
			res[2] = Integer.valueOf(split[2]);
			//$FALL-THROUGH$
		case 2:
			res[1] = Integer.valueOf(split[1]);
			//$FALL-THROUGH$
		case 1:
			res[0] = Integer.valueOf(split[0]);
			break;
		default:
			break;
		}
		return res;
	}
	
	/**
	 * @param projectPrefix
	 * @param version
	 * @return [{projectPrefix}-]{version}
	 */
	public static String createName(final String projectPrefix, final String version) {
		if ((projectPrefix != null) && !projectPrefix.isEmpty()) {
			return projectPrefix + "-" + version;
		}
		return version;
	}
	
	/**
	 * @param version
	 * @return the version without -SNAPSHOT
	 */
	public static String cleanSnapshot(final String version) {
		return version.replaceAll("-SNAPSHOT", "");
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Version [description=");
		builder.append(this.description);
		builder.append(", name=");
		builder.append(this.name);
		builder.append(", status=");
		builder.append(this.status);
		builder.append("]");
		return builder.toString();
	}
	
}
