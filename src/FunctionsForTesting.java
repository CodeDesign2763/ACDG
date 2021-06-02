/*
 * Класс FunctionsForTesting
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

package FunctionsForJUnit;
import static java.lang.System.out;

import java.nio.file.Files;
import java.nio.file.Paths;
import static java.lang.System.out;
import java.io.IOException;
import java.lang.Exception;
import java.io.File;
import java.util.Arrays;

/* Для тестирования приватных методов */
import java.lang.reflect.*;

/**
 * Статический класс с функциями для работы с JUnit
 */

public class FunctionsForTesting {
	
	/* Проверка выполнения приватного метода, возвращающего void
	 * записывающего что-то в файл 
	 * obj - объект класса className, метод которого нужно выполнить
	 * */
	public static boolean checkPrivMethodWOParameters(Object obj, String className, 
			String methodName, String path2OutputFile,
			String path2CorrectFile) {
		boolean result=false;
		byte[] CorrectResult;
		byte[] TestResult;
		Method targetMethod;
		Object retValue;
		Class fwcClass;

		try {
			
			
			/* Сделаем тестируемый метод видимым при помощи
			 * рефлексии */
			fwcClass=Class.forName(className);
			targetMethod=fwcClass.getDeclaredMethod(methodName);
			targetMethod.setAccessible(true);
			retValue=targetMethod.invoke(obj);
		
			/* Простейший способ сравнить 2 файла */
			CorrectResult= Files.readAllBytes(
					Paths.get(path2CorrectFile));
			TestResult= Files.readAllBytes(Paths.get(
					path2OutputFile));
			
		
			result=Arrays.equals(CorrectResult,TestResult);
		}	catch (IOException e) {
			out.println("Файлы не найдены");
			e.printStackTrace();
			result=false;
			
		}	catch (Exception e) {
			result=false;
			e.printStackTrace();
		}
		
		return result;
	}
	
	/* Метод возвращает значение приватного метода 
	 * obj - объект, метод класса которого будет исполняться
	 * clArr - массив типов параметров данных (Integer.class и т.д.) 
	 * valArr - массив значений параметров 
	 * Если параметров нет, то вместо clArr и valArr указывать null */
	public static Object checkPrivMethod(Object obj,  
			String className, String methodName, 
			Class[] clArr, Object[] valArr) {
	
		Object result=null;
		Method targetMethod;
		Object retValue;
		Class fwcClass;

		try {
		
			/* Сделаем тестируемый метод видимым при помощи
			* рефлексии */
			fwcClass=Class.forName(className);
			targetMethod=fwcClass.getDeclaredMethod(
					methodName,clArr);
			targetMethod.setAccessible(true);
			result=targetMethod.invoke(obj,valArr);
		
		}	catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
			
	}
}
