# csc326-OBP-202-2
## CoffeeMaker: Role-Based Order System
CoffeeMaker is a class project for [CSC326 Software Engineering](http://courses.ncsu.edu/csc326) in the [Computer Science Department of NC State University](http://www.csc.ncsu.edu/) and is developed by the Software Engineering students. The goal of CoffeeMaker is to engage with software engineering practices on a significant, relevant, and growing application that includes security and privacy requirements.

CoffeeMaker is an electronic recipe records system that provides customers with a means to place their beverage orders and receive notifications from the staff responsible for preparing beverages. Staff can update information about orders and current inventory including order status, editing, and adding recipes.

## Developer's Guide
The [Developer's Guide](https://github.ncsu.edu/engr-csc326-fall2023/csc326-OBP-202-2/wiki/Developer's-Guide) provides an overview of how to set up your system for developing, testing, and running CoffeeMaker.

* [Developer's Guide](https://github.ncsu.edu/engr-csc326-fall2023/csc326-OBP-202-2/wiki/Developer's-Guide)

## Users' Guide
The [Users' Guide](https://github.ncsu.edu/engr-csc326-fall2023/csc326-OBP-202-2/wiki/User's-Guide) provides an overview of how users can interact with the new functionality of the CoffeeMaker system.

* [Users' Guide](https://github.ncsu.edu/engr-csc326-fall2023/csc326-OBP-202-2/wiki/User's-Guide)

## Requirements
The [requirements of CoffeeMaker](https://github.ncsu.edu/engr-csc326-fall2023/csc326-OBP-202-2/wiki/Requirements) describe the functional and non-functional aspects of the system. Constraints on development are also provided.

* [CoffeeMaker Requirements](https://github.ncsu.edu/engr-csc326-fall2023/csc326-OBP-202-2/wiki/Requirements)

## Extra Credit
We have implemented the following extra credit for this project
### [Additional User role: Manager](https://github.ncsu.edu/engr-csc326-fall2023/csc326-OBP-202-2/blob/main/CoffeeMaker-Individual/src/main/resources/templates/manager.html)
* The role of a Staff User is split into two separate User roles: Manager and Barista
* Once authenticated, a Barista may:
    * Add Recipes [[UC8]](https://github.ncsu.edu/engr-csc326-fall2023/csc326-OBP-202-2/wiki/UC8)
    * Edit Recipes [[UC9]](https://github.ncsu.edu/engr-csc326-fall2023/csc326-OBP-202-2/wiki/UC9)
    * Delete Recipes [[UC6]](https://github.ncsu.edu/engr-csc326-fall2023/csc326-OBP-202-2/wiki/UC6)
    * View and Fullfill Beverage Orders [[UC3]](https://github.ncsu.edu/engr-csc326-fall2023/csc326-OBP-202-2/wiki/UC3) [[UC4]](https://github.ncsu.edu/engr-csc326-fall2023/csc326-OBP-202-2/wiki/UC4)
* Once authenticated, a Manager may:
    * Update the Inventory [[UC5]](https://github.ncsu.edu/engr-csc326-fall2023/csc326-OBP-202-2/wiki/UC5)
    * View the Order History [[UC13]](https://github.ncsu.edu/engr-csc326-fall2023/csc326-OBP-202-2/wiki/UC13)
### [Privacy Policy](https://github.ncsu.edu/engr-csc326-fall2023/csc326-OBP-202-2/blob/main/CoffeeMaker-Individual/src/main/resources/templates/privacy.html)
* We outline the following information for users
    * The user data we collect
    * The reasons for collecting user data
    * The way user data is used
### [Security Audit](https://github.ncsu.edu/engr-csc326-fall2023/csc326-OBP-202-2/wiki/Security-Audit)
* Potential problems with the authentication and authorization system were evaluated, documented, and mitigated.
### [UI Enhancement](https://github.ncsu.edu/engr-csc326-fall2023/csc326-OBP-202-2/tree/main/CoffeeMaker-Individual/src/main/resources/templates)
* Logo -  enhances service branding and cohesiveness
* Visuals - clarifies purpose and actions of buttons for users who may have difficulty reading text
* Buttons/Notifications - sized and colored appropriately to catch the attention of users and indicate results of actions for users who may have difficulty reading text
* Color scheme - pulls site together for a cohesive look for ease of look and access
### [Order History](https://github.ncsu.edu/engr-csc326-fall2023/csc326-OBP-202-2/blob/main/CoffeeMaker-Individual/src/main/resources/templates/orderhistory.html)
* We display the:
    * History of all placed orders for logging
    * Total amount earned from successfully for profit tracking
    * Total amount of ingredients of all place orders for future inventory ordering 
