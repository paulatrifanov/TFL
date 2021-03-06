package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the player_ratings database table.
 * 
 */
/**
 * @author Paula
 *
 */
@Entity
@Table(name = "player_ratings")
@NamedQuery(name = "PlayerRating.findAll", query = "SELECT p FROM PlayerRating p")
public class PlayerRating implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private double rating;

	// bi-directional many-to-one association to Player
	@ManyToOne
	@JoinColumn(name = "id_player")
	private Player player;

	public PlayerRating() {
		this.rating = 0.0;
		this.player = new Player();
	}

	public PlayerRating(Date date, Player player, double rating) {
		if (player != null) {
			this.player = player;
		}
		this.date = date;
		this.rating = rating;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getRating() {
		return this.rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@Override
	public String toString() {
		return "PlayerRating: playerId=" + this.player.getId() + " date=" + date + " rating=" + rating;
	}

}