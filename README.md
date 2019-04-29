# SFI IoT
SFI IoT project is an indoor positioning and asset management system. It is used for displaying the real-time location of a user inside the SFI corporate office on a web map and collecting the real-time sensors' data for management purposes. System structure shows as below:


![](https://github.com/kevinZhangSFI/sfi_iot/blob/master/Resources/System%20Diagram.png)

Resources used in this project:
Hardware: Estimote Beacons
SDK/API: Estimote SDK/API, Leaflet, Spring Boot

Andorid-Client: Android based client app, used for collecting sensors' data and user's location, send data to server via HTTP REST service

Recommended IDE: Android Studio 

Web-Client: Web based client app, used for displaying user's location on map in real-time

Server: Spring Boot application, provides REST service for HTTP request, stores sensors' data and user's location to MySQL database

Recommended IDE: Spring Tool Suite
