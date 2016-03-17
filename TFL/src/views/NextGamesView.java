package views;
import model.Game;
import model.Player;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import dataAccessLayer.GameDataAccess;


@ManagedBean(name = "nextGamesView")
@ViewScoped
public class NextGamesView implements Serializable{

	 private static final long serialVersionUID = 1L;
	    public static List<Game> games;
	    
	    @ManagedProperty("#{gameDataAccess}")
	    public GameDataAccess gamesData;
	    
	    private Game selectedGame;

//		@PostConstruct
//	    public void init() {
//	    	gamesData=new GameDataAccess();
//	    	//games=gamesData.listNextGames();
//	    	selectedGame=new Game();
//	    }
//	    
	    public List<Game> getGames() {
	        return games;
	    }
	    
	    public void setGames(List<Game> game) {
	        games=game;
	    }
	 
		public GameDataAccess getGamesData() {
			return gamesData;
		}

		public void setGamesData(GameDataAccess gamesData) {
			this.gamesData = gamesData;
		}
		
		public Game getSelectedGame() {
	        return selectedGame;
	    }
	 
		public void setSelectedGame(Game selectedGame) {
			this.selectedGame = selectedGame;
		}
		
		public void play(ActionEvent actionEvent) {
	        System.out.println("Welcome to Primefaces!!");
	    }
		
		public void remove(Game game) {
			System.out.println("Remove!");
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			LoginView firstBean = (LoginView) elContext.getELResolver().getValue(elContext, null, "loginView");
			//System.out.println("Done");
			//System.out.println(firstBean.getCurrentPlayer().getId());
			GameDataAccess gda=new GameDataAccess();
			gda.playGame(game, firstBean.getCurrentPlayer());
			System.out.println(firstBean.getCurrentPlayer().getUsername()+" is plaing on "+game.getDate());
//		    try {
//		        //actorService.remove(actor);
//		        //actorList = actorService.searchAll();
//		    } catch (Exception e) {
//		        e.printStackTrace();
//		    }
			System.out.println("Done");
		}
}
