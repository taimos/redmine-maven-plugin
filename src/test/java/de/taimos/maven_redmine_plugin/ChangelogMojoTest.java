package de.taimos.maven_redmine_plugin;

import de.taimos.maven_redmine_plugin.model.Ticket;
import de.taimos.maven_redmine_plugin.model.Version;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author Alexandr Kolosov
 * @since 1/15/14
 */
@Ignore
public class ChangelogMojoTest {

	@Test
	public void testTemplateBuild() throws MojoExecutionException, IOException {
		ChangelogMojo mojo = new ChangelogMojo();
		Map<Version, List<Ticket>> ticketsMap = getTestData();

		InputStream stream = ChangelogMojoTest.class.getResourceAsStream("/template.html");
		Assert.assertNotNull(stream);

		InputStreamReader reader = new InputStreamReader(stream);

		InputStream templateStream = mojo.buildTemplate(ticketsMap, "template", reader);
		Assert.assertNotNull(templateStream);
	}

	private Map<Version, List<Ticket>> getTestData() {
		Map<Version, List<Ticket>> ticketsMap = new LinkedHashMap<>();
		Version firstVersion = new Version();
		firstVersion.setCreated_on(new Date());
		firstVersion.setId(1);
		firstVersion.setName("TEST");
		firstVersion.setDescription("Test version");
		firstVersion.setStatus("READY");

		Ticket firstTicket1 = new Ticket();
		firstTicket1.setId(101);
		firstTicket1.setSubject("First ticket");
		firstTicket1.setDescription("First ticket description");

		Ticket firstTicket2 = new Ticket();
		firstTicket2.setId(102);
		firstTicket2.setSubject("Second ticket");
		firstTicket2.setDescription("Second ticket description");

		ticketsMap.put(firstVersion, Arrays.asList(firstTicket1, firstTicket2));
		return ticketsMap;
	}
}
