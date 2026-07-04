import React from 'react';
import './Button.css';

class Button extends React.Component {
  render() {
	  var classes = "btn"
	  if(this.props.selectedCategory === this.props.link) {
		  classes += " selected"
	  }
	  
    return (
        <a href={this.props.link} >
            <div className={classes} onClick={this.handleClick}>
                <span>{this.props.text}</span>
            </div>
        </a>
    );
  }

  handleClick = (event) => {
    this.props.onCategoryChanged(this.props.text)
  }
}

export default Button;

