import React from 'react';
import { BrowserRouter as Router, Route } from "react-router-dom";
import Navigation from './Navigation';
import Content from './Content';
import './App.css';
import 'primeflex/primeflex.css';

function App() {
  return (
      <Router>
        <div className="app">
            <Navigation/>

            <Route exact path="/" component={Home} />
            <Route path="/news" component={News} />
            <Route path="/sports" component={Sports} />
            <Route path="/it" component={IT} />
            <Route path="/entertainment" component={Entertainment} />
        </div>
    </Router>
  );
}

function Home() {
  return <Content category=""/>;
}

function News() {
  return <Content category="NEWS"/>;
}

function Sports() {
  return <Content category="SPORTS"/>;
}

function IT() {
  return <Content category="IT"/>;
}

function Entertainment() {
  return <Content category="ENTERTAINMENT"/>;
}

export default App;
