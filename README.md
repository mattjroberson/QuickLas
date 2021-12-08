![banner](./readme_assets/project_banner.png)

# Description
Quick LAS is an Android app that was the final project for a Mobile App Development course. It was a group project with four members. It interprets and graphs Log ASCII Standard (LAS) files. These files are commonly used in the oil and gas industries for sub surface scanning to detect optimal drill sites. 

<br>

For more details on the project see the [Final Project Report](https://github.com/mattjroberson/QuickLas/blob/master/CS4153-QuickLASFinalReport.pdf).

## <ins>Members</ins>
* __Roden, Jeff__ - Project Lead
* __Roberson, Matthew__ - Lead Programmer
* __Yeager, Jessey__ - Lead Designer
* __Stott, Lucas__ - Secondary Programmer & Designer


## <ins>Features</ins>
* Import LAS file from phone
* Create multiple "tracks" or graphs to be displayed simultaneously
* Scale graph using linear or logarithmic function
* Pick from thirty data sources or "curves" to graph
* Customize the styling for the graph lines

<t>For detailed user documentation see the [Final Project Report](https://github.com/mattjroberson/QuickLas/blob/master/CS4153-QuickLASFinalReport.pdf)

# Snapshots
 ![snapshot 1](./readme_assets/snapshot1.jpg)
![snapshot 2](./readme_assets/snapshot2.jpg)
![snapshot 3](./readme_assets/snapshot3.jpg)
![snapshot 4](./readme_assets/snapshot4.jpg)
![snapshot 5](./readme_assets/snapshot5.jpg)
![snapshot 6](./readme_assets/snapshot6.jpg)
<br><br>
Video Demonstration: https://youtu.be/Ms462kNj8h8

# Build Instructions
This project was built inside the Android Studio IDE. Clone the repo and import the project into Android Studio to build.

Follow the instructions in the [Final Project Report](https://github.com/mattjroberson/QuickLas/blob/master/CS4153-QuickLASFinalReport.pdf) to import the demo LAS file into the Android Emulator.

# Possible Improvements
* Late in the production, we realized the graphing library was only designed to implement line charts in the horizontal direction. The nature of the app depicting data from subsurface scans meant that we needed to use the vertical orientation. The solution we arrived at that allowed us to finish on schedule was to force the screen rotation when displaying the graph. This is a less than ideal patch that could be resolved with an improved graphing library.

# Known Bugs
* There are no bugs currently being tracked

# Contributions & Licensing
This project is closed to development, however feel free to fork and use any part of the codebase.

# Acknowledgements
This project relies on https://github.com/PhilJay/MPAndroidChart for the graphing interface.
