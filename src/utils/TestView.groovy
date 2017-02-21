package utils

import groovy.json.JsonSlurperClassic


class TestView implements Serializable {
    def steps
    def XLTV_HOST = "http://192.168.0.50:6516"
    def authentication = "xltv"
    def contentType = "APPLICATION_JSON"

    TestView(steps) { this.steps = steps }

    def getOrCreateProject(pn) {
        def Projects = steps.httpRequest url: "${XLTV_HOST}/api/v1/projects", authentication: "${authentication}", contentType: "${contentType}", acceptType: "${contentType}", httpMode: 'GET'
        def result = new JsonSlurperClassic().parseText(Projects.content)
        if (result.title.contains(pn)) {
            steps.echo "Project with title ${pn} exist"
            for (int i = 0; i < result.size(); i++) {

                if (result[i].title == "${pn}") {
                    return result[i].id
                }
            }
        } else {
            def Project = steps.httpRequest url: "${XLTV_HOST}/api/v1/projects", authentication: "${authentication}", contentType: "${contentType}", acceptType: "${contentType}", httpMode: 'POST', requestBody: "{\"title\":\"${pn}\"}"
            def response = new JsonSlurperClassic().parseText(Project.content)
            def projectId = response.id
            steps.echo "Project with title ${pn} was created and ID is ${projectId}"
            return projectId

        }

    }

    def getSpecificationNames(projectId) {

        def specNames = steps.httpRequest url: "${XLTV_HOST}/api/v1/projects/${projectId}/testspecifications", authentication: "${authentication}", contentType: "${contentType}", acceptType: "${contentType}", httpMode: 'GET'
        def result = new JsonSlurperClassic().parseText(specNames.content)
        def res = [:]

        for (int i = 0; i < result.size(); i++) {
            res.put(result[i].id, result[i].title)
        }
        return res
    }

    def createPassiveTestSpecification(projectId, title, testToolName="xlt.JUnit") {

        def testSpec = steps.httpRequest url: "${XLTV_HOST}/api/v1/projects/${projectId}/testspecifications", authentication: "${authentication}", contentType: "${contentType}", acceptType: "${contentType}", httpMode: 'POST', requestBody: "{\"title\":\"${title}\",\"testToolName\":\"${testToolName}\",\"qualificationType\":\"xlt.DefaultFunctionalTestsQualifier\"}"
        def response = new JsonSlurperClassic().parseText(testSpec.content)
        def testSpecId = response.id
        steps.echo "TestSpect with title ${title} was created and ID is ${testSpecId}"
        return testSpecId
    }
}





