def call() {
    pipeline {
        agent any

        environment {
            def URL = scm.getUserRemoteConfigs()[0].getUrl()
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
                    build job: 'security_job', parameters:[
                        string(name: 'URL', value: ${URL}),
                        string(name: 'BRANCH', value: ${env.BRANCH_NAME}),
                        string(name: 'REPO', value: ${REPO_NAME})
                    ]
                }
            }
        }
    }
}
