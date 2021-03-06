package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The persistent class for the team database table.
 * 
 */
/**
 * @author Paula
 *
 */
@Entity
@NamedQuery(name = "Team.findAll", query = "SELECT t FROM Team t")
public class Team implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	private double score;

	private Boolean winner;

	private Integer goals;

	public Integer getGoals() {
		return goals;
	}

	public void setGoals(Integer goals) {
		this.goals = goals;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "team_player", joinColumns = @JoinColumn(name = "id_team"), inverseJoinColumns = @JoinColumn(name = "id_player"))
	private Set<Player> players;

	public Team() {
		this.goals = 0;
		this.score = 0.0;
		this.players = new HashSet<Player>();
		this.winner = false;
		this.name = "Team name";
	}

	public Team(String name) {
		this();
		this.name = name;
	}

	public Team(String name, Set<Player> listOfPlayers) {
		this(name);
		this.players = listOfPlayers;
		setNewScore();
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getScore() {
		return this.score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public Boolean getWinner() {
		return this.winner;
	}

	public void setWinner(Boolean winner) {
		this.winner = winner;
	}

	public Set<Player> getPlayers() {
		return this.players;
	}

	public List<Player> getListPlayers() {
		return new ArrayList<Player>(this.players);
	}

	public void setPlayers(Set<Player> set) {
		this.players.clear();
		this.players.addAll(set);
		setNewScore();
	}

	public void addNewPlayer(Player p) {
		if (this.players == null) {
			this.players = new HashSet<Player>();
		}
		if (this.containsPlayer(p) == false) {
			this.players.add(p);
			this.score += p.getRating();
		}
	}

	private void setNewScore() {
		this.score = 0.0;
		for (Player player : this.players) {
			this.score += player.getRating();
		}
	}

	public boolean containsPlayer(Player player) {
		if (this.players != null) {
			for (Player p : this.players) {
				if (p.getId() == player.getId()) {
					return true;
				}
			}
		}
		return false;
	}
}