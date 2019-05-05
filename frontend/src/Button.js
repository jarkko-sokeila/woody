import React from 'react';
import './Button.css';

class Button extends React.Component {
  render() {
    return (
        <div className="btn" onClick={this.handleClick}>
            <a href="#/" >{this.props.text}</a>
        </div>
    );
  }

  handleClick = (event) => {
    console.log('I was clicked')
  }
}

export default Button;