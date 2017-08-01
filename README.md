# CheckWeather
A simple weather app which can tell the weather update of your city

## Weather API Used
- [OpenWeatherMap API](https://openweathermap.org/)

## Features 
- Show updated weather information of preferred city from [OpenWeatherMap](http://openweathermap.org/)
- Show next 5 days weather forecast ( Weather Description, Average Temperature)
- Shows suggestion of cities while user is typing ( <b>powered by [Google Places API Web Service](https://developers.google.com/places/web-service/) </b>)
- Auto suggestion option can be turned off from settings
- Showed previously cached weather data while offline
- User can refresh to get current weather information
- User can change location from setting options
- User Location can be get from GPS using clicking location button
- User can set preferred Temperature Unit ( Currently supported <b>Celsius</b>, <b>Fahrenheit</b>)
- User can set preferred Wind Speed Unit ( Currently supported <b>m/s</b>, <b>mph</b>, <b>km/h</b>)

## Pre-requisites
- Android 4.1(Jelly Bean) or Higher

## User Limits for Auto Suggestion (Google Place API)
The Google Places API Web Service enforces a default limit of <b>1,000 free requests per 24 hour period</b>. So if the request count pass the limit the app may crash. If this happens, just stop <b>Show City Name Suggestion</b> from app settings.

## Demo
![Imgur](http://i.imgur.com/hNUBc5d.png)
![Imgur](http://i.imgur.com/nbdFCmX.png)
![Imgur](http://i.imgur.com/LawC3hU.png)
![Imgur](http://i.imgur.com/Nwa8mcz.png)

## Apk Link
Download apk file from [here](https://github.com/PialKanti/CheckWeather/raw/master/app-debug.apk)
