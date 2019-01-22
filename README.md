# ef-app_android
The official Eurofurence App for Android

This repository holds the code for the Eurofurence Android app with the following features:

- When using the EF API, it is a complete package to start the app
- Update the UI instantly after data has changed with rxjava
- Login to integrate with other systems for private messages
- Notifications when favorited events are happening
- Info section for quick information
- Dealer information pages
- Maps of the convention area

# Adapting for another convention

The following steps will modify the app to work for your convention. First off, we have some requests when adapting:

- Do not remove the credits under the about page
- Do not change the license
- Add your own credits in the layout with the images, above or below the original developers

Now we can get started with the app itself!

## Changes to be made

* Change the package name in `app.gradle`
* Apply the changes under the Firebase header
* Change the settings in `res/xml/remote.xml` (This sets the API, countdowns and other remote settings)
* OPTIONAL: mirror these settings in firebase so you can change them on the fly
* Change the app colors in `res/values/colors.xml`
* Change the logo in the `res/mipmap-*` folders (these need to be scaled accordingly)
* Change the placeholder image in `res/drawable/placeholder_event.jpg`
* Change the banner image in `res/drawable/banner_2018.jpg`
* Change the text in `ui/StartActivity.kt` to reflect your convention name
* OPTIONAL: Translate all the strings for your language

### Firebase

Firebase is used for Cloud Messaging, Remote Preferences, Performance, Analytics and Crash tracking. To talk to your own backend you will need to create a new project here and fetch new settings.

If you have already Firebase, you will just need to update the google-services.json

* Go to [firebase](https://console.firebase.google.com/)
* Add a new project
* Add an app in this project
* Enter the package name from `app.gradle`
* Download the `google-services.json` and put the file in `app/`

Built artifacts will now talk to your Firebase instance instead of Eurofurence!

### Upgrading the API

In case you make model changes to the backend, you will need to update the model definitions. 

Make sure that the `swagger.json` is set in `app.gradle`, then run `gradle generateSwagger`
 
## How to build

To build the application yu will need to have Java and the Android SDK installed. Via the SDK you will need to install 

- Google Play Services
- Android SDK Tools
- Support Repository
- Android Build Tools (version is in app.gradle)

When all is installed, IntelliJ / Android Studio will likely complain about missing components. Install these if neccesary.

Afterwards, simply build it with gradle / IDE

# Used 3rd party platforms

- Firebase (Cloud messages, Remote configs)
- Google Play (App uploading, cloud testing)
