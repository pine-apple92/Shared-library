@Grab(group='org.codehaus.groovy', module='groovy-yaml', version='2.4.12')
import groovy.yaml.YamlSlurper

def call(String yamlFilePath = 'jenkins.yml') {
    // YAML 파일을 읽기
    def yamlContent = readFile(yamlFilePath)
    def yaml = new YamlSlurper().parseText(yamlContent)

    // build 섹션의 명령어 실행
    if (yaml.build) {
        yaml.build.each { command ->
            sh command
        }
    } else {
        error "No build section found in ${yamlFilePath}"
    }
}
