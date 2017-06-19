package com.ruvla.plsos.agg.model.configuration.defaults;

import java.io.IOException;
import java.util.Arrays;
import java.util.Queue;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.ruvla.plsos.agg.model.PlsosConstants;
import com.ruvla.plsos.agg.model.datasend.OutputFormatter;
import com.ruvla.plsos.agg.model.dataview.AggDataUnit;

public class DefaultJsonOutputFormatter extends OutputFormatter {

	public DefaultJsonOutputFormatter() {
		super();
	}

	@Override
	public Object format(Queue<? extends AggDataUnit> unitList) {
		if (format.equalsIgnoreCase(PlsosConstants.FORMAT_JSON)) {
			ObjectMapper mapper = new ObjectMapper();
			String jsonOutput = null;
			try {
				jsonOutput = mapper.writeValueAsString(unitList);
			} catch (JsonGenerationException e) {
				jsonOutput = "json generation exception";
				e.printStackTrace();
			} catch (JsonMappingException e) {
				jsonOutput = "json mapping exception";
				e.printStackTrace();
			} catch (IOException e) {
				jsonOutput = "json IO exception";
				e.printStackTrace();
			}
			return jsonOutput;
		} else if (format.equalsIgnoreCase(PlsosConstants.FORMAT_RAW)) {
			return Arrays.deepToString(unitList.toArray());
		} else {
			return "Not supported by " + this.getClass().getSimpleName();
		}
	}

}
