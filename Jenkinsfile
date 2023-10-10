pipeline {
    agent any
    tools {
        gradle 'Gradle-8.4'
    }
    stages {
        stage('Source') {
            steps {
                bat 'gradle --version'
                bat 'git --version'
                git url: 'https://github.com/khamroevjs/spring-boot-fullstack.git',
                    branch: 'ci-task',
                    changelog: false, 
                    poll: false
            }
        }
        stage('Clean') {
            steps {
                dir("${env.WORKSPACE}\\backend") {
                    bat 'gradle clean'
                }
            }
        }
        stage('Test') {
            steps {
                dir("${env.WORKSPACE}\\backend") {
                    bat 'gradle test'
                }
            }
        }
        stage('Build') {
            steps {
                dir("${env.WORKSPACE}\\backend") {
                    bat 'gradle war'
                }
            }
        }
        stage('SonarQube Analysis') {
            steps {
                dir("${env.WORKSPACE}\\backend") {
                    withSonarQubeEnv(installationName: 'sonar-server', credentialsId: 'sonar-token') {
                        bat 'gradle sonar'
                    }
                }
            }
        }
        stage('Deploy') {
            steps {
                deploy adapters: [tomcat9(credentialsId: 'tomcat-secret', path: '', url: 'http://localhost:8000/')],
                    contextPath: 'spring-boot', onFailure: false, war: '**/*.war'
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: '**/backend/build/libs/spring-boot-fullstack-1.0.0-plain.war',
                allowEmptyArchive: true,
                onlyIfSuccessful: true,
                fingerprint: true,
                followSymlinks: false
            
            junit allowEmptyResults: true, testResults: '**/build/test-results/test/TEST-*.xml'
            recordCoverage(tools: [[pattern: '**build/test.exec']])
        }
    }
}
