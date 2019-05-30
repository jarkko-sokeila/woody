import React from 'react';
import {faEye} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import './FeedItem.css';

class FeedItem extends React.Component {
    constructor(props) {
        super(props);
        
        this.state = {
        	item: this.props.item,
        };
        
        this.showHeader = this.showHeader.bind(this);
        this.getHeaderText = this.getHeaderText.bind(this);
        this.refresh = this.refresh.bind(this);
        this.clickLink = this.clickLink.bind(this);
  }
	
  getDate(date) {
	  var d = new Date(date);
	  /*console.log(d);*/
	  var hours = d.getHours()
	  if(hours < 10) {
		  hours = "0" + hours;
	  }
	  var minutes = d.getMinutes()
	  if(minutes < 10) {
		  minutes = "0" + minutes;
	  }
	  
	  var currentDate = new Date()
	  if(d.getDate() < currentDate.getDate()) {
		  return d.getDate() + "." + (d.getMonth() + 1) + " " + hours + ":" + minutes
	  }
	  
	  return hours + ":" + minutes
  }

  getSource(source, subCategory) {
    /*console.log("source: " + source + ", subCategory: " + subCategory)*/
    
    if(source === 'ILTALEHTI') {
        source = "Iltalehti"
    } else if (source === 'MUROPAKETTI') {
        source = "Muropaketti"
    } else if (source === 'SUOMIF1') {
        source = "SuomiF1"
    } else if (source === 'SUOMIFUTIS') {
        source = "SuomiFutis"
    } else if (source === 'SUOMIKIEKKO') {
        source = "SuomiKiekko"
    } else if (source === 'SUOMIKORIS') {
        source = "SuomiKoris"
    } else if (source === 'SUOMIURHEILU') {
        source = "SuomiUrheilu"
    } else if (source === 'TEKNIIKAN_MAAILMA') {
        source = "Tekniikan maailma"
    } else if (source === 'KAUPPALEHTI') {
        source = "Kauppalehti"
    } else if (source === 'IO_TECH') {
        source = "io-tech"
    } else if (source === 'SUOMENMAA') {
        source = "Suomenmaa"
    } else if (source === 'KALEVA') {
        source = "Kaleva"
    } else if (source === 'POLIISI') {
        source = "Poliisi"
    } else if (source === 'TIVI') {
        source = "Tivi"
    } else if (source === 'ME_NAISET') {
    	source = "Me naiset"
    } else if (source === 'BIKE') {
    	source = "Bike"
    } else if (source === 'RADIO_NOVA') {
    	source = "Radio Nova"
    } else if (source === 'PELAAJA') {
    	source = "Pelaaja"
    } else if (source === 'RAKENNUSLEHTI') {
    	source = "Rakennuslehti"
    } else if (source === 'PUHELINVERTAILU') {
    	source = "Puhelinvertailu"
    } else if (source === 'MOBIILI') {
    	source = "Mobiili"
    } else if (source === 'SOUNDI') {
    	source = "Soundi"
    } else if (source === 'MIKROBITTI') {
    	source = "Mikribitti"
    } else if (source === 'STARA') {
    	source = "Stara"
    } else if (source === 'FINDANCE') {
    	source = "Findance"
    } else if (source === 'HYMY') {
    	source = "Hymy"
    }
    
    if(subCategory != null) {
        source += " | " + this.getSubCategory(subCategory)
    }

    return source
  }

  getSubCategory(subCategory) {
      if(subCategory === 'HOMELAND') {
        return 'Kotimaa'
      } else if(subCategory === 'ABROAD') {
        return 'Ulkomaat'
      } else if(subCategory === 'SCIENCE') {
        return 'Tiede'
      } else if(subCategory === 'ECONOMY') {
        return 'Talous'
      } else if(subCategory === 'POLITICS') {
        return 'Politiikka'
      } 
      
      else if(subCategory === 'ICE_HOCKEY') {
        return 'Jääkiekko'
      } else if(subCategory === 'FOOTBALL') {
        return 'Jalkapallo'
      } else if(subCategory === 'FORMULA1') {
        return 'Formula 1'
      } else if(subCategory === 'WINTER_SPORTS') {
        return 'Talviurheilu'
      } else if(subCategory === 'ATHLEITCS') {
        return 'Yleisurheilu'
      } else if(subCategory === 'RALLY') {
        return 'Ralli'
      } else if(subCategory === 'FLOORBALL') {
        return 'Salibandy'
      } else if(subCategory === 'BASKETBALL') {
        return 'Koripallo'
      } else if(subCategory === 'VOLLEY_BALL') {
        return 'Lentopallo'
      } else if(subCategory === 'HARNESS_RACING') {
        return 'Ravit'
      } else if(subCategory === 'MARTIAL_ARTS') {
        return 'Kamppailulajit'
      } else if(subCategory === 'E_SPORTS') {
        return 'E-Urheilu'
      } else if(subCategory === 'GOLF') {
        return 'Golf'
      } else if(subCategory === 'MOTORBIKES') {
        return 'Moottoripyöräily'
      } else if(subCategory === 'TENNIS') {
        return 'Tennis'
      } else if(subCategory === 'OTHER_SPORTS') {
        return 'Muu urheilu'
      }

      else if(subCategory === 'MUSIC') {
        return 'Musiikki'
      } else if(subCategory === 'MOVIES') {
        return 'Elokuvat'
      }


      return subCategory
  }
  
  showHeader() {
	if(this.props.showHeader === undefined)
		return false
		  
	return true;
  }
  
  getHeaderText() {
	if(this.props.showHeader === 0)
		return 'NYT'
	else if(this.props.showHeader === 60)
		return '1 TUNTI'
	else if(this.props.showHeader === 120)
		return '2 TUNTIA'
	else if(this.props.showHeader === 240)
		return '4 TUNTIA'
	else
		return this.props.showHeader + ' MIN'	  
  }
  
  clickLink() {
	  console.log("Link with id " + this.state.item.id + " clicked")
	  var content = this;
	  fetch('/rest/linkclick', {
		   method: 'post',
		   headers: {'Content-Type':'application/x-www-form-urlencoded'},
		   body: "id="+this.state.item.id
		  })
		  .then(function(response) {
	          //console.log(response);
			  content.refresh()
	      });
  }
  
  refresh() {
	  console.log("Refresh item id " + this.state.item.id)
      var content = this;
      var url = "/rest/getfeed?id=" + this.state.item.id;

	  fetch(url)
      .then(function(response) {
          console.log(response);
          return response.json();
      })
      .then(function(feed) {
          console.log(feed);
          content.setState({ item: feed });
      });
  }
	
  render() {
    return (
        <div className="feedItem">
            {this.showHeader() ? (
                <div className="p-grid p-nogutter feedItem-header">
                    <div className="p-col-12 p-md-9 p-lg-9 p-nogutter">
                        <div className="cell text">
                            <span>{this.getHeaderText()}</span>
                        </div>
                    </div>
                    <div className="p-col-3 p-md-1 p-lg-1 p-nogutter">
                        <div className="cell source">
                            <span>Lähde</span>
                        </div>
                    </div>
                    <div className="p-col-2 p-md-1 p-lg-1 p-nogutter">
                        <div className="cell published">
                            <span>Julkaistu</span>
                        </div>
                    </div>
                    <div className="p-col p-md-1 p-lg-1 p-nogutter">
	                    <div className="cell published">
	                        <span>Klikit</span>
	                    </div>
	                </div>
                </div>
            ) : null}
	        <div className="p-grid p-nogutter feedItem-content">
	        	<div className="p-col-12 p-md-9 p-lg-9">
		        	<div className="cell text">
		        		<a href={this.state.item.link} target="_blank" rel="noopener noreferrer" onClick={this.clickLink}>{this.state.item.title}</a>
		                {this.props.showDescription &&
		                    <p>{this.state.item.description}</p>
		                }
		        	</div>
	        	</div>
	        	<div className="p-col-3 p-md-1 p-lg-1">
		        	<div className="cell source">
		        		<p>{this.getSource(this.state.item.rssSource, this.state.item.subCategory)}</p>
		        	</div>
		        </div>
	        	<div className="p-col-2 p-md-1 p-lg-1">
		        	<div className="cell published">
			    		<p>{this.getDate(this.state.item.published)}</p>
			    	</div>
			    </div>
			    <div className="p-col p-md-1 p-lg-1">
		        	<div className="cell published">
			    		<p><FontAwesomeIcon icon={faEye} className="eye" />{this.state.item.clickCount}</p>
			    	</div>
			    </div>
	    	</div>
        </div>
    );
  }
}

export default FeedItem;