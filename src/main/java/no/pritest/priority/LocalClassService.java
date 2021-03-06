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

package no.pritest.priority;

import no.pritest.prioritization.methodcoverage.ClassListProvider;

import java.io.File;
import java.util.List;


public class LocalClassService implements ClassService {
	private final File testClassDirectory;
	
	public LocalClassService(File testClassDirectory){
		this.testClassDirectory = testClassDirectory;
	}

	@Override
	public List<String> getClassList() {
		return ClassListProvider.getClassList(this.testClassDirectory);
	}
}
