# **WEATHER APP**

## Table of Contents

1. [App Overview](#App-Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
1. [Build Notes](#Build-Notes)

## App Overview
 *  Provides real-time weather forecasts, alerts, and climate trends, ensuring users can plan their day with accurate meteorological information.

### Description 

A functional weather app provides users with current, forecasted, and historical weather information for their location or any chosen location around the world. It typically offers:
Displaying temperature, humidity, wind speed, and direction, visibility, barometric pressure, and perceived temperature.
Providing short-term (hourly), medium-term (daily), and long-term (weekly) weather forecasts, including temperature highs and lows, chances of precipitation, and weather conditions like sunny, cloudy, or rainy.
 Push notifications for severe weather conditions like storms, hurricanes, or extreme temperatures, often sourced from national or regional meteorological services.
 Real-time weather radar images showing precipitation, cloud cover, and movement to help users visualize the weather patterns.
 Allowing users to save multiple locations, choose units of measurement (e.g., Celsius or Fahrenheit), and configure the type and frequency of notifications.

### App Evaluation

<!-- Evaluation of your app across the following attributes -->

- **Category:** Lifestyle, Travel 
- **Mobile:** The app utilizes mobile-specific features such as real-time weather updates based on the user’s location, augmented reality (AR) to visualize weather patterns, and the integration of smart notifications that alert users about impending weather conditions based on their daily routines and saved locations.
- **Story:** The app is presented not just as a weather forecasting tool but as a daily companion that helps users make informed decisions about their health, travel, and lifestyle based on the weather. The story revolves around the idea of 'Weather-Informed Decisions' that seamlessly fit into the user’s life.
- **Market:** The app is presented not just as a weather forecasting tool but as a daily companion that helps users make informed decisions about their health, travel, and lifestyle based on the weather. The story revolves around the idea of 'Weather-Informed Decisions' that seamlessly fit into the user’s life.
- **Habit:** Given the ever-changing nature of weather, users would likely open the app several times a day to check updates, especially with personalized notifications prompting them.
- **Scope:** Given the ever-changing nature of weather, users would likely open the app several times a day to check updates, especially with personalized notifications prompting them.

## Product Spec

### 1. User Features (Required and Optional)

Required Features:

- **Uses the internet and one or more APIs**
- **Uses a RecyclerView**
- **Uses a consistent custom styling**
- **Contains multiple user action points(E.g clicks, long click, text entry, navigation)

Stretch Features:

- **fill in here**

### 2. Chosen API(s)

- **list first API endpoint here**
  - api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={7dc1b7f05e79b0e2e9bb55d8e7a84e83}

### 3. User Interaction

Required Feature

- **First user action here**
  - Current Weather Conditions
        Display: Temperature, weather description, humidity, wind speed, weather icon.
        Interaction: Automatically fetched on app launch or via manual location entry through a search bar and 'search' button.
  - 5-Day Weather Forecast
        Display: Daily high/low temperatures, general weather conditions.
        Interaction: Swiping left/right on the current weather display or tapping on a '5-Day Forecast' button.
  - Hourly Weather Forecast
        Display: Temperature and weather conditions for each hour.
        Interaction: Selecting a day from the 5-day forecast by tapping or via a drop-down menu.


- **Second user action here**
  - Saving Favorite Locations
    Display: Weather information for saved locations.
    Interaction: Adding through search and tapping a 'save' icon, accessed via a menu or list.
  - Sunrise and Sunset Times
    Display: Times for sunrise and sunset.
    Interaction: Part of the daily weather forecast or accessed through a 'Sunrise/Sunset' button.
  - Weather Alerts and Warnings
    Display: Notifications for severe weather conditions.
    Interaction: Enabled by default with an option to customize or mute in the app's settings. Alerts section accessed via a dedicated button.

## Wireframes

<!-- Add picture of your hand sketched wireframes in this section -->
<img src="https://i.imgur.com/zje5v6U.jpg" width=600>

<!--### [BONUS] Digital Wireframes & Mockups -->

<!--### [BONUS] Interactive Prototype -->

## Build Notes

Here's a place for any other notes on the app, it's creation 
process, or what you learned this unit!  

For Milestone 2, include **2+ Videos/GIFs** of the build process here!

## License

Copyright **2023** **Roberto di Bari,Emanuel Warzel, Sebastian Hernandez, Eric Campillo**

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
