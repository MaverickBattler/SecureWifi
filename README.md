# SecureWifi
Mobile Application for Automated Security Testing of Wireless Access Points

## Short overview
The application is designed to check the password security of wireless access points by sequentially connecting to them with sets of passwords. Sets of passwords may consist of fixed and dynamic part. Dynamic part consists of passwords generated from the info given by the user. The application also saves statistics about connection attempts. In addition, the application allows a specialist to scan nearby Wi-Fi points and get information about their capabilities. 
The application is divided into 4 menu items, each of which is responsible for its functionality.

## Functionality
The developed application allows an information security specialist to:
* Scan Wi-Fi points nearby and view their list;
* View the properties of each scanned point, such as WPS support, security protocol used, etc.;
* For each of the properties of the scanned points, view their short description and the known vulnerabilities associated with them;
* Carry out attacks on one or more wireless access points by guessing passwords for them from one or more lists;
* Use to attack password lists predefined in the application, which are various lists of the most popular passwords;
* Set your own password lists, consisting of a fixed list and dynamic passwords generated from user-specified information;
* Set the number of dynamically generated passwords, information about people, places and keywords, that is, the data that could be used when setting a password for a point;
* View information about the ongoing attack both in the application itself and in the notification that appears during the attack;
* At any time view information about previous attacks, including the number of passwords tried, a list of such passwords, and whether the correct password for the point was found.

## Requirements to run the application
For the application to work properly, the user must allow the application to access the device's precise location data, which is necessary to scan nearby wireless access points. Application will only perform connection attempts to Wi-Fi points on <b>Android 9</b> (inclusive) or lower. Lowest supported version is Android 6. On Android versions higher than 9, device must have <b>Root</b>.

## Demo

App supports English and Russian languages, light and dark themes.

### Performing the attack (security check)

<img src="https://github.com/MaverickBattler/SecureWifi/assets/73700612/fc783c4d-812d-41f2-ac5c-c64ec9a241f4" height="800">

### Scanning Wi-Fi nearby

<img src="https://github.com/MaverickBattler/SecureWifi/assets/73700612/08e36de6-b1ee-4165-aae9-8970392d1c87" height="800">

### Creating custom passwords lists

<img src="https://github.com/MaverickBattler/SecureWifi/assets/73700612/7a8509b3-ab1c-4189-9db0-ff26806190e3" height="800">

## Application screens diagram

<img src="https://github.com/MaverickBattler/SecureWifi/assets/73700612/934ef01b-7ec3-4791-acbc-79aa66072d19" height="600" width="820">
