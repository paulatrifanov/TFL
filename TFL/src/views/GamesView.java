package views;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.ScheduleModel;

@ManagedBean(name = "gamesView")
@ViewScoped
public class GamesView implements Serializable{
	
    private static final long serialVersionUID = 1L;
    private ScheduleModel eventModel;
    
}
