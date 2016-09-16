import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BabyNames{

	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		boolean whileLoopBool = true;
		
		File directory = new File("names");
		File[] directoryFiles = directory.listFiles();
		ArrayList<YearNames> listOfYearNames = new ArrayList<YearNames>();
		YearNames currentYear = null;
		
		if(directoryFiles != null){																						//Check to see if the folder ("names") has files in it.
			for(File f : directoryFiles){																				//If not, for every file in "names" folder do{
				try (Scanner in = new Scanner(f)){														
					//currentYear is a YearNames object, creating new YearNames( this part is the integer 'year' based off of the file you're currently in's name )
					currentYear = new YearNames(Integer.parseInt(f.getName().substring(3, 7)));							
					while (in.hasNextLine()) {																			//For every line in the file do{
						String[] lineArray = in.nextLine().split(",");													//String[] = the line split by "," [name, gender, count]
						Name currentName = new Name(lineArray[0], lineArray[1], Integer.parseInt(lineArray[2]));		//Create Name object filled with name, gender, count (based off string[]
						currentYear.add(currentName);																	//Add that name to the currentYear object.
					}
					listOfYearNames.add(currentYear);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}	
		
		
		System.out.println("Welcome to the file reader and logger for the number of occurrences a name has in a year.");

//		While loop starts here.
		while(whileLoopBool){
			System.out.println("What first name do you want to look for in the database?");
			String userName = sc.nextLine();
			if(userName.equalsIgnoreCase("q")){
				whileLoopBool = false;
				break;
			}
			System.out.println();
			for(YearNames year: listOfYearNames){
				if(year.getCountByName(userName) > 0){
					System.out.print(year.getYear() + " ("); 
					System.out.printf("%.4f", year.getFractionByName(userName));
					System.out.println(") : " + year.getLineBar() + " 		Count: " + year.getCountByName(userName));
					
				}
			}
			System.out.println("\n\n");
		}
	}
}


class MyArrayList<E extends Comparable<E>> extends ArrayList<E>{
	public void sort(){
		Collections.sort(this);	
	}
	
	public boolean isSorted(){
		for(int i=0; i<this.size()-1; i++){
			if(this.get(i).compareTo(this.get(i+1)) == 1){
				return false;
			}
		}
		return true;	
	}
	
	public boolean contains(E element){
		if(isSorted()){
			if(Collections.binarySearch(this, element) >= 0){
				return true;
			}else{
				return false;
			}
		}else{
			return super.contains(element);
		}
	}
}


class Name implements Comparable<Name>{
	private String name, gender;
	private int count;
	
	public Name(String personName, String personGender, int personCount){
		name = personName;
		gender = personGender;
		count = personCount;
	}

	@Override
	public int compareTo(Name o) {
		// TODO Auto-generated method stub
		if(count > o.count){
			return 1;
		}else if(count < o.count){
			return -1;
		}else{
			if(name.compareTo(o.name) > 0){
				return 1;
			}else{
				return -1;
			}
		}
	}
	
	public boolean equals(Name o){
		if(name.equals(o.name) && gender.equals(o.gender)){
			return true;
		}else{
			return false;
		}
	}
	
	public String toString(){
		String stringReturn = name + ", " + gender + ". Occurred " + count + " times.";
		return stringReturn;
	}
	
	public String getName(){
		return name;
	}
	
	public String getGender(){
		return gender;
	}
	
	public int getCount(){
		return count;
	}
}

class YearNames{
	private MyArrayList<Name> babyNameList;
	private int count;
	private double totalCount = 0;
	private int total = 0;
	private int year;
	
	public YearNames(int year){
		this.year = year;
		babyNameList = new MyArrayList<Name>();
	}
	
	public void add(Name n){
		if(babyNameList.contains(n)){
			throw new IllegalArgumentException("Name is already in the database.");
		}else{
			babyNameList.add(n);
			totalCount += n.getCount();
			total++;
		}
	}
	
	public int getCountByName (String name){
		count = 0;
		if(babyNameList.contains(name)){
			System.out.println("No such name in the dataset");
			return -1;
		}else{
			for(Name n : babyNameList){
				if(n.getName().equalsIgnoreCase(name)){
					count+=n.getCount();
				}
			}
		}
		
		return count;
	}
	
	public double getFractionByName (String name){
		return (double)count/totalCount * 100;
	}
	
	public int getYear(){
		return year;
	}
	
	public int getSize(){
		return total;
	}
	
	public String getLineBar(){
		String returnBar = "";
		for(int i = 0; i<(double)count/totalCount * 10000; i++){
			returnBar += "|";
		}
		return returnBar;
	}

}