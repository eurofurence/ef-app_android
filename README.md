# ef-app_android
Eurofurence App (Android version)

This is the official repo for the Eurofurence Android application. Commits to this repo are considered unstable. Releases in here, as well as releases on the [Google Play Store](https://play.google.com/store/apps/details?id=org.eurofurence.connavigator)

This app integrates seamlessly with the ef-app_backend application, when run on a server.

## To build

- Clone this repo
- gradlew build

## To change the backend URL (running your own backed)

- go to `ApiService`
- Change the `basePath` parameter

## To change firebase/google api keys

- Download `google-services.json` from Firebase/Google Cloud

# Using this app for your own convention

We recommend to for this repo if you're intending on using it for your own convention. You can apply theming and related things, while still being up to date by pulling from the main repo.

# Helping out

- Create issues when you have one, even if it might be a bit silly (discussion is nice)

# Used 3rd party platforms

- Icons8 (In app icons, thanks a ton!)
- Google analytics (Basic event and screen tracking. No user data)
- Firebase (Cloud messages, Remote configs)
- Google API's (Maps)
- Google Play (App uploading, cloud testing)
- HockeyApp (Event tracking, Crash tracking, Feedback. No user specific data)
