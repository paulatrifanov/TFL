package views;
import model.Game;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import dataAccessLayer.GameDataAccess;

@ManagedBean(name = "nextGamesView")
@SessionScoped
public class NextGamesView implements Serializable{

	 private static final long serialVersionUID = 1L;
	    public static List<Game> games;
	    
	    @ManagedProperty("#{gameDataAccess}")
	    public GameDataAccess gamesData;
	    
	    private Game selectedGame;

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
		
		public void play(Game game) {
			System.out.println("PlayGame!");
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			LoginView firstBean = (LoginView) elContext.getELResolver().getValue(elContext, null, "loginView");
			GameDataAccess gda=new GameDataAccess();
			gda.playGame(game, firstBean.getCurrentPlayer());
			System.out.println("Done");
		}
		
		public void viewTeams(Game game)
		{
			this.setSelectedGame(game);
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();		
			try {
				context.redirect(context.getRequestContextPath() + "/faces/resources/teamsuser.xhtml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
}

