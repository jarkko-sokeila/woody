import React from 'react';
import './Content.css';

class Content extends React.Component {
    constructor(props) {
    super(props);

    this.state = {
      data: null,
      feeds: [],
    };
  }

  getData(){
    fetch('/rest/test')
      .then(response => response.text())
      .then(data => this.setState({ data }));
  }

  getFeeds(){
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
  }

  render() {
    return (
        <div>
            <div className="header">
                <span>News</span>
            </div>
            <div className="content">
                <h1>Hello, {this.state.data}</h1>
                <ul>
                    {this.state.feeds.length ?
                        this.state.feeds.map(item=><li key={item.id}><a href={item.link} target="_blank">{item.title}</a></li>) 
                        : <li>Loading...</li>
                    }
                </ul>
            </div>
        </div>
    );
  }
}

export default Content;