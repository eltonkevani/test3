package utils

def checkOutFrom(repo) {
    git url: "git@github.com:jenkinsci/${repo}"
}

