import React from 'react';
import Button from './Button';
import SplitButton from './SplitButton';
import './Navigation.css';

class Navigation extends React.Component {
  render() {
    return (
        <div className="navigation">
            <div className="nav-header"><p>News.io</p></div>
            <Button text="Etusivu"/>
            <Button text="Uutiset"/>
            <Button text="Urheilu"/>
            <Button text="IT"/>
            <Button text="Viihde"/>
            {/*<SplitButton/>*/}
        </div>
    );
  }
}

export default Navigation;