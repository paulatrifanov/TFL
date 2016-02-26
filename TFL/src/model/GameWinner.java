package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the game_winners database table.
 * 
 */
@Entity
@Table(name="game_winners")
@NamedQuery(name="GameWinner.findAll", query="SELECT g FROM GameWinner g")
public class GameWinner implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	//bi-directional many-to-one association to Game
	@ManyToOne
	@JoinColumn(name="id_game")
	private Game game;

	//bi-directional many-to-one association to Player
	@ManyToOne
	@JoinColumn(name="id_player")
	private Player player;

	public GameWinner() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Game getGame() {
		return this.game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}