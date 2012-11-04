package de.taimos.maven_redmine_plugin.model;

import java.io.IOException;
import java.util.Date;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author hoegertn
 * 
 */
public class TicketDeserializer extends JsonDeserializer<Ticket> {

	@Override
	public Ticket deserialize(final JsonParser jp, final DeserializationContext ctx) throws IOException, JsonProcessingException {
		final ObjectMapper codec = (ObjectMapper) jp.getCodec();
		final JsonNode json = jp.readValueAsTree();

		final Ticket t = new Ticket();
		t.setId(json.get("id").getIntValue());
		t.setSubject(json.get("subject").getTextValue());
		t.setTracker(json.get("tracker").get("name").getTextValue());
		t.setAssignee(json.get("assigned_to").get("name").getTextValue());
		t.setAuthor(json.get("author").get("name").getTextValue());
		t.setCreated(codec.convertValue(json.get("created_on"), Date.class));
		t.setStartDate(codec.convertValue(json.get("start_date"), Date.class));
		t.setUpdated(codec.convertValue(json.get("updated_on"), Date.class));
		t.setDescription(json.get("description").getTextValue());
		t.setFixedVersion(json.get("fixed_version").get("name").getTextValue());
		t.setPriority(json.get("priority").get("name").getTextValue());
		t.setStatus(json.get("status").get("name").getTextValue());

		return t;
	}
}
