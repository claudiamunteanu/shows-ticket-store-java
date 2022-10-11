import {APP_SHOWS_BASE_URL} from "./consts";

function status(response) {
    console.log('response status ' + response.status);
    if (response.status >= 200 && response.status < 300) {
        return Promise.resolve(response);
    } else {
        return Promise.reject(new Error(response.statusText));
    }
}

function json(response) {
    return response.json();
}

export function GetShows() {
    var headers = new Headers();
    headers.append('Accept', 'application/json');
    var antet = {
        method: 'GET',
        headers: headers,
        mode: 'cors'
    };
    var request = new Request(APP_SHOWS_BASE_URL, antet);

    console.log('Inainte de fetch pentru ' + APP_SHOWS_BASE_URL);

    return fetch(request)
        .then(status)
        .then(json)
        .then(data => {
            console.log('Request succeeded with JSON response', data);
            return data;
        }).catch(error => {
        console.log('Request failed', error);
        return error;
    })
}

export function GetShow(id) {
    var headers = new Headers();
    headers.append('Accept', 'application/json');
    var antet = {
        method: 'GET',
        headers: headers,
        mode: 'cors'
    };

    var showGetUrl = APP_SHOWS_BASE_URL+'/'+id;
    var request = new Request(showGetUrl, antet);

    console.log('Inainte de fetch GET pentru ' + showGetUrl);

    return new Promise((resolve, reject)=> (fetch(request)
        .then(status)
        .then(json)
        .then(data => {
            console.log('Request succeeded with JSON response', data);
            return resolve(data);
        }).catch(error => {
            console.log('Request failed', error);
            return reject(error);
        })))
    // return fetch(request)
    //     .then(status)
    //     .then(json)
    //     .then(data => {
    //         console.log('Request succeeded with JSON response', data);
    //         return data;
    //     }).catch(error => {
    //         console.log('Request failed', error);
    //         return error;
    //     })
}

export function DeleteShow(id) {
    console.log('Ininate de fetch delete')
    var headers = new Headers();
    headers.append('Accept', 'application/json');
    var antet = {
        method: 'DELETE',
        headers: headers,
        mode: 'cors'
    }

    var userDeleteUrl = APP_SHOWS_BASE_URL+'/'+id;
    var request = new Request(userDeleteUrl, antet);
    return fetch(request)
        .then(status)
        .then(response=>{
            console.log('Delete status '+response.status)
            return response.text();
        }).catch(error=>{
            console.log('Delete error '+error);
            return Promise.reject(error);
        })
}

export function AddShow(show){
    console.log('Inainte de fetch post'+JSON.stringify(show));

    var headers = new Headers();
    headers.append('Accept','application/json');
    headers.append('Content-Type','application/json');

    var antet = {
        method:'POST',
        headers:headers,
        mode:'cors',
        body:JSON.stringify(show)
    };
    var request = new Request(APP_SHOWS_BASE_URL, antet);
    return fetch(request)
        .then(status)
        .then(response=>{
            return response.text();
        }).catch(error=>{
            console.log('Request POST failed', error);
            return Promise.reject(error);
        })
}

export function UpdateShow(show){
    console.log('Inainte de fetch put'+JSON.stringify(show));

    var headers = new Headers();
    headers.append('Accept','application/json');
    headers.append('Content-Type','application/json');

    var antet = {
        method:'PUT',
        headers:headers,
        mode:'cors',
        body:JSON.stringify(show)
    };
    var request = new Request(APP_SHOWS_BASE_URL, antet);
    return fetch(request)
        .then(status)
        .then(response=>{
            return response.text();
        }).catch(error=>{
            console.log('Request PUT failed', error);
            return Promise.reject(error);
        })
}