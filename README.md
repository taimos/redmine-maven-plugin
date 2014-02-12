# redmine-maven-plugin

Maven plugin for redmine

Versions in Redmine must follow this pattern: [prefix-]x[.y[.z]]

Valid versions are e.g.:

* 1.1.0
* subproject-1.2
* sub-1


## Goals

* _changelog_ to create changelog
* _rpm-changelog_ to create changelog with RPM style (All closed versions)
* _close-version_ to close version
* _create-version_ to create new version
* _rename-version_ to rename a version
* _print_ to print changelog to console
* _assert-closed_ to assert that all tickets are closed

## Configuration

Find below the possible parameters for the plugin. The string in () is the maven expression. The string in [] is the default value. 

### Global

* _redmineUrl_ The URL of the Redmine system () [${project.issueManagement.url}] 
* _redmineKey_ The API key to access Redmine (redmineKey) []
* _projectIdentifier_ the name of the project in Redmine (projectIdentifier) [${project.artifactId}]
* _projectVersionPrefix_ The version prefix (projectVersionPrefix) []

### changelog

* _changelogFile_ The target file () [target/redmine/changelog]
* _changelogVersion_ The version to print changelog for (changelogVersion) [${project.version}]
* _changelogTemplate_ The freemarker template file for generated changelog []

### rpm-changelog

* _rpmChangelogFile_ The target file () [target/redmine/rpm-changelog]
* _rpmChangelogAuthor_ The author in RPM format (rpmChangelogAuthor) []
* _rpmMinimalVersion_ The version to start changelog with (rpmMinimalVersion) [0.0.0]
* _changelogTemplate_ The freemarker template file for generated changelog []

### print

* _changelogVersion_ The version to print changelog for (changelogVersion) [${project.version}]
* _changelogTemplate_ The freemarker template file for generated changelog []

### close-version
* _closeVersion_ The version to close (closeVersion) [${project.version}]

### create-version
* _createVersion_ The version to create (createVersion) [${project.version}]

### rename-version
* _renameVersion_ The version to rename (renameVersion) [${project.version}]
* _newName_ The new version name (newName) [${project.version}]

### assert-closed
* _assertVersion_ The version to check for open tickets (assertVersion) [${project.version}]

## Usage example

	<plugin>
		<groupId>de.taimos</groupId>
		<artifactId>redmine-maven-plugin</artifactId>
		<version>1.7.0</version>
		<configuration>
			<projectIdentifier>myproject</projectIdentifier>
			<projectVersionPrefix>myprefix</projectVersionPrefix>
			<changelogFile>target/redmine/changelog</changelogFile>
			<changelogVersion>${project.version}</changelogVersion>
			<rpmChangelogFile>target/redmine/rpmchangelog</rpmChangelogFile>
			<rpmChangelogAuthor>John Doe &lt;john@doe.com&gt;</rpmChangelogAuthor>
		</configuration>
	</plugin>

### _changelogTemplate_ usage example

For example, if _changelogTemplate_ variable specifies path to file with the following content:

    <html>
    <head>Simple test template</head>
    <body>
    <#list versions as version>
        ${version}
        <#assign tickets = tickets[version]/>
            <#list tickets as ticket>
                #${ticket.id} - ${ticket.subject}
            </#list>
    </#list>
    </body>
    </html>

After executing maven goal _redmine:changelog_ the output file _changelog_ will be look like this:

    <html>
    <head>Simple test template</head>
    <body>
        Version [description=Test version, name=TEST, status=READY]
                #101 - First ticket
                #102 - Second ticket
    </body>
    </html>

