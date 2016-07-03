package helpers;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import model.Player;

/**
 * @author Paula
 *
 */
public class RedirectView {

	/**
	 * Redirects user player to pageForNormalUser if user is not an admin, to pageForAdmin otherwise
	 * @param player
	 * @param pageForNormalUser
	 * @param pageForAdmin
	 */
	public static void Redirect(Player player, String pageForNormalUser, String pageForAdmin) {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		if (player != null) {
			try {
				if (player.getType() == 1) {
					context.redirect(context.getRequestContextPath() + pageForNormalUser);
				} else {
					context.redirect(context.getRequestContextPath() + pageForAdmin);
				}

			} catch (Exception ex) {
				System.out.println("Redirection error!" + ex.getMessage());
			}
		}
	}

	/**
	 * Redirects user to page
	 * @param page
	 */
	public static void Redirect(String page) {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		try {
			context.redirect(context.getRequestContextPath() + page);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
