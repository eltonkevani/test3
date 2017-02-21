package utils
import groovy.json.JsonSlurper


class TestView implements Serializable {
    def steps
    def XLTV_HOST = "http://192.168.0.50:6516"
    def authentication = "xltv"
    def contentType = "APPLICATION_JSON"

    TestView(steps) {this.steps = steps}
    def getOrCreateProject(text) {
        def project = steps.httpRequest url: "${XLTV_HOST}/api/v1/projects", authentication: "${authentication}", contentType: "${contentType}", acceptType: "${contentType}", httpMode: 'GET'
        def result = new JsonSlurper().parseText(project.content)
        if (result.title.contains(text)){
            steps.echo "project with ${text} exist"
        }
        steps.echo "${result[1].title}"
    }


}





