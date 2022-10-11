<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a name="readme-top"></a>
<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->



<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->

<!--[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url] 
[![Issues][issues-shield]][issues-url] -->
[![License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]


<!--
 PROJECT LOGO
<br />
<div align="center">
  <a href="https://github.com/claudiamunteanu/product-management">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a>
-->
# Ticket Store
Simple desktop application for a ticket store, created for the "Systems for Design and Implementation" course at my university from my second year.
  <!--
  <p align="center">
    Simple CRUD mobile application for the management of a store's products, created for the "Mobile Application Programming" course at my university.
    <br />
    <a href="https://github.com/claudiamunteanu/product-management"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/claudiamunteanu/product-management">View Demo</a>
    ·
    <a href="https://github.com/claudiamunteanu/product-management/issues">Report Bug</a>
    ·
    <a href="https://github.com/claudiamunteanu/product-management/issues">Request Feature</a>
  </p>
</div>
-->


<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#setup-and-build">Setup and Build</a></li>
        <li><a href="#running">Running</a></li>
      </ul>
    </li>
    <!--
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    -->
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <!--<li><a href="#acknowledgments">Acknowledgments</a></li>-->
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## About The Project

The user creates an account if he doesn't have one and then logs into it. There, he can view a list of all of the shows, he can filter them by date and he can buy tickets for one of those filtered shows.

The project contains two versions for the server: an RPC server and one made with Protobuff. It also contains a web server which uses the same database, and a simple client made with React for CRUD operations on shows.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Built With
* [![SQLite][SQLite.org]][SQLite-url]
* [![IntelliJ IDEA][Intellij.com]][Intellij-url]
* [![Java][Java.com]][Java-url]
* [![Gradle][Gradle.org]][Gradle-url]

#### Web CRUD operations application
* [![React][React.org]][React-url]
* [![Spring][Spring.io]][Spring-url]
* [![JavaScript][JavaScript.com]][JavaScript-url]
* [![NPM][NPM.com]][NPM-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

* [IntelliJ][IntelliJ-url]
* [SQLite][SQLite-url]
* [React][React-url]
* [NPM][NPM-url]
* [SQLite Studio](https://sqlitestudio.pl/)

### Setup and build

* Clone the repo
   ```sh
   git clone https://github.com/claudiamunteanu/shows-tickets-store-java.git
   ```
   
### Running

To run the desktop version:
1. Deploy and run the `AppServer` module using either the `StartProtobuffServer` class or the `StartRpcServer` class
2. Deploy and run the `AppClient` module. You must use the same protocol as the server: if you ran the protobuff server, use the `StartProtobuffClient` class. If you ran the RPC server, use the `StartRpcClient` class.

To run the web version:
1. Deploy and run the `RestServices` module using the `StartRestServices` class.
   * If you wish, you can test that the server is running correctly by running the `StartRestClient` class.
2. In the terminal, go to inside the `app-rest-client` folder. Then, deploy and run following command:
   ```
    npm start
   ```

If you wish, you can modify the database at any time using SQLite Studio, where you can manage the shows (which you can also manage using the web application), the users or the tickets.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->
<!--## Usage

Use this space to show useful examples of how a project can be used. Additional screenshots, code examples and demos work well in this space. You may also link to more resources.

_For more examples, please refer to the [Documentation](https://example.com)_

<p align="right">(<a href="#readme-top">back to top</a>)</p>
-->



<!-- CONTRIBUTING -->
<!--
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>
-->


<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTACT -->
## Contact

Munteanu Claudia-Maria - Linkedin: [claudiamunteanu][linkedin-url]

Project Link: [https://github.com/claudiamunteanu/shows-tickets-store-java](https://github.com/claudiamunteanu/shows-tickets-store-java)

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- ACKNOWLEDGMENTS -->
<!--## Acknowledgments

* []()
* []()
* []()

<p align="right">(<a href="#readme-top">back to top</a>)</p>

-->

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/claudiamunteanu/shows-tickets-store-java.svg?style=for-the-badge
[contributors-url]: https://github.com/claudiamunteanu/shows-tickets-store-javat/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/claudiamunteanu/shows-tickets-store-java.svg?style=for-the-badge
[forks-url]: https://github.com/claudiamunteanu/shows-tickets-store-java/network/members
[stars-shield]: https://img.shields.io/github/stars/claudiamunteanu/shows-tickets-store-java.svg?style=for-the-badge
[stars-url]: https://github.com/claudiamunteanu/shows-tickets-store-java/stargazers
[issues-shield]: https://img.shields.io/github/issues/claudiamunteanu/shows-tickets-store-java.svg?style=for-the-badge
[issues-url]: https://github.com/claudiamunteanu/shows-tickets-store-java/issues
[license-shield]: https://img.shields.io/github/license/claudiamunteanu/shows-tickets-store-java.svg?style=for-the-badge
[license-url]: https://github.com/claudiamunteanu/shows-tickets-store-java/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/claudiamunteanu
[JavaScript.com]: https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E
[JavaScript-url]: https://www.javascript.com/
[SQLite.org]: https://img.shields.io/badge/sqlite-%2307405e.svg?style=for-the-badge&logo=sqlite&logoColor=white
[SQLite-url]: https://www.sqlite.org/index.html
[React.org]: https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB
[React-url]: https://reactjs.org/
[Spring.io]: https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white
[Spring-url]: https://spring.io/
[Intellij.com]: https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white
[Intellij-url]: https://www.jetbrains.com/idea/
[Java.com]: https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white
[Java-url]: https://www.java.com/en/
[Gradle.org]: https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white
[Gradle-url]: https://gradle.org/
[NPM.com]: https://img.shields.io/badge/NPM-%23000000.svg?style=for-the-badge&logo=npm&logoColor=white
[NPM-url]: https://www.npmjs.com/
