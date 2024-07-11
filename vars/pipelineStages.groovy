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
                    script{
                        def URL = env.GIT_URL
                        def BRANCH = env.GIT_BRANCH

                        build job: 'security_job', parameters:[
                            string(name: 'URL', value: URL),
                            string(name: 'BRANCH', value: BRANCH),
                            string(name: 'REPO', value: REPO_NAME)
                        ]
                    }
                }
            }
        }
    }
}
