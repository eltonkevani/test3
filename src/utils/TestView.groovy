package utils

/*

class TestView implements Serializable {
    def steps
    TestView(steps) {this.steps = steps}
    def mvn(repo) {
        steps.git url: "git@github.com:jenkinsci/${repo}"
        steps.echo "lalalalalal"
    }
}
*/


import groovyx.net.http.RESTClient
import org.apache.http.HttpRequest
import org.apache.http.HttpRequestInterceptor
import org.apache.http.protocol.HttpContext

import static groovyx.net.http.ContentType.JSON

class TestView implements Serializable {


    static def host = 'http://192.168.0.50:6516'
    static def contextPath = ''


    private RESTClient restClient
    private projectName
    private projectId
    private enabled

    private static def instances = [:]

    public static TestView getInstance(String pn, enabled = true) {
        if (pn in instances) {
            return instances[pn]
        } else {
            println("TestView: creating new instance for project $pn")
            instances.put(pn, new TestView(pn, enabled))
            return instances[pn]
        }
    }

    public TestView(pn, enabled = true) {
        this.enabled = enabled
        if (enabled) {
            projectName = pn
            restClient = createClient()
            projectId = getOrCreateProject(projectName)
        }
    }

    private RESTClient createClient() {

        def username = 'admin'
        def password = 'admin'


        def client = new RESTClient(host)
        client.client.addRequestInterceptor(new HttpRequestInterceptor() {
            void process(HttpRequest httpRequest, HttpContext httpContext) {
                httpRequest.addHeader('Authorization', 'Basic ' + (username + ":" + password).bytes.encodeBase64().toString())
            }
        })
        return client
    }


    private getOrCreateProject(String projectName) {
        def resp1;
        def pid;
        try {
            resp1 = restClient.get(path: "$contextPath/api/internal/projects", contentType: JSON)
            pid = resp1.data.find { it.title == projectName }
        } catch (Exception e) {
            println("TestView: Error trying to get project $projectName")
        }

        if (pid != null) {
            return pid.name
        }
        // else continue...

        def jsonbody = [
                id: '',
                type: 'xlt.Project',
                title: projectName
        ]

        def resp;
        try {
            resp = restClient.post(path: "$contextPath/api/internal/projects",
                    contentType: JSON,
                    requestContentType: JSON,
                    body: jsonbody)

            return resp.data.name

        } catch (Exception e) {
           println("TestView: Error trying to create project $projectName")

        }

        return null
    }





}

