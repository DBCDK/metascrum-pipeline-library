package dk.dbc.metascrum.jenkins

class Maven implements Serializable {
    static def verify(script, pmdEnabled = true) {
        script.withEnv(["MAVEN_OPTS=-Dmaven.repo.local=\$WORKSPACE/.repo"])
        script.sh "printenv"
        script.sh "mvn -B -D sourcepath=src/main/java verify pmd:pmd javadoc:aggregate"
        script.junit testResults: '**/target/*-reports/TEST-*.xml'
        if (pmdEnabled) {
            def java = script.scanForIssues tool: [$class: 'Java']
            def javadoc = script.scanForIssues tool: [$class: 'JavaDoc']
            script.publishIssues issues: [java, javadoc]
            def pmd = script.scanForIssues tool: [$class: 'Pmd'], pattern: '**/target/pmd.xml'
            script.publishIssues issues: [pmd]
        }
    }

    static def deploy(script) {
        script.sh "mvn -D deploy -Dmaven.test.skip=true"
    }
}

