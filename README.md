# EXIF Editor Tool - CS443 Project Final

EXIF Editor Tool is an Android application that allows users to import images (or take photos with the in-app camera) and be able to view and edit the EXIF metadata. They can also view their images on a map to see where they were taken.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

IDE
* [Android Studio](https://developer.android.com/studio/) - The IDE used to compile and emulate

ADB 
* [ABD](https://www.xda-developers.com/install-adb-windows-macos-linux/) - Used to send images to emulated device.

Emulator
* Pixel 2 XL - API 27 (Android 8.1.0) - Unit testing was done for this device.


### Installing

* Download the project into your local directory.
* Open in Android Studio
* Click 'Run'

Once the emulator is running,
* Import images into the emulator.

Example on Mac:

In Terminal:
```
adb devices
```
You will now see something like:
```
List of devices attached
emulator-5554	device
```
Now use the 'push' command to send the photos from the local computer to the emulator:
```
adb push path/to/Sample_Images/*.jpg /storage/emulated/0/Download/
```
Make sure to write the complete path to the Sample_Images folder and then use * to import all images.

Example:
```
DMBP:~ Doron$ adb devices
List of devices attached
emulator-5554	device

DMBP:~ Doron$ adb push AndroidStudioProjects/CS443_Project_Final/Sample_Images/*.jpg /storage/emulated/0/Download/
AndroidStudioProjects/CS443_Project_Final/Sample_I...PR1094.jpg: 1 file pushed. 135.5 MB/s (3837658 bytes in 0.027s)
AndroidStudioProjects/CS443_Project_Final/Sample_I...150033.jpg: 1 file pushed. 143.5 MB/s (3575381 bytes in 0.024s)
AndroidStudioProjects/CS443_Project_Final/Sample_I..._125531.jpg: 1 file pushed. 17.4 MB/s (1926479 bytes in 0.106s)
AndroidStudioProjects/CS443_Project_Final/Sample_I..._074702.jpg: 1 file pushed. 52.0 MB/s (2142221 bytes in 0.039s)
AndroidStudioProjects/CS443_Project_Final/Sample_I...MG_4197.jpg: 1 file pushed. 131.6 MB/s (964408 bytes in 0.007s)
AndroidStudioProjects/CS443_Project_Final/Sample_I...MG_4198.jpg: 1 file pushed. 155.9 MB/s (911931 bytes in 0.006s)
AndroidStudioProjects/CS443_Project_Final/Sample_I...MG_4308.jpg: 1 file pushed. 116.3 MB/s (889054 bytes in 0.007s)
AndroidStudioProjects/CS443_Project_Final/Sample_I...MG_4387.jpg: 1 file pushed. 130.6 MB/s (859198 bytes in 0.006s)
AndroidStudioProjects/CS443_Project_Final/Sample_I...G_4427.jpg: 1 file pushed. 102.8 MB/s (1061364 bytes in 0.010s)
AndroidStudioProjects/CS443_Project_Final/Sample_I...IMG_4508.jpg: 1 file pushed. 13.2 MB/s (928774 bytes in 0.067s)
AndroidStudioProjects/CS443_Project_Final/Sample_I...IMG_4529.jpg: 1 file pushed. 44.2 MB/s (963128 bytes in 0.021s)
AndroidStudioProjects/CS443_Project_Final/Sample_I...kfdbsk1.jpg: 1 file pushed. 181.6 MB/s (784413 bytes in 0.004s)
12 files pushed. 54.4 MB/s (18844009 bytes in 0.330s)
DMBP:~ Doron$ 
```
**NOTE: The images will be in the Downloads folder but will *NOT* be visible in the app yet.**
**To make them viewable in the app, please open Google Photos. Once they are viewable in Google Photos, then they will be viewable in Exif Editor.**

## Running the tests

* Import an image by clicking the + Floating Action Button.
* Select an image.
* Once an image is loaded in the app, swipe right and click on "Map" in the Menu Drawer.
* If the image had proper GPS, it will be shown on the map.
* Go back to the Gallery and click on the imported image.
* Modify the metadata as needed. Try changing the GPS.
* Click the "Go" button to update.

*Note: This does not update the database. The old coordinates will remain. Import the image again to see the update one.*
* Import the same image again. 
* Open the map.
* You should now see the old and the new GPS markers.



## Authors

* **Doron Galambos** - *UMass Boston Student* - [Github: dgalambos](https://github.com/dgalambos)
* **Vasko Nikolov** - *UMass Boston Student* - [Github: wasoto](https://github.com/wasoto)
