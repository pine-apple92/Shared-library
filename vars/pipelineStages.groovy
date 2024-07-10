def call() {
    return {
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
    }
}
