/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package incident;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.net.ftp.FTPClient;
import javax.servlet.jsp.PageContext;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

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
        boolean isMultipartContent = ServletFileUpload.isMultipartContent(request);
        if (!isMultipartContent)
        {
            out.println("You are not trying to upload<br />");
            return;
        }

        out.println("You are trying to upload <br />");
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        try
        {
            List<FileItem> fields = upload.parseRequest(request);
            out.println("Number of fields: " + fields.size() + "<br/><br/>");
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
                            String path = "images/";
                            String filename = path + fileItem.getName();
                            client.storeFile(filename, is);
                            client.logout();
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
    }

}
