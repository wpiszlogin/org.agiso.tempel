/* org.agiso.tempel.core.processor.xstream.TemplateParamBeanAttributesConverter (12-12-2013)
 * 
 * TemplateParamBeanAttributesConverter.java
 * 
 * Copyright 2013 agiso.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.agiso.tempel.core.processor.xstream;

import org.agiso.tempel.core.fetcher.StringParamFetcher;
import org.agiso.tempel.core.model.beans.TemplateParamBean;
import org.agiso.tempel.core.model.beans.TemplateParamConverterBean;
import org.agiso.tempel.core.model.beans.TemplateParamFetcherBean;
import org.agiso.tempel.core.model.beans.TemplateParamValidatorBean;
import org.agiso.tempel.core.validator.DefaultParamValidator;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * Kowerter XStream obsługujący atrybuty "converter" i "validator" znacznika
 * &lt;param&gt; definicji parametru szablonu.
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
class TemplateParamBeanAttributesConverter extends ReflectionConverter {
	public TemplateParamBeanAttributesConverter(Mapper mapper, ReflectionProvider reflectionProvider) {
		super(mapper, reflectionProvider);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(TemplateParamBean.class);
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		String fetcherClassName = reader.getAttribute("fetcher");
		if(fetcherClassName != null) {
			if(fetcherClassName.isEmpty()) {
				throw new ConversionException("Empty string is invalid 'fetcher' value");
			}
		}

		String converterClassName = reader.getAttribute("converter");
		if(converterClassName != null) {
			if(converterClassName.isEmpty()) {
				throw new ConversionException("Empty string is invalid 'converter' value");
			}
		}

		String validatorClassName = reader.getAttribute("validator");
		if(validatorClassName != null) {
			if(validatorClassName.isEmpty()) {
				throw new ConversionException("Empty string is invalid 'validator' value");
			}
		}

		TemplateParamBean templateParam = (TemplateParamBean)super.unmarshal(reader, context);
		if(templateParam.getFetcher() != null && fetcherClassName != null) {
			throw new ConversionException("Param and attribute 'fetcher' tag defined simultaneously");
		} else if(templateParam.getFetcher() == null) {
			if(fetcherClassName == null) {
				fetcherClassName = getDefaultFetcherClassName();
			}
			templateParam.setFetcher(new TemplateParamFetcherBean()
					.withFetcherClassName(fetcherClassName)
			);
		}
		if(templateParam.getConverter() != null && converterClassName != null) {
			throw new ConversionException("Param and attribute 'converter' tag defined simultaneously");
		} else if(templateParam.getConverter() == null) {
			if(converterClassName == null) {
				converterClassName = getDefaultConverterClassName();
			}
			templateParam.setConverter(new TemplateParamConverterBean()
					.withConverterClassName(converterClassName)
			);
		}
		if(templateParam.getValidator() != null && validatorClassName != null) {
			throw new ConversionException("Param and attribute 'validator' tag defined simultaneously");
		} else if(templateParam.getValidator() == null) {
			if(validatorClassName == null) {
				validatorClassName = getDefaultValidatorClassName();
			}
			templateParam.setValidator(new TemplateParamValidatorBean()
					.withValidatorClassName(validatorClassName)
			);
		}
		return templateParam;
	}

	/**
	 * @return
	 */
	private String getDefaultFetcherClassName() {
		return StringParamFetcher.class.getName();
	}

	/**
	 * @return
	 */
	private String getDefaultConverterClassName() {
		return null;
	}

	/**
	 * @return
	 */
	private String getDefaultValidatorClassName() {
		return DefaultParamValidator.class.getName();
	}
}
