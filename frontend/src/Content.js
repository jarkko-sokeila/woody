import React from 'react';
import {faArrowCircleUp} from "@fortawesome/free-solid-svg-icons";
import {faBars} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Cookies from 'universal-cookie';
import Switch from '@material-ui/core/Switch';
import FeedItem from './FeedItem';
import './Content.css';



class Content extends React.Component {
    constructor(props) {
        super(props);
        this.cookies = new Cookies();
        console.log("cookie: " + this.cookies.get('showDescription'))

        this.state = {
            data: null,
            feeds: [],
            showDescription: typeof this.cookies.get('showDescription') === 'undefined' ? 'true': this.cookies.get('showDescription')
        };

        this.test = 0;
        console.log("this.state showDescription " + this.state.showDescription)
        this.getFeeds = this.getFeeds.bind(this);
        this.toggleMenu = this.toggleMenu.bind(this);
        this.showHeader = this.showHeader.bind(this);
        this.renderFeedItems = this.renderFeedItems.bind(this);
  }

  handleChange = name => event => {
	this.setState({showDescription: event.target.checked})
    this.cookies.set('showDescription', event.target.checked);
    console.log("cookie showDescription " + this.cookies.get('showDescription'))
  };

  getData() {
    fetch('/rest/test')
        .then(response => response.text())
        .then(data => this.setState({ data }));
  }

  getFeeds() {
      var category = this.props.category;
      var url;
      var content = this;
    if(category === null || category === "") {
        url = "/rest/news";
    } else {
        url = "/rest/news?category=" + category;
    }
    console.log("Read news from url " + url)
    fetch(url)
        .then(function(response) {
            console.log(response);
            return response.json();
        })
        .then(function(feeds) {
            console.log(feeds.content);
            content.setState({ feeds: feeds.content });
        });
  }

  getHeaderTitle() {
    var category = this.props.category
    if(category === 'NEWS')
        return "Uutiset";
    else if(category === 'SPORTS')
        return "Urheilu";
    else if(category === 'IT')
        return "IT";
    else if(category === 'ENTERTAINMENT')
        return "Viihde";

    return "Etusivu"
  }

  componentDidMount(){
    console.log('I was triggered during componentDidMount')
    this.getData();
    this.getFeeds();
    this.onTopInit();
  }
  
  onTopInit() {
	  window.addEventListener('scroll', (event) => {
		  if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
		      document.getElementById("topBtn").style.display = "block";
		    } else {
		      document.getElementById("topBtn").style.display = "none";
		    }
	    });
  }
  
  scrollFunction() {
    if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
      document.getElementById("topBtn").style.display = "block";
    } else {
      document.getElementById("topBtn").style.display = "none";
    }
  }
  
  toggleMenu() {
	  console.log('Toggle menu')
	  this.props.onToggle();
  }

  topFunction = (event) => {
	  console.log('Back to top')
	  document.body.scrollTop = 0;
	  document.documentElement.scrollTop = 0;
  }

  showHeader(item) {
      console.log("test loop value " + this.test)
      if(this.test === 0) {
        this.test++
        return true
      }
    
      this.test++
      return false
  }

  renderFeedItems(item, i) {
    console.log("Test")
    var showHeader = this.showHeader(item)
    return <FeedItem key={item.id} item={item} showDescription={this.state.showDescription} showHeader={showHeader} />
  }
  
  render() {
    return (
        <div>
            <div className="header">
                <div className="header-title"><FontAwesomeIcon icon={faBars} onClick={this.toggleMenu} id="menuBtn" /><span>{this.getHeaderTitle()}</span></div> {/* News, {this.state.data}*/}
                <div className="test">
                	<FormControlLabel
		                control={
		                    <Switch checked={this.state.showDescription === 'true'}
		                        onChange={this.handleChange('showDescription')}
		                        value="showDescription" />
		                 }
		                 label="Esikatselu"
                	 />
                </div>
            </div>
            <div className="content">
                {this.state.feeds.length ?
                    this.state.feeds.map(this.renderFeedItems) 
                    : <span>Loading...</span>
                }
            </div>
            <footer className="footer">
            	<div className="center"><p>Copyright 2019 WooDy</p></div>
            </footer>
            
            <FontAwesomeIcon icon={faArrowCircleUp} onClick={this.topFunction} id="topBtn" />
        </div>
    );
  }
}

export default Content;