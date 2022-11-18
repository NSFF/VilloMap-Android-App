# VilloMap

This project represents an Android Phone application where you can view OPEN Villo bicycle locations on a map interface. It fetches the Villo location data from https://data.mobility.brussels/nl/info/villo/ .

# Software Used

* Android Studio Dolphin 2021.3.1

* API SDK: 21 Android (Lollipop)

# Setting up the project

If you initialized the project on your local setup, you will need to create your google maps android SDK API key and add a variable MAPS_API_Key=<your key here> in your local.properties folder to make the google maps work.

# Features

* Displaying all OPEN Villo bicycle locations
* Pulling Villo data from https://data.mobility.brussels/geoserver/bm_bike/wfs?service=wfs&version=1.1.0&request=GetFeature&typeName=bm_bike:villo&outputFormat=json&srsName=EPSG:4326 on First startup or if existing data is older than 15 min
* Displaying date of last update of the data
* Refresh button to forcefully refresh all data pulled from https://data.mobility.brussels/geoserver/bm_bike/wfs?service=wfs&version=1.1.0&request=GetFeature&typeName=bm_bike:villo&outputFormat=json&srsName=EPSG:4326
* Persisting Data 
* Fetching Data from Core Data if already exists
* Updating data on Startup if it's more than 15 minutes old
* Tapping Annotations pops up a little callout showing more information like the Street
* User location will be shown if given permission

# Known Bugs

No Bugs as of yet

# References
I used several tutorials to help me during this project:

* Android online beginner course: https://developer.android.com/courses/android-basics-kotlin/course
* Fixed a bug where design preview would not show by changing tools:layout: https://stackoverflow.com/questions/23898992/android-studio-google-map-v2-fragment-rendering
* Install failure of emulator: https://stackoverflow.com/questions/4709137/solution-to-install-failed-insufficient-storage-error-on-android
* 