Stackoverflow login

Documentation: https://api.stackexchange.com/docs/authentication

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

Remember to add key and not secret.

**Note**: The key in the source code is invalid and you need to add your OWN key that you receive on step 2.

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

## 8 Now on clicking update button in app, you will see the response from server.

You can modify endpoints and do whatever at this point.

**example**

![](https://i.imgur.com/nhdpXMw.png)