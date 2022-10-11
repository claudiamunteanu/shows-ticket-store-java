import React from 'react';
import ShowTable from './Show';
import './ShowApp.css';
import ShowForm from './ShowForm';
import {GetShows, DeleteShow, AddShow, UpdateShow, GetShow} from "./utils/rest-calls";

class ShowApp extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            shows: [{
                "id": 0,
                "artistName": "Delia",
                "dateTime": "2020-01-05T19:00:00",
                "place": "Bucharest",
                "availableSeats": 500,
                "soldSeats": 1000
            }],
            deleteFunc: this.deleteFunc.bind(this),
            addFunc: this.addFunc.bind(this),
            updateFunc: this.updateFunc.bind(this),
            findFunc: this.findFunc.bind(this),
        }
        console.log('ShowApp constructor')
    }

    addFunc(show) {
        console.log('inside addFunc ' + show);
        AddShow(show)
            .then(res => GetShows())
            .then(shows => this.setState({shows}))
            .catch(error => console.log('eroare add ', error));
    }

    updateFunc(show) {
        console.log('inside updateFunc ' + show);
        UpdateShow(show)
            .then(res => GetShows())
            .then(shows => this.setState({shows}))
            .catch(error => console.log('erroare update ', error));
    }

    deleteFunc(show) {
        console.log('inside deleteFunc ' + show);
        DeleteShow(show)
            .then(res => GetShows())
            .then(shows => this.setState({shows}))
            .catch(error => console.log('eroare add ', error));
    }

    async findFunc(show) {
        console.log('inside findFunc ' + show);
        let foundShow = await GetShow(show)
        console.log('found this SHOWWW: ', foundShow)
        return foundShow
    }

    componentDidMount() {
        console.log('inside componentDidMount')
        GetShows().then(shows => this.setState({shows}));
    }

    render() {
        return (
            <div className="ShowApp">
                <h1>App Show Management</h1>
                <ShowForm addFunc={this.state.addFunc} updateFunc={this.state.updateFunc}
                          findFunc={this.state.findFunc}/>
                <br/>
                <br/>
                <ShowTable shows={this.state.shows} deleteFunc={this.state.deleteFunc}/>
            </div>
        );
    }
}

export default ShowApp;
