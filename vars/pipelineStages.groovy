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
                    withSonarQubeEnv(credentialsId: 'sonarqube', installationName:'sonar') {
                        sh "./mvnw clean package sonar:sonar -Dsonar.projectKey=Web-goat -Dsonar.projectName=Web-goat -Dsonar.lgin=sqa_3af6559cb970551de0956a209e5a7bc07e6dbd5e"
                    }
                }
            }
        }
    }
}
