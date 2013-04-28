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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

/**
 * @author thoeger
 * 
 */
public class DateDeserializer extends JsonDeserializer<Date> {
	
	@Override
	public Date deserialize(final JsonParser jp, final DeserializationContext ctx) throws IOException, JsonProcessingException {
		final String text = jp.getText();
		return DateDeserializer.parse(text);
	}
	
	static Date parse(final String text) {
		// BEWARE THIS IS UGLY CODE STYLE
		
		// Redmine 2.x 2012-01-06T14:43:04Z
		Date parsed = DateDeserializer.parseString(text, "yyyy-MM-dd'T'HH:mm:ssZ");
		
		if (parsed == null) {
			// Redmine 2.x Date only 2012-01-06
			parsed = DateDeserializer.parseString(text, "yyyy-MM-dd");
		}
		
		if (parsed == null) {
			throw new RuntimeException("Cannot parse date");
		}
		
		return parsed;
	}
	
	private static Date parseString(final String s, final String pattern) {
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.parse(s);
		} catch (final ParseException e) {
			// cannot parse date so we try other format
		}
		return null;
	}
}
