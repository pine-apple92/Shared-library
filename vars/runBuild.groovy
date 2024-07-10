def call(String yamlFilePath = 'jenkins.yml') {
    // YAML 파일을 읽기
    def yaml = readYaml file: yamlFilePath

    // build 섹션의 명령어 실행
    if (yaml.build) {
        sh "chmod 777 ./*"
        yaml.build.each { command ->
            sh command
        }
    } else {
        error "No build section found in ${yamlFilePath}"
    }
}