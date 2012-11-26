package de.taimos.maven_redmine_plugin.model;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonParseException;
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

	static Date parse(final String text) throws JsonParseException {
		// BEWARE THIS IS UGLY CODE STYLE
		Date parsed = null;

		try {
			// Redmine 2.x 2012-01-06T14:43:04Z
			final SimpleDateFormat newDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			parsed = newDF.parse(text);
		} catch (final ParseException e) {
			// cannot parse date so we try other format
		}

		if (parsed == null) {
			try {
				// Redmine 2.x Date only 2012-01-06
				final SimpleDateFormat newDF = new SimpleDateFormat("yyyy-MM-dd");
				parsed = newDF.parse(text);
			} catch (final ParseException e) {
				// cannot parse date so we try other format
			}
		}

		if (parsed == null) {
			try {
				// Redmine 1.x 2012/10/09 09:29:19 +0200
				final SimpleDateFormat oldDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z");
				parsed = oldDF.parse(text);
			} catch (final ParseException e) {
				// cannot parse date so we try other format
			}
		}
		if (parsed == null) {
			try {
				// Redmine 1.x Date only 2012/10/09
				final SimpleDateFormat oldDF = new SimpleDateFormat("yyyy/MM/dd");
				parsed = oldDF.parse(text);
			} catch (final ParseException e) {
				// cannot parse date so we try other format
			}
		}
		if (parsed == null) {
			throw new RuntimeException("Cannot parse date");
		}

		return parsed;
	}
}
