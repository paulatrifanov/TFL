package dataAccessLayer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import model.Player;

public class PlayerDataAccess {
    public static EntityManagerFactory emf;
    public static EntityManager em;

    public PlayerDataAccess() {
	emf = Persistence.createEntityManagerFactory("TFL");
	em = emf.createEntityManager();
	em.getTransaction().begin();
    }

    private  void createPlayer() {
	Player emp = new Player();
	emp.setId(3);
	emp.setType(1);
	emp.setUsername("george");
	emp.setPassword("new");
	emp.setRating(20.5);
	emp.setAvailable(true);
	em.persist(emp);
	em.getTransaction().commit();
    }

    public  boolean createUser(String username, String password, int type,boolean available, double rating ) {
    	try
    	{
    		Player emp = new Player();	
        	emp.setType(type);
        	emp.setUsername(username);
        	emp.setPassword(password);
        	emp.setRating(rating);
        	emp.setAvailable(available);
        	em.persist(emp);
        	em.getTransaction().commit();
    	}
    	catch(Exception ex)
    	{
    		System.out.println(ex.getMessage());
    		return false;
    	}
    	return true;
        }
    
    public boolean loginUser(String username, String password) {
	TypedQuery<Player> query = em.createQuery("SELECT c FROM Player c", Player.class);
	List<Player> result = new ArrayList<Player>();
	result = query.getResultList();
   
	for (Player player : result) {
		System.out.println(player.toString());
	}
	
	TypedQuery<Player> querynew = em
		.createQuery("SELECT c FROM Player c WHERE c.username = :name AND c.password=:pass", Player.class);
	querynew.setParameter("name", username);
	querynew.setParameter("pass", password);
	Player play = new Player();
	try {
	    play = querynew.getSingleResult();
	    if (play.getId() != 0) {
		System.out.println(play.toString());
		for (Player p : result) {
		    if ((p.getUsername().compareTo(username) == 0) && (p.getPassword().compareTo(password) == 0)) {
			return true;
		    }
		}
	    }
	} catch (Exception ex) {
	    System.out.println("Username or password incorrect!");
	}
	return false;
    }

//     public static void main(String[] args) {
//    	 PlayerDataAccess pda=new PlayerDataAccess();
//    	 pda.createPlayer();
//     }
}
