# SecureWifi
Mobile Application for Automated Security Testing of Wireless Access Points

## Short overview
The developed application allows an information security specialist to:
* scan Wi-Fi points nearby and view their list;
* view the properties of each scanned point, such as WPS support, security protocol used, etc.;
* for each of the properties of the scanned points, view their short description and the known vulnerabilities associated with them;
* carry out attacks on one or more wireless access points by guessing passwords for them from one or more lists;
* use to attack password lists predefined in the application, which are various lists of the most popular passwords;
* set your own password lists, consisting of a fixed list and dynamic passwords generated from user-specified information;
* set the number of dynamically generated passwords, information about people, places and keywords, that is, the data that could be used when setting a password for a point;
* view information about the ongoing attack both in the application itself and in the notification that appears during the attack;
* at any time view information about previous attacks, including the number of passwords tried, a list of such passwords, and whether the correct password for the point was found.


## Functionality
The application is designed to check the password security of wireless access points by sequentially connecting to them with sets of passwords. Sets of passwords may consist of fixed and dynamic part. Dynamic part consists of passwords generated from the info given by the user. The application also saves statistics about connection attempts. In addition, the application allows a specialist to scan nearby Wi-Fi points and get information about their capabilities. 
The application is divided into 4 menu items, each of which is responsible for its functionality.

## Use-case diagram

<img src="https://github.com/MaverickBattler/SecureWifi/assets/73700612/f8208729-16e9-45ff-b95d-1b5e4eda493f">

## Application screens diagram

<img src="https://github.com/MaverickBattler/SecureWifi/assets/73700612/934ef01b-7ec3-4791-acbc-79aa66072d19" height="700">

## Screenshots

App supports English and Russian languages, light and dark themes.

<img src="https://github.com/MaverickBattler/SecureWifi/assets/73700612/3d5d3989-6738-4e76-8943-8fd51bb472e0" height="700">
<img src="https://github.com/MaverickBattler/SecureWifi/assets/73700612/6d10a11f-6fa0-43e6-8116-204fc1511a3c" height="700">
<img src="https://github.com/MaverickBattler/SecureWifi/assets/73700612/f5930919-f550-4289-88ae-4584cc03bfd9" height="700">
<img src="https://github.com/MaverickBattler/SecureWifi/assets/73700612/4883aa64-6cfa-4f9e-b911-628f579de5a9" height="700">
<img src="https://github.com/MaverickBattler/SecureWifi/assets/73700612/db5dcb0d-4ef4-4fb9-85c9-99b9bff6f2a5" height="700">
<img src="https://github.com/MaverickBattler/SecureWifi/assets/73700612/0cb02eb0-e28e-400a-a726-11f7715f26ce" height="700">
<img src="https://github.com/MaverickBattler/SecureWifi/assets/73700612/b218bff6-48e6-42d6-9e1e-e8b7dfc40e8c" height="700">

## Requirements to run the application
For the application to work properly, the user must allow the application to access the device's precise location data, which is necessary to scan nearby wireless access points. Application will only perform connection attempts to Wi-Fi points on <b>Android 9</b> (inclusive) or lower. Lowest supported version is Android 6. On Android versions higher than 9, device must have <b>Root</b>.
