import React from 'react';
import Button from './Button';
import './Navigation.css';

class Navigation extends React.Component {
	constructor(props) {
	    super(props);
	    this.state = {
	      selectedCategory: window.location.pathname,
	    };
	    this.nav = React.createRef();
	    this.categorySelected = this.categorySelected.bind(this);
	    this.toggle = this.toggle.bind(this);
	}
	
	componentDidMount(){
	}
	
	toggle() {
		var elem = this.nav.current;
		console.log("elem: " + elem); 
		elem.classList.toggle('visible')
	}
	
	categorySelected(category) {
		this.setState({ selectedCategory: category });
	}
	
  render() {
    return (
        <div ref={this.nav} className="navigation">
            <div className="nav-header"><a href="/"><p>WooDy</p></a></div>
            <Button text="Etusivu" link="/" selectedCategory={this.state.selectedCategory} onCategoryChanged={this.categorySelected} />
            <Button text="Uutiset" link="/news" selectedCategory={this.state.selectedCategory} onCategoryChanged={this.categorySelected} />
            <Button text="Urheilu" link="/sports" selectedCategory={this.state.selectedCategory} onCategoryChanged={this.categorySelected} />
            <Button text="IT" link="/it" selectedCategory={this.state.selectedCategory} onCategoryChanged={this.categorySelected} />
            <Button text="Viihde" link="/entertainment" selectedCategory={this.state.selectedCategory} onCategoryChanged={this.categorySelected} />
            <Button text="Autot" link="/cars" selectedCategory={this.state.selectedCategory} onCategoryChanged={this.categorySelected} />
            <Button text="Moottoripyörät" link="/motorbikes" selectedCategory={this.state.selectedCategory} onCategoryChanged={this.categorySelected} />
            <Button text="Tiede" link="/science" selectedCategory={this.state.selectedCategory} onCategoryChanged={this.categorySelected} />
            <Button text="Lifestyle" link="/lifestyle" selectedCategory={this.state.selectedCategory} onCategoryChanged={this.categorySelected} />
        </div>
    );
  }
}

export default Navigation;

