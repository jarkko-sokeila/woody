import React from 'react';
import './FeedItem.css';

class FeedItem extends React.Component {
	
  getDate(date) {
	  var d = new Date(date.published);
	  console.log(d);
	  var hours = d.getHours()
	  if(hours < 10) {
		  hours = "0" + hours;
	  }
	  var minutes = d.getMinutes()
	  if(minutes < 10) {
		  minutes = "0" + minutes;
	  }
	  
	  return hours + ":" + minutes
  }
	
  render() {
    return (
        <div className="feedItem">
        	<div className="cell text">
        		<a href={this.props.item.link} target="_blank" rel="noopener noreferrer" >{this.props.item.title}</a>
        		<p>{this.props.item.description}</p>
        	</div>
        	<div className="cell source">
        		<p>{this.props.item.rssSource}</p>
        	</div>
        	<div className="cell published">
	    		<p>{this.getDate(this.props.item)}</p>
	    	</div>
        </div>
    );
  }
}

export default FeedItem;