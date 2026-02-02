pipeline {
  agent any

  environment {
    ACR_LOGIN_SERVER = credentials('ACR_LOGIN_SERVER') // store as secret text in Jenkins
    ACR_USERNAME     = credentials('ACR_USERNAME')
    ACR_PASSWORD     = credentials('ACR_PASSWORD')
    KUBECONFIG_CRED  = credentials('AKS_KUBECONFIG')   // kubeconfig file credential
  }

  stages {
    stage('Build & Test') {
      steps {
        sh 'cd ticket-service && mvn -q test'
        sh 'cd comment-service && mvn -q test'
        sh 'cd user-service && mvn -q test'
        sh 'cd api-gateway && mvn -q test'
      }
    }

    stage('Docker Login ACR') {
      steps {
        sh 'echo $ACR_PASSWORD | docker login $ACR_LOGIN_SERVER -u $ACR_USERNAME --password-stdin'
      }
    }

    stage('Build & Push Images') {
      steps {
        sh 'docker build -t $ACR_LOGIN_SERVER/miniticket/ticket-service:latest ticket-service'
        sh 'docker build -t $ACR_LOGIN_SERVER/miniticket/comment-service:latest comment-service'
        sh 'docker build -t $ACR_LOGIN_SERVER/miniticket/user-service:latest user-service'
        sh 'docker build -t $ACR_LOGIN_SERVER/miniticket/api-gateway:latest api-gateway'

        sh 'docker push $ACR_LOGIN_SERVER/miniticket/ticket-service:latest'
        sh 'docker push $ACR_LOGIN_SERVER/miniticket/comment-service:latest'
        sh 'docker push $ACR_LOGIN_SERVER/miniticket/user-service:latest'
        sh 'docker push $ACR_LOGIN_SERVER/miniticket/api-gateway:latest'
      }
    }

    stage('Deploy to AKS') {
      steps {
        // write kubeconfig file from Jenkins credential
        sh 'cp $KUBECONFIG_CRED ./kubeconfig'
        sh 'export KUBECONFIG=$PWD/kubeconfig && kubectl apply -f infra/k8s/'
      }
    }
  }
}
