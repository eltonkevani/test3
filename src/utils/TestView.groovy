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
        def proj = pn
        if (result.title.contains(proj)) {
            steps.echo "Project with title: ${proj} exist"
            for (int i = 0; i < result.size(); i++) {

                if (result[i].title == "${proj}") {
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
        steps.echo "startaaaaaaaaaaaaaaaaaaa the loooop"
        for (int i = 0; i < result.size(); i++) {
            
            res.put(result[i].title, result[i].id)
            steps.echo "${result[i].title}"
        }
        steps.echo "enddddddddddddddddddd the loooop"
        return res
    }

    def createPassiveTestSpecification(projectId, title, testToolName="xlt.JUnit") {
        def res = getSpecificationNames(projectId)
        steps.echo "passiveeeeeeeeeeeeeeeeeeeeeeeeee"
        steps.echo "resssssssssssssss ${res}"
        steps.echo "titleeeeee ${title}"
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
