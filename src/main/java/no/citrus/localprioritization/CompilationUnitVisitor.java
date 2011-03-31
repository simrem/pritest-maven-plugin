package no.citrus.localprioritization;

import java.util.ArrayList;
import java.util.List;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;

public class CompilationUnitVisitor extends VoidVisitorAdapter<Object> {

	private List<ClassType> types;
	private List<String> importStatements;
	
	public CompilationUnitVisitor() {
		this.types = new ArrayList<ClassType>();
		this.importStatements = new ArrayList<String>();
	}
	
	@Override
	public void visit(CompilationUnit cu, Object obj) {
		if (cu.getTypes() != null) {
			for (TypeDeclaration td : cu.getTypes()) {
				ClassOrInterfaceDeclarationVisitor cidv = new ClassOrInterfaceDeclarationVisitor();
				td.accept(cidv, null);
				this.types = cidv.getTypes();
			}
		}
		
		if (cu.getImports() != null) {
			for (ImportDeclaration id : cu.getImports()) {
				ImportVisitor iv = new ImportVisitor();
				id.accept(iv, null);
				this.importStatements.add(iv.getImportStatement());
			}
		}
	}

	public List<ClassType> getTypes() {
		return types;
	}

	public List<String> getImportStatements() {
		return this.importStatements;
	}
	
	private class ImportVisitor extends VoidVisitorAdapter<String> {

		private String importStatement;
		
		public ImportVisitor() {
			this.importStatement = null;
		}

		@Override
		public void visit(ImportDeclaration n, String arg) {
			n.getName().accept(this, arg);
		}

		@Override
		public void visit(NameExpr n, String arg) {
			this.importStatement = n.getName() + "." + this.importStatement;
		}

		@Override
		public void visit(QualifiedNameExpr n, String arg) {
			if (this.importStatement == null) {
				this.importStatement = n.getName();
			} else {
				this.importStatement = n.getName() + "." + this.importStatement;
			}
			
			if (n.getQualifier() != null) {
				n.getQualifier().accept(this, arg);
			}
		}

		public String getImportStatement() {
			return importStatement;
		}
	}
}
