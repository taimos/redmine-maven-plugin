package de.taimos.maven_redmine_plugin.model;

import java.util.Date;

/**
 * @author hoegertn
 * 
 */
public class Version implements Comparable<Version> {

	private Date created_on;

	private String description;

	private Integer id;

	private String name;

	private String status;

	private Date updated_on;

	private Date due_date;

	/**
	 * @return the created_on
	 */
	public Date getCreated_on() {
		return this.created_on;
	}

	/**
	 * @param created_on
	 *            the created_on to set
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
	 * @param description
	 *            the description to set
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
	 * @param id
	 *            the id to set
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
	 * @param name
	 *            the name to set
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
	 * @param status
	 *            the status to set
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
	 * @param updated_on
	 *            the updated_on to set
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
	 * @param due_date
	 *            the due_date to set
	 */
	public void setDue_date(final Date due_date) {
		this.due_date = due_date;
	}

	@Override
	public int compareTo(final Version o) {
		return Version.compareVersions(this.name, o.name);
	}

	private static int compareVersions(final String first, final String second) {
		final int[] me = Version.splitVersion(first);
		final int[] other = Version.splitVersion(second);

		if (me[0] == other[0]) {
			if (me[1] == other[1]) {
				return me[2] - other[2];
			}
			return me[1] - other[1];
		}
		return me[0] - other[0];
	}

	private static int[] splitVersion(final String version) {
		final String[] split = version.split("\\.");
		if (split.length != 3) {
			throw new RuntimeException("Illegal version name");
		}
		final int[] res = new int[3];
		res[0] = Integer.valueOf(split[0]);
		res[1] = Integer.valueOf(split[1]);
		res[2] = Integer.valueOf(split[2]);
		return res;
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		System.out.println(Version.compareVersions("1.0.0", "1.1.0"));
		System.out.println(Version.compareVersions("2.0.0", "1.1.0"));
		System.out.println(Version.compareVersions("1.2.0", "1.1.0"));
		System.out.println(Version.compareVersions("1.0.1", "1.1.0"));
		System.out.println(Version.compareVersions("1.1.1", "1.1.0"));
	}

}
