package views;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
 
import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DualListModel;

import dataAccessLayer.EntityManagerHelper;
import dataAccessLayer.GameDataAccess;
import dataAccessLayer.PlayerDataAccess;
import model.Game;
import model.Player;
import model.Team;
import model.TeamPlayer;

@ManagedBean(name = "teamsView")
@ViewScoped
public class TeamsView implements Serializable{

	     
	    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		@ManagedProperty("#{playerDataAccess}")
	    private PlayerDataAccess service;

		private boolean existTeams;
		
		public boolean isExistTeams() {
			return existTeams;
		}

		public void setExistTeams(boolean existTeams) {
			this.existTeams = existTeams;
		}

		public List<Player> themesSource=new ArrayList<Player>();
	  	
	    public List<Player> themesTarget = new ArrayList<Player>();
	    
	    private DualListModel<Player> players;
	     
	    private Team teamOne;
	    
	    private Team teamTwo;
	    
	    private Game selectedGame;
	    
	    public Game getSelectedGame() {
			return selectedGame;
		}

		public void setSelectedGame(Game selectedGame) {
			this.selectedGame = selectedGame;
		}

		public Team getTeamOne() {
			return teamOne;
		}

		public void setTeamOne(Team teamOne) {
			this.teamOne = teamOne;
		}

		public Team getTeamTwo() {
			return teamTwo;
		}

		public void setTeamTwo(Team teamTwo) {
			this.teamTwo = teamTwo;
		}

		@PostConstruct
	    public void init() {

			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			NextGamesView firstBean = (NextGamesView) elContext.getELResolver().getValue(elContext, null, "nextGamesView");
			if(firstBean.getSelectedGame()!=null)
			{
				System.out.println("There is a selected game");
				System.out.println(firstBean.getSelectedGame().getDate());
				this.selectedGame=firstBean.getSelectedGame();
				Game g=new Game();
				g=EntityManagerHelper.em.find(Game.class, this.selectedGame.getId());
				if(g!=null)
				{
					GameDataAccess gda=new GameDataAccess();
					
					List<Team> list=gda.listGameTeams(g);
					if(list!=null)
					{
						if(list.size()!=0)
						{
							this.existTeams=true;
							this.setTeamOne(list.get(0));
							System.out.println("Team one players:");
							for(TeamPlayer player: this.teamOne.getTeamPlayers())
							{
								this.themesSource.add(player.getPlayer());
							}
							this.setTeamTwo(list.get(1)); 
							System.out.println("Second team players:");
							for(TeamPlayer player: this.teamTwo.getTeamPlayers())
							{
								this.themesTarget.add(player.getPlayer());
								System.out.println(player.getPlayer().getUsername());
							}
						}
						else
						{
							this.existTeams=false;
						}
					}
				}
				players=new DualListModel<>(themesSource, themesTarget);
			}
			else
			{
				System.out.println("No selected game!");
				this.existTeams=false;
			}
	    }

	    public PlayerDataAccess getService() {
	        return service;
	    }
	 
	    public void setService(PlayerDataAccess service) {
	        this.service = service;
	    }
	 
	    public DualListModel<Player> getPlayers() {
	        return players;
	    }
	 
	    public void setPlayers(DualListModel<Player> themes) {
	        this.players = themes;
	    }
	     
	    public void onTransfer(TransferEvent event) {
	        StringBuilder builder = new StringBuilder();
	        for(Object item : event.getItems()) {
	            builder.append(((Player) item).getUsername()).append("<br />");
	        }
	         
	        FacesMessage msg = new FacesMessage();
	        msg.setSeverity(FacesMessage.SEVERITY_INFO);
	        msg.setSummary("Items Transferred");
	        msg.setDetail(builder.toString());
	         
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	    } 
	 
	    public void onSelect(SelectEvent event) {
	        FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Selected", event.getObject().toString()));
	    }
	     
	    public void onUnselect(UnselectEvent event) {
	        FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Unselected", event.getObject().toString()));
	    }
	     
	    public void onReorder() {
	        FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "List Reordered", null));
	    } 
	
}