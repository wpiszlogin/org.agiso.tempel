/* org.agiso.tempel.templates.tempel.add.TempelAddITest (13-02-2013)
 * 
 * TempelAddITest.java
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
package org.agiso.tempel.templates.tempel.add;

import java.io.File;
import java.util.HashMap;

import org.agiso.tempel.ITempel;
import org.agiso.tempel.Temp;
import org.agiso.tempel.test.AbstractTemplateTest;
import org.testng.annotations.Test;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public class TempelAddITest extends AbstractTemplateTest {
	private static final String GROUP_ID    = "org.agiso.tempel.templates";
	private static final String TEMPLATE_ID = "tempel.add";
	private static final String VERSION     = "1.0.0";

//	--------------------------------------------------------------------------
	public TempelAddITest() {
		super(GROUP_ID, TEMPLATE_ID, VERSION);
	}

//	--------------------------------------------------------------------------
	@Test
	public void testTempelAdd() throws Exception {
		String outPath = getOutputPath(true);

		ITempel tempel = getTempelInstance();
		tempel.startTemplate(
				GROUP_ID + ":" + TEMPLATE_ID + ":" + VERSION,
				new HashMap<String, String>(), new File(outPath).getCanonicalPath()
		);

		String md5 = Temp.DigestUtils_countDigest("MD5", new File(outPath));
		assert "4028a1a1e284375c00a4847558a94132".equals(md5) : md5;
	}
}
