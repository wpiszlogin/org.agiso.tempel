/* org.agiso.tempel.support.base.provider.CachingTemplateProviderElement (21-01-2013)
 * 
 * CachingTemplateProviderElement.java
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
package org.agiso.tempel.support.base.provider;

import java.util.HashMap;
import java.util.Map;

import org.agiso.tempel.api.ITemplateRepository;
import org.agiso.tempel.api.ITemplateSourceFactory;
import org.agiso.tempel.api.internal.ITempelEntryProcessor;
import org.agiso.tempel.api.model.Template;
import org.agiso.tempel.support.base.repository.HashBasedTemplateRepository;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public abstract class CachingTemplateProviderElement extends BaseTemplateProviderElement implements ITemplateSourceFactory {
	private final Map<String, CacheEntry> cache = new HashMap<String, CacheEntry>();

//	--------------------------------------------------------------------------
	@Override
	public boolean contains(String key, String groupId, String templateId, String version) {
		if(!cache.containsKey(key)) {
			cache.put(key, doGet(key, groupId, templateId, version));
		}
		return cache.get(key) != null;
	}

	@Override
	public Template get(String key, String groupId, String templateId, String version) {
		if(!cache.containsKey(key)) {
			cache.put(key, doGet(key, groupId, templateId, version));
		}

		final CacheEntry cacheEntry = getCacheEntry(key);
		if(cacheEntry == null) {
			return null;
		}

		if(cacheEntry.repository == null) {
			final ITemplateRepository templateRepository = new HashBasedTemplateRepository();

			try {
				tempelFileProcessor.process(cacheEntry.definition, new ITempelEntryProcessor() {
					@Override
					public void processObject(Object object) {
						CachingTemplateProviderElement.this.processObject("MAVEN",
								object, templateRepository, CachingTemplateProviderElement.this
						);
					}
				});
				System.out.println("Wczytano ustawienia z biblioteki szablonu " + key);
			} catch(Exception e) {
				System.err.println("Błąd wczytywania ustawień z biblioteki szablonu '" + key + "': " + e.getMessage());
				throw new RuntimeException(e);
			}

			cacheEntry.repository = templateRepository;
		}

		return cacheEntry.repository.get(key, groupId, templateId, version);
	}

	@SuppressWarnings("unchecked")
	protected final <T extends CacheEntry> T getCacheEntry(String key) {
		return (T)cache.get(key);
	}

//	--------------------------------------------------------------------------
	protected abstract <T extends CacheEntry> T doGet(String key, String groupId, String templateId, String version);

//	--------------------------------------------------------------------------
	public static class CacheEntry {
		public String definition;
		public ITemplateRepository repository;
	}
}