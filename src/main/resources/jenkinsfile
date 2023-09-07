   <script>
        pipeline{
            agent any
            environment {
                registry = "vugiangcoder/test-build"
                registryCredential = "dockerhub"
                dockerImage = ""
            }
            stages{
                stage("Clone Git repository"){
                    steps{
                        git(
                            url:"https://gitlab.com/VuGiangCoder/deploy.git",
                            branch: "main",
                            changelog: true,
                            poll: true
                        )

                    }
                }
                stage("Build image"){
                    steps{
                        script{
                            dockerImage=docker.build registry + ":latest"
                        }
                    }
                }
                stage("Run container"){
                    steps{
                        shell "docker run -rm -d -p 3000:3000 vugiangcoder/test-build:latest"
                    }
                }
                stage("Push image to Docker Hub"){
                    steps{
                        script{
                            docker.withRegistry( "", registryCredential){
                                dockerImage.push()
                            }
                        }
                    }
                }
                stage('Deploy') {
                            steps {
                                dir('sourcecode'){
                                    script {
                                        kubernetesDeploy(configs: "deployment.yaml", "deployment-service.yaml")
                                    }
                                }
                            }
                        }
            }
            post {
                always {
                    // Dọn dẹp sau khi pipeline hoàn thành (tuỳ chọn, bạn có thể bỏ đi nếu không cần)
                    script {
                        dockerImage.clean()
                    }
                }
            }
        }
    </script>