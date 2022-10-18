package dk.dbc.metascrum.jenkins

class Maven implements Serializable {
    static def verify(script) {
        script.echo "Building and stuff..."
        script.sh "mvn -D sourcepath=src/main/java verify pmd:pmd javadoc:aggregate"
        script.junit testResults: '**/target/*-reports/TEST-*.xml'
        script {
            def java = scanForIssues tool: [$class: 'Java']
            def javadoc = scanForIssues tool: [$class: 'JavaDoc']
            publishIssues issues: [java, javadoc]

            def pmd = scanForIssues tool: [$class: 'Pmd'], pattern: '**/target/pmd.xml'
            publishIssues issues: [pmd]
        }
    }
}

