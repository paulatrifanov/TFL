package dataAccessLayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import model.Player;

public class TeamGenerator {

	public static int factorial(int number)
	{
		int result=1;
		for(int i=1;i<=number;i++)
		{
			result*=i;
		}
		return result;
	}
	
	public static int Combination(int n,int k)
	{
		return factorial(n)/(factorial(k)*factorial(n-k));
	}
	
    
	private static List<List<Player>> resultList=new ArrayList<List<Player>>();
	public static List<Player> list=new ArrayList<Player>();
	
	public static List<List<Player>> getResultList() {
		return resultList;
	}

	public static void setResultList(List<List<Player>> resultList) {
		TeamGenerator.resultList = resultList;
	}


	private static double totalValue=0;
	
	
	public static void GetTotalValue(List<Player> players)
	{
			 for(Player p:players)
			 {
				 totalValue+=p.getRating();
			 }
			 totalValue=totalValue/2;
			 
	}
	
    public static void  getCombinations(Player[] arr, int len, int startPosition, Player[] result){
    		  
        if (len == 0){	
        	resultList.add(Arrays.asList(result));
        	List<Player> lisst=new ArrayList<Player>();
        	for(Player p:Arrays.asList(result))
        	{
        		lisst.add(p);
        	}
        	resultList.add(lisst);
            return;
        }            
        for (int i = startPosition; i <= arr.length-len; i++){
            result[result.length - len] = arr[i];
            getCombinations(arr, len-1, i+1, result);
        }
    }  

    
	public static  TreeMap<Double,List<Player>> generateTeams()
	{
			
		Player[] array = new Player[list.size()];
		list.toArray(array); 
		
		GetTotalValue(list);
		System.out.println("Total value" +totalValue);
		
		getCombinations(array, 3, 0,new Player[3]);
		
		TreeMap<Double,List<Player>> map = new TreeMap<Double, List<Player>>(new MapComparator(totalValue));
		 
		 double idealValue=0;
		 
		 for(List<Player> list:resultList)
	        {
			 	idealValue=0;
	        //	System.out.println("combination:");
	        	for(Player p:list)
	        	{
	        		idealValue+=p.getRating();
	        		//System.out.println(p.getUsername());
	        	}
	        	map.put(idealValue, list);
	        	//System.out.println("Strength "+idealValue);
	        }
		 return map;
		
	}


	public static void printMap(Map<Double,List<Player>> map) {
		
		for (Map.Entry<Double, List<Player>> entry : map.entrySet()) {
			
			System.out.println("Key : " + entry.getKey() 
                                      + " Value : " + entry.getValue());
		}
	}
	
	public static void main(String[] args) {
		//fac un ob player data access
		PlayerDataAccess pda=new PlayerDataAccess();
		//List<Player> list=new ArrayList<Player>();
		list=pda.listPlayers();
		//transform lista in array
		//Player[] array = new Player[list.size()];
		//list.toArray(array); 
		
		//GetTotalValue(list);
		//System.out.println("Total value" +totalValue);
		//generez toate perechile posibile
		//getCombinations(array, 3, 0,new Player[3]);

		//generez echipele impreuna cu 
		TreeMap<Double,List<Player>> map = new TreeMap<Double, List<Player>>(
				new MapComparator(totalValue));
		 map=generateTeams();		 
		 printMap(map);
	}
}