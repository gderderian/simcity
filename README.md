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
  	+ Main CityGUI animation and graphics
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

### Running Scenarios
The scenarios listed in the World tab show the different scenarios that we have successfully implemented. Only one scenario can be run at a time, then the program must be restarted.
In the control panel, a specific scenario can be selected by the user and when "Start Scenario" is clicked, it will create people, send them to jobs, and perform the following actions:
+ Restaurant Scenarios - Workers/customers for a specific restaurant will go to their restaurant and do their normal actions there.
+ Full Scenario - This runs multiple restaurant tests at the same time.
+ Bus Scenario - This shows a person waiting for a bus and taking it to their final destination.
+ Average Joe - A person is created within their house (bottom right house), they try to make food and see that their fridge is empty. They then try to go to the market to buy more food. Market workers are also created for this scenario, but the scenario does not go beyond placing the order.
+ A* Animation - This creates multiple vehicles and people moving around the city to display A* animation capabilities.

### Overall State of Program

#####Transportation
+ Market Delivery Trucks are tested, but they have not been fully integrated yet. They show up in some scenarios to simply drive around to show the A* animation capabilities. They don't communicate with markets yet, except within tests.
+ Buses are fully tested and implemented, but we haven't yet updated the person to choose when to take the bus. We have a scenario that shows one person taking a bus, but we still need to implement this in a more general sense.
+ Cars are fully tested and implemented, but they haven't been integrated into the city yet. We just need to finish updating the Person Agent to include this form of transportation.

#####Animation
+ A* animation has been successfully implemented on a city-wide level to handle sidewalks, streets, and crosswalks. It hasn't been added inside of buildings yet. It sometimes lags when many guis are added to the panel, but they should eventually find their way to their destinations.

#####Person Agent
+ The PersonAgent needs to be redone so that there is an Event class which has an intent and a location. This is so that the person's actions and messages will be timed correctly when they take the bus or car. Right now, the Person's actions are based on the assumption that they can essentially be completed inside one method - there are semaphores within the DoGoTo method that take care of timing, but these get messed up when the person goes to the bus or takes the car. Instead, we now have a specific bus test which shows that the bus system is functional.

#####Housing
+ All houses and apartments are able to be clicked on and there is minimal gui functionality. The best scenario involving house gui components is the Average Joe scenario. There is no landlord gui capabilities at this time, however the landlord role is fully unit tested (rent collection and fixing appliances) as is the house (putting away groceries and cooking food).
