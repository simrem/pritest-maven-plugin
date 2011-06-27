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

package no.citrus.runner.junit.priority;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import no.citrus.util.JavaPackageUtil;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;


public class GitStatusProvider {
	
	private File basedir;
	private final String sourceDirectory;
	private final String testSourceDirectory;

	public GitStatusProvider(File basedir, String sourceDirectory, String testSourceDirectory) {
		this.basedir = basedir;
		this.sourceDirectory = sourceDirectory.replace(basedir + File.separator, "");
		this.testSourceDirectory = testSourceDirectory.replace(basedir + File.separator, "");;
	}
	
	public List<String> getGitStatusPriorityList() throws NoWorkTreeException, IOException {
		List<String> gitStatusList = new ArrayList<String>();
		gitStatusList = callGitStatus();
		List<String> finalList = new ArrayList<String>();
		
		finalList.addAll(gitStatusList);

		return finalList;
	}
	
	public List<String> callGitStatus() throws NoWorkTreeException, IOException {
		File repoPath = new File(basedir.getAbsolutePath() + "/.git");
		RepositoryBuilder repoBuilder = new RepositoryBuilder();
		Repository repo = repoBuilder.setGitDir(repoPath).build();
		
		Git git = new Git(repo);
		Status status = git.status().call();
		
		List<String> gitStatusList = new ArrayList<String>();
		
		addTestCasesToList(status.getUntracked(), gitStatusList);
		addTestCasesToList(status.getModified(), gitStatusList);
		
		return gitStatusList;
	}

	private void addTestCasesToList(Set<String> untracked, 
			List<String> gitStatusList) {
		
		JavaPackageUtil jpu = 
			new JavaPackageUtil(new String[]{sourceDirectory, testSourceDirectory});
		
		for (String untrackedFile : untracked) {
			String testCaseName = jpu.prepareTestCaseName(untrackedFile);
			if (testCaseName != null && !gitStatusList.contains(testCaseName)) {
				gitStatusList.add(testCaseName);
			}
		}
	}
}
