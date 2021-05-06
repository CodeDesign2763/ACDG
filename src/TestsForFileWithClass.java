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

package ACDG;

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

/**
 * Класс c набором тестов для класса FileWithClass
 */
class TestsForFileWithClass {
	
	@Test
	@DisplayName("Метод getClassName")
	public void testConstructorAndConv2String() {
		FileWithClass cfc1 = new FileWithClass("ClassA.java");
		assertEquals(cfc1.getClassName(),"ClassA");
	}	
	
	@Test
	@DisplayName("Метод getFileName")
	public void testGetFileName() {
		FileWithClass fwc1= 
				new FileWithClass("/home/user1/ClassB.java");
		assertEquals(fwc1.getFileName(),"ClassB.java");
	}
	
	@Test
	@DisplayName("Предварительное удаление комментариев")
	public void testCommentDeletion() {
		byte[] CorrectResult;
		byte[] TestResult;
		Method targetMethod;
		Object retValue;
		Class fwcClass;
		//JavaCommentsDeletionTest.txt_wo_comments
		try {
			FileWithClass fwc1 = new FileWithClass(
					"../data/JavaCommentsDeletionTest.txt");
			
			/* Если файл существует - удалим его */
			Files.deleteIfExists(Paths.get(
					"../temp/" + 
					"JavaCommentsDeletionTest.txt_wo_comments"));
			
			/* Сделаем тестируемый метод видимым при помощи
			 * рефлексии */
			fwcClass=Class.forName("ACDG.FileWithClass");
			targetMethod=fwcClass.getDeclaredMethod(
					"deleteCommentsAndOtherStuff",
					ProgramLanguage.class);
			targetMethod.setAccessible(true);
			retValue=targetMethod.invoke(fwc1,ProgramLanguage.JAVA);
		
			/* Простейший способ сравнить 2 файла */
			CorrectResult= Files.readAllBytes(Paths.get(
					"../data/" + 
					"JavaCommentsDeletionTest_CORRECT_OUTPUT.txt"));
			TestResult= Files.readAllBytes(Paths.get(
					"../temp/" + 
					"JavaCommentsDeletionTest.txt_wo_comments"));
			
			/* Нужно именно использовать Arrays */
			assertEquals(true,
					Arrays.equals(CorrectResult,TestResult));
		}	catch (IOException e) {
			out.println("Файлы не найдены");
			e.printStackTrace();
			fail();
		}	catch (Exception e) {
			fail();
		}
	}
	
	@Test
	@DisplayName("Синтаксический анализ")
	public void testXMLTreeGeneration() {
		FileWithClass fwc1 = new FileWithClass("../data/source.txt");
		//fwc1.deleteCommentsAndOtherStuff(ProgramLanguage.JAVA);
		fwc1.convSourceFile2XML(ProgramLanguage.JAVA);
	}
	
}
