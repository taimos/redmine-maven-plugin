package de.taimos.maven_redmine_plugin.model;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

/**
 * @author hoegertn
 * 
 */
public class TicketDeserializer extends JsonDeserializer<Ticket> {

	@Override
	public Ticket deserialize(final JsonParser jp, final DeserializationContext ctx) throws IOException, JsonProcessingException {
		final JsonNode json = jp.readValueAsTree();

		final Ticket t = new Ticket();
		t.setId(json.get("id").getIntValue());
		t.setSubject(json.get("subject").getTextValue());
		t.setTracker(TicketDeserializer.getNestedName(json, "tracker"));
		t.setAssignee(TicketDeserializer.getNestedName(json, "assigned_to"));
		t.setAuthor(json.get("author").get("name").getTextValue());
		t.setCreated(DateDeserializer.parse(json.get("created_on").getTextValue()));
		t.setStartDate(DateDeserializer.parse(json.get("start_date").getTextValue()));
		t.setUpdated(DateDeserializer.parse(json.get("updated_on").getTextValue()));
		t.setDescription(json.get("description").getTextValue());
		t.setFixedVersion(TicketDeserializer.getNestedName(json, "fixed_version"));
		t.setPriority(TicketDeserializer.getNestedName(json, "priority"));
		t.setStatus(TicketDeserializer.getNestedName(json, "status"));

		return t;
	}

	private static String getNestedName(final JsonNode json, final String field) {
		if (json.has(field) && json.get(field).has("name")) {
			return json.get(field).get("name").getTextValue();
		}
		return null;
	}
}
