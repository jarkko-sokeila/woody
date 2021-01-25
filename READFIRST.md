# React Project

## Prerequisites

- NodeJS ^8.9
- npm ^5.6

## Steps to Run application in development

1. Open Terminal in frontend project, execute: `npm install` 
   to install frontend dependencies
2. Start backend services with `bootRun` gradle task 
3. Start frontend, run `npm run start` in frontend Terminal
4. Open your browser on http://localhost:3000

## Steps to Run application in production

1. Run `build` gradle task
2. Copy a final jar from Woody/build/libs/Woody.jar to 
   desired location e.g. /opt/woody
3. Start application `java -jar WooDy.jar`
4. Open your browser on http://localhost:8080

## Jenkins build setup
***!Note, Jenkinsfile supports currently only linux***

1. Create new pipeline Item
2. Configure pPipeline section. Select `Pipeline script from SCM`
3. Configure Git repository location
4. Configure script path: `Jenkisfile`

## Testing

Use Postman to test REST services. File `WooDy.postman_collection.json` 
contains requests for testing purposes.