### Removed WebModuleServlet and RootWebModuleServlet ###
`WebModuleServlet` and `RootWebModuleServlet`, which gave the capabilities of initializing the module directly from the servlet's _web.xml_, have been removed, in favour of `org.impalaframework.web.servlet.ExternalModuleServlet`, which uses the _moduledefinitions.xml_. This is more flexible and more consistent with other modules.

**Action:** If you are using this servlet in web.xml, will need to convert to using one of the other supported servlets.


---


### ModuleManagementFactory renamed ###
`ModuleManagementFactory` interface has been renamed to `ModuleManagementFacade`. Also `DefaultModuleManagementFactory` (SVN 2398)

This should not affect you unless you are working with the project internals.

### Renamed folder 'spring' ###
The module folder **spring**, which contains the module Spring context file and other resources, has now been renamed to **resources** (SVN 2410)

**Action:** In each project, rename the folder 'spring' to 'resources'.


---


### Default context location for root module ###
This now follows same convention as other modules (_modulename_-context.xml).

**Action:** If root module context doesn't explicitly specify context locations, will need to rename the file accordingly.
Alternatively, explicitly set this location by adding _context-locations_ entry in _module.properties_.

