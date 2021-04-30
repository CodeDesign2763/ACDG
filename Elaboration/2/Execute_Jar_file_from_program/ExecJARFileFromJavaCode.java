/*
 * MWE для демонстрации возможности запуска JAR-файла из кода на java
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
 * If not, see <https://www.gnu.org/licenses/>.
*/

import static java.lang.System.out;
import java.lang.Exception;
import java.io.InputStream;

/**
 * Класс предназначен для шифрования и расшифрования
 * символьных строк (String) при помощи шифров Цезаря и Виженера.
 * Реализован как простой шифр Виженера, так и шифр Виженера с ключом.
 */
public class ExecJARFileFromJavaCode {
	public static void main (String[] args) {
		Process proc;
		InputStream inputStream;
		InputStream errStream;
		
		try {
			proc = Runtime.getRuntime().exec("java -jar plantuml.jar -tpng united.plantuml");
			proc.waitFor();
			
			/* Получение текстового вывода программы */
			inputStream = proc.getInputStream();
			errStream = proc.getErrorStream();
			byte inp[]=new byte[inputStream.available()];
			inputStream.read(inp,0,inp.length);
			out.println("Вывод STDIN:\n " + new String(inp));

			byte err[]=new byte[errStream.available()];
			errStream.read(err,0,err.length);
			out.println("Вывод STDERR:\n" + new String(err));
			
			out.println("Выполнение программы завершено");
		} catch (Exception e) {};

		

	}
	
}

