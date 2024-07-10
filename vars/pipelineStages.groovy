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
                    def mvn = tool 'Default Maven';
                    withSonarQubeEnv() {
                        sh "./mvnw clean verify sonar:sonar -Dsonar.projectKey=Web-goat -Dsonar.projectName='Web-goat'"
                    }
                }
            }
        }
    }
}
