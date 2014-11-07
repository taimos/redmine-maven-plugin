/**
 * 
 */
package de.taimos.maven_redmine_plugin.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Copyright 2013 Hoegernet<br>
 * <br>
 * 
 * @author hoegertn
 * 
 */
public class VersionTest {
	
	@Test
	public void testCompare() {
		Assert.assertEquals(-1, Version.compareVersions("1.0.0", "1.1.0"));
		Assert.assertEquals(1, Version.compareVersions("2.0.0", "1.1.0"));
		Assert.assertEquals(1, Version.compareVersions("2", "1.1.0"));
		Assert.assertEquals(1, Version.compareVersions("1.2.0", "1.1.0"));
		Assert.assertEquals(-1, Version.compareVersions("1.0.1", "1.1.0"));
		Assert.assertEquals(0, Version.compareVersions("1.1.0", "1.1"));
		Assert.assertEquals(1, Version.compareVersions("1.2", "1.1.2"));
		Assert.assertEquals(1, Version.compareVersions("1.2", "demo"));
	}
	
	@Test
	public void testCleaner() {
		Assert.assertEquals("1.2.0", Version.cleanSnapshot("1.2.0-SNAPSHOT"));
		Assert.assertEquals("1.2.0", Version.cleanSnapshot("1.2.0"));
	}
	
	@Test
	public void testName() {
		Assert.assertEquals("demo-1.2.0", Version.createName("demo", "1.2.0"));
		Assert.assertEquals("1.2.0", Version.createName("", "1.2.0"));
		Assert.assertEquals("1.2.0", Version.createName(null, "1.2.0"));
	}
	
}
