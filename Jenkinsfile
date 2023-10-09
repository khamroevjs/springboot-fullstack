pipeline {
    agent any
    tools {
        gradle 'Gradle-8.4'
    }
    parameters {
        string(name: 'TOMCAT_PATH',
            defaultValue: 'C:/Program Files/Apache Software Foundation/Tomcat 10.1',
            description: 'Enter Tomcat Server 10 folder path')
    }
    stages {
        stage('Source') {
            steps {
                pwsh 'gradle --version'
                pwsh 'git --version'
                git url: 'https://github.com/khamroevjs/spring-boot-fullstack.git'
                    branch: 'ci-task',
                    changelog: false, 
                    poll: false
            }
        }
        stage('Clean') {
            steps {
                dir("${env.WORKSPACE}/backend") {
                    pwsh 'gradle clean'
                }
            }
        }
        stage('Test') {
            steps {
                dir("${env.WORKSPACE}/backend") {
                    pwsh 'gradle test'
                }
            }
        }
        stage('Build') {
            steps {
                dir("${env.WORKSPACE}/backend") {
                    pwsh 'gradle war'
                }
            }
        }
        stage('Deploy') {
            steps {
                input message: 'Confirm deployment to production...', ok: 'Deploy'
                pwsh "\"${env.TOMCAT_PATH}/bin/shutdown.bat\""
                pwsh "cp \"${env.WORKSPACE}/backend/build/libs/spring-boot-fullstack-1.0.0-plain.war\" \"${env.TOMCAT_PATH}/webapps/spring-boot.war\""
                pwsh "\"${env.TOMCAT_PATH}/bin/startup.bat\""
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
        }
    }
}