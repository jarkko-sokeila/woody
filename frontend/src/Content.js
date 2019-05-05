import React from 'react';
import {faArrowCircleUp} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import FeedItem from './FeedItem';
import './Content.css';



class Content extends React.Component {
    constructor(props) {
    super(props);

    this.state = {
      data: null,
      feeds: [],
    };
  }

  getData() {
    fetch('/rest/test')
      .then(response => response.text())
      .then(data => this.setState({ data }));
  }

  getFeeds() {
      var content = this;
    fetch('/rest/news')
        .then(function(response) {
            console.log(response);
            return response.json();
        })
        .then(function(feeds) {
            console.log(feeds.content);
            content.setState({ feeds: feeds.content });
        });
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
	  //window.onscroll = scrollFunction();
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
                <span>News</span>
            </div>
            <div className="content">
                <h4>Hello, {this.state.data}</h4>
                {this.state.feeds.length ?
                    this.state.feeds.map(item=><FeedItem key={item.id} item={item}/>) 
                    : <span>Loading...</span>
                }
            </div>
            <div className="footer">
            	<div className="center"><p>Copyright 2019 news.io</p></div>
            </div>
            
            <FontAwesomeIcon icon={faArrowCircleUp} onClick={this.topFunction} id="topBtn" />
            
        </div>
    );
  }
}

export default Content;