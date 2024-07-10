def call() {
    def repoUrl = scm.getUserRemoteConfigs()[0].getUrl()
    def repoName = repoUrl.tokenize('/').last().replaceAll(/\.git$/, '')
    return repoName
}
