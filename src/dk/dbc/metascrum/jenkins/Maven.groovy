package dk.dbc.metascrum.jenkins

class Maven {
    static def verify(step) {
        step.sh "mvn -D sourcepath=src/main/java verify pmd:pmd javadoc:aggregate"
        step.junit testResults: '**/target/*-reports/TEST-*.xml'
        step.script {
            def java = scanForIssues tool: [$class: 'Java']
            def javadoc = scanForIssues tool: [$class: 'JavaDoc']
            publishIssues issues: [java, javadoc]

            def pmd = scanForIssues tool: [$class: 'Pmd'], pattern: '**/target/pmd.xml'
            publishIssues issues: [pmd]
        }
    }
}
