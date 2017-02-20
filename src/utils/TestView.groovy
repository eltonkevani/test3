package utils
import groovy.json.JsonSlurper


class TestView implements Serializable {
    def steps
    def jsonSlurper = new JsonSlurper()
    TestView(steps) {this.steps = steps}
    def mvn(text) {
        def result = slurper.parseText(text)
        steps.echo "${result['1']}"
    }
}





