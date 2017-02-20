package utils

//def checkOutFrom(repo) {
 //   git url: "git@github.com:jenkinsci/${repo}"
//}

class TestView implements Serializable {
    def steps
    TestView(steps) {this.steps = steps}
    def mvn(repo) {
        steps.git url: "git@github.com:jenkinsci/${repo}"
    }
}