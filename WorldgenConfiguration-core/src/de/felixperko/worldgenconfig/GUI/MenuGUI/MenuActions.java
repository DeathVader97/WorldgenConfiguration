package de.felixperko.worldgenconfig.GUI.MenuGUI;

import de.felixperko.worldgenconfig.MainMisc.Main;

public class MenuActions {
	
	public static void newProject(){
		Main.main.stage.mainTable.addActor(new NewProjectWindow());
	}
	
	public static void openProject(String name){
		
	}
	
	public static void importProject(String path){
		
	}
	
	public static void exportProject(String name, String path){
		
	}
	
	public static void options(){
	}
}
