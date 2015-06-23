/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package incident;

import cims.Property;
import com.fasterxml.classmate.types.ResolvedPrimitiveType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.Callable;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspWriter;
import org.apache.jasper.runtime.JspWriterImpl;

/**
 *
 * @author Sasa2905
 */
public class SocketRunnable implements Runnable {

    String socketString;
    public SocketRunnable() {
        socketString = "";
    }
    @Override
    public void run() {
        try {
            String ipadress = Property.IPADRESS.getProperty();
            int port = Integer.valueOf(Property.IPPORT.getProperty());
            Socket socket = new Socket(ipadress, port);
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            socketString = (String) input.readObject();
            System.out.println(socketString);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    public String getString() {
        return socketString;
    }
}
