/*
 * Класс Repo
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
 * Класс RelationRepo, реализующий шаблонированный интефейс 
 * Repository
 */
class Model implements ModelScannerIface, ModelRelationIface {
	//private IRepository<Relation> relations;
	//private IRepository<FileWithClass> filesWithClasses;
	private ArrayList<Relation> relations;
	private ArrayList<FileWithClass> filesWithClasses;
	private ArrayList<String> classes;
	private String projectName;
	
	
	private CProgramLanguage cProgramLanguage;
		
	public Model(CProgramLanguage pl) {
		cProgramLanguage=pl;
		relations=new ArrayList<Relation>();
		filesWithClasses=new ArrayList<FileWithClass>();
		classes = new ArrayList<String>();
		projectName="DefaultProjectName";
	}
	
	public Model(CProgramLanguage pl, String pName) {
		cProgramLanguage=pl;
		relations=new ArrayList<Relation>();
		filesWithClasses=new ArrayList<FileWithClass>();
		classes = new ArrayList<String>();
		projectName=pName;
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
				this,this);
				
		filesWithClasses.add(newFWC);
		classes.add(newFWC.getClassName());
	}
	
	/* Сгенерировать код PlantUML для отношений */
	public String genPlantUMLCode4Relations() {
		String res="\n";
		for (Relation r : relations) {
			res += r.genPlantUMLCode() + "\n";
		}
		return res;
	}
	
	/* Путь до итогового PlantUML файла */
	public String getPath2FinalPlantUMLFile() {
		String res="../output/"+projectName+".plantuml";
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
	
	
	/* Добавить класс */
	@Override
	public void addClass(String className) {
		classes.add(className);
	}
	
	
		
}
