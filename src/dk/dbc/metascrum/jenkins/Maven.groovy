package dk.dbc.metascrum.jenkins

class Maven implements Serializable {
    static def verify(script, pmdEnabled = true, profiles="") {
        def prof = ""
        if(!profiles.isEmpty()) {
            prof = "-P\"${profiles}\""
        }
        script.sh "mvn -B ${prof} -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn verify pmd:pmd javadoc:aggregate"
        script.junit testResults: '**/target/*-reports/TEST-*.xml'
        if (pmdEnabled) {
            def java = script.scanForIssues tool: [$class: 'Java']
            def javadoc = script.scanForIssues tool: [$class: 'JavaDoc']
            script.publishIssues issues: [java, javadoc]
            def pmd = script.scanForIssues tool: [$class: 'Pmd'], pattern: '**/target/pmd.xml'
            script.publishIssues issues: [pmd]
        }
    }

    static def deploy(script, projectList = "") {
        def pl = ""
        if (!projectList.isEmpty()) {
            pl = "-pl ${projectList} -am"
        }
        script.sh "mvn deploy -Dmaven.test.skip=true ${pl}"
    }
}

