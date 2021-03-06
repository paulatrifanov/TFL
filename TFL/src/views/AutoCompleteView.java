package views;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import dataAccessLayer.PlayerDataAccess;
import model.Player;

@ManagedBean
@SessionScoped
public class AutoCompleteView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Player> allPlayers;
	private Player selectedPlayer;

	public Player getSelectedPlayer() {
		return selectedPlayer;
	}

	public void setSelectedPlayer(Player selectedPlayer) {
		this.selectedPlayer = selectedPlayer;
	}

	@PostConstruct
	public void init() {
		allPlayers = PlayerDataAccess.ListActivePlayers();
		this.selectedPlayer = new Player();
	}

	public List<Player> completeTheme(String query) {
		List<Player> filteredPlayers = new ArrayList<Player>();

		for (Player p : allPlayers) {
			if (p.getUsername().toLowerCase().startsWith(query)) {
				filteredPlayers.add(p);
			}
		}
		return filteredPlayers;
	}
}