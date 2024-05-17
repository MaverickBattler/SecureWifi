# SecureWifi
Mobile Application for Automated Security Testing of Wireless Access Points

## Short overview

The developed application allows an information security specialist to:
• scan Wi-Fi points nearby and view their list;
• view the properties of each scanned point, such as WPS support, security protocol used, etc.;
• for each of the properties of the scanned points, view their short description and the known vulnerabilities associated with them;
• carry out attacks on one or more wireless access points by guessing passwords for them from one or more lists;
• use to attack password lists predefined in the application, which are various lists of the most popular passwords;
• set your own password lists, consisting of a fixed list and dynamic passwords generated from user-specified information;
• set the number of dynamically generated passwords, information about people, places and keywords, that is, the data that could be used when setting a password for a point;
• view information about the ongoing attack both in the application itself and in the notification that appears during the attack;
• at any time view information about previous attacks, including the number of passwords tried, a list of such passwords, and whether the correct password for the point was found.


## Functionality
The application is designed to check the password security of wireless access points by sequentially connecting to them with sets of passwords. Sets of passwords may consist of fixed and dynamic part. Dynamic part consists of passwords generated from the info given by the user. The application also saves statistics about connection attempts. In addition, the application allows a specialist to scan nearby Wi-Fi points and get information about their capabilities. 
The application is divided into 4 screens, each of which is responsible for its functionality.

### 1.	Screen for connecting to nearby Wi-Fi hotspots.
On the Connect to Nearby Wi-Fi Points screen, the user can see a button that initiates a password security check for nearby Wi-Fi points. The security check includes scanning nearby Wi-Fi points, filtering the results, generating a set of passwords for each of the points, and then connecting to each of the points with the generated set of passwords. During the connection, the user can see the information about which point the device is trying to connect to, how many passwords have already been tried, how many are left to try and the speed of trying passwords. During connection attempts, data about them is stored in the application's memory, so the same password will not be tried twice to the same point. The point will also be skipped if a valid password has already been found for it. At the moment, the passwords that are generated for each Wi-Fi point consist of a fixed set of passwords, including some of the most popular Wi-Fi passwords, as well as a set consisting of passwords generated specifically for this point according to the information about it. At the moment, these are passwords obtained by manipulating the name of the access point.

Screenshots with an example of work on this screen:

![1](https://github.com/MaverickBattler/SecureWifi/assets/73700612/6823ab23-17ce-4018-a932-adaa75d7e476)
![2](https://github.com/MaverickBattler/SecureWifi/assets/73700612/4ab4aeec-2caf-4d61-ba62-5406caeb56ca)
![3](https://github.com/MaverickBattler/SecureWifi/assets/73700612/66901710-c35b-4bd2-97c7-3c8cd2130d50)

### 2.	Wi-Fi scanning screen.
On the Wi-Fi scanning screen, there is one button that can be pressed to scan. Upon receipt of the scan results, they are shown in the list. For each Wi-Fi point, its SSID (name), capabilities such as WPS, and signal strength are displayed. In the future, it is planned to make it possible to click on the elements of the list, after which there will be a transition to a separate screen with full information about this access point, obtained as a result of scanning. It is also planned to make the signal level display more understandable visually.

Screenshots with an example of work on this screen:

![4](https://github.com/MaverickBattler/SecureWifi/assets/73700612/d16647a4-686e-4b52-a33d-1a21f57e184f)

### 3.	Screen with the results of the Wi-Fi security check.
The Wi-Fi security test results screen retrieves the Wi-Fi point security test data from the database that was collected from the tests on the first screen. In particular, for each point, its SSID (name), the fact that the correct password was found, and the number of passwords tried for this point are displayed. In the future, it is planned to add the ability to open a separate screen by clicking on an element of the list, which will show all information about the security testing of a given Wi-Fi point, including the correct password, if it was guessed, as well as a list of all passwords tried for this point.

Screenshots with an example of work on this screen:

![5](https://github.com/MaverickBattler/SecureWifi/assets/73700612/a4347789-c6c0-4ae4-8fb6-cacf95efb692)

## Requirements to run the application
For the application to work properly, the user must allow the application to access the device's precise location data, which is necessary to scan nearby wireless access points. At the moment, for the correct operation of the application, if the check is carried out near the wireless access point with the correct password that was remembered by the device, the user is recommended to forget the password for this point manually in the application Wi-Fi settings. In the current version, the application will definitely work for Android versions up to 8.1 (API level 27). No functionality has been tested for newer versions.
