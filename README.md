# redmine-maven-plugin

Maven plugin for redmine

## Usage example

	<plugin>
		<groupId>de.taimos</groupId>
		<artifactId>redmine-maven-plugin</artifactId>
		<version>1.6.0</version>
		<configuration>
			<projectIdentifier>myproject</projectIdentifier>
			<projectVersionPrefix>myprefix</projectVersionPrefix>
			<changelogFile>target/redmine/changelog</changelogFile>
			<changelogVersion>${project.version}</changelogVersion>
			<rpmChangelogFile>target/redmine/rpmchangelog</rpmChangelogFile>
			<rpmChangelogAuthor>John Doe &lt;john@doe.com&gt;</rpmChangelogAuthor>
		</configuration>
	</plugin>

