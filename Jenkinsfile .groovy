pipeline {
    agent any

    parameters {
        string(name: 'https://github.com/racheli3936/selenium-jenkinsProject.git', defaultValue: 'https://github.com/racheli3936/selenium-jenkinsProject.git', description: 'כתובת ה-Repository')
        string(name: 'main', defaultValue: 'main', description: 'שם הבראנצ לביצוע')
    }

    environment {
        MAIN_BRANCH = 'main' // הגדרת הבראנצ' המרכזי
    }

    stages {
        stage('Checkout Code') {
            steps {
                script {
                    if (params.BRANCH_NAME == MAIN_BRANCH) {
                        // אם הבראנצ' שבחרת תואם לבראנצ' המרכזי
                        checkout scm
                    } else {
                        // אחרת, השתמש בפקודת git להורדת הקוד
                        sh "git clone -b ${params.BRANCH_NAME} ${params.REPO_URL} ."
                    }
                }
            }
        }

        stage('Compile') {
            steps {
                echo "Starting compilation stage"
                timeout(time: 5, unit: 'MINUTES') {
                    sh 'mvn compile'
                }
                echo "Compilation stage completed successfully"
            }
        }

        stage('Run Tests') {
            steps {
                echo "Starting test stage"
                timeout(time: 5, unit: 'MINUTES') {
                    sh 'mvn test'
                }
                echo "Test stage completed successfully"
            }
        }
    }

    post {
        success {
            echo 'Build succeeded!'
        }
        failure {
            echo 'Build failed!'
        }
    }

    triggers {
        cron('H 5 * * 1-5') // כל יום שני עד שישי בשעה 05:30
        cron('H 14 * * 1-5') // כל יום שני עד שישי בשעה 14:00
    }
}
