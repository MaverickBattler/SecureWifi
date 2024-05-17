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

![Use case diagram drawio](https://github.com/MaverickBattler/SecureWifi/assets/73700612/f8208729-16e9-45ff-b95d-1b5e4eda493f)

## Application screens diagram

![Диаграмма окон drawio (1)](https://github.com/MaverickBattler/SecureWifi/assets/73700612/ddd79a59-77c6-444f-bda6-eb47c0b3437d)

## Requirements to run the application
For the application to work properly, the user must allow the application to access the device's precise location data, which is necessary to scan nearby wireless access points. Application will only perform connection attempts to Wi-Fi points on <b>Android 9</b> (inclusive) or lower. Lowest supported version is Android 6. On Android versions higher than 9, device must have <b>Root</b>.
