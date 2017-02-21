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
            steps.echo "Project with title ${text} exist"
        }else{
            createProject(text)
        }
        //steps.echo "${result[1].title}"
    }

   def createProject(text){
       def setproject = steps.httpRequest url: "${XLTV_HOST}/api/v1/projects", authentication: "${authentication}", contentType: "${contentType}", acceptType: "${contentType}", httpMode: 'POST', requestBody: "{\"title\":\"${text}\"}"
       def response = new JsonSlurper().parseText(setproject.content)
       def projectId = response.id
       steps.echo "Project with title ${text} was created and ID is ${projectId}"
   }
}





