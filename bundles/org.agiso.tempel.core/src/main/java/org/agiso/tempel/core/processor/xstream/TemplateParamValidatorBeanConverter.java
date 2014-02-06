/* org.agiso.tempel.core.processor.xstream.converter.TemplateParamValidatorBeanConverter (12-12-2013)
 * 
 * TemplateParamValidatorBeanConverter.java
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

import org.agiso.tempel.core.model.beans.TemplateParamValidatorBean;
import org.agiso.tempel.core.validator.DefaultParamValidator;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * Konwerter XStream obsługujący konwersję znacznika &lt;validator&gt; definicji
 * walidatora parametru szablonu.
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
class TemplateParamValidatorBeanConverter extends AbstractConfigurableConverter {
	public TemplateParamValidatorBeanConverter(Mapper mapper, ReflectionProvider reflectionProvider) {
		super(mapper, reflectionProvider);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(TemplateParamValidatorBean.class);
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		String validatorClassName = reader.getAttribute("class");
		if(validatorClassName == null) {
			validatorClassName = DefaultParamValidator.class.getName();
		} else {
			if(validatorClassName.isEmpty()) {
				throw new ConversionException("Empty string is invalid validator 'class' value");
			}
		}

		TemplateParamValidatorBean templateParamValidator = new TemplateParamValidatorBean();
		templateParamValidator.setValidatorClassName(validatorClassName);
		templateParamValidator.setProperties(readProperties(reader));
		return templateParamValidator;
	}
}
