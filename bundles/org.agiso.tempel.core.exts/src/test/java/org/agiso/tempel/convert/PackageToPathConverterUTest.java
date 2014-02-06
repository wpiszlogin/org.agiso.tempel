/* org.agiso.tempel.convert.PackageToPathConverterUTest (12-11-2012)
 * 
 * PackageToPathConverterUTest.java
 * 
 * Copyright 2012 agiso.org
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
package org.agiso.tempel.convert;

import org.agiso.tempel.api.ITemplateParamConverter;
import org.testng.annotations.Test;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public class PackageToPathConverterUTest {
	private ITemplateParamConverter<String, String> converter = new PackageToPathConverter();

//	--------------------------------------------------------------------------
	@Test
	public void testConvertNull() throws Exception {
		assert null == converter.convert(null);
	}

	@Test
	public void testConvertEmpty() throws Exception {
		assert "".equals(converter.convert(""));
	}

	@Test
	public void testConvertBlank() throws Exception {
		assert "  ".equals(converter.convert("  "));
	}

	@Test
	public void testConvert() throws Exception {
		assert "aa/bb/cc/dd".equals(converter.convert("aa.bb.cc.dd"));
	}
}
