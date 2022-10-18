package dk.dbc.metascrum.jenkins

class Maven implements Serializable {
    static def verify(script) {
        script.echo "Building and stuff..."
        script.sh "mvn -D sourcepath=src/main/java verify pmd:pmd javadoc:aggregate"
        script.junit testResults: '**/target/*-reports/TEST-*.xml'
        def java = script.scanForIssues tool: [$class: 'Java']
        def javadoc = script.scanForIssues tool: [$class: 'JavaDoc']
        script.publishIssues issues: [java, javadoc]
        def pmd = script.scanForIssues tool: [$class: 'Pmd'], pattern: '**/target/pmd.xml'
        script.publishIssues issues: [pmd]
    }
}

