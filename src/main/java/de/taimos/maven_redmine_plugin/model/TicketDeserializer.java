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

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author hoegertn
 * 
 */
public class TicketDeserializer extends JsonDeserializer<Ticket> {
	
	@Override
	public Ticket deserialize(final JsonParser jp, final DeserializationContext ctx) throws IOException {
		final JsonNode json = jp.readValueAsTree();
		
		final Ticket t = new Ticket();
		t.setId(json.get("id").asInt());
		t.setSubject(json.get("subject").asText());
		t.setTracker(TicketDeserializer.getNestedName(json, "tracker"));
		t.setAssignee(TicketDeserializer.getNestedName(json, "assigned_to"));
		t.setAuthor(TicketDeserializer.getNestedName(json, "author"));
		if (json.has("created_on")) {
			t.setCreated(DateDeserializer.parse(json.get("created_on").asText()));
		}
		if (json.has("start_date")) {
			t.setStartDate(DateDeserializer.parse(json.get("start_date").asText()));
		}
		if (json.has("updated_on")) {
			t.setUpdated(DateDeserializer.parse(json.get("updated_on").asText()));
		}
		if (json.has("description")) {
			t.setDescription(json.get("description").asText());
		}
		t.setFixedVersion(TicketDeserializer.getNestedName(json, "fixed_version"));
		t.setPriority(TicketDeserializer.getNestedName(json, "priority"));
		t.setStatus(TicketDeserializer.getNestedName(json, "status"));
		
		return t;
	}
	
	private static String getNestedName(final JsonNode json, final String field) {
		if (json.has(field) && json.get(field).has("name")) {
			return json.get(field).get("name").asText();
		}
		return null;
	}
}
