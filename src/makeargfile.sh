#!/bin/bash

#Script for automatic creation of an javac argfile with paths 
# to source files.
#Run every time after adding a new class!
 
#Copyright 2021 Alexander Chernokrylov <CodeDesign2763@gmail.com>
 
#This is a part of ACDG.
#This program is free software: you can redistribute it and/or 
#modify it under the terms of the GNU General Public License as 
#published by the Free Software Foundation, either version 3 of the 
#License, or (at your option) any later version.

#This program is distributed in the hope that it will be useful,
#but WITHOUT ANY WARRANTY; without even the implied warranty of
#MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#GNU General Public License for more details.

#You should have received a copy of the GNU General Public License
#along with this program.  
#If not, see <https://www.gnu.org/licenses/>.

if [[ -e "sourcefilepaths.txt" ]]
then
  rm sourcefilepaths.txt
fi
touch sourcefilepaths.txt
find ./main/java/com/acdg -maxdepth 1 -name "*.java" >> sourcefilepaths.txt
find ./test/java/com/acdg -maxdepth 1 -name "*.java" >> sourcefilepaths.txt
find ./test/java/com/functionsforjunit -maxdepth 1 -name "*.java" >> sourcefilepaths.txt
