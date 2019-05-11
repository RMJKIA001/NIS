import java.io.Serializable;



/**
 * This class in a message object that will be sent to and from the server. This will make it easier logistically
 * for the client as they can query things from the server.
 * We can also had the picture functionality here.
 * The implements Serializable was recommended by Google
 */
public class Message implements Serializable{

    protected static final long serialVersionUID = 1112122200L;

    static final int  MESSAGE = 1, LOGOUT = 3;

    private final int type;
    private final String message;

    Message(int type, String message){
        this.type = type;
        this.message = message;
    }

    public int getType(){
        return type;
    }
    public String getMessage(){
        return message;
    }
    //TODO: add getter for pictures
}
