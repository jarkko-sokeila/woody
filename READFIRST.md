# React Project

## Prerequisites

- NodeJS ^8.9
- npm ^5.6

## Steps to Run application in development

1. Open Terminal in frontend project, execute: `npm install` 
   to install frontend dependencies
2  Start backend services with `bootRun` gradle task 
3. Start frontend, run `npm run start` in frontend Terminal
4. Open your browser on http://localhost:3000

## Steps to Run application in production

1. Run `build` gradle task
2. Copy a final jar from Woody/build/libs/Woody.jar to 
   desired location e.g. /opt/woody
3. Start application `java -jar WooDy.jar`
4. Open your browser on http://localhost:8080