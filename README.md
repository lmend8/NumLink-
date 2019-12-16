# NumLink

Luis Mendoza 

CS 371-Software Development-Fall 2019

Java - framework JavaFX



# About
 NumLink is a game with the purpose of linking each number with respective endpoints by using the amount of slots equal to the source number. 


# Usage
This project is the source code for NumLink. Follow the instruction below to compile and run this project.

 1. Download and install the dependencies in the dependencies section. 
 2. Eclipse project is provided with this source code ,but you can use any Java IDE of your choice. 
 3. Compile with your IDE and hit run to play. 

# Dependencies 
Download and install JavaFx framework from https://openjfx.io/. For example, for mac, you need to download javafx-mx.jar library into /Library/Java/VirtualMachines/jdk1.8.0_131.jdk/Contents/Home/lib and modify/specify the apropriate build path in your IDE. 

# How to make a Level 
 Making new levels in very easy in NumLink. All level are specified in text format, comma delimeted. To make a new Level just add a text file, name it "level<levelNumber>.txt". make sure it doesn't not conflict with other level files already specifed. Place your text file in the root directory of the provided project along with the other level files. You might want to start with one of the level files that is already provide, otherwise follow these instruction:

1. The file must five rows and five columns.
2. Each cell can only contain one of three values, a non-zero number, an "X" , or 0. A non-zero number denotes the number of slots that must be used in the solution. "X" denotes the endpoint. 0 denotes an empty cell.



