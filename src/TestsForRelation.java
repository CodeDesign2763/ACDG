/*
 * Тесты для класса Relation
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

/**
 * Класс c набором тестов для класса Relation
 */
class TestsForRelation {
	
	@Test
	@DisplayName("Конструктор и метод conv2String")
	public void testConstructorAndConv2String() {
		Relation rel1 = new Relation(1,2,RelationCode.INHERITANCE);
		assertEquals(rel1.conv2String(),"1 наследует 2");
	}	
	
	@Test
	@DisplayName("Методы get")
	public void testGetMethods() {
		Relation rel1 = new Relation(10,20,RelationCode.ASSOCIATION);
		assertEquals(rel1.getClass1Ind(),10);
		assertEquals(rel1.getClass2Ind(),20);
		assertEquals(rel1.getRelationCode(),RelationCode.ASSOCIATION);
	}	
}
