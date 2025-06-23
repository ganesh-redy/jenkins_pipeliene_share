// vars/sharepipe.groovy
def call(String name, String image) {
    // Login to Docker Hub and build/push image
    stage("Docker Hub Login and Build/Push") {
        withCredentials([usernamePassword(
            credentialsId: 'docker-hub',
            usernameVariable: 'DOCKER_USER',
            passwordVariable: 'DOCKER_PASS'
        )]) {
            sh """
                echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                docker build -t ${name}/${image}:${env.BUILD_NUMBER} .
                docker push ${name}/${image}:${env.BUILD_NUMBER}
            """
        }
    }
}
