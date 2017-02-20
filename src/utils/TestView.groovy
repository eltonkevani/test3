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


@Grab(group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.7.1', transitive = false)
@Grab(group = 'org.apache.httpcomponents', module = 'httpmime', version = '4.5.2', transitive = false)
@Grab(group = 'org.apache.httpcomponents', module = 'httpclient', version = '4.5.2', transitive = true)
@Grab(group = 'net.sf.json-lib', module = 'json-lib', version = '2.3', classifier = 'jdk15')
@Grab(group = 'xml-resolver', module = 'xml-resolver', version = '1.2')

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.content.StringBody

class TestView implements Serializable {
    def sendMultiPartFile() {
        String user = 'admin'
        String password = 'admin'
        def userPassBase64 = "$user:$password".toString().bytes.encodeBase64()
        def http = new HTTPBuilder("http://192.168.0.50:6516/api/v1/projects")

        http.setHeaders(
                'Authorization': "Basic $userPassBase64",
        )
        http.setContentType('application/json')

        http.request(Method.GET) { req ->
            uri.path = '/api/v1/projects'

            response.success = { resp, reader ->
                assert resp.statusLine.statusCode == 200
                println "Got response: ${resp.statusLine}"
                println "Content-Type: ${resp.headers.'Content-Type'}"
                println reader.text
            }

            response.'404' = {
                println 'Not found'
            }
        }
    }


}