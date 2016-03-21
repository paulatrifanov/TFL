package dataAccessLayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.TypedQuery;

import model.Game;
import model.GameLoser;
import model.GamePlayer;
import model.GameWinner;
import model.Player;
import model.Team;

@ManagedBean(name = "gameDataAccess")
@ApplicationScoped
public class GameDataAccess implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	public GameDataAccess() {
		if (EntityManagerHelper.em.getTransaction().isActive() == false) {
			EntityManagerHelper.em.getTransaction().begin();
		}
	}

	public List<Game> listPreviousGames() {
		System.out.println("List previous games!");
		TypedQuery<Game> query =EntityManagerHelper.em.createQuery("SELECT g FROM Game g where g.date < current_date", Game.class);
		List<Game> result = new ArrayList<Game>();
		result = query.getResultList();

		for (Game g : result) {
			System.out.println("ID" + g.getId() + " Date:" + g.getDate() + " Difference" + g.getDifference());
		}
		return result;
	}

	public List<Game> listGames() {
		TypedQuery<Game> query =EntityManagerHelper.em.createQuery("SELECT g FROM Game g", Game.class);
		List<Game> result = new ArrayList<Game>();
		result = query.getResultList();

		for (Game g : result) {
			System.out.println("ID" + g.getId() + " Date:" + g.getDate() + " Difference" + g.getDifference());
			System.out.println("Game players:");
			for (GamePlayer p : g.getGamePlayers()) {
				System.out.println(p.getPlayer().getUsername());
			}
			for (GameLoser gl : g.getGameLosers()) {
				System.out.println(gl.getPlayer().getUsername());
			}
			for (GameWinner gw : g.getGameWinners()) {
				System.out.println(gw.getPlayer().getUsername());
			}
		}
		return result;
	}

	
	public List<Game> listNextGames() {
		TypedQuery<Game> query = EntityManagerHelper.em.createQuery("SELECT g FROM Game g where g.date > current_date", Game.class);
		List<Game> result = new ArrayList<Game>();
		result = query.getResultList();

		for (Game g : result) {
			System.out.println("ID" + g.getId() + " Date:" + g.getDate() + " Difference" + g.getDifference());
		}
		return result;
	}

	
	public  List<Game> listGamesForPlayer(Player player) {

		//TypedQuery<Game> query = EntityManagerHelper.em.createQuery("SELECT g FROM Game g where g.date > current_date", Game.class);
		Player p=EntityManagerHelper.em.find(Player.class, player.getId());
		//System.out.println(p.toString());
		List<Game> result = new ArrayList<Game>();
		//result = query.getResultList();

		Date currentDate=new Date();
		currentDate=Calendar.getInstance().getTime();		
		for (GamePlayer gp : p.getGamePlayers()) {
			
			System.out.println("ID" + gp.getId()+"GameID: "+gp.getGame().getId()+"GameDate:" +gp.getGame().getDate());
			if(gp.getGame().getDate().after(currentDate))
			{
				result.add(gp.getGame());
			}
		}
		
		System.out.println("Games seted!");
		return result;
	}
	
	public void playGame(Game game, Player player) {		
		Player play =EntityManagerHelper.em.find(Player.class, player.getId());
		Game findGame=EntityManagerHelper.em.find(Game.class, game.getId());	
		GamePlayer gp=new GamePlayer();
		if(gp.isPlayingGame(play, findGame)==false)
		{
			gp.setGame(findGame);
			gp.setPlayer(player);		
			game.addGamePlayer(gp);
			player.addGamePlayer(gp);
			EntityManagerHelper.em.persist(gp);
			EntityManagerHelper.em.persist(findGame);
			EntityManagerHelper.em.persist(play);
			EntityManagerHelper.em.getTransaction().commit();
		}
	}
	
//	public static void main(String[] args) {
//		Game g=new Game();
//		g=EntityManagerHelper.em.find(Game.class, 1);
//		for(Team t:listGameTeams(g))
//		{
//			System.out.println(t.getName());
//		}
//		listGameTeams(g);
//	}

	public List<Team> listGameTeams(Game game)
	{
		Game g=new Game();
		g=EntityManagerHelper.em.find(Game.class, game.getId());
		if(g.getTeams()!=null)
		{
			return g.getTeams();
		}
		return null;
	}
}
