package at.furti.springrest.client.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.Element;

import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.link.LinkManager;
import at.furti.springrest.client.repository.RepositoryEntry;
import at.furti.springrest.client.util.RepositoryUtils;

/**
 * Creates for each {@link CrudRepository} in the basePackage a FactoryBean that
 * instantiates the Repository.
 * 
 * Therefor a {@link DataRestClient} is needed. At first we try to get the
 * "client" attribute from the XML element. If it is present we try to get the
 * client by beanname from the value. If not get the client by the default
 * beanname ("restClient"). If no client is found a Exception is thrown.
 * 
 * @author Daniel
 * 
 */
public class RestRepositoryBeanDefinitionParser implements BeanDefinitionParser {

	private static final String DEFAULT_CLIENT_NAME = "restClient";
	private static final String LINK_MANAGER_NAME = "at.furti.linkManager";

	private static final String ATTR_BASEPACKAGE = "basePackage";
	private static final String ATTR_CLIENT = "client-ref";

	private ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();

	public BeanDefinition parse(Element element, ParserContext parserContext) {
		RuntimeBeanReference client = extractClient(element, parserContext);
		String basePackage = element.getAttribute(ATTR_BASEPACKAGE);

		Assert.hasText(basePackage,
				"Attribute [basePackage] is required to setup restrepositories");

		RuntimeBeanReference linkManager = createLinkManager(parserContext, client);

		try {
			List<RepositoryEntry> entries = getEntries(basePackage);

			if (CollectionUtils.isEmpty(entries)) {
				return null;
			}

			for (RepositoryEntry entry : entries) {
				createBeanDefinition(entry, parserContext, client, linkManager);
			}
		} catch (IOException ex) {
			// TODO: logging
			ex.printStackTrace(System.err);
		}

		return null;
	}

	/**
	 * @param parserContext
	 * @return
	 */
	private RuntimeBeanReference createLinkManager(ParserContext parserContext,
			RuntimeBeanReference client) {
		if (!parserContext.getRegistry().containsBeanDefinition(LINK_MANAGER_NAME)) {
			RootBeanDefinition linkManager = new RootBeanDefinition(LinkManager.class);

			linkManager.getConstructorArgumentValues()
					.addGenericArgumentValue(client);

			parserContext.getRegistry().registerBeanDefinition(LINK_MANAGER_NAME,
					linkManager);
		}

		return new RuntimeBeanReference(LINK_MANAGER_NAME);
	}

	/**
	 * @param entry
	 * @param parserContext
	 * @param client
	 */
	private void createBeanDefinition(RepositoryEntry entry,
			ParserContext parserContext, RuntimeBeanReference client,
			RuntimeBeanReference linkManager) {
		RootBeanDefinition repositoryDefinition = new RootBeanDefinition(
				RestRepositoryCreator.class);

		ConstructorArgumentValues arguments = repositoryDefinition
				.getConstructorArgumentValues();
		arguments.addGenericArgumentValue(client);
		arguments.addGenericArgumentValue(entry);
		arguments.addGenericArgumentValue(linkManager);

		parserContext.getRegistry().registerBeanDefinition(entry.getRepoId(),
				repositoryDefinition);
	}

	/**
	 * Creates a {@link RuntimeBeanReference} for the {@link DataRestClient}
	 * 
	 * Checks if the client-ref Attribute is set in the element. If set it is used
	 * as a beanname. The DEFAULT_CLIENT_NAME is used otherwise.
	 * 
	 * @param element
	 * @param parserContext
	 */
	private RuntimeBeanReference extractClient(Element element,
			ParserContext parserContext) {
		String clientName = element.getAttribute(ATTR_CLIENT);

		if (StringUtils.isEmpty(clientName)) {
			clientName = DEFAULT_CLIENT_NAME;
		}

		if (!parserContext.getRegistry().containsBeanDefinition(clientName)) {
			throw new NoSuchBeanDefinitionException(clientName,
					"Could not find client");
		}

		return new RuntimeBeanReference(clientName);
	}

	/**
	 * Scans the basePackage for classes to process and returns them.
	 * 
	 * @return
	 * @throws IOException
	 */
	private List<RepositoryEntry> getEntries(String basePackage)
			throws IOException {
		Assert.notNull(basePackage);

		List<RepositoryEntry> entries = new ArrayList<RepositoryEntry>();

		Resource[] resources = this.patternResolver
				.getResources(getPattern(basePackage));

		MetadataReaderFactory metadataFactory = new CachingMetadataReaderFactory(
				patternResolver);

		for (Resource resource : resources) {
			if (resource.isReadable()) {
				MetadataReader reader = metadataFactory.getMetadataReader(resource);
				try {
					Class<?> clazz = Class.forName(reader.getClassMetadata()
							.getClassName());

					// Add the entry if it should be processed
					if (shouldProcess(clazz)) {
						entries.add(new RepositoryEntry(clazz, 
								RepositoryUtils.getRepositoryId(clazz), 
								RepositoryUtils.getRepositoryRel(clazz),
								RepositoryUtils.extractEntryType(clazz)));
					}
				} catch (ClassNotFoundException cnf) {
					// TODO: Logging
				}
			}
		}

		return entries;
	}

	/**
	 * @return
	 */
	private String getPattern(String basePackage) {
		StringBuilder builder = new StringBuilder();

		builder.append(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX);
		builder.append(ClassUtils.convertClassNameToResourcePath(basePackage));
		builder.append("/**/*.class");

		return builder.toString();
	}

	/**
	 * Checks if the clazz is a repository clazz to enhance.
	 * 
	 * @param clazz
	 * @return
	 */
	private boolean shouldProcess(Class<?> clazz) {
		if (clazz == null) {
			return false;
		}

		return CrudRepository.class.isAssignableFrom(clazz);
	}
}
