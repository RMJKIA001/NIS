import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;


public class Client {

    // I/O stuff
    private ObjectInputStream sInput;
    private ObjectOutputStream sOutput;
    private Socket socket;
    private String server, username;
    private int port;
    private ServerListener sl;
    // Constructor for client
    public Client(String server, int port, String username)
    {
        this.server = server;  
        this.port = port;
        this.username = username;
    }

    // Starts the client and dialog with server
    public boolean start(){
        try 
        {
            socket = new Socket(server, port);
        } catch (IOException e) {
            display("Error connecting to server: ");
            return false;
        }
        String msg = "Connection accepted " + socket.getInetAddress() + ": " + socket.getPort();
        display(msg);
        msg = "- Use command 'LOGOUT' to logout of chat server. ";
        display(msg);
        // Create data streams
        try {
            sInput = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream((socket.getOutputStream()));
        } catch (IOException e) {
            display("Exception in creating new streams");
            return false;
        }
        try {
            String usernames = (String) sInput.readObject();
            System.out.println("Usernames being used at the moment are: " + usernames);
            Scanner userscan = new Scanner(System.in);
            while (usernames.contains(username))
            {
                System.out.println("Could you please choose a new username. This one has been taken.");
                System.out.print("New username: ");
                username = userscan.nextLine();
            }
            System.out.println("Username has been accepted");
        } catch (IOException | ClassNotFoundException e) {
        }

        // Create thread to listen from server
        sl = new ServerListener();
        sl.start();

        // Send username to server as String
        try 
        {
            sOutput.writeObject(username);
            System.out.println("Welcome to the server");
        } catch (IOException e) {
            display("Exception doing login");
            disconnect();
            return false;
        }

        // Success that client connected to server
        return true;
    }

    public void display(String msg){
        System.out.println(msg);
    }
    public void endSL()
    {
    //Ends the server Listener thread (Which caused the infinite loop)
    sl.setT(false);
    }
    public void disconnect()
    {    
        try 
        {
            if(sInput != null) sInput.close();
            if(sOutput != null) sOutput.close();
            if(socket != null) socket.close();
        }
        catch(IOException e) {} 
        System.exit(0);      //a bit of a cheat but this stops any errors from showing
    }
    //Send Message to server
    public void sendMessage(Message msg)
    {
        try 
        {
            sOutput.writeObject(msg);
        } catch (IOException e) {
            display("Exception in sending message to the server");
        }
    }
    public static void main (String[] args){
        //default values
        int portNumber = 1500;
        String serverAddress = "localhost";
        String userName = "HeWhoMustNotBeNamed";

        // get values from command line
        switch(args.length){
            case 3:
                serverAddress = args[2];
            case 2:
                portNumber = Integer.parseInt(args[1]);
            case 1:
                userName = args[0];
            case 0:
                break;
            default:
                System.out.println("Use command: java Client [username] [portNumber] " +
                        "[serverAddress]");
                return;
        }

        // create the Client object
        Client client = new Client(serverAddress,portNumber,userName);
       // System.out.println("DEBUG: Client created");
        // try to connect to server
        if (!client.start()){
           // System.out.println("DEBUG: Client did not start");
            return;
        }

        // get messages from the client
        Scanner scanner = new Scanner(System.in);
       // System.out.println("DEBUG: in scanner");
        //infinite loop getting messages
        while (true){
            System.out.print(">> ");
            String msg = scanner.nextLine();
            // sends message to server that client wants to logout
            if (msg.equalsIgnoreCase("LOGOUT"))
            {
                client.sendMessage(new Message(Message.LOGOUT, ""));
                client.endSL();
                break;
            } 
            else
            {
                client.sendMessage(new Message(Message.MESSAGE, msg));
            }
        }
        client.disconnect();
    }



    // Inner class that listens to new messages from the server
    class ServerListener extends Thread{
        boolean t = true;
        @Override
        public void run(){
            while (t){
                try {
                    String msg = (String) sInput.readObject();
                    // TODO: add later
                    if (msg.equalsIgnoreCase("DISCONNECT")){
                        display("User disconnected");
                        disconnect();
                    } else {
                        System.out.println(msg);
                        System.out.print("> ");
                    }
                } catch (IOException e) {
                    display("Connection to server is closed");
                    t=false;
                } catch (ClassNotFoundException e) {
                    //display("I am the issue");

                }
            }
        }
        public void setT(boolean n){
        t=n;}
    }
}

