/*
 * Тесты для класса ClassElement
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
//import java.lang.reflect.*;

/**
 * Класс c набором тестов для класса ClassElement
 */
class TestsForClassElement {
	@Test
	@DisplayName("ClassElement: int a")
	public void testGenPlantUMLCodeIntA() {
		ClassElement ce=new ClassElement("a", "int",
				ACCESS_MODIFIERS.PUBLIC,
				true,
				false,
				"",false);
		assertEquals("+ {static} a:int",ce.genPlantUMLCode());
	}
		
	@Test
	@DisplayName("ClassElement: void b")
	public void testGenPlantUMLCodeVoidB() {
		ClassElement ce=new ClassElement("b", "void",
				ACCESS_MODIFIERS.PRIVATE,
				false,
				true,
				"(String s)",false);
		assertEquals("- b(String s):void",ce.genPlantUMLCode());
	}
}
