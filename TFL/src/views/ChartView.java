package views;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
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
		return lineModel;
	}
	public void setLineModel2(LineChartModel lineModel2) {
		this.lineModel = lineModel2;
	}
	
	private boolean existRatings;
	
	public boolean isExistRatings() {
		return existRatings;
	}
	public void setExistRatings(boolean existRatings) {
		this.existRatings = existRatings;
	}

	public List<Player> players=new ArrayList<Player>();

    private LineChartModel lineModel;
    
    public void addPlayerToChart(Player player)
    {
    	boolean exists=false;
    	for(Player p:this.players)
    	{
    		if(p.getId()==player.getId())
    		{
    			exists=true;
    		}
    	}
    	if(exists==false)
    	{
    		this.players.add(player);
    	}
    }
    
    public void createLineModels() {      
   
        lineModel = initCategoryModel();
        lineModel.setTitle("Rating chart");
        lineModel.setLegendPosition("e");
        lineModel.setShowPointLabels(true);
        
        DateAxis axis = new DateAxis("Dates");
        lineModel.getAxes().put(AxisType.X, axis);
        
        axis.setTickFormat("%b %#d, %y");
        
        Axis yAxis = lineModel.getAxis(AxisType.Y);
        yAxis.setLabel("Rating");
    }
    
    private LineChartModel initCategoryModel() {
 
        LineChartModel model = new LineChartModel();
 
        if(this.players==null)
        {
        	System.out.println("Null players!");
        }
        else
        {
        	System.out.println("Players not null");
        	for(Player player:this.players)
        	{
        		System.out.println(player.getUsername());
        		//il adaug in chart numai daca are ratinguri

        		if(player.getPlayerRatings().isEmpty())
        		{
        			System.out.println(player.getUsername()+"has no ratings!");
        			this.existRatings=true;
        		}
        		else
        		{
        			this.existRatings=false;
        			ChartSeries playerLineChart = new ChartSeries();
        			playerLineChart.setLabel(player.getUsername());
        			for(PlayerRating playerRating:player.getPlayerRatings())
        			{
        				System.out.println(playerRating.getRating());
        				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                		playerLineChart.set(sdf.format(playerRating.getDate()), playerRating.getRating());
        			}
        			model.addSeries(playerLineChart);
        		}
        	}
        	
        }  
        return model;
    }
	
}
