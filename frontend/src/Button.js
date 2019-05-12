import React from 'react';
import './Button.css';

class Button extends React.Component {
  render() {
    return (
        <a href={this.props.link} >
            <div className="btn" onClick={this.handleClick}>
                <span>{this.props.text}</span>
            </div>
        </a>
        
    );
  }

  handleClick = (event) => {
    console.log('I was clicked')
  }
}

export default Button;