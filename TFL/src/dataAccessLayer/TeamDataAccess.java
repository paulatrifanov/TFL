package dataAccessLayer;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import helpers.EntitiesManager;
import model.Player;
import model.Team;

/**
 * @author Paula
 *
 */
@ManagedBean(name = "teamDataAccess")
@SessionScoped
public class TeamDataAccess implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static EntityManager em = EntitiesManager.GetManager();

	/**
	 * @param teamId
	 *            Id of team
	 * @return Team with id teamId
	 */
	public static Team FindTeam(int teamId) {
		Team game = em.find(Team.class, teamId);
		return game;
	}

	/**
	 * Updates team with new data from team teamToSave
	 * 
	 * @param teamToSave
	 * @return
	 */
	public static Team UpdateTeam(Team teamToSave) {
		em.getTransaction().begin();
		Team team = em.find(Team.class, teamToSave.getId());
		team.setName(teamToSave.getName());
		team.setWinner(teamToSave.getWinner());
		team.setGoals(teamToSave.getGoals());
		team.setScore(teamToSave.getScore());
		team.getPlayers().removeAll(team.getPlayers());
		team.setPlayers(teamToSave.getPlayers());
		em.merge(team);
		em.getTransaction().commit();
		em.refresh(team);
		return team;
	}

	/**
	 * Removes all players from team with id teamId
	 * 
	 * @param teamId
	 * @return
	 */
	public static Team RemoveAllPlayers(int teamId) {
		em.getTransaction().begin();
		Team team = em.find(Team.class, teamId);
		if (team != null) {
			team.getPlayers().clear();
			team.setScore(0);
		}
		em.getTransaction().commit();
		em.refresh(team);
		return team;
	}

	/**
	 * Removes a player from team
	 * 
	 * @param teamID
	 * @param playerID
	 * @return
	 */
	public static Team RemovePlayerFromTeam(int teamID, int playerID) {
		em.getTransaction().begin();
		Team team = em.find(Team.class, teamID);
		Player player = em.find(Player.class, playerID);
		if (team != null && player != null) {
			team.getPlayers().remove(player);
			team.setScore(team.getScore() - player.getRating());
		}
		em.getTransaction().commit();
		em.refresh(team);
		return team;
	}

	/**
	 * Sets new name for team with id teamID
	 * 
	 * @param teamID
	 * @param name
	 * @return
	 */
	public static Team SaveTeamName(int teamID, String name) {
		em.getTransaction().begin();
		Team team = em.find(Team.class, teamID);
		if (team != null) {
			team.setName(name);
		}
		em.getTransaction().commit();
		em.refresh(team);
		return team;
	}

	/**
	 * Adds player with id playerID to team with id teamID
	 * 
	 * @param teamID
	 * @param playerID
	 * @return
	 */
	public static Team AddNewPlayer(int teamID, int playerID) {
		em.getTransaction().begin();
		Team t = em.find(Team.class, teamID);
		Player p = em.find(Player.class, playerID);
		t.addNewPlayer(p);
		em.merge(t);
		em.getTransaction().commit();
		em.refresh(t);
		return t;
	}

	/**
	 * Persists new team to database
	 * 
	 * @param team
	 *            Team to persist
	 * @return
	 */
	public static Team CreateNewTeam(Team team) {
		em.getTransaction().begin();
		em.persist(team);
		em.getTransaction().commit();
		em.refresh(team);
		return team;
	}
}