import React from 'react';
import './FeedItem.css';

class FeedItem extends React.Component {
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
        </div>
    );
  }
}

export default FeedItem;