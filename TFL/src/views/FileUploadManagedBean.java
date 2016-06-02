package views;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.print.DocFlavor.URL;

import org.primefaces.model.UploadedFile;
 
@ManagedBean
@SessionScoped
public class FileUploadManagedBean {
	 private UploadedFile file;
	 
	    public UploadedFile getFile() {
	        return file;
	    }
	 
	    public void setFile(UploadedFile file) {
	        this.file = file;
	    }
	     
	    public void upload() {
	    	System.out.println("upload");
	        if(file != null) {
	            FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
	            try {
					System.out.println(file.getInputstream());
					copyFile(file.getFileName(), file.getInputstream());
					ELContext elContext = FacesContext.getCurrentInstance().getELContext();
					LoginView firstBean = (LoginView) elContext.getELResolver().getValue(elContext, null, "loginView");
					System.out.println(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath());
					
					firstBean.getCurrentPlayer().setPicture("D:\\Code\\TFL\\TFL\\WebContent\\images\\"+file.getFileName());
					System.out.println(firstBean.getCurrentPlayer().getPicture());
					
				} catch (IOException e) {
					e.printStackTrace();
				}
	            FacesContext.getCurrentInstance().addMessage(null, message);
	        }
	    }

	    public void copyFile(String fileName, InputStream in) {
	           try {
	        	    String destination="D:\\Code\\TFL\\TFL\\WebContent\\images\\";

	                OutputStream out = new FileOutputStream(new File(destination + fileName));
	              
	                int read = 0;
	                byte[] bytes = new byte[1024];
	              
	                while ((read = in.read(bytes)) != -1) {
	                    out.write(bytes, 0, read);
	                    System.out.println(bytes);
	                }
	              
	                in.close();
	                out.flush();
	                out.close();
	              
	                System.out.println("New file created!");
	                } catch (IOException e) {
	                System.out.println(e.getMessage());
	                }
	    }
	    
	    public void saveFileToDatabase(String fileName, InputStream in) {
	           try {
	        	    String destination="D:\\tmp\\";
	        	   
	                OutputStream out = new FileOutputStream(new File(destination + fileName));
	              
	                int read = 0;
	                byte[] bytes = new byte[1024];
	              
	                while ((read = in.read(bytes)) != -1) {
	                    out.write(bytes, 0, read);
	                    System.out.println(bytes);
	                }
	              
	                in.close();
	                out.flush();
	                out.close();
	              
	                System.out.println("New file created!");
	                } catch (IOException e) {
	                System.out.println(e.getMessage());
	                }
	    }
}