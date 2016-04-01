package views;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import dataAccessLayer.GameDataAccess;
import dataAccessLayer.PlayerDataAccess;
import model.Game;
import model.Player;

@ManagedBean(name = "playersView")
@SessionScoped
public class PlayersView implements Serializable{

	private static final long serialVersionUID = 1L;
	
	 @ManagedProperty("#{playerDataAccess}")
	 public PlayerDataAccess playerData;
	 
	 public List<Player> players;
	 
    public PlayerDataAccess getPlayerData() {
		return playerData;
	}

	public void setPlayerData(PlayerDataAccess playerData) {
		this.playerData = playerData;
	}
    
    private Player selectedPlayer;

	public List<Player> getPlayers() {
		return this.players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public Player getSelectedPlayer() {
		return selectedPlayer;
	}

	public void setSelectedPlayer(Player selectedPlayer) {
		this.selectedPlayer = selectedPlayer;
	}
	
	public void remove(Player player) {
		System.out.println("Remove player "+player.getId());
		PlayerDataAccess gda=new PlayerDataAccess();
		gda.removePlayer(player.getId());
		players.remove(player);
	}
	
	public void  redirectToChart(Player player)
	{
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		ChartView firstBean = (ChartView) elContext.getELResolver().getValue(elContext, null, "chartView");
		
		firstBean.addPlayerToChart(player);
		System.out.println("Players from ChartView");
		for(Player p:firstBean.getPlayers())
		{
			System.out.println(p.toString());
		}
		
		firstBean.createLineModels();
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

			try {
				context.redirect(context.getRequestContextPath() + "/faces/resources/userchart.xhtml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		System.out.println("Hello!");
	}
	
	@PostConstruct
	public void init() {
		System.out.println("Hello from players init");
		this.players=new ArrayList<Player>();
		this.playerData=new PlayerDataAccess();
	    this.players=this.playerData.listPlayers();
	    
	}
	
	

}