import React from 'react';
import {faArrowCircleUp} from "@fortawesome/free-solid-svg-icons";
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
    this.state = {
      data: null,
      feeds: [],
      showDescription: this.cookies.get('showDescription') === 'true'
    };

    console.log("cookie showDescription " + this.cookies.get('showDescription'))
    this.getFeeds = this.getFeeds.bind(this);
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

  topFunction = (event) => {
	  console.log('Back to top')
	  document.body.scrollTop = 0;
	  document.documentElement.scrollTop = 0;
  }
  
  render() {
    return (
        <div>
            <div className="header">
                <div className="header-title"><span>{this.getHeaderTitle()}</span></div> {/* News, {this.state.data}*/}
                <div className="test">
                	<FormControlLabel
		                control={
		                    <Switch checked={this.cookies.get('showDescription') === "true" }
		                        onChange={this.handleChange('showDescription')}
		                        value="showDescription" />
		                 }
		                 label="Esikatselu"
                	 />
                </div>
            </div>
            <div className="content">
                {this.state.feeds.length ?
                    this.state.feeds.map(item=><FeedItem key={item.id} item={item} showDescription={this.cookies.get('showDescription') === "true"}/>) 
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