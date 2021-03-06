package views;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import dataAccessLayer.PlayerDataAccess;
import dataAccessLayer.PlayerRatingAccess;
import helpers.PasswordValidation;
import model.Player;
import model.PlayerRating;

@ManagedBean(name = "singUpView")
public class SingUpView {

	private String name;

	private String pass;

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getConfirmation() {
		return confirmation;
	}

	public void setConfirmation(String confirmation) {
		this.confirmation = confirmation;
	}

	private String confirmation;

	private Double initialRating;

	public Double getInitialRating() {
		return initialRating;
	}

	public void setInitialRating(Double initialRating) {
		this.initialRating = initialRating;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@PostConstruct
	public void init() {
		this.initialRating = 5.0;
	}

	public void save() {
		if (this.pass.compareTo(this.confirmation) != 0) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, null, "Passwords did not match!"));
		} else {
			if (PasswordValidation.isValid(this.pass)) {
				Player player = PlayerDataAccess.CreateNewUser(this.name, this.pass, this.initialRating);
				PlayerRating playerRating = new PlayerRating(new Date(), player, player.getRating());
				PlayerRatingAccess.RegisterNewRating(playerRating);
				this.name = " ";
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Account created successfully!"));
			}
		}
		this.pass = " ";
		this.confirmation = " ";
	}

	public void newPlayer(ActionEvent event) {
		Player player = PlayerDataAccess.CreateNewUser(this.name, this.pass, this.initialRating);
		PlayerRating playerRating = new PlayerRating(new Date(), player, player.getRating());
		PlayerRatingAccess.RegisterNewRating(playerRating);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO!", "Player " + this.name + " successfullt added!"));
	}
}