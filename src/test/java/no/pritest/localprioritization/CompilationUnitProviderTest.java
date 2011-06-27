/**
    This file is part of Pritest.

    Pritest is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Pritest is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
*/

package no.pritest.localprioritization;

import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


public class CompilationUnitProviderTest {
	private List<File> files;
	
	@Before
	public void setup(){
		files = ClassListProvider.getFileList(new File("src/main/java/"), new String[]{".java"});
	}
	
	@Test
	public void should_return_list_of_CompilationUnit_objects() throws ParseException, IOException{
		List<CompilationUnit> compilationUnits = CompilationUnitProvider.getCompilationUnits(files);
		assertThat(compilationUnits.isEmpty(), equalTo(false));
	}
}
