pipeline {
    agent {
        label 'migration'
    }

    tools {
        maven 'apache-maven-latest'
        jdk 'openjdk-jdk17-latest'
    }

    environment {
        BUILD_KEY = (github.isPullRequest() ? CHANGE_TARGET : BRANCH_NAME).replaceFirst(/^v/, '')
        CAPELLA_PRODUCT_PATH = "${WORKSPACE}/capella/capella"
        CAPELLA_BRANCH = 'master'
      }

      options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }
    
    stages {
    
        stage('Generate Target Platform') {
            steps {        
                script { 
                    if(github.isPullRequest()){
                        github.buildStartedComment()
                    }
        
                    currentBuild.description = BUILD_KEY
                    
                    sh 'env'
                    sh 'mvn clean verify -f releng/org.polarsys.capella.diffmerge.defer.releng.target/pom.xml'
                   }
             }
        }
        
        stage('Build and Package') {
              steps {
                  script {
                      def customParams = github.isPullRequest() ? '-DSKIP_SONAR=true' : '-Psign'
              
                      sh "mvn -Djacoco.skip=true -DjavaDocPhase=none ${customParams} clean package -f pom.xml"
                   }
             }
        }

        stage('Deploy to Nightly') {
              steps {
                script {
                    def deploymentDirName = 
                        (github.isPullRequest() ? "${BUILD_KEY}-${BRANCH_NAME}-${BUILD_ID}" : "${BRANCH_NAME}-${BUILD_ID}")
                        .replaceAll('/','-')        
                    
                    deployer.addonNightlyDropins("${WORKSPACE}/releng/org.polarsys.capella.diffmerge.defer.releng.site/target/*-dropins-*.zip", deploymentDirName)
                    deployer.addonNightlyUpdateSite("${WORKSPACE}/releng/org.polarsys.capella.diffmerge.defer.releng.site/target/*-updateSite-*.zip", deploymentDirName)                    
					currentBuild.description = "${deploymentDirName} - <a href=\"https://download.eclipse.org/capella/addons/deferredmerge/dropins/nightly/${deploymentDirName}\">drop-in</a> - <a href=\"https://download.eclipse.org/capella/addons/deferredmerge/updates/nightly/${deploymentDirName}\">update-site</a>"
                   }
             }
        }
        
        stage('Download Capella') {
            steps {
                script {
                    def capellaURL = capella.getDownloadURL("master", 'linux', '')
                    
                    sh "curl -k -o capella.tar.gz ${capellaURL}"
                    sh "tar xzf capella.tar.gz"

                   }
             }
        }

        stage('Install test features') {
            steps {
                script {
                    sh "chmod 755 ${CAPELLA_PRODUCT_PATH}"
                    sh "chmod 755 ${WORKSPACE}/capella/jre/bin/java"
                    
                    eclipse.installFeature("${CAPELLA_PRODUCT_PATH}", capella.getTestUpdateSiteURL("master"), 'org.polarsys.capella.test.feature.feature.group')
                    
                    eclipse.installFeature("${CAPELLA_PRODUCT_PATH}", "file:/${WORKSPACE}/releng/org.polarsys.capella.diffmerge.defer.releng.site/target/repository/".replace("\\", "/"), 'org.polarsys.capella.diffmerge.defer.feature.feature.group')
                    eclipse.installFeature("${CAPELLA_PRODUCT_PATH}", "file:/${WORKSPACE}/releng/org.polarsys.capella.diffmerge.defer.releng.site/target/repository/".replace("\\", "/"), 'org.polarsys.capella.diffmerge.defer.tests.feature.feature.group')
                   }
             }
        }

        stage('Run tests') {
            steps {
                script {
                    wrap([$class: 'Xvnc', takeScreenshot: false, useXauthority: true]) {
                        tester.runUITests("${CAPELLA_PRODUCT_PATH}", 'DiffMergeDeferTestSuite', 'org.polarsys.capella.diffmerge.defer.tests.ju', 
                            ['org.polarsys.capella.diffmerge.defer.tests.ju.testsuites.DiffMergeDeferTestSuite'])                             
                    }
                }
            }
        }
/*
        stage('Run RCPTT') {
            steps {
                script {
                    wrap([$class: 'Xvnc', takeScreenshot: false, useXauthority: true]) {
                        tester.runRcptt("-Prcptt")                           
                    }
                }
            }
        }
*/
        stage('Sonar') {
            steps {
                script {
                    tester.publishTests()
                    sonar.runSonar("eclipse-capella_capella-deferred-merge", "eclipse/capella-deferred-merge", 'sonarcloud-token-deferred-merge')
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/*.log, *.log, *.exec'
        }
        
    	success  {
    		script {
    			if(github.isPullRequest()){
        			github.buildSuccessfullComment()
        		}
        	}
    	}
    	
	    unstable {
	    	script {
	    		if(github.isPullRequest()){
	        		github.buildUnstableComment()
	        	}
	        }
	    }
    
	    failure {
	    	script {
	    		if(github.isPullRequest()){
	        		github.buildFailedComment()
	        	}
	        }
	    }
	    
	    aborted {
	    	script {
	    		if(github.isPullRequest()){
	        		github.buildAbortedComment()
	        	}
	        }
	    }
      }

}