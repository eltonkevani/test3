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

def sendMultiPartFile() {

    def http = new HTTPBuilder()

    http.request( 'http://ajax.googleapis.com', GET, TEXT ) { req ->
        uri.path = '/ajax/services/search/web'
        uri.query = [ v:'1.0', q: 'Calvin and Hobbes' ]
        headers.'User-Agent' = "Mozilla/5.0 Firefox/3.0.4"
        headers.Accept = 'application/json'

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

sendMultiPartFile()

