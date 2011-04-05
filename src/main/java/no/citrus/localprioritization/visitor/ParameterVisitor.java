package no.citrus.localprioritization.visitor;

import japa.parser.ast.body.Parameter;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.visitor.VoidVisitorAdapter;

/**
* Created by IntelliJ IDEA.
* User: sveinung
* Date: 4/5/11
* Time: 7:41 PM
* To change this template use File | Settings | File Templates.
*/
public class ParameterVisitor extends VoidVisitorAdapter<Object> {

    private String parameterName = null;

    @Override
    public void visit(Parameter parameter, Object obj) {
        if (parameter.getType() != null) {
            parameter.getType().accept(this, obj);
        }
    }

    @Override
    public void visit(ClassOrInterfaceType cit, Object obj) {
        parameterName = cit.getName();
    }

    public String getParameterName() {
        return parameterName;
    }
}
