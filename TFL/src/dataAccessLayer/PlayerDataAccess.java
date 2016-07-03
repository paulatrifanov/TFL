package dataAccessLayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import helpers.EntitiesManager;
import model.Player;

/**
 * @author Paula
 *
 */
@ManagedBean(name = "playerDataAccess")
@SessionScoped
public class PlayerDataAccess implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static EntityManager em = EntitiesManager.GetManager();

	/**
	 * @param playerid
	 *            Id of player to find
	 * @return
	 */
	public static Player FindPlayer(int playerid) {
		Player player = em.find(Player.class, playerid);
		return player;
	}

	/**
	 * @param username
	 *            Username for new user
	 * @param password
	 *            Password for new user
	 * @param rating
	 *            New user's rating
	 * @return Createa user
	 */
	public static Player CreateNewUser(String username, String password, Double rating) {
		Player emp = new Player(username, password, rating);
		emp.setStars(rating.intValue());
		try {
			em.getTransaction().begin();
			em.persist(emp);
			em.getTransaction().commit();
			em.refresh(emp);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return emp;
	}

	/**
	 * @param username
	 *            Username to find in database
	 * @param password
	 *            Password to find in database
	 * @return
	 */
	public static int LoginUser(String username, String password) {
		TypedQuery<Player> query = em
				.createQuery("SELECT c FROM Player c WHERE c.username = :name AND c.password=:pass", Player.class);
		query.setParameter("name", username);
		query.setParameter("pass", password);
		int result = 0;
		try {
			Player player = query.getSingleResult();
			if (player != null) {
				result = player.getId();
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return result;
	}

	/**
	 * Updates password for user with id playerId to new password
	 * 
	 * @param playerId
	 * @param password
	 *            New password
	 * @return
	 */
	public static Player UpdatePassword(int playerId, String password) {
		em.getTransaction().begin();

		Player play = em.find(Player.class, playerId);
		try {
			play.setPassword(password);
			em.persist(play);
			em.getTransaction().commit();
			em.refresh(play);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return play;
	}

	/**
	 * Updates profile picture
	 * 
	 * @param playerId
	 * @param newPicture
	 * @return
	 */
	public static Player UpdateProfilePicture(int playerId, String newPicture) {
		em.getTransaction().begin();
		Player play = em.find(Player.class, playerId);
		try {
			play.setPicture(newPicture);
			em.merge(play);
			em.getTransaction().commit();
			return play;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	/**
	 * Changes the availability for player with id playerId
	 * 
	 * @param playerId
	 * @param available
	 * @return
	 */
	public static Player ChangeAvailable(int playerId, boolean available) {
		em.getTransaction().begin();
		Player play = em.find(Player.class, playerId);
		play.setAvailable(available);
		em.persist(play);
		em.getTransaction().commit();
		em.refresh(play);
		return play;
	}

	/**
	 * Updates rating for player with id playerId
	 * 
	 * @param playerId
	 * @param rating
	 * @return
	 */
	public static Player UpdateRating(int playerId, Double rating) {
		em.getTransaction().begin();
		Player play = em.find(Player.class, playerId);
		if (play != null) {
			play.setRating(rating);
			play.setStars(play.getRating().intValue());
		}
		em.merge(play);
		em.getTransaction().commit();
		em.refresh(play);
		return play;
	}

	/**
	 * Removes player from database
	 * 
	 * @param playerId
	 * @return
	 */
	public static boolean RemovePlayer(int playerId) {
		em.getTransaction().begin();
		Player player = em.find(Player.class, playerId);
		if (player != null) {
			player.setArchive(true);
			em.getTransaction().commit();
			return true;
		} else {
			System.out.println("Player not found");
			return false;
		}
	}

	/**
	 * @return A list of active players
	 */
	public static List<Player> ListActivePlayers() {
		TypedQuery<Player> query = em.createQuery("SELECT p FROM Player p WHERE p.archive = false", Player.class);
		List<Player> result = new ArrayList<Player>();
		result = query.getResultList();
		return result;
	}
}