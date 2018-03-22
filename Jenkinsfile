pipeline {
  agent any
  stages {
    stage('error') {
      steps {
        build(job: 'Security Validator', wait: true)
        withMaven()
      }
    }
  }
}