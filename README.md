
CS201 Team 16 SimCity Project
======

### Important Project Links
  + [General Team Information](https://github.com/usc-csci201-fall2013/team16/wiki)
  + [Design Document List](https://github.com/usc-csci201-fall2013/team16/wiki/Design-Documents)

### Team Members & Primary Assignments
  + Justine Cocchi, Team Leader
  	+ Houses and homeowner role
  	+ Apartments and landlord role
  	+ restaurant4
  + Grant Derderian, Git/GitHub Expert
  	+ Market and associated roles (MarketWorker, MarketCustomer, MarketManager)
  	+ Global city timekeeping
  	+ Program documentation design and Git upkeeping
  	+ restaurant3
  + Teryun (Tom) Lee
  	+ Master Bank class design
  	+ Individual bank branch design and associated roles (BankTeller, BankCustomer, BankManager)
  	+ restaurant5
  + Holly Mitchell
  	+ PersonAgent, all data, actions, and scheduler
  	+ PersonAgent integration into roles and buildings
  	+ Organized activity logging across roles
  	+ restaurant2
  + Trevor Reed, A* Expert
  	+ Global Transportation: buses, cars, market truck
  	+ City GUI A*/Path Finding
  	+ Main CityGUI images/design
  	+ restaurant1

The initial design of the new SimCity components (markets, banks, GUI and layout, transporation, housing, etc.) was conceived through the collective work of all team members through various meetings we held several times per week for several hours. From there, specific assignments were distributed to each team member as noted above. Although one person took ownership of each major component, all team members contributed to each other's designs and final code.

Each one of our team's members contributed to the various GUI files within our program in order to ensure that each building was properly integrated into the main city layout. Additionally, we worked together in order to handle the overall successful integration of our own restaurant projects and individual buildings we were each responsible for.

Testing was conducted on a per-building or per-role basis within the testing folders named either per-person (i.e. "granttesting") or per single building unit (i.e. "BankTest").

### Compile Instructions (v1)
To compile and run our SimCity, clone this GitHub repo into a new directory on your local computer. Then, follow these instructios:
+ Open the Eclipse IDE on your computer.
+ Choose File -> New -> Other. Select the Java Project from Exisitng Ant Buildfile in the list of options
+ Click on Browse, navigate to the directory of the repo you cloned from out assignment on GitHub, and then select the build.xml file.
+ Click the Link to the buildfile in the file system checkbox and then click on finish.

__To run the program as a whole__, navigate to the `CityGui.java` file located within `src/city.gui`. Then, click the small arrow next to the green play button in the Eclipse toolbar and select `CityGui` from the dropdown list. The program will now run. To close it, click the red stop sign in Eclipse.

__To test different buildings within the program__, navigate to any of the various test files located within the named project testing folders, such as `src/BankTest` or `src/city.transporation.test`. If you receive a message about JUnit not being in your build path, accept Eclipse's warning/suggestion to add it. If you don't see this, go to Project -> Properties in Eclipse's menu. Click on Java Build Path in the left menu, then on Libraries in the upper menu, and then on Add Library. Click JUnit, then select JUnit 3, and then click Finish. Click OK to exit the Project Settings dialog. With your selected test file still open, click the small arrow next to the green play button in the Eclipse toolbar and select the name of the test you would like to run from the dropdown list. That test will now run, showing you the results on the left.

### Testing Scenarios

More information on how to test the differnet elements in the program will be posted here soon.

### Overall State of Program

More information on what's working and not working will be posted here soon.