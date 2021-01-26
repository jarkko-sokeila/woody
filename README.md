# WooDy news collector

## Screenshots

![Preview2](https://user-images.githubusercontent.com/8104602/105889422-cf654680-6016-11eb-9b92-9c5d3d8d8cb0.png)
![Preview2](https://user-images.githubusercontent.com/8104602/105890865-9037f500-6018-11eb-975f-8863639193f5.png)
![Preview2](https://user-images.githubusercontent.com/8104602/105890867-90d08b80-6018-11eb-9d41-d886168ba9d7.png)

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

## Testing

Use Postman to test REST services. File `WooDy.postman_collection.json` 
contains requests for testing purposes.

## Jenkins build setup
***!Note, Jenkinsfile supports currently only linux***

1. Create new pipeline Item
2. Configure Pipeline section. Select `Pipeline script from SCM`
3. Configure Git repository location
4. Configure script path: `Jenkisfile`

Features

- JavaDoc
- Junit report
- Jacoco code coverage report

![Preview2](https://user-images.githubusercontent.com/8104602/105889578-ff144e80-6016-11eb-9c86-85dc8c216818.png)
![Preview2](https://user-images.githubusercontent.com/8104602/105889579-ff144e80-6016-11eb-961b-7461e9a28767.png)
