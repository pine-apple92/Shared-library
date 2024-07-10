def call() {
    pipeline {
        agent any

        environment {
            REPO_NAME = getRepoName()
        }

        stages{
            stage('Checkout') {
                steps {
                    checkout scm
                }
            }

            stage('Build') {
                steps {
                    script {
                        // jenkins.yml 파일에서 빌드 명령어를 읽어와 실행
                        runBuild()
                    }
                }
            }

            stage('Deploy') {
                steps {
                    echo 'Deploying...'
                    // 배포 명령어 추가
                    //sh './deploy.sh'
                }
            }

            stage('SonarQube Analysis') {
                steps{
                    //def mvn = tool 'Default Maven';
                    dir("${WORKSPACE}"){
                    // Run SonarQube analysis for Python
                        script {
                            def scannerHome = tool name: 'sonar', type: 'hudson.plugins.sonar.SonarRunnerInstallation'
                            withSonarQubeEnv('sonar') {
                                withCredentials([string(credentialsId: 'sonarqube', variable: 'SONAR_TOKEN')]) {
                                    sh "${scannerHome}/bin/sonar-scanner \
                                        -Dsonar.projectKey=Web-goat \
                                        -Dsonar.host.url=http://172.17.0.4:9000 ${env.SONAR_TOKEN} \
                                        -Dsonar.java.binaries=. \
                                        -Dsonar.projectBaseDir=${WORKSPACE}"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
