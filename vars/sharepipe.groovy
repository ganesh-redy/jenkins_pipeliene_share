def call(String name, String image) {
  withEnv([
    "NAME=${name}",
    "DOCKER_IMAGE=${image}"
  ]) {
    stage("Google Cloud Login") {
      sh '''
        gcloud auth activate-service-account --key-file=$GOOGLE_APPLICATION_CREDENTIALS
      '''
    }

    stage("Docker Hub Login") {
      withCredentials([usernamePassword(
        credentialsId: 'docker-hub',
        usernameVariable: 'DOCKER_USER',
        passwordVariable: 'DOCKER_PASS'
      )]) {
        sh '''
          echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
        '''
      }
    }

    stage("Build and Push Docker Image") {
      sh '''
        docker build -t $NAME/$DOCKER_IMAGE:$BUILD_NUMBER .
        docker push $NAME/$DOCKER_IMAGE:$BUILD_NUMBER
      '''
    }
  }
}
