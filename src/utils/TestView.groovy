package utils
import groovy.json.JsonSlurper


class TestView implements Serializable {
    def steps

    TestView(steps) {this.steps = steps}
    def mvn(text) {
        def result = new JsonSlurper().parseText(text)
        if (result.title.contains("XL Deploy (master)")){
            steps.echo "OK"
        }
        steps.echo "${result[1].title}"
    }
}





