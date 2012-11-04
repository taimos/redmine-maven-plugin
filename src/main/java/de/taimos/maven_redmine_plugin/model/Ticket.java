package de.taimos.maven_redmine_plugin.model;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

/**
 * @author thoeger
 * 
 */
@JsonDeserialize(using = TicketDeserializer.class)
public class Ticket implements Comparable<Ticket> {

	private Integer id;

	private String tracker;

	private String subject;

	private String assignee;

	private String author;

	private Date created;

	private String description;

	private Integer doneRatio;

	private String fixedVersion;

	private String priority;

	private Date startDate;

	private String status;

	private Date updated;

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
	 * @return the tracker
	 */
	public String getTracker() {
		return this.tracker;
	}

	/**
	 * @param tracker
	 *            the tracker to set
	 */
	public void setTracker(final String tracker) {
		this.tracker = tracker;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return this.subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(final String subject) {
		this.subject = subject;
	}

	/**
	 * @return the assignee
	 */
	public String getAssignee() {
		return this.assignee;
	}

	/**
	 * @param assignee
	 *            the assignee to set
	 */
	public void setAssignee(final String assignee) {
		this.assignee = assignee;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return this.author;
	}

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(final String author) {
		this.author = author;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return this.created;
	}

	/**
	 * @param created
	 *            the created to set
	 */
	public void setCreated(final Date created) {
		this.created = created;
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
	 * @return the doneRatio
	 */
	public Integer getDoneRatio() {
		return this.doneRatio;
	}

	/**
	 * @param doneRatio
	 *            the doneRatio to set
	 */
	public void setDoneRatio(final Integer doneRatio) {
		this.doneRatio = doneRatio;
	}

	/**
	 * @return the fixedVersion
	 */
	public String getFixedVersion() {
		return this.fixedVersion;
	}

	/**
	 * @param fixedVersion
	 *            the fixedVersion to set
	 */
	public void setFixedVersion(final String fixedVersion) {
		this.fixedVersion = fixedVersion;
	}

	/**
	 * @return the priority
	 */
	public String getPriority() {
		return this.priority;
	}

	/**
	 * @param priority
	 *            the priority to set
	 */
	public void setPriority(final String priority) {
		this.priority = priority;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return this.startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
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
	 * @return the updated
	 */
	public Date getUpdated() {
		return this.updated;
	}

	/**
	 * @param updated
	 *            the updated to set
	 */
	public void setUpdated(final Date updated) {
		this.updated = updated;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(this.tracker);
		sb.append(" #");
		sb.append(this.id);
		sb.append(": ");
		sb.append(this.subject);
		return sb.toString();
	}

	@Override
	public int compareTo(final Ticket o) {
		int res = this.tracker.compareTo(o.tracker);
		if (res == 0) {
			res = this.id - o.id;
		}
		return res;
	}

}
