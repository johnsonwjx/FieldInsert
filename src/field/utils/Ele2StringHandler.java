package field.utils;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class Ele2StringHandler {
	private Logger logger = LogManager.getLogger(Ele2StringHandler.class.getName());
	private XMLWriter writer = null;
	private StringWriter out;

	public Ele2StringHandler(StringWriter out, OutputFormat format) {
		this.out = out;
		writer = new XMLWriter(out, format);
	}

	public void write(Element ele) {
		try {
			writer.write(ele);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void write(String str) {
		try {
			writer.write(str);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void println() {
		try {
			writer.println();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void release() {
		try {
			if (out != null) {
				out.close();
			}
			if (writer != null) {
				writer.close();

			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
