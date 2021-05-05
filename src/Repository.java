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
import java.util.ArrayList;

/**
 * Класс RelationRepo, реализующий шаблонированный интефейс 
 * Repository
 */
class Repository<T> implements IRepository<T> {
	
	private ArrayList<T> list;
	
	public Repository() {
		list = new ArrayList<T>();
	}
	
	@Override
	public void add(T entity) {
		list.add(entity);
	}
	
	@Override
	public T get(int index) {
		return list.get(index);
	}
	
	@Override
	public void update(int index, T entity) {
		list.remove(index);
		list.add(index,entity);
	}
	
	@Override
	public void delete(int index) {
		list.remove(index);
	}
	
}
