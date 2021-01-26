# WooDy news collector

## Screenshot

![Preview2](https://user-images.githubusercontent.com/8104602/105889422-cf654680-6016-11eb-9b92-9c5d3d8d8cb0.png)

## Prerequisites

- NodeJS ^8.9
- npm ^5.6
- Java 1.8

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
2. Configure Pipeline section. Select `Pipeline script from SCM`
3. Configure Git repository location
4. Configure script path: `Jenkisfile`

## Testing

Use Postman to test REST services. File `WooDy.postman_collection.json` 
contains requests for testing purposes.

## Jenkins

Jenins build implemented with Jenkinsfile

![Preview2](https://user-images.githubusercontent.com/8104602/105889578-ff144e80-6016-11eb-9c86-85dc8c216818.png)
![Preview2](https://user-images.githubusercontent.com/8104602/105889579-ff144e80-6016-11eb-961b-7461e9a28767.png)
