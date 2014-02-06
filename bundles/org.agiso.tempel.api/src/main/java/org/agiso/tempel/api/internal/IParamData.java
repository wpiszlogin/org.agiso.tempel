/* org.agiso.tempel.api.internal.IParamData (22-12-2013)
 * 
 * IParamData.java
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
package org.agiso.tempel.api.internal;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public interface IParamData {
	/**
	 * @return Identyfikator (klucz) parametru.
	 */
	public String getKey();

	/**
	 * @return Nazwa parametru.
	 */
	public String getName();

	/**
	 * @return Pełna nazwa klasy typu parametru. Domyślnie {@link String}.
	 */
	public String getType();

	/**
	 * @return Wartość domyślna parametru.
	 */
	public String getValue();
	public void setValue(String value);
}
