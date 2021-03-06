/*
 * Тесты для класса ClassFromCode
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
 * Класс c набором тестов для класса FileWithClass
 */
class TestsForFileWithClass {
	
	/* Язык программирования Java */
	private static CProgramLanguage javaPL;
	static {
		javaPL = AvailablePLs.getPLbyEnum(ProgramLanguage.JAVA);
	}
	
	@Test
	@DisplayName("Method getClassName")
	public void testConstructorAndConv2String() {
		FileWithClass cfc1 = new FileWithClass("ClassA.java",
				javaPL
				);
		assertEquals(cfc1.getClassName(),"ClassA");
	}	
	
	@Test
	@DisplayName("Method getFileName")
	public void testGetFileName() {
		FileWithClass fwc1= 
				new FileWithClass("/home/user1/ClassB.java",
				javaPL);
		assertEquals(fwc1.getFileName(),"ClassB.java");
	}
	
	@Test
	@DisplayName("Preliminary deletion of comments")
	public void testCommentDeletion2() {
		byte[] CorrectResult;
		byte[] TestResult;
		Method targetMethod;
		Object retValue;
		Class fwcClass;
		//JavaCommentsDeletionTest.txt_wo_comments
		
		try {
			FileWithClass fwc1 = new FileWithClass(
					"../data/JavaCommentsDeletionTest.txt",
					javaPL);
			
			/* Если файл существует - удалим его */
			Files.deleteIfExists(Paths.get(
					"../temp/" + 
					"JavaCommentsDeletionTest.txt_wo_comments"));
			
			assertEquals(true, 
					FunctionsForTesting.checkPrivMethodWOParameters(
					fwc1, "com.acdg.FileWithClass", 
					"deleteCommentsAndOtherStuff", "../temp/" + 
					"JavaCommentsDeletionTest.txt_wo_comments", 
					"../data/" + 
					"JavaCommentsDeletionTest_CORRECT_OUTPUT.txt"));
			
		}	catch (IOException e) {
			out.println("Файлы не найдены");
			e.printStackTrace();
			fail();
		}	catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	@DisplayName("Parsing file SimpleClassXMLConvTest.txt")
	public void testSimpleClassXMLConv() {
		FileWithClass fwc1 = new 
				FileWithClass("../data/SimpleClassXMLConvTest.txt",
				javaPL);
		fwc1.convSourceFile2XML();
		fwc1.showMethodsList();
		assertEquals(true,fwc1.getConv2XMLResult());
	}
	
	@Test
	@DisplayName("Parsing a non-existent file")
	public void testXMLConvIncorrectFilePath() {
		FileWithClass fwc1 = new FileWithClass("../data/source2.txt",
				javaPL);
		fwc1.convSourceFile2XML();
		
		assertEquals(false,fwc1.getConv2XMLResult());
	}
	
	@Test
	@DisplayName("Parsing SpecialityForm.java")
	public void testSpecialityFormXMLConv() {
		FileWithClass fwc1 = 
				new FileWithClass("../data/SpecialityForm.java",
				javaPL);
		fwc1.convSourceFile2XML();
		fwc1.showMethodsList();
		
		assertEquals(true,fwc1.getConv2XMLResult());
		
	}
	
	@Test
	@DisplayName("Parsing MainForm.java")
	public void testMainFormXMLConv() {
		FileWithClass fwc1 = 
				new FileWithClass("../data/MainForm.java",
				javaPL);
		fwc1.convSourceFile2XML();
		fwc1.showMethodsList();
		assertEquals(true,fwc1.getConv2XMLResult());
	}
	
	@Test
	@DisplayName("Testing universal function for testing")
	public void testUnivTestFunction() {
		FileWithClass fwc1 = 
				new FileWithClass("../data/MainForm.java",
				javaPL);
		assertEquals("../temp/MainForm.java_wo_comments",
				(String) FunctionsForTesting.checkPrivMethod(
				fwc1, "com.acdg.FileWithClass",
				"getPath2FileWOComments",null,null));
		
	}
}
