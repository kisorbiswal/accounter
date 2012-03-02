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

			if (src != null) {
				src.println("private static Dictionary cache;");
				src.println("private static ArrayList<String> messgaeUsageOrder;");
				src.println("private static HashMap<String,Integer> messgaeUsageCount;");

				// If it is TRUE then only we'll track MessageStatistics
				src.print("public static final boolean TRACK_MESSAGE_STATISTICS = Boolean.TRUE;");

				src.println(classType.getSimpleSourceName()
						+ "Generated() {if (cache == null) {cache = Dictionary.getDictionary(\""
						+ classType.getSimpleSourceName() + "\");}"
						+ "messgaeUsageOrder = new ArrayList<String>();"
						+ "messgaeUsageCount = new HashMap<String,Integer>();"
						+ "}");

				src.println("@Override");
				src.print("public ArrayList<String> getMessagesUsageOrder(){"
						+ "return messgaeUsageOrder;}");
				src.println("@Override");
				src.print("public HashMap<String,Integer> getMessgaesUsageCount(){"
						+ "return messgaeUsageCount;}");
				src.println("private String value(String key, HashMap<String,String> values) {"
						+ "String string = cache.get(key);"
						+ "for (String name : values.keySet()) "
						+ "{String value=values.get(name); string = string.replace(\"{\" + name + \"}\", value);}"
						+ "return string;}");
				for (JMethod method : classType.getMethods()) {
					src.println("@Override");
					src.println(method.getReadableDeclaration(false, true,
							true, true, true)
							+ "{HashMap<String,String> map=new HashMap<String,String>();");
					JParameter[] parameters = method.getParameters();

					for (JParameter parameter : parameters) {
						src.print("map.put(\"" + parameter.getName()
								+ "\", String.valueOf(" + parameter.getName()
								+ "));");
					}
					src.print("String key =\"" + method.getName() + "\";");
					src.print("if(TRACK_MESSAGE_STATISTICS){");
					src.print("if(!messgaeUsageOrder.contains(key)){");
					src.print("messgaeUsageOrder.add(key);}");

					src.print("Integer count = messgaeUsageCount.get(key);");
					src.print("if(messgaeUsageCount.containsKey(key)){"
							+ "messgaeUsageCount.put(key,messgaeUsageCount.get(key)+1);"
							+ "}else{" + "messgaeUsageCount.put(key,1);" + "}");
					src.print("}");
					src.print("return value(key,map);");
					src.println("}");
				}

				src.commit(logger);
			}
			// log.info(typeName + " Generated");
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
		composer.addImplementedInterface("com.vimukti.accounter.web.client.externalization.IMessageStats");

		// Need to add whatever imports your generated class needs.
		composer.addImport(classType.getQualifiedSourceName());
		composer.addImport("com.google.gwt.i18n.client.Dictionary");
		composer.addImport("java.util.HashMap");
		composer.addImport("java.util.ArrayList");

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
