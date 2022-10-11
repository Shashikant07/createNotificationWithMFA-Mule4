package org.mule.extension.cloudhubMFA.internal;

import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.runtime.extension.api.annotation.Configurations;

@Xml(prefix = "cloudhubMFA")
@Extension(name="cloudHubMFA")
@Configurations(CustomConfiguration.class)
public class customExtension{
	
}
