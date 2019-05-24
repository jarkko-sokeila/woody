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
            showDescription: typeof this.cookies.get('showDescription') === 'undefined' ? true: this.cookies.get('showDescription') === 'true'
        };

        this.lastFetchTime= null
        this.timeIndex = 0
        this.timeGroups = [0, 5, 10, 30, 60, 120]
        this.timeGroupsBool = [true, true, true, true, true, true]
        console.log("this.state showDescription " + this.state.showDescription)
        this.getFeeds = this.getFeeds.bind(this);
        this.toggleMenu = this.toggleMenu.bind(this);
        this.showHeader = this.showHeader.bind(this);
        this.renderFeedItems = this.renderFeedItems.bind(this);
        this.checkTimeDifference = this.checkTimeDifference.bind(this);
        this.tick = this.tick.bind(this);
  }

  handleChange = name => event => {
	this.timeIndex = 0
	this.timeGroupsBool = [true, true, true, true, true, true]
	this.setState({showDescription: event.target.checked})
    this.cookies.set('showDescription', event.target.checked)
    console.log("event: " + event.target.checked + " ,state showDescription: " + this.state.showDescription + " ,cookie showDescription: " + this.cookies.get('showDescription'))
  };

  getData() {
    fetch('/rest/test')
        .then(response => response.text())
        .then(data => this.setState({ data }));
    
    this.lastFetchTime = new Date()
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
    this.timerID = setInterval(() => this.tick(),1000);
  }

  componentWillUnmount() {
    clearInterval(this.timerID);
  }

  tick() {
	  //console.log("Last fetch time: " + this.lastFetchTime)
	  /*fetch("/rest/unreadcount?datetime=" + this.lastFetchTime.getTime())
      .then(function(response) {
          //console.log(response);
          return response.text();
      })
      .then(function(count) {
          console.log("Unread count is " + count);
      });*/
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
      //console.log("test loop value " + this.test)
	  var currentDate = new Date()
	  var published = new Date(item.published)
	  var difference = (currentDate.getTime() - published.getTime())
	  //console.log("current date: " + currentDate.toLocaleString() + " ,published: " + published.toLocaleString() + " ,difference: " + difference)
	  
      if(this.timeGroupsBool[0] === true && difference < this.minToMs(5)) {
    	  this.timeGroupsBool[0] = false
    	  return 0
      }
	  
	  if(this.timeGroupsBool[1] === true && difference >= this.minToMs(5) && difference < this.minToMs(10)) {
    	  this.timeGroupsBool[1] = false
    	  return 5
      }
	  
	  if(this.timeGroupsBool[2] === true && difference >= this.minToMs(10) && difference < this.minToMs(30)) {
    	  this.timeGroupsBool[2] = false
    	  return 10
      }
	  
	  if(this.timeGroupsBool[3] === true && difference >= this.minToMs(30) && difference < this.minToMs(60)) {
    	  this.timeGroupsBool[3] = false
    	  return 30
      }
    
	  if(this.timeGroupsBool[4] === true && difference >= this.minToMs(60) && difference < this.minToMs(120)) {
    	  this.timeGroupsBool[4] = false
    	  return 60
      }
	  
	  if(this.timeGroupsBool[5] === true && difference >= this.minToMs(120)) {
    	  this.timeGroupsBool[5] = false
    	  return 120
      }
	  
      return undefined
  }
  
  /*showHeader(item) {
      //console.log("test loop value " + this.test)
	  var currentDate = new Date()
	  var published = new Date(item.published)
	  var difference = (currentDate.getTime() - published.getTime())
	  console.log("current date: " + currentDate.toLocaleString() + " ,published: " + published.toLocaleString() + " ,difference: " + difference)
	  
      if(this.timeIndex === 0) {
        this.timeIndex++
        return 0
      }
    
	  console.log("Test timegroup with index " + this.timeIndex)
      if(this.timeIndex < this.timeGroups.length && this.checkTimeDifference(this.timeGroups[this.timeIndex], difference)) {
    	  var header = this.timeGroups[this.timeIndex]
    	  this.timeIndex++
    	  return header
      }
	  
      return undefined
  }*/
  
  minToMs(min) {
	  return min * 60 * 1000
  }
  
  checkTimeDifference(time, difference) {
	  console.log("is difference " + difference + " higher than " + (time * 60 * 1000) + " , time " + time)
	  if(difference > (time * 60 * 1000)) {
		  return true
	  }
	  
	  return false
  }

  renderFeedItems(item, i) {
    //console.log("Test")
    var showHeader = this.showHeader(item)
    return <FeedItem key={item.id} item={item} showDescription={this.state.showDescription} showHeader={showHeader} />
  }
  
  render() {
    return (
        <div>
            <div className="header">
                <div className="header-title">
                	<FontAwesomeIcon icon={faBars} onClick={this.toggleMenu} id="menuBtn" />
                	<span>{this.getHeaderTitle()}</span></div> {/* News, {this.state.data}*/}
                <div className="test">
                	<FormControlLabel
		                control={
		                    <Switch checked={this.state.showDescription}
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