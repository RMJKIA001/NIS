===Requirements===
Java 8 is required as we use the Base64 library available from java 8

===Running the program===
****On windows command prompt***
-done after compilation (although class files have been provided for you)
1) Open a terminl in the NIS folder
   run the Server program using : 
   java -cp lib/hamcrest-core-1.3.jar;lib/junit-4.12.jar;bin/ Server
2) Open another terminal in the NIS folder
   run the Client program using : 
   java -cp lib/hamcrest-core-1.3.jar;lib/junit-4.12.jar;bin/ Client
   2.1) Enter the message to send on the Client terminal when prompted.
3) Watch encryption on Client terminal
   Watch decryption on Server terminal

===Running the Testing===
****On windows command prompt***
-done after compilation (although class files have been provided for you)
1) Open a terminl in the NIS folder
   run the Test program using : 
   java -cp lib/hamcrest-core-1.3.jar;lib/junit-4.12.jar;bin/ TestRunner
2) Watch the test cases pass


===Compiling===
****On windows command prompt***
-Navigate to the NIS folder where the java files are stored.
-Compile the src files from the NIS folder : 
 javac -d bin/ -cp lib/hamcrest-cor-1.3.jar;lib/junit-4.12.jar;bin/ src/*.java
-Compile the test files using from the NIS folder :
 javac -d bin/ -cp lib/hamcrest-cor-1.3.jar;lib/junit-4.12.jar;bin/ test/*.java
*Class files have been provided to allow for easy running.

===File structure===
-NIS
README.txt
--src
  Client.java
  Hash.java
  Server.java
  rsaGenerator.java
---client
   public.key
   private.key
---server
   public.key
   private.key
--lib
  hamcrest-core-1.3.jar
  junit-4.12.jar
--test
  BaseTest.java
  JUnitTestSuite.jav
  TestClient.java
  TestHash.java
  TestRSA.java
  TestRunner.java
  TestServer.java
--bin
  (All class files)

===File descriptions===
-Client.java : Client side program. This is to be run after the server is running. It connects to the local host server on port 3000, gets the message from the user input, encrypts the message and finally sends it to the server.
-Hash.java : This implents the SHA3-256 hashing algorithm used in Client and Server
-Server.java : Server side program. This needs to be run first. It creates an available connection point on port 3000 and waits for a client to connect. Once the connected client has sent its message, it decrypts the message and only displays it if the message is authenticated.
-rsaGenerator.java : This has methods implented for dealing with the RSA encryption that is used in the Client and Server programs. 












