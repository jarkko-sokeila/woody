pipeline {
    agent any

    tools {
       jdk "Java 8"
    }

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                script {
                    sh './gradlew clean assemble javadoc'
                }
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
                script {
                    sh './gradlew test'
                }
                junit 'WooDy/build/test-results/test/**/*.xml'
                jacoco()
            }
        }
        stage('Report') {
            steps {
                echo 'Report..'

                publishHTML target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: false,
                    keepAll: true,
                    reportDir: 'WooDy/build/docs/javadoc',
                    reportFiles: 'index.html',
                    reportName: 'JavaDoc'
                  ]

                publishHTML target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: false,
                    keepAll: true,
                    reportDir: 'WooDy/build/reports/tests/test',
                    reportFiles: 'index.html',
                    reportName: 'Junit report'
                  ]

                publishHTML target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: false,
                    keepAll: true,
                    reportDir: 'WooDy/build/reports/jacoco/test/html',
                    reportFiles: 'index.html',
                    reportName: 'Jacoco report'
                  ]
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'WooDy/build/libs/**/*.jar', fingerprint: false
        }
    }
}