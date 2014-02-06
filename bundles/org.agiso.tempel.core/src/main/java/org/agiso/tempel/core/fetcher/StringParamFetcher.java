/* org.agiso.tempel.core.fetcher.StringParamFetcher (22-12-2013)
 * 
 * StringParamFetcher.java
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
package org.agiso.tempel.core.fetcher;

import org.agiso.tempel.api.ITemplateParamFetcher;
import org.agiso.tempel.api.internal.IParamData;
import org.agiso.tempel.api.internal.IParamReader;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public class StringParamFetcher implements ITemplateParamFetcher<String> {
	@Override
	public String fetch(IParamReader reader, IParamData param) {
		return reader.getParamValue(param.getKey(), param.getName(), param.getValue());
	}
}
