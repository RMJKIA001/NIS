===Requirements===
Java 8 is required as we use the Base64 library available from java 8

===Running the program===
-done after compilation (although class files have been provided for you)
1) Open a terminl
   run the Server program using : java Server
2) Open another terminal
   run the Client program using : java Client
   2.1) Enter the message to send on the Client terminal when prompted.
3) Watch encryption on Client terminal
   Watch decryption on Server terminal

===Compiling===
-Navigate to the NIS folder where the java files are stored.
-Compile the java files using : javac [filename].java 
 i.e javac Server.java
 	 javac Client.java
 	 javac Hash.java
 	 javac rsaGenerator.java
*Class files have been provided to allow for easy running.

===File structure===
-NIS
 Client.java
 Hash.java
 Server.java
 rsaGenerator.java
--client
  public.key
  private.key
--server
  public.key
  private.key

===File descriptions===
-Client.java : Client side program. This is to be run after the server is running. It connects to the local host server on port 3000, gets the message from the user input, encrypts the message and finally sends it to the server.
-Hash.java : This implents the SHA3-256 hashing algorithm used in Client and Server
-Server.java : Server side program. This needs to be run first. It creates an available connection point on port 3000 and waits for a client to connect. Once the connected client has sent its message, it decrypts the message and only displays it if the message is authenticated.
-rsaGenerator.java : This has methods implented for dealing with the RSA encryption that is used in the Client and Server programs. 






