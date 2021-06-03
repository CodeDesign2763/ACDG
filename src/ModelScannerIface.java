/*
 * Интерфейс ModelScannerIface
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
import java.util.ArrayList;


/**
 * Интерфейс модели, который используется стратегией распознавания
 * XML файла для добавления обнаруженных отношений
 */
interface ModelScannerIface {
	/* Добавить отношение */
	public void addRelation(Relation r);
	
	public void addClass(String className);
	
	/* Есть ли такой класс */
}

