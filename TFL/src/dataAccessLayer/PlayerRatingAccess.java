package dataAccessLayer;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import helpers.EntitiesManager;
import model.PlayerRating;

/**
 * @author Paula
 *
 */
@ManagedBean(name = "playerRatingAccess")
@SessionScoped
public class PlayerRatingAccess implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static EntityManager em = EntitiesManager.GetManager();

	/**
	 * Persists new rating for a player
	 * 
	 * @param playerRating
	 *            Rating to persist
	 * @return
	 */
	public static PlayerRating RegisterNewRating(PlayerRating playerRating) {
		try {
			em.getTransaction().begin();
			em.persist(playerRating);
			em.getTransaction().commit();
			em.refresh(playerRating);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return playerRating;
	}

	/**
	 * Updates rating to a new value
	 * 
	 * @param ratingId
	 *            Id of rating to update
	 * @param newValue
	 *            New value for rating
	 */
	public static void UpdateRating(int ratingId, double newValue) {
		PlayerRating playerRating = em.find(PlayerRating.class, ratingId);
		if (playerRating != null) {
			em.getTransaction().begin();
			playerRating.setRating(newValue);
			em.getTransaction().commit();
		}
	}
}