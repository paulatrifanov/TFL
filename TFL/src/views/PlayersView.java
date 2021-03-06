package views;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import dataAccessLayer.PlayerDataAccess;
import model.Player;

@ManagedBean(name = "playersView")
@SessionScoped
public class PlayersView implements Serializable {

	private static final long serialVersionUID = 1L;

	public List<Player> players;

	private Player selectedPlayer;

	public List<Player> getPlayers() {
		return this.players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public Player getSelectedPlayer() {
		return selectedPlayer;
	}

	public void setSelectedPlayer(Player selectedPlayer) {
		this.selectedPlayer = selectedPlayer;
	}

	public void remove(Player player) {
		if (PlayerDataAccess.RemovePlayer(player.getId()) == true) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO!",
					"Player " + player.getUsername() + " successfully removed!"));
			PlayerDataAccess.RemovePlayer(player.getId());
			players.remove(player);
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "WARN!",
					"Could not remove player " + player.getUsername()));
		}
	}

	public void addPlayerToChart(Player player) {
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		ChartView chartView = (ChartView) elContext.getELResolver().getValue(elContext, null, "chartView");
		chartView.addPlayerToChart(player);
	}

	public void onrate(Player player) {
		player.setRating(player.getStars().doubleValue());
		PlayerDataAccess.UpdateRating(player.getId(), player.getRating());
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, null,
				"Player " + player.getUsername() + " rated to " + player.getStars() + " stars!");
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void oncancel() {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancel Event", "Rate Reset");
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	@PostConstruct
	public void init() {
		this.players = new ArrayList<Player>();
		this.players = PlayerDataAccess.ListActivePlayers();
	}
}