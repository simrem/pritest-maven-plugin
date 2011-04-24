package no.citrus.localprioritization;

import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import no.citrus.localprioritization.model.ClassCover;
import no.citrus.localprioritization.model.ClassType;

public class TotalMethodCoverage {
	private List<ClassCover> testCases;

	public TotalMethodCoverage(String pathToProjectSource, String pathToTestSuite)
			throws ParseException, IOException {
		
		Map<String, ClassCover> sourceClassCover = retrieveClassCoverage(pathToProjectSource);
		Map<String, ClassCover> testClassCover = retrieveClassCoverage(pathToTestSuite);
		
		Collection<ClassCover> testCaseCollection = testClassCover.values();
		
	}

	private Map<String, ClassCover> retrieveClassCoverage(String pathToProjectSource)
			throws ParseException, IOException {
		
		List<File> fileList = 
			ClassListProvider.getFileList(new File(pathToProjectSource), new String[] {".java"});
		
		List<CompilationUnit> compilationUnits = CompilationUnitProvider.getCompilationUnits(fileList);
		
		ClassTypeProvider classTypeProvider = new ClassTypeProvider(compilationUnits);
		Map<String, ClassType> classTypes = classTypeProvider.getClassTypes();
		
		MethodCoverageProvider mcp = new MethodCoverageProvider(classTypes, compilationUnits);
		
		return mcp.getMethodCoverage();
	}

	public List<ClassCover> getTestCases() {
		return testCases;
	}
}
