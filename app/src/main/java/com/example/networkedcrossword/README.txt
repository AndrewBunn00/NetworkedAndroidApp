CS455 Networked Crossword Puzzle
By Andrew Bunn, Rajpreet Judge, Derek Olson, Joel Tanig, and Vlad Vitalaru

What Our App Is:
- Our app is a 2 player crossword puzzle game that can be played over the network.
- One player hosts the game, and the other player joins the game.
- Both players have access to the board and all the hints.
- The players take turns finding words on the board, and the game ends when all the words have been found.
- The player with the most points (ie solved words) wins.


Getting started:


Setting up a device (emulator):
- In the AVD manager, create a new virtual device
- Select a device to emulate (Pixel 6 & Pixel 6 Pro are good ones)
- Select a system image (Android 11.0 with API 30 is good)
- Select a name for the device (Dont necessarily need, may be easier to keep track of)


Adding Another Run Config:
- In the run window, click the dropdown that says 'app' and select 'Edit Configurations'
- Click the copy button (two papers) to copy the current configuration
- Rename the new configuration to something like 'client'
- Keep all the defaults. A second config is needed to run the two separate devices.


Connecting two emulators:
- Enable telnet in the control panel (for Windows)
- Run both emulators in the ide (server on is on 10.0.2.15 & client on 10.0.2.2, it is coded to be this way)
- FOR LATER: the server should connect on port 4000 and the client should connect on port 6000 (this
is necessary for some of the weirdness associated with connecting the emulators as opposed to two real devices)


Connect to a device via Telnet:
- First emulator ID will be 5554, and second 5556 (confirm in the run window of the IDE, can search
for "555" to find if it is 5554 or 5556)
- To connect via telnet, run 'telnet localhost:5554' in the command prompt (for connecting to server device)
- Be sure to run the command prompt as administrator
- If command did not work, do 'telnet localhost 5554' (no colon)
- Start the server with port 4000 from the app in the emulator (enter port number 4000 and hit 'Create')


Telnet Auth and Redirection:
- In the command prompt (for windows) it will ask the user to authenticate before any redirections can be made,
enter 'auth <authToken>' to authenticate
- The auth token is found in the location of the specified path in the command prompt (something
like 'C:/Users/Bob/.emulator_console_auth_token')
- Now, in the telnet window, add a redirect (type 'redir add tcp:6000:4000'). This will redirect the
port 6000 on the client to port 4000 on the server
- If it fails, need to say auth <authToken> (telnet window says where to get it at the top)
- After adding the redirect successfully, enter port 6000 on the client and hit "Join"
- Should see communication in the run window of the ide, the server should print out 'client connected'


Server Client Communication:
- On successful connection, the server sends the client the seed for which board to use. The client
then generates the board.
- The client will be blocking for read, waiting for the server to write to its buffer.
- Once the server turn is over, it will update a list of all the words that have been found, and send
that list to the client.
- The client will then update its list of words found, and update the board with the new words found
and be able to take their turn.
- The client will update the word list and send it back to the server, who will then update their
board and word list.
- This process repeats until the game is over.


Code Organization:

Server & Client:
- The server and client are both their own classes, called to run in a NetworkThread (which is an extension of a thread).
- The server and client both have their own run methods, which are called when the thread is started.
- The server will start up and be waiting for a client to connect, and the client will be waiting for the server to send
the seed for the board.
- This means that the client is blocking for a read right away, and the server's first action is a write.
- After the client receives the seed, it will generate the board. The server generates the board from the same seed it sent to the client
- The shared object between the main activity and the server or client is called Data.
- The Data object contains many flags for determining when the game is over, when the client is connected, when the client has
sent to the server, when the server has sent to the client etc.
- The data object also contains the list of words solved (a boolean array), the player scores, and whose turn it is.
- The server and client send a Json string containing certain information from data to each other, and then update their own data
object with the information from the other side.


Board Generation:


The UI:
- The whole game is contained in the main activity.
- The start up screen that allows users to host or join a game and proceed to the game window is all
removed once the game starts.
- The game appears in the same activity, having the layout added to the main activity after
the other stuff is cleared out.


Group Contributions:
- Andrew:
    Created the NetworkThread, Server, and Client classes. Handled the server and client communication.
    Created the Data class, which is the shared object between the main activity and the server/client.
    Created the JsonParser class with Raj, which parses the Json strings sent between the server and client.
    Handled updating the UI for switching between connection and game screen.

- Raj:

- Derek:
    Created part of the Game class which and rendering in the board based on the seed.
    Created the CrosswordBoard view specifically the board generation which uses a canvas and 2d graphics to generate the board.
    Worked on getting the network thread to communicate with the game activity and subsequent crosswordboard view.
    
- Joel:

- Vlad:

Notes:
- If the back button or home button is hit, the app will restart resulting in undefined behavior.
Android phones do this by default, restarting the app when it is left. So if this happens, please
restart both runs of the app.
