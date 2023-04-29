Networked Crossword Puzzle
By

Getting started:

Setting up a device:

Connecting two emulators:

- Enable telnet in the control panel (for Windows)
- Run both emulators in the ide (server on is on 10.0.2.15 & client on 10.0.2.2, it is coded to be this way)

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