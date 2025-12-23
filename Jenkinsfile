pipeline {
    agent any // The job can run on any available Jenkins agent (or master, in our case)

    tools {
        // Assuming you are using Maven, ensure Maven is configured in Jenkins -> Manage Jenkins -> Tools
        // The name 'M3' should match your Maven tool configuration name in Jenkins
        maven 'M3'
    }

    stages {
        stage('Checkout') {
            steps {
                // The pipeline automatically checks out the code from the Git repository
                echo 'Checking out code from SCM...'
				checkout scm
            }
        }
        stage('Build & Test') {
            steps {
                // Runs Maven commands to clean, compile, and run unit tests
                sh 'mvn clean install'
            }
        }
        stage('Build Docker Image') {
            steps {
               echo 'Building Docker image...'
               sh 'docker build -t spring-boot-app:latest .'
            }
        }
        stage('Deploy Local Docker') {
            steps {
                script {
                    echo 'Stopping and removing old container...'
                    // Stop and remove any previously running container with the same name
                    sh 'docker stop spring-boot-container || true'
                    sh 'docker rm spring-boot-container || true'

                    echo 'Running new container...'
                    // Run the new image, mapping the host port 8081 to the container port 8080
                    sh '''
						docker run -d -p 8081:8080 --name spring-boot-container spring-boot-app:latest
					'''
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished.'
        }
        failure {
            echo 'Pipeline failed. Check logs for details.'
        }
    }
}