package model;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.persistence.*;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import model.PlayerRating;
import java.util.List;

/**
 * The persistent class for the player database table.
 * 
 */
@Entity
@NamedQuery(name="Player.findAll", query="SELECT p FROM Player p")
public class Player implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return id+"##"+ username+"##"  + password +"##" + rating +"##"+ this.available+"##"+this.type+"##";
	}
	public Player()
	{
		this.archive=false;
		this.available=true;
		this.rating=0.0;
		this.type=1;
		this.picture="../images/noimage.png";
	}
	
	public Player(String username, String password)
	{
		this.archive=false;
		this.available=true;
		this.password=password;
		this.rating=0.0;
		this.type=1;
		this.username=username;		
		this.picture="../images/noimage.png";
	}
	
	public double getMinRating()
	{
		double min=this.rating;
		for(PlayerRating playerRating: this.playerRatings)
		{
			if(min>playerRating.getRating())
			{
				min=playerRating.getRating();
			}
		}
		return min;
	}
	
	public double getMaxRating()
	{
		double max=this.rating;
		for(PlayerRating playerRating: this.playerRatings)
		{
			if(max<playerRating.getRating())
			{
				max=playerRating.getRating();
			}
		}
		return max;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private Boolean available;

	private String password;

	private byte[] image;

	private double rating;

	private Integer type;

	private String username;

	private Boolean archive;
	
	private String picture;
	
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}

	@ManyToMany(mappedBy="players",fetch=FetchType.EAGER)
	private List<Game> games;

	//bi-directional many-to-one association to PlayerRating
	@OneToMany(mappedBy="player", fetch=FetchType.EAGER)
	private List<PlayerRating> playerRatings;

	//bi-directional many-to-many association to Team
	@ManyToMany(mappedBy="players",fetch=FetchType.EAGER)
	private List<Team> teams;

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

	public StreamedContent getImage() {
		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent();
		} else {
			return new DefaultStreamedContent(new ByteArrayInputStream(this.image));
		}
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public double getRating() {
		return this.rating;
	}

	public void setRating(double rating) {
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
	
	public List<Game> getGames() {
		return this.games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}

	public List<PlayerRating> getPlayerRatings() {
		return this.playerRatings;
	}

	public void setPlayerRatings(List<PlayerRating> playerRatings) {
		this.playerRatings = playerRatings;
	}

	public PlayerRating addPlayerRating(PlayerRating playerRating) {
		getPlayerRatings().add(playerRating);
		playerRating.setPlayer(this);

		return playerRating;
	}

	public PlayerRating removePlayerRating(PlayerRating playerRating) {
		getPlayerRatings().remove(playerRating);
		playerRating.setPlayer(null);

		return playerRating;
	}

	public List<Team> getTeams() {
		return this.teams;
	}

	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}
	
	public int GetGames(boolean winned)
	{
		int count=0;

		if(this.getTeams()==null)
			System.out.println("No teams");
		else
		{
			for(Team t:this.getTeams())
			{
				if(t.getWinner()==true)
				{
					count++;
				}
				System.out.println(t.getWinner());
			}
		}
		System.out.println("winned games: "+count);
		return count;
	}
	
	public int GetTotalPlayedGames()
	{
		if(this.games!=null)
		{
			return this.games.size();
		}
		return 0;
	}
}