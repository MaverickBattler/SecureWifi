# SecureWifi
Master's thesis on "Mobile Application for Automated Security Testing of Wireless Access Points".
## Functionality
The application is designed to check the complexity of passwords of wireless access points by sequentially connecting to them with a set of passwords generated for each point. The application also saves statistics about connection attempts. In addition, the application allows you to scan nearby Wi-Fi points and get information about their capabilities. 
The application is divided into 3 screens, each of which is responsible for its functionality.

### 1.	Screen for connecting to nearby Wi-Fi hotspots.
On the Connect to Nearby Wi-Fi Points screen, the user can see a button that initiates a password security check for nearby Wi-Fi points. The security check includes scanning nearby Wi-Fi points, filtering the results, generating a set of passwords for each of the points, and then connecting to each of the points with the generated set of passwords. During the connection, the user can see the information about which point the device is trying to connect to, how many passwords have already been tried, how many are left to try and the speed of trying passwords. During connection attempts, data about them is stored in the application's memory, so the same password will not be tried twice to the same point. The point will also be skipped if a valid password has already been found for it. At the moment, the passwords that are generated for each Wi-Fi point consist of a fixed set of passwords, including some of the most popular Wi-Fi passwords, as well as a set consisting of passwords generated specifically for this point according to the information about it. At the moment, these are passwords obtained by manipulating the name of the access point.

Screenshots with an example of work on this screen:

![image](https://github.com/MaverickBattler/SecureWifi/assets/73700612/9d7ba059-6580-460c-aa22-c80b1976169c)
![image](https://github.com/MaverickBattler/SecureWifi/assets/73700612/31e0c421-fc22-422b-82ad-cc7098f8cbdc)

![image](https://github.com/MaverickBattler/SecureWifi/assets/73700612/c23b3739-f99f-42b3-8f66-2b909f21bde8)

### 2.	Wi-Fi scanning screen.
On the Wi-Fi scanning screen, there is one button that can be pressed to scan. Upon receipt of the scan results, they are shown in the list. For each Wi-Fi point, its SSID (name), capabilities such as WPS, and signal strength are displayed. In the future, it is planned to make it possible to click on the elements of the list, after which there will be a transition to a separate screen with full information about this access point, obtained as a result of scanning. It is also planned to make the signal level display more understandable visually.

Screenshots with an example of work on this screen:

![image](https://github.com/MaverickBattler/SecureWifi/assets/73700612/f5d9d892-1abb-443c-aca2-f5c11f8145b6)

### 3.	Screen with the results of the Wi-Fi security check.
The Wi-Fi security test results screen retrieves the Wi-Fi point security test data from the database that was collected from the tests on the first screen. In particular, for each point, its SSID (name), the fact that the correct password was found, and the number of passwords tried for this point are displayed. In the future, it is planned to add the ability to open a separate screen by clicking on an element of the list, which will show all information about the security testing of a given Wi-Fi point, including the correct password, if it was guessed, as well as a list of all passwords tried for this point.

Screenshots with an example of work on this screen:

![image](https://github.com/MaverickBattler/SecureWifi/assets/73700612/f35d99de-f5bd-42ee-84d2-6451fe33cf8f)

## Requirements to run the application
For the application to work properly, the user must allow the application to access the device's precise location data, which is necessary to scan nearby wireless access points. At the moment, for the correct operation of the application, if the check is carried out near the wireless access point with the correct password that was remembered by the device, the user is recommended to forget the password for this point manually in the application Wi-Fi settings. In the current version, the application will definitely work for Android versions up to 8.1 (API level 27). No functionality has been tested for newer versions.
