package com.vimukti.accounter.web.server.i18n;

import java.io.PrintWriter;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class MessagesGenerator extends Generator {

	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {

		JClassType classType;

		try {
			classType = context.getTypeOracle().getType(typeName);
			String className = typeName + "Generated";

			SourceWriter src = getSourceWriter(classType, context, logger);

			src.println("private static Dictionary cache;");

			src.println(classType.getSimpleSourceName()
					+ "Generated() {if (cache == null) {cache = Dictionary.getDictionary(\""
					+ classType.getSimpleSourceName() + "\");}}");
			src.println("private String value(String key, String... values) {"
					+ "String string = cache.get(key);"
					+ "int i = 0;for (String value : values) "
					+ "{string = string.replace(\"{\" + i + \"}\", value);}"
					+ "return string;}");
			for (JMethod method : classType.getMethods()) {
				src.println("@Override");
				src.println(method.getReadableDeclaration(false, true, true,
						true, true) + '{');
				JParameter[] parameters = method.getParameters();
				src.print("return value(\"" + method.getName() + "\"");
				for (JParameter parameter : parameters) {
					src.print(", String.valueOf(" + parameter.getName() + ")");
				}
				src.print(");");
				src.println("}");
			}

			src.commit(logger);
			System.out.println(typeName + " Generated");
			return className;
		} catch (NotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	public SourceWriter getSourceWriter(JClassType classType,
			GeneratorContext context, TreeLogger logger) {
		String packageName = classType.getPackage().getName();
		String simpleName = classType.getSimpleSourceName() + "Generated";
		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(
				packageName, simpleName);
		composer.addImplementedInterface(classType.getQualifiedSourceName());

		// Need to add whatever imports your generated class needs.
		composer.addImport(classType.getQualifiedSourceName());
		composer.addImport("com.google.gwt.i18n.client.Dictionary");

		PrintWriter printWriter = context.tryCreate(logger, packageName,
				simpleName);
		if (printWriter == null) {
			return null;
		} else {
			SourceWriter sw = composer.createSourceWriter(context, printWriter);
			return sw;
		}
	}

}
