package cellsociety_team11;

import java.util.ArrayList;

public class Configuration {
	int width=6;
	int height=6;
	ArrayList<String>states;
	String name="GameOfLife";
	String myFile;
	public Configuration(String fileName){
		myFile=fileName;
	}
	public Configuration(){}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	public ArrayList<String>getStates(){
		states=new ArrayList<String>();
		for(int i=0; i<6; i++) {
			for (int j=0; j<6; j++) {
				if ((i==1 || i==2) && (j==1 || j==2) &&  (!(i==2 && j==2))) {
					states.add("live");				
				}
				else if ((i==3 ||i==4) && (j==3 || j==4) && (!(i==3 && j==3))) {
					states.add("live");
				}
				else {
					states.add("dead");
				}
			}
		}
		return states;
	}
	public String getName(){
		return name;
	}
	
}
