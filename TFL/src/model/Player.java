package model;

import java.io.Serializable;
import javax.persistence.*;
import model.PlayerRating;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * The persistent class for the player database table.
 * 
 */
@Entity
@NamedQuery(name = "Player.findAll", query = "SELECT p FROM Player p")
public class Player implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Boolean available;

	private String password;

	private Double rating;

	private Integer type;

	private String username;

	private Boolean archive;

	private String picture;

	private Integer stars;

	@ManyToMany(mappedBy = "players", fetch = FetchType.EAGER)
	private Set<Game> games;

	// bi-directional many-to-one association to PlayerRating
	@OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
	private Set<PlayerRating> playerRatings;

	@Override
	public String toString() {
		return id + "##" + username + "##" + password + "##" + rating + "##" + this.available + "##" + this.type + "##"
				+ this.picture;
	}

	public Player() {
		this.archive = false;
		this.available = true;
		this.rating = 0.0;
		this.type = 1;
		this.picture = "noimage.png";
	}

	public Player(String username, String password, Double rating) {
		this();
		this.username = username;
		this.password = password;
		this.setRating(rating);
	}

	/**
	 * @return Minimum value of current player ratings
	 */
	public double getMinRating() {
		double min = this.rating;
		for (PlayerRating playerRating : this.playerRatings) {
			if (min > playerRating.getRating()) {
				min = playerRating.getRating();
			}
		}
		return min;
	}

	/**
	 * @return Maximum value of current player ratings
	 */
	public double getMaxRating() {
		double max = this.rating;
		for (PlayerRating playerRating : this.playerRatings) {
			if (max < playerRating.getRating()) {
				max = playerRating.getRating();
			}
		}
		return max;
	}

	public Integer getStars() {
		return stars;
	}

	public void setStars(Integer stars) {
		this.stars = stars;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getAvailable() {
		return this.available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Double getRating() {
		return this.rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getArchive() {
		return this.archive;
	}

	public void setArchive(Boolean archive) {
		this.archive = archive;
	}

	public Set<Game> getGames() {
		return this.games;
	}

	public void setGames(Set<Game> games) {
		this.games = games;
	}

	public Set<PlayerRating> getPlayerRatings() {
		return this.playerRatings;
	}

	public void setPlayerRatings(Set<PlayerRating> playerRatings) {
		this.playerRatings = playerRatings;
	}

	public PlayerRating addPlayerRating(PlayerRating playerRating) {
		getPlayerRatings().add(playerRating);
		playerRating.setPlayer(this);
		return playerRating;
	}

	public PlayerRating removePlayerRating(PlayerRating playerRating) {
		if (this.playerRatings.contains(playerRating)) {
			this.playerRatings.remove(playerRating);
		}
		return playerRating;
	}

	/**
	 * @return Number of total played games
	 */
	public int GetTotalPlayedGames() {
		if (this.games != null) {
			return this.games.size();
		}
		return 0;
	}

	public boolean disableButtons() {
		if (this.type == 1)
			return true;
		return false;
	}

	/**
	 * @param game
	 * @return True if player has rating for game
	 */
	public boolean hasRatingForGame(Game game) {
		if (this.playerRatings != null) {
			for (PlayerRating playerRating : this.playerRatings) {
				if (playerRating.getDate().toString().compareTo(game.getDate().toString()) == 0) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param game
	 * @return Last rating before game
	 */
	public double getRatingBefore(Game game) {
		PlayerRating result = null;
		if (this.playerRatings != null && this.playerRatings.size() != 0) {
			Iterator<PlayerRating> itr = this.playerRatings.iterator();
			PlayerRating aux = itr.next();
			while (aux.getDate().after(game.getDate()) && itr.hasNext()
					|| aux.getDate().compareTo(game.getDate()) == 0) {
				aux = itr.next();
			}
			result = aux;
			while (itr.hasNext()) {
				aux = itr.next();
				if (aux.getDate().before(game.getDate()) && aux.getDate().after(result.getDate())
						&& aux.getDate().compareTo(result.getDate()) != 0) {
					result = aux;
				}
			}
		}
		return result.getRating();
	}

	/**
	 * @param game
	 * @return Rating for game, if exist, null otherwise
	 */
	public PlayerRating getRatingForGame(Game game) {
		if (this.playerRatings != null && this.playerRatings.size() != 0) {
			for (PlayerRating playerRating : this.playerRatings) {
				if (playerRating.getDate().compareTo(game.getDate()) == 0) {
					return playerRating;
				}
			}
		}
		return null;
	}

	/**
	 * @return Number of winned games
	 */
	public int NumberOfWinnedGames() {
		int result = 0;
		if (this.games != null) {
			for (Game game : this.games) {
				if (game.getTeam1().containsPlayer(this)) {
					if (game.getTeam1().getWinner()) {
						result++;
					}
				}

				if (game.getTeam2().containsPlayer(this)) {
					if (game.getTeam2().getWinner()) {
						result++;
					}
				}
			}
		}
		return result;
	}

	/**
	 * @return Number of losed games
	 */
	public int NumberOfLosedGames() {
		int result = 0;
		if (this.games != null) {
			for (Game game : this.games) {
				if (game.getTeam1().containsPlayer(this)) {
					if (game.getTeam1().getWinner() == false) {
						result++;
					}
				}

				if (game.getTeam2().containsPlayer(this)) {
					if (game.getTeam2().getWinner() == false) {
						result++;
					}
				}
			}
		}
		return result;
	}

	/**
	 * @return Next game
	 */
	public Game NextPlayedGame() {
		Game result = null;
		if (this.games != null) {
			Iterator<Game> itr = this.games.iterator();
			while (result == null && itr.hasNext()) {
				Game aux = itr.next();
				if (aux.getDate().after(new Date())) {
					result = aux;
				}
			}
			while (itr.hasNext()) {
				Game aux = itr.next();
				if (aux.getDate().before(result.getDate()) && aux.getDate().after(new Date())) {
					result = aux;
				}
			}
		}
		return result;
	}

	/**
	 * @return Last played game
	 */
	public Game LastPlayedGame() {
		Game result = null;
		if (this.games != null) {
			Iterator<Game> itr = this.games.iterator();
			while (result == null && itr.hasNext()) {
				Game aux = itr.next();
				if (aux.getDate().before(new Date())) {
					result = aux;
				}
			}
			while (itr.hasNext()) {
				Game aux = itr.next();
				if (aux.getDate().before(new Date()) && aux.getDate().after(result.getDate())) {
					result = aux;
				}
			}
		}
		return result;
	}
}