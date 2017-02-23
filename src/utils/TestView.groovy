package utils

import groovy.json.JsonSlurperClassic


class TestView implements Serializable {
    def steps
    def XLTV_HOST
    def authentication
    def contentType = "APPLICATION_JSON"

    TestView(steps, XLTV_HOST, authentication) {
        this.steps = steps
        this.XLTV_HOST = XLTV_HOST
        this.authentication = authentication
         }

    def getOrCreateProject(pn) {
        def Projects = steps.httpRequest url: "${XLTV_HOST}/api/v1/projects", authentication: "${authentication}", contentType: "${contentType}", acceptType: "${contentType}", httpMode: 'GET'
        def result = new JsonSlurperClassic().parseText(Projects.content)
        def res = ["test":"123","test2":"123","TEST master":"123"]
        if(pn in res){
             steps.echo "trueeeeeeeeeeeeeeeeeeeeeeeeeeee"         
         }else{ stepsecho "falseeeeeeeeeeeeeeeeeeeeeeeeeee"}
        def proj = result.title.contains(pn)
        steps.echo "MALAKAAAAAAAAAAAAAAAAAAAAAAAAAAAA: ${proj}"
        if (result.title.contains(pn)) {
            steps.echo "Project with title: ${pn} exist"
            for (int i = 0; i < result.size(); i++) {

                if (result[i].title == "${pn}") {
                    return result[i].id
                }
            }
        } else {
            steps.echo "Project with title: ${pn} does not exist and will be created"
            def Project = steps.httpRequest url: "${XLTV_HOST}/api/v1/projects", authentication: "${authentication}", contentType: "${contentType}", acceptType: "${contentType}", httpMode: 'POST', requestBody: "{\"title\":\"${pn}\"}"
            def response = new JsonSlurperClassic().parseText(Project.content)
            def projectId = response.id
            steps.echo "Project with title: ${pn} was created and ID is: ${projectId}"
            return projectId

        }

    }

    def getSpecificationNames(projectId) {

        def specNames = steps.httpRequest url: "${XLTV_HOST}/api/v1/projects/${projectId}/testspecifications", authentication: "${authentication}", contentType: "${contentType}", acceptType: "${contentType}", httpMode: 'GET'
        def result = new JsonSlurperClassic().parseText(specNames.content)
        def res = [:]
        
        for (int i = 0; i < result.size(); i++) {
            res.put(result[i].title, result[i].id)
        }
        return res
    }

    def createPassiveTestSpecification(projectId, title, testToolName="xlt.JUnit") {
        def res = getSpecificationNames(projectId)
        if (title in res){
            steps.echo "TestSpect with title: ${title} exist and ID is: ${res[title]}"
            return res[title]
        }else {
            steps.echo "TestSpect with title: ${title} does not exist and will be created"
            def testSpec = steps.httpRequest url: "${XLTV_HOST}/api/v1/projects/${projectId}/testspecifications", authentication: "${authentication}", contentType: "${contentType}", acceptType: "${contentType}", httpMode: 'POST', requestBody: "{\"title\":\"${title}\",\"testToolName\":\"${testToolName}\",\"qualificationType\":\"xlt.DefaultFunctionalTestsQualifier\"}"
            def response = new JsonSlurperClassic().parseText(testSpec.content)
            def testSpecId = response.id
            steps.echo "TestSpect with title: ${title} was created and ID is: ${testSpecId}"
            return testSpecId
        }
    }
}
