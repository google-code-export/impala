Impala 1.0M5 is probably the last release which has substantial backwardly incompatible changes. The following steps are based on an upgrade from an earlier version of Impala. While some of these steps may not be necessary, and altogether they may not be sufficient, they should be helpful in bringing your Impala version up to date.

[More details on backward incompatible changes to Impala 1.0M5](Compatibility_1_0M5.md).

1)
Fix compile errors. This you can do pretty easily in Eclipse using `CTRL + Shift + O` to 'Organise Imports'.

2)
Search and replace in all Spring context locations:
`org.impalaframework.spring.module.ModuleContributionPostProcessor`
with
`org.impalaframework.spring.service.exporter.ModuleContributionPostProcessor`

3)
Search and replace in all Spring context locations:
`org.impalaframework.spring.module.ModuleArrayContributionExporter`
with
`org.impalaframework.spring.service.exporter.ModuleArrayContributionExporter`

4)
Search and replace in all Spring context locations:
`org.impalaframework.spring.module.ContributionProxyFactoryBean`
with
`org.impalaframework.spring.service.proxy.ContributionProxyFactoryBean`

5)
Search and replace in _WEB-INF/web.xml_:
`org.impalaframework.web.loader.ImpalaContextLoaderListener`
with
`org.impalaframework.web.spring.loader.ImpalaContextLoaderListener`

6)
Search and replace in all Spring context locations:
`org.impalaframework.web.loader.ExternalModuleContextLoader`
with
`org.impalaframework.web.spring.loader.ExternalModuleContextLoader`

7)
Search and replace in _WEB-INF/web.xml_:
`org.impalaframework.web.generic.ModuleRedirectingServlet`
with
`with org.impalaframework.web.spring.integration.ModuleProxyServlet`

8)
Search and replace in all Spring context locations:
`org.impalaframework.web.generic.ServletFactoryBean`
with
`org.impalaframework.web.spring.integration.ServletFactoryBean`

9)
Search and replace in all Spring context locations:
`org.impalaframework.web.servlet.InternalModuleServlet`
with
`org.impalaframework.web.spring.servlet.InternalModuleServlet`

10)
Search and replace in all module.properties:
Replace `context-locations` with `config-locations`.

11)
Change _impala.properties_ (and _impala-embedded.properties_) to use where possible to property based configuration (see WebApplicationBootstrapping). If you would like to stick with the old configuration method, replace `bootstrapLocations=` with `all.locations=` in `module.properties`.

12)
Fix any remaining errors, which can be picked up by running the application's test suite and deploying the web application.