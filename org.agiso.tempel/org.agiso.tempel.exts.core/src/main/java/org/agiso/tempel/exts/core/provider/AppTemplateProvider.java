/* org.agiso.tempel.exts.core.provider.AppTemplateProvider (15-12-2012)
 * 
 * AppTemplateProvider.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.exts.core.provider;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.ITemplateRepository;
import org.agiso.tempel.api.ITemplateSource;
import org.agiso.tempel.api.ITemplateSourceFactory;
import org.agiso.tempel.api.impl.FileTemplateSource;
import org.agiso.tempel.api.internal.ITempelEntryProcessor;
import org.agiso.tempel.api.internal.ITemplateProviderElement;
import org.agiso.tempel.api.model.Template;
import org.agiso.tempel.exts.base.provider.BaseTemplateProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@Component
public class AppTemplateProvider extends BaseTemplateProvider implements ITemplateProviderElement {
	private String settingsPath;
	private String repositoryPath;

	@Autowired
	private ITemplateRepository templateRepository;

//	--------------------------------------------------------------------------
	@Override
	public int getOrder() {
		return 30;
	}

//	--------------------------------------------------------------------------
	@Override
	public void initialize(Map<String, Object> globalProperties) throws IOException {
		super.initialize(globalProperties);


		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		int index = path.lastIndexOf("/repo/");
		// Inicjalizacja repozytoriów z zasobami dla poszczególnych poziomów:
		if(index != -1) {
			// Rzeczywiste środowisko uruchomieniowe (uruchomienie z linni komend):
			settingsPath = path.substring(0, index) + "/conf/tempel.xml";
			repositoryPath = path.substring(0, index) + "/repository";
		} else {
			// Deweloperskie środowisko uruchomieniowe (uruchomienie z eclipse'a):
			path = System.getProperty("user.dir");

			settingsPath = path + "/src/test/configuration/application/tempel.xml";
			repositoryPath = path + "/src/test/repository/application";
		}


		readAppTemplates(templateRepository);
	}

//	--------------------------------------------------------------------------
	@Override
	public boolean contains(String key, String groupId, String templateId, String version) {
		return templateRepository.contains(key, groupId, templateId, version);
	}

	@Override
	public Template get(String key, String groupId, String templateId, String version) {
		return templateRepository.get(key, groupId, templateId, version);
	}

//	--------------------------------------------------------------------------
	/**
	 * @param xStream
	 * @param templateRepository
	 * @throws IOException
	 */
	private void readAppTemplates(final ITemplateRepository templateRepository) throws IOException {
		// Mapa szablonów globalnych (katalog konfiguracyjny aplikacji):
		File appSettingsFile = new File(settingsPath);

		try {
			tempelFileProcessor.process(appSettingsFile, new ITempelEntryProcessor() {
				@Override
				public void processObject(Object object) {
					AppTemplateProvider.this.processObject("GLOBAL", object, templateRepository,
							new ITemplateSourceFactory() {
								@Override
								public ITemplateSource createTemplateSource(Template template, String source) {
									try {
										return new FileTemplateSource(getTemplatePath(template), source);
									} catch(IOException e) {
										throw new RuntimeException(e);
									}
								}
							}
					);
				}
			});
			System.out.println("Wczytano ustawienia globalne z pliku " + appSettingsFile.getCanonicalPath());
		} catch(Exception e) {
			System.err.println("Błąd wczytywania ustawień globalnych: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	private String getTemplatePath(Template template) {
		if(Temp.StringUtils_isEmpty(template.getGroupId())) {
			throw new RuntimeException("Szablon GLOBAL bez groupId");
		}

		String path = repositoryPath;
		path = path + '/' + template.getGroupId().replace('.', '/');
		path = path + '/' + template.getTemplateId();
		path = path + '/' + template.getVersion();
		return path;
	}
}