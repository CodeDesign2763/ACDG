/*
 * Класс Model
 * 
 * Copyright 2021 Alexander Chernokrylov <CodeDesign2763@gmail.com>
 * 
 * This is a part of ACDG.
 * This program is free software: you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  
 * If not, see <https://www.gnu.org/licenses/>.
*/

package ACDG;
import static java.lang.System.out;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import static java.lang.System.out;
import java.io.FileWriter;
import java.util.ArrayList;

enum ProgramLanguage {
	JAVA,
	CSHARP,
	CPP
}

/**
 * Класс реализует Модель - компонент паттерна MVP.
 */
 
class Model implements ModelScannerIface, ModelRelationIface, 
		ModelFWCIface, ACDGEventListener, ACDGEventSource {

	private ArrayList<Relation> relations;
	private ArrayList<FileWithClass> filesWithClasses;
	private ArrayList<String> classes;
	private String projectName;
	private String path2Java;
	private ParamProcMode paramProcMode;
	
	/* Результат генерации диаграммы классов */
	private boolean classDiagrGenResult;
	
	private ArrayList<ACDGEventListener> eventListeners;
	
	private CProgramLanguage cProgramLanguage;
	
	/* Running under Windows? */
	private static boolean fWindows=System.getProperty("os.name")
			.startsWith("Windows");
	
	public boolean getClassDiagrGenResult()	{
		return classDiagrGenResult;
	}
	
	@Override
	public String getPath2Java() {
		return path2Java;
	}
	
	@Override
	public void addACDGEventListener(ACDGEventListener listener) {
		eventListeners.add(listener);
	}	
	
	private void fireEvent(ACDGEvent event) {
		for (ACDGEventListener listener : eventListeners) {
			listener.onACDGEventReceived(event);
		}
	}
	
	@Override
	public void onACDGEventReceived(ACDGEvent event) {
		if (event.getEventType() == ACDGEventType.TEXTMESSAGE)
			fireEvent(event);
	}
	
	public Model(CProgramLanguage pl) {
		cProgramLanguage=pl;
		relations=new ArrayList<Relation>();
		filesWithClasses=new ArrayList<FileWithClass>();
		classes = new ArrayList<String>();
		projectName="DefaultProjectName";
		classDiagrGenResult=false;
		path2Java="java";
		eventListeners = new ArrayList<ACDGEventListener>();
		paramProcMode=ParamProcMode.ONLY_DATATYPE;
	}
	
	public Model(CProgramLanguage pl, String pName) {
		cProgramLanguage=pl;
		relations=new ArrayList<Relation>();
		filesWithClasses=new ArrayList<FileWithClass>();
		classes = new ArrayList<String>();
		projectName=pName;
		classDiagrGenResult=false;
		path2Java="java";
		eventListeners = new ArrayList<ACDGEventListener>();
		paramProcMode=ParamProcMode.ONLY_DATATYPE;
	}
	
	public Model(CProgramLanguage pl, String pName, String p2J) {
		cProgramLanguage=pl;
		relations=new ArrayList<Relation>();
		filesWithClasses=new ArrayList<FileWithClass>();
		classes = new ArrayList<String>();
		projectName=pName;
		classDiagrGenResult=false;
		path2Java=p2J;
		eventListeners = new ArrayList<ACDGEventListener>();
		paramProcMode=ParamProcMode.ONLY_DATATYPE;
	}
	
	public Model(CProgramLanguage pl, String pName, String p2J,
			ParamProcMode ppm) {
		cProgramLanguage=pl;
		relations=new ArrayList<Relation>();
		filesWithClasses=new ArrayList<FileWithClass>();
		classes = new ArrayList<String>();
		projectName=pName;
		classDiagrGenResult=false;
		path2Java=p2J;
		eventListeners = new ArrayList<ACDGEventListener>();
		paramProcMode=ppm;
		
	}
	
	/* Имеется ли такое отношение */
	private boolean hasRelation(Relation r) {
		return relations.contains(r);
	}
	
	@Override
	public void addRelation(Relation r) {
		if (!hasRelation(r)) {
			relations.add(r);
			//out.println("Такого отношения еще не было");
		} else {
			//out.println("Такое отношение уже есть");
		}
	}
	
	/* Возвращает имя класса по индексу файла с классом */
	@Override
	public String getClassName(int index) {
		return classes.get(index);
	}
	
	/* Возвращает индекс класса по его имени */
	@Override
	public int getClassInd(String s) {
			
		return classes.indexOf(s);
	}
	
	/* Добавить файл */
	public void addFileWithClass(String path2File) {
		FileWithClass newFWC = 
				new FileWithClass(path2File, cProgramLanguage,
				this,this,this,paramProcMode);
		newFWC.addACDGEventListener(this);
				
		filesWithClasses.add(newFWC);
		classes.add(newFWC.getClassName());
	}
	
	/* Сгенерировать код PlantUML для отношений */
	private String genPlantUMLCode4Relations() {
		String res="\n";
		for (Relation r : relations) {
			res += r.genPlantUMLCode() + "\n";
		}
		return res;
	}
	
	/* Путь до итогового PlantUML файла */
	public String getPath2FinalPlantUMLFile() {
		String res;
		if (!fWindows) {
			res="../output/"+projectName+".plantuml";
		} else {
			res="..\\output\\"+projectName+".plantuml";
		}
		return res;
	}
	
	/* Путь до итогового PlantUML файла */
	public String getPath2FinalPlantUMLDiagram() {
		String res;
		if (!fWindows) {
			res="../output/"+projectName+".png";
		} else {
			res="..\\output\\"+projectName+".png";
		}
		return res;
	}
	
	/* Сгенерировать итоговый PlantUML код */
	public void genFinalPlantUMLFile() {
		try {
			FileWriter writer = 
					new 
					FileWriter(getPath2FinalPlantUMLFile(),false);
			/* Добавляем @startuml */
			writer.write("@startuml\n");
			
			/* Добавляем описание классов для всех файлов проекта */
			for (FileWithClass fwc : filesWithClasses) {
				fwc.genClassDescr();
				writer.write(fwc.conv2PlantUMLString());
			}
			
			/* Добавляем описание отношений */
			writer.write(genPlantUMLCode4Relations());
			
			/* Добавляем @enduml */
			writer.write("@enduml\n");
			
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* Сгенерировать диаграмму классов */
	public void genClassDiagr() {
		classDiagrGenResult=true;
		Process proc;
		
		File NULL_F = new File(
          (System.getProperty("os.name")
                     .startsWith("Windows") ? "NUL" : "/dev/null"));
		try {
			
			var processBuilder = new ProcessBuilder();
			String path2PlantUML = (fWindows) ? 
					"..\\bin\\plantuml.jar" 
					: "../bin/plantuml.jar";
			processBuilder.command(path2Java,
					"-jar",
					path2PlantUML,
					"-tpng", getPath2FinalPlantUMLFile());
			processBuilder.redirectOutput(NULL_F);
			processBuilder.redirectError(NULL_F);
			proc = processBuilder.start();
			proc.waitFor();
		
		} catch (Exception e) { 
			classDiagrGenResult=false;
			e.printStackTrace();
		} 
	}
	
	/* Добавить класс */
	@Override
	public void addClass(String className) {
		classes.add(className);
	}
}
