package utils
import groovy.json.JsonSlurperClassic


class TestView implements Serializable {
    def steps
    def XLTV_HOST = "http://192.168.0.50:6516"
    def authentication = "xltv"
    def contentType = "APPLICATION_JSON"

    TestView(steps) {this.steps = steps}
    def getOrCreateProject(text) {
        def getproject = steps.httpRequest url: "${XLTV_HOST}/api/v1/projects", authentication: "${authentication}", contentType: "${contentType}", acceptType: "${contentType}", httpMode: 'GET'
        def result = new JsonSlurperClassic().parseText(getproject.content)
        if (result.title.contains(text)){
            steps.echo "Project with title ${text} exist"
            def project = result.find { it.title == text }
            def projectId = project.id
            return projectId
        }else{
            def setproject = steps.httpRequest url: "${XLTV_HOST}/api/v1/projects", authentication: "${authentication}", contentType: "${contentType}", acceptType: "${contentType}", httpMode: 'POST', requestBody: "{\"title\":\"${text}\"}"
            def response = new JsonSlurperClassic().parseText(setproject.content)
            def projectId = response.id
            steps.echo "Project with title ${text} was created and ID is ${projectId}"
            return projectId

        }

    }

   def createProject(text){

   }
}





