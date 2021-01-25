import React from 'react';
import {faArrowCircleUp} from "@fortawesome/free-solid-svg-icons";
import {faBars} from "@fortawesome/free-solid-svg-icons";
import {faSyncAlt} from "@fortawesome/free-solid-svg-icons";
import {faCog} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Badge from '@material-ui/core/Badge';
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
            unreadCount: 0,
            showDescription: typeof this.cookies.get('showDescription') === 'undefined' ? true: this.cookies.get('showDescription') === 'true'
        };

        this.lastFetchTime= null
        this.timeIndex = 0
        this.timeGroups = [0, 5, 10, 30, 60, 120, 240]
        this.timeGroupsBool = [true, true, true, true, true, true, true]
        console.log("this.state showDescription " + this.state.showDescription)
        this.getFeeds = this.getFeeds.bind(this);
        this.showHeader = this.showHeader.bind(this);
        this.toggleMenu = this.toggleMenu.bind(this);
        this.getData = this.getData.bind(this);
        this.renderFeedItems = this.renderFeedItems.bind(this);
        this.checkTimeDifference = this.checkTimeDifference.bind(this);
        this.refresh = this.refresh.bind(this);
        this.tick = this.tick.bind(this);
  }

  handleShowDescriptionToggle = name => event => {
	this.timeIndex = 0
	this.timeGroupsBool = [true, true, true, true, true, true, true]
	this.setState({showDescription: event.target.checked})
    this.cookies.set('showDescription', event.target.checked)
    console.log("event: " + event.target.checked + " ,state showDescription: " + this.state.showDescription + " ,cookie showDescription: " + this.cookies.get('showDescription'))
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
    this.setState({unreadCount: 0})
    this.timeGroupsBool = [true, true, true, true, true, true, true]
    console.log("Read news from url " + url)
    fetch(url)
        .then(function(response) {
            console.log(response);
            return response.json();
        })
        .then(function(feeds) {
            console.log(feeds.content);
            content.timeGroupsBool = [true, true, true, true, true, true, true]
            content.setState({ feeds: feeds.content });
        });

    this.lastFetchTime = new Date()
  }

  refresh() {
    this.topFunction()
    this.getFeeds()
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
    else if(category === 'CARS')
        return "Autot";
    else if(category === 'MOTORBIKES')
        return "Moottoripyörät";
    else if(category === 'SCIENCE')
        return "Tiede";
    else if(category === 'LIFESTYLE')
        return "Lifestyle";

    return "Etusivu"
  }

  componentDidMount(){
    console.log('I was triggered during componentDidMount')
    this.getData();
    this.getFeeds();
    this.onTopInit();
    this.timerID = setInterval(() => this.tick(),5000);
  }

  componentWillUnmount() {
    clearInterval(this.timerID);
  }

  tick() {
      console.log("Last fetch time: " + this.lastFetchTime)
      var category = this.props.category;
      var content = this;
      var url;
      if(category === null || category === "") {
        url = "/rest/unreadcount?datetime=" + this.lastFetchTime.getTime();
      } else {
        url = "/rest/unreadcount?category=" + category + "&datetime=" + this.lastFetchTime.getTime();
      }
	  fetch(url)
      .then(function(response) {
          //console.log(response);
          return response.text();
      })
      .then(function(count) {
          //console.log("Unread count is " + count + " state unread count " + content.state.unreadCount);
          if(content.state.unreadCount !== parseInt(count)) {
            content.timeGroupsBool = [true, true, true, true, true, true, true]
            content.setState({unreadCount: parseInt(count)})
          }
      });
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

  topFunction() {
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
	  
	  if(this.timeGroupsBool[5] === true && difference >= this.minToMs(120) && difference < this.minToMs(240)) {
    	  this.timeGroupsBool[5] = false
    	  return 120
      }
	  
	  if(this.timeGroupsBool[6] === true && difference >= this.minToMs(240)) {
    	  this.timeGroupsBool[6] = false
    	  return 240
      }
	  
      return undefined
  }
  
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
    //console.log("Show header " + showHeader)
    return <FeedItem key={item.id} item={item} showDescription={this.state.showDescription} showHeader={showHeader} />
  }
  
  render() {
    return (
        <div>
            <div className="header">
                <div className="header-title">
                	<FontAwesomeIcon icon={faBars} onClick={this.toggleMenu} id="menuBtn" />
                	<span>{this.getHeaderTitle()}</span>
                </div>
                {this.state.unreadCount > 0 ?
                    <div className="header-refresh" onClick={this.refresh}>
                        <Badge className="refresh-badge" badgeContent={this.state.unreadCount} color="secondary">
                            <FontAwesomeIcon className="refresh-icon" icon={faSyncAlt}  />
                        </Badge>
                        <span className="refresh-text">Lataa uutiset</span>
                    </div>
                    : <div className="refresh-empty"></div>
                }
                <div className="menu">
                    <FontAwesomeIcon className="menu-icon" icon={faCog} />
                    <div className="menu-content">
                        <FormControlLabel
                            control={
                                <Switch checked={this.state.showDescription}
                                    onChange={this.handleShowDescriptionToggle('showDescription')}
                                    value="showDescription" />
                            }
                            label="Esikatselu"
                        />
                    </div>
                </div>
            </div>
            <div className="content">
                {this.state.feeds.length ? this.state.feeds.map(this.renderFeedItems)
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

