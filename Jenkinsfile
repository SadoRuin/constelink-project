pipeline {
    agent any

    tools {
        nodejs "nodejs"
    }

    stages {
        stage('Project Build') {
            steps {
                script {
                    if(env.BRANCH_NAME == 'dev-front') {
                        echo "Front Project Build Step"
                        dir('Frontend') {
                            sh 'npm install'
                            sh 'npm run build'
                        }
                    } else if(env.BRANCH_NAME == 'feature-back/auth-server') {
                        echo "Auth Server Project Build Step"
                        dir('Backend/AuthServer') {
                            sh 'chmod +x gradlew'
                            sh './gradlew clean build -x test'
                        }
                    }
                }
            }
        }
        stage('Image Build') {
            steps {
                script {
                    def gitCommitHash = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                    echo "${gitCommitHash}"
                    podTemplate(yaml: '''
                      kind: Pod
                      metadata:
                        name: kaniko
                      spec:
                        containers:
                        - name: kaniko
                          image: gcr.io/kaniko-project/executor:debug
                          imagePullPolicy: Always
                          command:
                          - sleep
                          args:
                          - 99d
                          volumeMounts:
                          - name: docker-config
                            mountPath: /kaniko/.docker
                          tty: true
                        nodeSelector:
                          node-role.kubernetes.io/control-plane: ""
                        volumes:
                        - name: docker-config
                          secret:
                            secretName: regcred
                        ''') {
                        node(POD_LABEL) {
                            container(name: 'kaniko', shell: '/bin/sh') {
                                if(env.BRANCH_NAME == 'dev-front') {
                                    echo "Front Image Build Step"
                                    sh '''
                                    /kaniko/executor --context=`pwd`/Frontend --dockerfile=`pwd`/Frontend/Dockerfile --destination=sadoruin/constelink-front:${gitCommitHash}
                                    '''
                                } else if(env.BRANCH_NAME == 'feature-back/auth-server') {
                                    echo "Auth Server Image Build Step"
                                    sh '''
                                    /kaniko/executor --context=`pwd`/Backend/AuthServer --dockerfile=`pwd`/Backend/AuthServer/Dockerfile --destination=sadoruin/constelink-auth-server:${gitCommitHash}
                                    '''
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
