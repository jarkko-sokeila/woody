import React from 'react';
import { BrowserRouter as Router, Route } from "react-router-dom";
import Navigation from './Navigation';
import Content from './Content';
import './App.css';
import 'primeflex/primeflex.css';

class App extends React.Component {
	constructor(props) {
	    super(props);
	    this.state = {
	    	menuVisible: null
	    };
	    
	    this.nav = React.createRef();
	    this.overlay = React.createRef();
	    this.toggleMenu = this.toggleMenu.bind(this);
	    this.Home = this.Home.bind(this);
	    this.News = this.News.bind(this);
	    this.Sports = this.Sports.bind(this);
	    this.IT = this.IT.bind(this);
	    this.Entertainment = this.Entertainment.bind(this);
	    this.Cars = this.Cars.bind(this);
	    this.Motorbikes = this.Motorbikes.bind(this);
	    this.Science = this.Science.bind(this);
	    this.Lifestyle = this.Lifestyle.bind(this);
	    this.toggleMenu = this.toggleMenu.bind(this);
	}
	
	toggleMenu() {
		//this.setState({menuVisible: !this.menuVisible})
		console.log("App toggle menu ")
		this.nav.current.toggle();
		
		var elem = this.overlay.current;
		console.log("elem: " + elem); 
		elem.classList.toggle('display')
	}
	
	setIsMenuOpen(isOpen) {
		this.setState({menuVisible: isOpen})
		console.log("Set menu open " + isOpen)
	}
	
	Home() {
	  return <Content category="" onToggle={this.toggleMenu} />;
	}

	News() {
	  return <Content category="NEWS" onToggle={this.toggleMenu} />;
	}

	Sports() {
	  return <Content category="SPORTS" onToggle={this.toggleMenu} />;
	}

	IT() {
	  return <Content category="IT" onToggle={this.toggleMenu} />;
	}

	Entertainment() {
	  return <Content category="ENTERTAINMENT" onToggle={this.toggleMenu} />;
	}
	
	Cars() {
	  return <Content category="CARS" onToggle={this.toggleMenu} />;
	}
	
	Motorbikes() {
	  return <Content category="MOTORBIKES" onToggle={this.toggleMenu} />;
	}
	
	Science() {
	  return <Content category="SCIENCE" onToggle={this.toggleMenu} />;
	}
	
	Lifestyle() {
	  return <Content category="LIFESTYLE" onToggle={this.toggleMenu} />;
	}
	
render() {
  return (
      <Router>
        <div className="app">
        	<div ref={this.overlay} onClick={this.toggleMenu} id="overlay"></div>
            <Navigation ref={this.nav} onInit={this.setIsMenuOpen} />

            /*If new category url is added, remember add it also in UrlController*/
            <Route exact path="/" component={this.Home} />
            <Route path="/news" component={this.News} />
            <Route path="/sports" component={this.Sports} />
            <Route path="/it" component={this.IT} />
            <Route path="/entertainment" component={this.Entertainment} />
            <Route path="/cars" component={this.Cars} />
            <Route path="/motorbikes" component={this.Motorbikes} />
            <Route path="/science" component={this.Science} />
            <Route path="/lifestyle" component={this.Lifestyle} />
        </div>
      </Router>
  );
	}
}



export default App;

