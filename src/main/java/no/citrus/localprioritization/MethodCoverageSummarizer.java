package no.citrus.localprioritization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.citrus.localprioritization.model.ClassCover;
import no.citrus.localprioritization.model.MethodCover;
import no.citrus.localprioritization.model.ProcessedMethodCall;

public class MethodCoverageSummarizer {

	private final Map<String, ClassCover> coveredClasses;
	private Map<String, MethodCover> transitiveMethodCalls;

	public MethodCoverageSummarizer(Map<String, ClassCover> coveredClasses, ClassCover coveredTestCase) {
		this.coveredClasses = coveredClasses;
		
		transitiveMethodCalls = summarizeCoverageOfTestCase(coveredTestCase);
	}

	private Map<String, MethodCover> summarizeCoverageOfTestCase(ClassCover coveredTestCase) {
		Map<String, MethodCover> methods = coveredTestCase.getMethods();
		
		Map<String, MethodCover> transitiveMethodCalls = new HashMap<String, MethodCover>();
		
		for (MethodCover methodCover : methods.values()) {
			List<ProcessedMethodCall> methodCalls = methodCover.getMethodCalls();
			for (ProcessedMethodCall processedCall : methodCalls) {
				ClassCover calledClass = coveredClasses.get(processedCall.getClassName());
				if (calledClass != null) {
					MethodCover calledMethod = calledClass.getMethods().get(processedCall.getMethodName());
					if (calledMethod != null) {
						transitiveMethodCalls = summarizeRecursively(calledMethod, transitiveMethodCalls);
					}
				}
			}
		}
		
		return transitiveMethodCalls;
	}

	private Map<String, MethodCover> summarizeRecursively(MethodCover methodCall,
			Map<String, MethodCover> transitiveMethodCalls) {
		
		if (transitiveMethodCalls.get(makeClassAndMethodNameString(methodCall)) == null) {
			
			transitiveMethodCalls.put(makeClassAndMethodNameString(methodCall), methodCall);
			
			List<ProcessedMethodCall> methodCalls = methodCall.getMethodCalls();
			for (ProcessedMethodCall processedCall : methodCalls) {
				ClassCover calledClass = coveredClasses.get(processedCall.getClassName());
				if (calledClass != null) {
					MethodCover calledMethod = calledClass.getMethods().get(processedCall.getMethodName());
					if (calledMethod != null) {
						transitiveMethodCalls = summarizeRecursively(calledMethod, transitiveMethodCalls);
					}
				}
			}
		}
		
		return transitiveMethodCalls;
	}

	private String makeClassAndMethodNameString(MethodCover methodCall) {
		return methodCall.getClassName() + "." + methodCall.getMethodName();
	}

	public Map<String, MethodCover> getSummarizedCoverage() {
		return transitiveMethodCalls;
	}
}
