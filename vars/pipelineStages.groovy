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
                environment{
                    scannerHome = tool 'sonar'
                }
                steps{
                    withSonarQubeEnv(credentialsId:"sonarqube",installationName:'sonar') {
                            //sh "${scannerHome}/bin/sonar-scanner"
                        sh "${scannerHome}/bin/sonar-scanner \
                                        -Dsonar.projectKey=${env.REPO_NAME} \
                                        -Dsonar.java.binaries=. \
                                        -Dsonar.projectBaseDir=${WORKSPACE}"
                    }
                    
                }
            }
        }
    }
}
