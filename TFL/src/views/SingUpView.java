package views;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import dataAccessLayer.PlayerDataAccess;

@ManagedBean(name="singUpView")
public class SingUpView {
	
	private String name;
    
    private String pass;
    
    private String cpass;
    
    private boolean available;
    
    public void setCpass(String value)
    {
    	this.cpass=value;
    }
    
    public String getCpass()
    {
    	return this.cpass;
    }
    
    public void setAvailable(boolean value)
    {
    	this.available=value;
    }
    
    public boolean getAvailable()
    {
    	return this.available;
    }
    
    public String getName() {
        return this.name;
    }
 
    public void setName(String name) {
        this.name =name;
    }
 
    public String getPass() {
        return this.pass;
    }
 
    public void setPass(String value) {
        this.pass = value;
    }
     
    public void save() {
    	PlayerDataAccess pda=new PlayerDataAccess();
    	pda.createUser(this.name, this.pass, 1, this.available, 0.0);
    	
    	System.out.println(this.available+ " "+this.name+" "+this.pass);
    	System.out.println("Data saved!");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Data Saved"));
    }

	
}
