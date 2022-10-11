import React from 'react';

class ShowForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {id: '', artistName: '', dateTime: '', place: '', availableSeats: '', soldSeats: ''};
    }

    handleIdChange = (event) => {
        this.setState({id: event.target.value});
    }
    handleArtistNameChange = (event) => {
        this.setState({artistName: event.target.value});
    }
    handleDateTimeChange = (event) => {
        this.setState({dateTime: event.target.value});
    }
    handlePlaceChange = (event) => {
        this.setState({place: event.target.value});
    }
    handleAvailableSeatsChange = (event) => {
        this.setState({availableSeats: event.target.value});
    }
    handleSoldSeatsChange = (event) => {
        this.setState({soldSeats: event.target.value});
    }

    handleSubmit = (event) => {
        var show = {
            artistName: this.state.artistName,
            dateTime: this.state.dateTime.replace('T', ' '),
            place: this.state.place,
            availableSeats: this.state.availableSeats,
            soldSeats: this.state.soldSeats
        }
        this.handleReset(event);
        console.log('A show was submitted: ');
        console.log(show);
        this.props.addFunc(show);
        event.preventDefault();
    }

    handleUpdate = (event) => {
        var show = {
            id: this.state.id,
            artistName: this.state.artistName,
            dateTime: this.state.dateTime.replace('T', ' '),
            place: this.state.place,
            availableSeats: this.state.availableSeats,
            soldSeats: this.state.soldSeats
        }
        this.handleReset(event);
        console.log('A show was submitted: ');
        console.log(show);
        this.props.updateFunc(show);
        event.preventDefault();
    }

    handleLoad = (event) => {
        this.props.findFunc(this.state.id).then(data=>this.loadData(data))
        event.preventDefault();
    }

    loadData(data){
        this.setState({artistName:data.artistName});
        this.setState({dateTime:data.dateTime.replace(' ','T')});
        this.setState({place:data.place});
        this.setState({availableSeats:data.availableSeats});
        this.setState({soldSeats:data.soldSeats});
    }

    handleReset=(event)=>{
        this.setState({artistName:''});
        this.setState({dateTime:''});
        this.setState({place:''});
        this.setState({availableSeats:''});
        this.setState({soldSeats:''});
        event.preventDefault();
    }

    render() {
        return (
            <form>
                <label>
                    Artist name:
                    <input type="text" value={this.state.artistName} onChange={this.handleArtistNameChange}/>
                </label><br/><br/>
                <label>
                    Date & Time:
                    <input type="datetime-local" value={this.state.dateTime}
                           onChange={this.handleDateTimeChange}/>
                </label><br/><br/>
                <label>
                    Place:
                    <input type="text" value={this.state.place} onChange={this.handlePlaceChange}/>
                </label><br/><br/>
                <label>
                    Available Seats:
                    <input type="number" value={this.state.availableSeats}
                           onChange={this.handleAvailableSeatsChange}/>
                </label><br/><br/>
                <label>
                    Sold Seats:
                    <input type="number" value={this.state.soldSeats} onChange={this.handleSoldSeatsChange}/>
                </label><br/><br/>
                <input type="submit" value="Add Show" onClick={this.handleSubmit}/><br/><br/>
                <label>
                    ID:
                    <input type="number" value={this.state.id} onChange={this.handleIdChange}/>
                </label>
                <input type="submit" value="Update Show" onClick={this.handleUpdate}/>
                <input type="submit" value="Load Show" onClick={this.handleLoad}/>
                <input type="submit" value="Reset" onClick={this.handleReset}/>
            </form>
        );
    }
}

export default ShowForm;