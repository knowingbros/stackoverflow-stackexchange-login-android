# Stackoverflow login - Android App

> Documentation: https://api.stackexchange.com/docs/authentication 

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**

- [1 Create an app on stackapps](#1-create-an-app-on-stackapps)
- [2 On successful app creation, you will get details](#2-on-successful-app-creation-you-will-get-details)
- [3 In SessionUtil file, add relevant details](#3-in-sessionutil-file-add-relevant-details)
- [4 Create secrets.properties in the root folder and add "key"](#4-create-secretsproperties-in-the-root-folder-and-add-key)
- [5 In AndroidManifest.xml, add host and scheme](#5-in-androidmanifestxml-add-host-and-scheme)
- [6 Run the app and click on login button](#6-run-the-app-and-click-on-login-button)
- [7 Login to stackoverflow and then approve the app](#7-login-to-stackoverflow-and-then-approve-the-app)
- [8 You will be redirected to app upon clicking "approve" on step 7](#8-you-will-be-redirected-to-app-upon-clicking-approve-on-step-7)
- [9 Now on clicking `update` button in app, you will see the response from server in the Android Studio logcat.](#9-now-on-clicking-update-button-in-app-you-will-see-the-response-from-server-in-the-android-studio-logcat)
- [10 Documentation](#10-documentation)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## 1 Create an app on stackapps

**url**

https://stackapps.com/apps/oauth/register

**example**

![](https://i.imgur.com/EoNeLeY.png)

## 2 On successful app creation, you will get details

**example**

![](https://i.imgur.com/ODEG7Xv.png)

## 3 In SessionUtil file, add relevant details

[SessionUtil.kt](app/src/main/java/com/reputationoverflow/session/SessionUtil.kt)

**example**

![](https://i.imgur.com/qG85mgM.png)

## 4 Create secrets.properties in the root folder and add "key"

STACK_EXCHANGE_KEY="YOUR_KEY_HERE"

Remember to add "key" and not secret.

**example**

![](https://i.imgur.com/8He7mXm.png)

## 5 In AndroidManifest.xml, add host and scheme

Notice the resemblance in `host` and `scheme` here and the `redirect_uri` in step 3. They should match.

**example**

![](https://i.imgur.com/rl0tmXR.png)

## 6 Run the app and click on login button

You will see login screen

**example**

![](https://i.imgur.com/LZiBQVv.jpg)

## 7 Login to stackoverflow and then approve the app

**example**

![](https://i.imgur.com/o9px0y5.jpg)

## 8 You will be redirected to app upon clicking "approve" on step 7

Notice, the `login` button is hidden and `logout` button is shown now.

**example**

![](https://i.imgur.com/mnuOaF2.jpg)

## 9 Now on clicking `update` button in app, you will see the response from server in the Android Studio logcat.

Open Android Studio logcat and notice the response on clicking "update" button.

The update will send request to this endpoint in [StackExchangeApiService.kt](app/src/main/java/com/reputationoverflow/network/StackExchangeApiService.kt)

```kotlin 
@GET("/me?order=desc&sort=reputation&site=stackoverflow")
suspend fun getMyReputation(): String
```

The Documentation for above endpoint is here: https://api.stackexchange.com/docs/me

You can modify endpoints and do whatever at this point.

**example**

![](https://i.imgur.com/nhdpXMw.png)

## 10 Documentation

Find more Documentation, API endpoints here: https://api.stackexchange.com/docs/

**example**

![](https://i.imgur.com/229OU79.png)

