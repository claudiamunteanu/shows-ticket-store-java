import React from 'react';
import './ShowApp.css'

class ShowRow extends React.Component {
    handleClick = (event) => {
        console.log('delete button pentru ' + this.props.show.id);
        this.props.deleteFunc(this.props.show.id);
    }

    render() {
        return (
            <tr>
                <td>{this.props.show.id}</td>
                <td>{this.props.show.artistName}</td>
                <td>{this.props.show.dateTime}</td>
                <td>{this.props.show.place}</td>
                <td>{this.props.show.availableSeats}</td>
                <td>{this.props.show.soldSeats}</td>
                <td>
                    <button onClick={this.handleClick}>Delete</button>
                </td>
            </tr>
        )
    }
}

class ShowTable extends React.Component {
    render() {
        var rows = []
        var functieStergere = this.props.deleteFunc;
        this.props.shows.forEach(function (show) {
            rows.push(
                <ShowRow show={show} key={show.id} deleteFunc={functieStergere}/>)
        });
        return (<div className="ShowTable">
                <table className="center">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Artist Name</th>
                        <th>Date & Time</th>
                        <th>Place</th>
                        <th>Available Seats</th>
                        <th>Sold Seats</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>{rows}</tbody>
                </table>
            </div>
        );
    }
}

export default ShowTable;