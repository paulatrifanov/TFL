package views;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import dataAccessLayer.PlayerDataAccess;
import model.Player;

@ManagedBean(name = "loginView")
@SessionScoped
public class LoginView implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
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
    	currentPlayer=new Player();
    }
    
    public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public String getUsername() {
	return this.username;

    }

    public void setUsername(String value) {
	this.username = value;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String value) {
	this.password = value;
    }

	public String login() throws IOException {

		PlayerDataAccess pda = new PlayerDataAccess();
		if ((this.username != null) && (this.password != null)) {
			currentPlayer = new Player();
			currentPlayer = pda.loginUser(this.username, this.password);

			if (currentPlayer != null) {
				this.playedGames = this.currentPlayer.getGamePlayers().size();
				this.winner = this.currentPlayer.getGameWinners().size();
				this.looser = this.currentPlayer.getGameLosers().size();
				
				this.currentPlayer.setAvailable(false);
				System.out.println("Available from login:"+this.currentPlayer.getAvailable());
				if (currentPlayer.getType() == null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Warning!", "This username does not have rights!"));
					return "/index";
				} else {
					if (currentPlayer.getType() == 1) {
						ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
						context.redirect(context.getRequestContextPath() + "/faces/resources/user.xhtml");
					} else {
						ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
						context.redirect(context.getRequestContextPath() + "/faces/resources/adminuser.xhtml");
					}
				}
			}
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!", "Incorrect username or password!"));
		return "/index";
	}
	
	public void buttonAction(ActionEvent actionEvent) {
		System.out.println("Hello from logout!");
		this.username=null;
		this.password=null;
		this.currentPlayer=new Player();
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		try {
			context.redirect(context.getRequestContextPath() + "/faces/index.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	  public void changePassword() {
		  if(this.oldPass.compareTo(this.currentPlayer.getPassword())==0)
		  {
			  System.out.println("Old password is correct!");
			  if(this.newPass.compareTo(this.confirmPass)==0)
			  {
				  PlayerDataAccess pda=new PlayerDataAccess();
				  pda.changePasswordForPlayer(this.currentPlayer.getId(), this.newPass);
			  }
		  }
		  else
		  {
			  System.out.println("Old password does not match!");
		  }
	}
	  
	  public void addMessage() {
		 	if(this.currentPlayer.getAvailable()==true)
		 	{
		 		System.out.println("Available will be set false");
		 		PlayerDataAccess pda=new PlayerDataAccess();
		 		this.currentPlayer=pda.changeAvailable(this.currentPlayer, false);
		 		System.out.println("Available:"+this.currentPlayer.getAvailable());
		 	}
		 	else
		 		
		 	{
		 		if(this.currentPlayer.getAvailable()==false)
		 		{
		 			PlayerDataAccess pda=new PlayerDataAccess();
					this.currentPlayer=pda.changeAvailable(this.currentPlayer, true);
			 		System.out.println("Availabble will be set true");
			 		System.out.println("Available:"+this.currentPlayer.getAvailable());
		 		}
		 	}
	        String summary = this.currentPlayer.getAvailable() ? "Checked" : "Unchecked";
	        System.out.println("Available from add message:"+this.currentPlayer.getAvailable());
	        
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
	    }
    
    
}
