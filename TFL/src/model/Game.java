package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import javax.persistence.*;
import model.Player;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * The persistent class for the games database table.
 * 
 */
@Entity
@Table(name = "game")
@NamedQuery(name = "Game.findAll", query = "SELECT g FROM Game g")
public class Game implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer difference;

	private Boolean archive;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	// uni-directional many-to-many association to Player
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "game_player", joinColumns = { @JoinColumn(name = "game_id") }, inverseJoinColumns = {
			@JoinColumn(name = "player_id") })
	private Set<Player> players;

	// bi-directional many-to-many association to Player
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "players_waiting", joinColumns = { @JoinColumn(name = "game_id") }, inverseJoinColumns = {
			@JoinColumn(name = "player_id") })
	private Set<Player> playersWaiting;

	// bi-directional many-to-one association to Team
	@OneToOne
	@JoinColumn(name = "firstteam")
	private Team team1;

	// bi-directional many-to-one association to Team
	@OneToOne
	@JoinColumn(name = "secondteam")
	private Team team2;

	public Game() {
		this.date = null;
		this.difference = 0;
		this.archive = false;
		this.players = new HashSet<Player>();
		this.playersWaiting = new HashSet<Player>();
		this.team1 = new Team();
		this.team2 = new Team();
	}

	public Game(Date date, Team firstTeam, Team secondTeam) {
		this();
		this.team1 = firstTeam;
		this.team2 = secondTeam;
		this.date = date;
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

	public Integer getDifference() {
		return this.difference;
	}

	public void setDifference(Integer difference) {
		this.difference = difference;
	}

	public Set<Player> getPlayersWaiting() {
		return playersWaiting;
	}

	public void setPlayersWaiting(Set<Player> playersWaiting) {
		this.playersWaiting = playersWaiting;
	}

	public Set<Player> getPlayers() {
		return this.players;
	}

	public void setPlayers(Set<Player> players) {
		this.players = players;
	}

	public Team getTeam1() {
		return this.team1;
	}

	public void setTeam1(Team team1) {
		this.team1 = team1;
	}

	public Team getTeam2() {
		return this.team2;
	}

	public void setTeam2(Team team2) {
		this.team2 = team2;
	}

	public Boolean getArchive() {
		return this.archive;
	}

	public void setArchive(Boolean archive) {
		this.archive = archive;
	}

	/**
	 * @param player
	 * Adds player player to game's list of players
	 */
	public void addPlayer(Player player) {
		if (this.playingThisGame(player) == false) {
			this.players.add(player);
		}
	}

	/**
	 * @param player
	 * @return True if player is playing this game,  false othherwise
	 */
	public boolean playingThisGame(Player player) {
		if (this.players != null) {
			for (Player gamePlayer : this.players) {

				int a = gamePlayer.getId();
				int b = player.getId();
				if (a == b) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param player
	 * @return Message informing user if he is playing the game
	 */
	public String playUnplay(Player player) {
		if (this.players != null) {
			for (Player gamePlayer : this.players) {

				int a = gamePlayer.getId();
				int b = player.getId();
				if (a == b) {
					return "Call off";
				}
			}
		}
		return "Play";
	}

	/**
	 * @return The last player that is waiting to play
	 */
	public Player getFirstPlayerWaiting() {
		Player result = new Player();
		if (this.playersWaiting != null) {
			if (this.playersWaiting.size() != 0) {
				Iterator<Player> it = this.playersWaiting.iterator();
				result = it.next();
			}
		}
		return result;
	}

	/**
	 * @return String representig game date
	 */
	public String dateToDisplay() {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy hh:ss");
		return format.format(this.date);
	}

	@Override
	public String toString() {
		return "Game [id=" + id + ", date=" + date + ", difference=" + difference + "]";
	}
}