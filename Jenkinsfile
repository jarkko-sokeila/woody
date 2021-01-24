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
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'WooDy/build/libs/**/*.jar', fingerprint: false
        }
    }
}