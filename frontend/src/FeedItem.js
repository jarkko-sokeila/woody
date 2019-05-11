import React from 'react';
import './FeedItem.css';

class FeedItem extends React.Component {
	
  getDate(date) {
	  var d = new Date(date);
	  console.log(d);
	  var hours = d.getHours()
	  if(hours < 10) {
		  hours = "0" + hours;
	  }
	  var minutes = d.getMinutes()
	  if(minutes < 10) {
		  minutes = "0" + minutes;
	  }
	  
	  return hours + ":" + minutes
  }

  getSource(source, subCategory) {
    console.log("source: " + source + ", subCategory: " + subCategory)
    
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
	
  render() {
    return (
        <div className="feedItem">
        	<div className="cell text">
        		<a href={this.props.item.link} target="_blank" rel="noopener noreferrer" >{this.props.item.title}</a>
                {this.props.showDescription &&
                    <p>{this.props.item.description}</p>
                }
        	</div>
        	<div className="cell source">
        		<p>{this.getSource(this.props.item.rssSource, this.props.item.subCategory)}</p>
        	</div>
        	<div className="cell published">
	    		<p>{this.getDate(this.props.item.published)}</p>
	    	</div>
        </div>
    );
  }
}

export default FeedItem;