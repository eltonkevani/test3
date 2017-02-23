properties([buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '10', numToKeepStr: '10')),
            [$class: 'GithubProjectProperty', displayName: '', projectUrlStr: 'https://github.com/eltonkevani/test3'],
            pipelineTriggers([[$class: 'GitHubPushTrigger']])])

stage('Build') {
    parallel Linux: {
        node('node1-salt') {
         echo "This is a pipeline for branch: ${env.BRANCH_NAME}"

        }
    }
}