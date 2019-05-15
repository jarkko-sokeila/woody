import React from 'react';
import Button from './Button';
import './Navigation.css';

class Navigation extends React.Component {
	constructor(props) {
	    super(props);
	    this.state = {
	      selectedCategory: window.location.pathname,
	    };
	    
	    this.categorySelected = this.categorySelected.bind(this);
	}
	
	categorySelected(category) {
		this.setState({ selectedCategory: category });
	}
	
  render() {
    return (
        <div className="navigation">
            <div className="nav-header"><a href="/"><p>WooDy</p></a></div>
            <Button text="Etusivu" link="/" selectedCategory={this.state.selectedCategory} onCategoryChanged={this.categorySelected} />
            <Button text="Uutiset" link="/news"selectedCategory={this.state.selectedCategory} onCategoryChanged={this.categorySelected} />
            <Button text="Urheilu" link="/sports"selectedCategory={this.state.selectedCategory} onCategoryChanged={this.categorySelected} />
            <Button text="IT" link="/it" selectedCategory={this.state.selectedCategory} onCategoryChanged={this.categorySelected} />
            <Button text="Viihde" link="/entertainment" selectedCategory={this.state.selectedCategory} onCategoryChanged={this.categorySelected} />
            {/*<SplitButton/>*/}

            
            {/*<form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_blank">
                <input type="hidden" name="cmd" value="_donations" />
                <input type="hidden" name="business" value="jarkko.sokeila@gmail.com" />
                <input type="hidden" name="currency_code" value="EUR" />
                <input type="image" src="https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif" border="0" name="submit" title="PayPal - The safer, easier way to pay online!" alt="Donate with PayPal button" />
                <img alt="" border="0" src="https://www.paypal.com/en_FI/i/scr/pixel.gif" width="1" height="1" />
            </form>*/}
        </div>
    );
  }
}

export default Navigation;