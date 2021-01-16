[README FILE of BULLETINBOARD CLIENT]

Made By: Kush Banbah 55786740
Type of program: Java GUI
Developed Using Eclipse
Does not require an IDE to run
Supported Systems: Windows (has not been tested on other OSs)

Requires Java JRE and JDK to be installed before running, if Java JRE is not installed please download from
https://java.com/en/download/

To install JDK install from:
https://www.oracle.com/in/java/technologies/javase-downloads.html

Windows copies of both files are included in the zip file labelled 55786740.zip 
Open the zip file -> Libaries -> JDK and JRE

Windows PC must be 8 or later (64 bit)

Double click on BulletinBoardClient.jar to run the program once all libraries are installed

[How to use run program]
[CONNECT]

Enter the IP address and Click on the connect button
If the connection is a sucess then a message will be displayed and command buttons will be enabled.
Otherwise an error message will appear and you can retry connection
The client may take some time before failing to or establishing connection depending on speed. 

[POST]

To POST to server click on the POST button
The small text field at the bottom will be enabled
Type messages in the textfield and click on enter to acknowledge a message
Once all the messages have been entered, type only "." to POST all messages to the server

[READ]

To READ from the server click on the READ button
The client will recieve all the  messages and output on the display field

[QUIT]

Click on the QUIT button
A message box will appear telling you if the QUIT happened sucessfully or not, if not you may need to restart the server and/or check your connection
The client can then connect to another Server by entering a new IP and clicking on connect even if unsucessful quit

[X]
To exit click on the X in the top right and it will automatically make the client sent a QUIT message to the server
after displaying the sucess of the QUIT method the client will exit

If either POST or READ fail, an error message will be displayed and the client will close the connection by attempting to send QUIT
The user can then reconnect to another server as if QUIT was pressed

The program prevents the User from ever inputting a wrong command so the server will never have to send ERROR=command not understood


[How to compile]

To open/edit/run the source code directly in an IDE
Set the package name in the first line and change the class name to the file name if it has any issues.
Import required libaries (notably windowsbuilder)
Does not need any other setup to open in another IDE
.java files can also be opened by a text editor