import React from 'react';
import {faCaretDown} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import './Button.css';

const divStyle = {
    borderLeft: '1px solid #0d8bf2',
};

class SplitButton extends React.Component {
  render() {
    return (
        <div className="splitContainer">
            <div className="btn splitBtn">
                <a href="#/">Button</a>
            </div>
            <div className="dropdown">
                <div className="btn">
                    <a href="#/" style={divStyle}>
                        <FontAwesomeIcon icon={faCaretDown} />
                    </a>
                </div>
            </div>
        </div>
    );
  }
}

export default SplitButton;