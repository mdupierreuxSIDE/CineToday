#!groovy

node('java') {
/*
    def dockerImage = docker.image('android')
    dockerImage.inside {
        stage('Checkout') {
            git url: '/Users/bod/gitrepo/CineToday', branch: 'jenkins'
        }
*/
        stage('Build') {
            sh 'uname -a && ./gradlew lintDebug testDebug'
        }
    //}
}