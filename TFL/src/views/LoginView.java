package views;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import dataAccessLayer.GameDataAccess;
import dataAccessLayer.PlayerDataAccess;
import model.Player;

@ManagedBean(name = "loginView")
@SessionScoped
public class LoginView implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int playedGames;
    private int winner;
    private int looser;
    private boolean value;

	public boolean getValue() {
        return value;
    }
 
    public void setValue(boolean value) {
        this.value = value;
    }
    
    public int getPlayedGames() {
		if(this.currentPlayer.getGames()!=null)
		{
			playedGames= this.currentPlayer.getGames().size();
		}
		return playedGames;
	}

	public void setPlayedGames(int playedGames) {
		this.playedGames = playedGames;
	}

	public int getWinner() {
		return winner;
	}
	
	public void setWinner(int winner) {
		this.winner = winner;
	}

	public int getLooser() {
		return looser;
	}

	public void setLooser(int looser) {
		this.looser = looser;
	}
	
	private String oldPass;
	private String newPass;
	private String confirmPass;
	
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

	private Player currentPlayer;
    
    @PostConstruct
    public void init() {
    	this.currentPlayer=new Player();
    }
    
    public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

    public String login() {
		if ((this.currentPlayer.getUsername() != null) && (this.currentPlayer.getPassword()!= null)) {
			currentPlayer = PlayerDataAccess.loginUser(this.currentPlayer.getUsername(), this.currentPlayer.getPassword());
			if(this.currentPlayer!=null)
			{
				RedirectView.Redirect(this.currentPlayer, "/faces/resources/userview.xhtml", "/faces/resources/userview.xhtml");
			}
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!", "Incorrect username or password!"));
		return "/index";
	}

	public void logout(ActionEvent event)  {
	    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	    RedirectView.Redirect(this.currentPlayer, "/faces/index.xhtml", "/faces/index.xhtml");
		System.out.println("Logout pressed!");
	}
	
	public void redirectToHome(ActionEvent event)
	{
		RedirectView.Redirect(this.currentPlayer, "/faces/resources/userview.xhtml", "/faces/resources/userview.xhtml");
	}
	
	public void redirectToNextGames(ActionEvent actionEvent)
	{
		ELContext context = FacesContext.getCurrentInstance().getELContext();
		NextGamesView firstBean = (NextGamesView) context.getELResolver().getValue(context, null, "nextGamesView");
		firstBean.setGames(GameDataAccess.listNextGames());	
		RedirectView.Redirect(this.currentPlayer, "/faces/resources/nextusergames.xhtml", "/faces/resources/nextadmingames.xhtml");
	}
	
	public void redirectToPersonalDates(ActionEvent actionEvent)
	{
		if(this.currentPlayer.getGames()!=null)
		{
			this.playedGames=this.currentPlayer.GetTotalPlayedGames();
		}
		if(this.currentPlayer.getTeams()!=null)
		{
			this.winner=this.currentPlayer.GetGames(true);
			this.looser=this.currentPlayer.GetGames(false);
		}
		RedirectView.Redirect(this.currentPlayer,"/faces/resources/viewpersonaldates.xhtml","/faces/resources/viewpersonaldates.xhtml");
	}
	
	public void redirectToChangePass(ActionEvent actionEvent)
	{
		this.oldPass="";
		this.newPass="";
		this.confirmPass="";
		RedirectView.Redirect(this.currentPlayer,"/faces/resources/changepassword.xhtml","/faces/resources/changepassword.xhtml");
	}
	
	public void redirectToCharts(ActionEvent actionEvent)
	{
		System.out.println("Hello from redirect to charts!");
		ELContext context = FacesContext.getCurrentInstance().getELContext();
		ChartView firstBean = (ChartView) context.getELResolver().getValue(context, null, "chartView");
		firstBean.createLineModels();
		RedirectView.Redirect(this.currentPlayer, "/faces/resources/userchart.xhtml",  "/faces/resources/userchart.xhtml");	
	}
	
	public void redirectToMyChart(ActionEvent actionEvent)
	{
		System.out.println("Hello from redirect to my chart!");
		ELContext context = FacesContext.getCurrentInstance().getELContext();
		ChartView firstBean = (ChartView) context.getELResolver().getValue(context, null, "chartView");
		firstBean.players=new ArrayList<Player>();
		firstBean.players.add(this.currentPlayer);
		firstBean.createLineModels();
		RedirectView.Redirect(this.currentPlayer, "/faces/resources/userchart.xhtml",  "/faces/resources/userchart.xhtml");	
	}
	
	public void redirectToPlayers(ActionEvent actionEvent)
	{
		System.out.println("Hello from redirect to players!");
		RedirectView.Redirect(this.currentPlayer, "/faces/resources/viewplayers.xhtml", "/faces/resources/adminplayersview.xhtml");	
	}
	
	 public void redirectToHistory(ActionEvent actionEvent)
	 {
	    ELContext context = FacesContext.getCurrentInstance().getELContext();
		NextGamesView firstBean = (NextGamesView) context.getELResolver().getValue(context, null, "nextGamesView");
		firstBean.setGames(GameDataAccess.listPreviousGames());	
		RedirectView.Redirect(this.currentPlayer, "/faces/resources/historyuser.xhtml", "/faces/resources/historyadmin.xhtml");
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