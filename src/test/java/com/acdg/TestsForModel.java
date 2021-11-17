/*
 * Тесты для класса Model
 * 
 * Copyright 2021 Alexander Chernokrylov <CodeDesign2763@gmail.com>
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
 * If not, see <https://www.gnu.org/licenses/>. */

package com.acdg;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.lang.System.out;
import java.io.IOException;
import java.util.Arrays;
import java.lang.Exception;
import java.io.File;

/* Для тестирования приватных методов */
import java.lang.reflect.*;

import com.functionsforjunit.FunctionsForTesting;
/**
 * Класс c набором тестов для класса Model
 */
class TestsForModel {
	
	@Test
	@DisplayName("Generating PlantUML code for some files")
	public void testPlantUMLGeneration4SomeFiles() {
		byte[] CorrectResult=null;
		byte[] TestResult=null;
		
		/* Отладка */
		//String pwdString = Path.of("").toAbsolutePath().toString();
		//System.out.println("!!!!! PWD IS:"+ pwdString);
		//assertEquals("",pwdString);
		/* Выяснили, что рабочий каталог - src */
		/* /home/user1/text/edu/VSTU/ВвРПО/Контрольная работа/src */
		
		Model m1 = new Model(
				AvailablePLs.getPLbyEnum(ProgramLanguage.JAVA));
		m1.addFileWithClass("main/java/com/acdg/AvailablePLs.java");
		m1.addFileWithClass("main/java/com/acdg/CProgramLanguage.java");
		m1.addFileWithClass("main/java/com/acdg/Relation.java");
		m1.addFileWithClass("main/java/com/acdg/JavaProcStrategy.java");
		m1.genFinalPlantUMLFile();
		
				
		try {

			/* Простейший способ сравнить 2 файла */
			CorrectResult= Files.readAllBytes(Paths.get(
					"../data/" + 
					"DefaultProjectName.plantuml_CORRECT"));
			TestResult= Files.readAllBytes(Paths.get(
					"../output/" + 
					"DefaultProjectName.plantuml"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		assertEquals(true,
				Arrays.equals(CorrectResult,TestResult));
	}
	
	
	@Test
	@DisplayName("Generating class diagram for some files")
	public void testDiagrGeneration() {
		byte[] CorrectResult=null;
		byte[] TestResult=null;
		String pName="FirstDiagram";
		
		Model m1 = new Model(
				AvailablePLs.getPLbyEnum(ProgramLanguage.JAVA),
				pName);
		m1.addFileWithClass("main/java/com/acdg/AvailablePLs.java");
		m1.addFileWithClass("main/java/com/acdg/CProgramLanguage.java");
		m1.addFileWithClass("main/java/com/acdg/Relation.java");
		m1.addFileWithClass("main/java/com/acdg/JavaProcStrategy.java");
		m1.genFinalPlantUMLFile();
		m1.genClassDiagr();
		
		try {

			/* Простейший способ сравнить 2 файла */
			CorrectResult= Files.readAllBytes(Paths.get(
					"../data/" + 
					pName+".png_CORRECT"));
			TestResult= Files.readAllBytes(Paths.get(
					"../output/" + 
					pName + ".png"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		assertEquals(true,
				Arrays.equals(CorrectResult,TestResult));
	}
}
