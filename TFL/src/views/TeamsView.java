package views;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import dataAccessLayer.GameDataAccess;
import dataAccessLayer.TeamDataAccess;
import helpers.PlayerHelper;
import helpers.TeamGenerator;
import model.Game;
import model.Player;
import model.Team;

@ManagedBean(name = "teamsView")
@SessionScoped
public class TeamsView implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	public int indexOfMap=0;
	private boolean existTeams;
	
	private Team firstTeam;
	private Team secondTeam;
	
	public Team getFirstTeam() {
		return firstTeam;
	}

	public void setFirstTeam(Team firstTeam) {
		this.firstTeam = firstTeam;
	}

	public Team getSecondTeam() {
		return secondTeam;
	}

	public void setSecondTeam(Team secondTeam) {
		this.secondTeam = secondTeam;
	}

	private boolean showNextPrevious;
	
	public boolean isShowNextPrevious() {
		return showNextPrevious;
	}

	public void setShowNextPrevious(boolean showNextPrevious) {
		this.showNextPrevious = showNextPrevious;
	}

	public boolean isExistTeams() {
		return this.existTeams;
	}

	public void setExistTeams(boolean existTeams) {
		this.existTeams = existTeams;
	}
	
	public Map<String, List<List<Player>>> map = new HashMap<String, List<List<Player>>>();

	private DualListModel<Player> players;

	@PostConstruct
	public void init() {
		if(players==null)
		{
			players=new DualListModel<Player>();
		}	
		this.firstTeam=new Team("Team name");
		this.secondTeam=new Team("Team name");
	}

	public DualListModel<Player> getPlayers() {
		return players;
	}

	public void setPlayers(DualListModel<Player> themes) {
		this.players = themes;
	}

	public void onTransfer(TransferEvent event) {
		
		 System.out.println("Source:");
		this.firstTeam.setPlayers( this.players.getSource());
		
		 for(Player p: this.firstTeam.getPlayers())
		 {
		 System.out.println(p.getUsername());
		 }

		System.out.println("Target:");
		this.secondTeam.setPlayers( this.players.getTarget());

		 for(Player p: this.secondTeam.getPlayers())
		 {
		 System.out.println(p.getUsername());
		 }
	}

	 public void getNextTeam(ActionEvent actionEvent) {
			System.out.println("Next Team");
			System.out.println("Index of map:"+indexOfMap);
			System.out.println("Inex to comapre: "+(TeamGenerator.map.size()-1));
			if(indexOfMap==(TeamGenerator.map.size()-1))
			{
				indexOfMap=0;
				System.out.println("new Index of map:"+indexOfMap);
			}
			else
			{
				indexOfMap=indexOfMap+1;
				System.out.println("new Index of map:"+indexOfMap);
			}
			Object key = TeamGenerator.map.keySet().toArray(new Object[TeamGenerator.map.size()])[indexOfMap];
			
			List<Player> allPlayers=new ArrayList<Player>();
			allPlayers.addAll(this.firstTeam.getPlayers());
			allPlayers.addAll(this.secondTeam.getPlayers());
			
			List<Player> firstList = TeamGenerator.map.get(key);
			System.out.println("First  list:");
			for(Player p:firstList)
			{
				System.out.println(p.getUsername());
			}
				
			this.firstTeam.setPlayers(firstList);
					
			List<Player> secondList = new ArrayList<Player>();

			for(Player p:allPlayers)
			{
				if(PlayerHelper.ExistsInList(p, firstList)==false)
				{
					secondList.add(p);
				}	
			}
			this.secondTeam.setPlayers(secondList);
			
			System.out.println("SecondList list:");
			
			for(Player p:secondList)
			{
				System.out.println(p.getUsername());
			}
			
			players=new DualListModel<>(this.firstTeam.getPlayers(),this.secondTeam.getPlayers());
	    }
	 	 
	 public void getPreviousTeam(ActionEvent actionEvent) {
			System.out.println("Previous Team");
			System.out.println("Index of map:"+indexOfMap);
			System.out.println("Inex to comapre: "+(TeamGenerator.map.size()-1));
			if(indexOfMap==0)
			{
				indexOfMap=9;
				System.out.println("new Index of map:"+indexOfMap);
			}
			else
			{
				indexOfMap=indexOfMap-1;
				System.out.println("new Index of map:"+indexOfMap);
			}
			Object key = TeamGenerator.map.keySet().toArray(new Object[TeamGenerator.map.size()])[indexOfMap];
			
			List<Player> allPlayers=new ArrayList<Player>();
			allPlayers.addAll(this.firstTeam.getPlayers());
			allPlayers.addAll(this.secondTeam.getPlayers());
			
			List<Player> firstList = TeamGenerator.map.get(key);
			System.out.println("First  list:");
			for(Player p:firstList)
			{
				System.out.println(p.getUsername());
			}
				
			this.firstTeam.setPlayers(firstList);
			
			List<Player> secondList = new ArrayList<Player>();

			for(Player p:allPlayers)
			{
				if(PlayerHelper.ExistsInList(p, firstList)==false)
				{
					secondList.add(p);
				}				
			}
			this.secondTeam.setPlayers(secondList);
			
			System.out.println("SecondList list:");
			
			for(Player p:secondList)
			{
				System.out.println(p.getUsername());
			}
			
			players=new DualListModel<>(this.firstTeam.getPlayers(),this.secondTeam.getPlayers());
	    }

	 public void addMessage()
	 {
		 System.out.println("Add message!");
		 System.out.println("Team is:"+this.secondTeam.getWinner());
	 }
	 
	 public void saveTeams() {
		   
		System.out.println("Hello from save teams!");
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
     	NextGamesView firstBean = (NextGamesView) elContext.getELResolver().getValue(elContext, null, "nextGamesView");
     	
     	Game game=firstBean.getSelectedGame();

     	if(game.getTeam1()!=null && game.getTeam2()!=null)
     	{
     		System.out.println("Game already has teams!Update teams!");
     		game.setTeam1(this.firstTeam);
     		game.setTeam2(this.secondTeam);
         	firstBean.setSelectedGame(GameDataAccess.UpdateTeams(game));
     	}
     	else
     	{
     		System.out.println("Game has no teams yet! Add teams first!");
     		firstTeam=TeamDataAccess.CreateNewTeam(firstTeam);
     		System.out.println("FirstTeam: "+firstTeam.getName());
     		for(Player p:firstTeam.getPlayers())
     		{
     			System.out.println(p.toString());
     		}
     		secondTeam=TeamDataAccess.CreateNewTeam(secondTeam);
     		
     		System.out.println("SecondTeam: "+secondTeam.getName());
     		for(Player p:secondTeam.getPlayers())
     		{
     			System.out.println(p.toString());
     		}
     		
     		firstBean.setSelectedGame(GameDataAccess.AddTeams(game.getId(), firstTeam.getId(), secondTeam.getId()));	
     		}
	 }
}