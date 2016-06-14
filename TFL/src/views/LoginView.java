package views;

import java.io.Serializable;
import java.util.ArrayList;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import dataAccessLayer.GameDataAccess;
import dataAccessLayer.PlayerDataAccess;
import helpers.RedirectView;
import model.Player;

@ManagedBean(name = "loginView")
@SessionScoped
public class LoginView implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String oldPass;
	private String newPass;
	private String confirmPass;
    private boolean value;
    private Player currentPlayer=new Player();
    
	public boolean getValue() {
        return value;
    }
 
    public void setValue(boolean value) {
        this.value = value;
    }
    	
	public String getOldPass() {
		return oldPass;
	}
	
	public void setOldPass(String oldPass) {
		this.oldPass = oldPass;
	}
	
	public String getNewPass() {
		return newPass;
	}
	
	public void setNewPass(String newPass) {
		this.newPass = newPass;
	}
	
	public String getConfirmPass() {
		return confirmPass;
	}
	
	public void setConfirmPass(String confirmPass) {
		this.confirmPass = confirmPass;
	}
 
    public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

    public void login() {
		int canLogin=PlayerDataAccess.loginUser(this.currentPlayer.getUsername(), this.currentPlayer.getPassword());
		if(canLogin!=0)
		{
			this.currentPlayer=PlayerDataAccess.getPlayerWithID(canLogin);
			RedirectView.Redirect("/resources/userview.xhtml");
		}
		else
		{
			FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!", "Incorrect username or password!"));
			
		}	
	}

	public void logout(ActionEvent event)  {
	    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	    RedirectView.Redirect("/index.xhtml");
	}
	
	public void redirectToHome(ActionEvent event)
	{
		RedirectView.Redirect("/resources/userview.xhtml");
	}
	
	public void redirectToNextGames(ActionEvent actionEvent)
	{
		ELContext context = FacesContext.getCurrentInstance().getELContext();
		NextGamesView firstBean = (NextGamesView) context.getELResolver().getValue(context, null, "nextGamesView");
		firstBean.setGames(GameDataAccess.listNextGames());	
		RedirectView.Redirect(this.currentPlayer, "/resources/nextusergames.xhtml", "/resources/nextadmingames.xhtml");
	}
	
	public void redirectToPersonalInfo(ActionEvent actionEvent)
	{
		new PersonalInformationView(this.currentPlayer);
		RedirectView.Redirect("/resources/viewpersonalinfo.xhtml");
	}
	
	public void redirectToChangePass(ActionEvent actionEvent)
	{
		this.oldPass="";
		this.newPass="";
		this.confirmPass="";
		RedirectView.Redirect("/resources/changepassword.xhtml");
	}
	
	public void redirectToCharts(ActionEvent actionEvent)
	{
		ELContext context = FacesContext.getCurrentInstance().getELContext();
		ChartView firstBean = (ChartView) context.getELResolver().getValue(context, null, "chartView");
		firstBean.createLineModels();
		RedirectView.Redirect("/resources/userchart.xhtml");	
	}
	
	public void redirectToMyChart(ActionEvent actionEvent)
	{
		ELContext context = FacesContext.getCurrentInstance().getELContext();
		ChartView firstBean = (ChartView) context.getELResolver().getValue(context, null, "chartView");
		firstBean.players=new ArrayList<Player>();
		firstBean.players.add(this.currentPlayer);
		firstBean.createLineModels();
		RedirectView.Redirect("/resources/userchart.xhtml");	
	}
	
	public void redirectToPlayers(ActionEvent actionEvent)
	{
		System.out.println("Hello from redirect to players!");
		RedirectView.Redirect(this.currentPlayer, "/resources/viewplayers.xhtml", "/resources/adminplayersview.xhtml");	
	}
	
	 public void redirectToHistory(ActionEvent actionEvent)
	 {
	    ELContext context = FacesContext.getCurrentInstance().getELContext();
		NextGamesView firstBean = (NextGamesView) context.getELResolver().getValue(context, null, "nextGamesView");
		firstBean.setGames(GameDataAccess.listPreviousGames());	
		RedirectView.Redirect(this.currentPlayer, "/resources/historyuser.xhtml", "/resources/historyadmin.xhtml");
	 }
	
	  public void changePassword() {
		  if(this.oldPass.compareTo(this.currentPlayer.getPassword())==0)
		  {
			  if(this.newPass.compareTo(this.confirmPass)==0)
			  {
				  PlayerDataAccess.updatePassword(this.currentPlayer.getId(),this.newPass);
				  FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO", "Succesfully saved new password!"));
			  }
			  else
			  {
				  FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn!", "New password does not match!"));  
			  }
		  }
		  else
		  {
			  FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn!", "Wrong old password!"));
		  }
	}
	  
	  public void changeAvailability() {
			PlayerDataAccess.changeAvailable(this.currentPlayer);
			if(this.currentPlayer.getAvailable())
			{
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO", "You are now available!"));
			}
			else
			{
				FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO", "From now you are unavailable!"));		
			}
	  }  
}