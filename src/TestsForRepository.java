/*
 * Тесты для класса RelationRepo
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
 * Класс c набором тестов для класса ClassFromCode
 */
class TestsForRepository {
	
	@Test
	@DisplayName("add-get-update-delete")
	public void testAGUD() {
		Repository<Relation> rr = new Repository<Relation>();
				
		/* Проверка add и get */
		rr.add(new Relation(1,2,RelationCode.DEPENDENCY, "",null));
		assertEquals(rr.get(0).conv2String(),new Relation(1,2,RelationCode.DEPENDENCY, "",null).conv2String());
		
		/* Проверка update */
		rr.add(new Relation(3,4,RelationCode.ASSOCIATION, "",null));
		rr.add(new Relation(5,6,RelationCode.COMPOSITION, "",null));
		rr.update(1,new Relation(10,20,RelationCode.REALIZATION, "",null));
		assertEquals(rr.get(1).conv2String(),new Relation(10,20,RelationCode.REALIZATION, "",null).conv2String());
		
		/* Проверка delete */
		rr.delete(1);
		assertEquals(rr.get(1).conv2String(),new Relation(5,6,RelationCode.COMPOSITION, "",null).conv2String());
		
	}	
}
