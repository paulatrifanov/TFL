package views;
import model.Game;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import dataAccessLayer.GameDataAccess;


@ManagedBean(name = "nextGamesView")
@ViewScoped
public class NextGamesView implements Serializable{

	 private static final long serialVersionUID = 1L;
	    public List<Game> games;
	    
	    @ManagedProperty("#{gameDataAccess}")
	    public GameDataAccess gamesData;
	    
	    private Game selectedGame;

		@PostConstruct
	    public void init() {
	    	gamesData=new GameDataAccess();
	    	games=gamesData.listNextGames();
	    	selectedGame=new Game();
	    }
	    
	    public List<Game> getGames() {
	        return games;
	    }
	    
	    public void setCars(List<Game> game) {
	        this.games=game;
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
		
		public void getNextGames()
		{
			System.out.println("Get next games!");
		}
}
