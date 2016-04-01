package views;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import model.Player;
import model.PlayerRating;


@ManagedBean(name = "chartView")
@SessionScoped
public class ChartView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<Player> getPlayers() {
		return players;
	}
	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public LineChartModel getLineModel2() {
		return lineModel2;
	}
	public void setLineModel2(LineChartModel lineModel2) {
		this.lineModel2 = lineModel2;
	}
	
	private List<Player> players;

    private LineChartModel lineModel2;
    
    @PostConstruct
    public void init() {
        this.players=new ArrayList<Player>();
    	createLineModels();
    } 
    
    public void addPlayerToChart(Player player)
    {
    	this.players.add(player);
    }
    
    public void createLineModels() {        
        lineModel2 = initCategoryModel();
        lineModel2.setTitle("Rating chart");
        lineModel2.setLegendPosition("e");
        lineModel2.setShowPointLabels(true);
        lineModel2.getAxes().put(AxisType.X, new CategoryAxis("Date"));
        Axis yAxis = lineModel2.getAxis(AxisType.Y);
        yAxis.setLabel("Rating");
        if(this.players.size()!=0)
        {
        	System.out.println("There are players");
        }
        else
        {
        	System.out.println("There are no players");
        }
    }
    
    private LineChartModel initCategoryModel() {
    	
        LineChartModel model = new LineChartModel();
        for(Player p:this.players)
        {
        	ChartSeries playerLineChart = new ChartSeries();
        	playerLineChart.setLabel(p.getUsername());
        	if(!p.getPlayerRatings().isEmpty())
        	{
        		System.out.println(p.getUsername()+"has ratings");
        		for(PlayerRating pl:p.getPlayerRatings())
            	{
        			System.out.println(pl.getDate().toString());
            		playerLineChart.set(pl.getDate().toString(), pl.getRating());
            	}
            	model.addSeries(playerLineChart);
            	System.out.println(p.getUsername());
        	}
        	else
        	{
        		System.out.println("This player has no ratings!");
        	}
        } 
        
        return model;
    }
	
}