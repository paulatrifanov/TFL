package views;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;
import dataAccessLayer.PlayerDataAccess;
import helpers.PlayerHelper;
import helpers.TeamGenerator;
import model.Player;
	 
	@ManagedBean
	public class AutoCompleteView {
	    
	    public List<Player> selectedPlayers;
	    private List<Player> allPlayers;
	    
	    @PostConstruct
		public void init() {
			this.selectedPlayers=new ArrayList<Player>();
			allPlayers=PlayerDataAccess.ListAllPlayers();
		}
	    
	    public List<Player> completeTheme(String query) {
	        List<Player> filteredPlayers = new ArrayList<Player>();
	         
	        for(Player p:allPlayers)
	        {
	        	if(p.getUsername().toLowerCase().startsWith(query))
	        	{
	        		filteredPlayers.add(p);	
	        	}
	        }	     
	        return  filteredPlayers;
	    }
	     
	    public List<Player> getSelectedPlayers() {
			return selectedPlayers;
		}

		public void setSelectedPlayers(List<Player> selectedPlayers) {
			this.selectedPlayers = selectedPlayers;
		}

		public void onItemSelect(SelectEvent event) {
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Item Selected", event.getObject().toString()));
	    }
		
		public void addPlayers()
		{
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			TeamsView firstBean = (TeamsView) elContext.getELResolver().getValue(elContext, null, "teamsView");
			List<Player> allPlayers=new ArrayList<Player>();
			allPlayers.addAll(firstBean.getFirstTeam().getPlayers());
			allPlayers.addAll(firstBean.getSecondTeam().getPlayers());

			for(Player play:this.selectedPlayers)
			{
				if(PlayerHelper.ExistsInList(play, allPlayers)==false)
				{
					allPlayers.add(play);
				}
			}
			this.selectedPlayers=new ArrayList<Player>();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO!", "New players added to this game!"));
			if(allPlayers.size()<=3)
			{
				firstBean.getFirstTeam().setPlayers(allPlayers);
			}
			else
			{
				firstBean.setShowNextPrevious(true);
				List<List<Player>> listed=generateTeams(allPlayers);
				for(List<Player> l:listed)
				{
					System.out.println("Team");
					for(Player p:l)
					{
						System.out.println(p.toString());
					}
				}
				firstBean.getFirstTeam().setPlayers(listed.get(0));
				firstBean.getSecondTeam().setPlayers(listed.get(1));
			}
			firstBean.setPlayers( new DualListModel<>(firstBean.getFirstTeam().getPlayers(), firstBean.getSecondTeam().getPlayers()));
			this.selectedPlayers=new ArrayList<Player>();
		}
		
		public List<List<Player>> generateTeams(List<Player> players)
		{	
			List<List<Player>> list=new ArrayList<List<Player>>();
			TeamGenerator.list=players;
			TeamGenerator.generateTeams();
			TeamGenerator.printMap(TeamGenerator.map);
			Object key = TeamGenerator.map.keySet().toArray(new Object[TeamGenerator.map.size()])[0];
			
			List<Player> firstList = TeamGenerator.map.get(key);
			list.add(firstList);
			
			System.out.println("First team:");
			for(Player p:firstList)
			{
				System.out.println(p.getUsername());
			}
			
			List<Player> secondList = new ArrayList<Player>();

			for(Player p:players)
			{
				if(PlayerHelper.ExistsInList(p, firstList)==false)
				{
					secondList.add(p);
				}
			}
			System.out.println("Second list:");
			for(Player p:secondList)
			{
				System.out.println(p.getUsername());
			}
			list.add(secondList);
			return list;
		}
}