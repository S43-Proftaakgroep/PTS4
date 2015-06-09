/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package incident;

import cims.DatabaseManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.net.ftp.FTP;

/**
 *
 * @author Eric
 */
@WebServlet(name = "UploadFileFtp", urlPatterns =
{
    "/UploadFileFtp"
})
public class UploadFileFtp extends HttpServlet {

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        int incidentId = -1;
        String message = "";
        boolean succes = false;
        String nameOfFile = "";
        boolean isMultipartContent = ServletFileUpload.isMultipartContent(request);
        if (!isMultipartContent)
        {
            out.println("You are not trying to upload");
            return;
        }

        out.println("You are trying to upload");
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        try
        {
            List<FileItem> fields = upload.parseRequest(request);
            out.println("Number of fields: " + fields.size());
            Iterator<FileItem> it = fields.iterator();
            if (!it.hasNext())
            {
                out.println("No fileds found");
                return;
            }
            while (it.hasNext())
            {
                FileItem fileItem = it.next();
                boolean isFormField = fileItem.isFormField();
                if (isFormField)
                {
                    out.println("FieldName: " + fileItem.getFieldName() + ", Fileitem String: " + fileItem.getString());
                    if (fileItem.getFieldName().equals("incidentId"))
                    {
                        incidentId = Integer.parseInt(fileItem.getString());
                    }
                    else if (fileItem.getFieldName().equals("message"))
                    {
                        message = fileItem.getString();
                    }
                }
                else
                {
                    out.println("\n \n Name: " + fileItem.getName()
                            + "\n \n Content Type: " + fileItem.getContentType()
                            + "\n \n Size (Bytes): " + fileItem.getSize()
                            + "\n \n To String: " + fileItem.toString());

                    if (fileItem.getContentType().equals("image/jpeg") || fileItem.getContentType().equals("image/png"))
                    {
                        InputStream is = fileItem.getInputStream();
                        FTPClient client = new FTPClient();
                        try
                        {
                            client.connect("a-chan.nl");
                            client.login("cims@a-chan.nl", "1234");
                            client.setFileType(FTP.BINARY_FILE_TYPE);
                            String path = "images/";
                            String filename = path + fileItem.getName();
                            client.storeFile(filename, is);
                            client.logout();
                            succes = true;
                            nameOfFile = filename;
                        }
                        catch (Exception ex)
                        {
                            //Error handling
                            out.println("\n \n \n File upload 1 error! \n \n \n");
                        } finally
                        {
                            try
                            {
                                if (is != null)
                                {
                                    is.close();
                                }
                                client.disconnect();
                            }
                            catch (Exception ex)
                            {
                                //Error handling
                                out.println("\n \n \n File upload 2 error! \n \n \n");
                            }
                        }
                    }
                }
            }
        }
        catch (FileUploadException ex)
        {
            ex.printStackTrace();
        }

        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        if (succes && incidentId != -1 && !nameOfFile.equals(""))
        {
            //Voeg filename toe aan bijbehorend incident in de database.

            if (DatabaseManager.addFileNameToIncident(nameOfFile, incidentId))
            {
                //Gelukt met toevoegen aan de database.
                out.println("File upload gelukt!");
            }
            else
            {
                //Niet gelukt om het toe te voegen aan de database.
                out.println("File upload niet gelukt!");
            }
        }
        else
        {
            out.println("Geen bestand geselecteerd!");
        }
        
        response.setHeader("Refresh", "5; index.jsp");
    }

}
