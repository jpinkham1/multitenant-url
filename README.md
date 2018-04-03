# multitenant-url
URL based multitenancy for SAAS grails3 web apps with one database and schema per tenant using hibernate

Grails3 comes with nice multitenant support, but the solution did not match my needs so I came up with a slightly different approach.

* I am only interested in schema per tenant (shared connection pool)
* I want to use grails scaffolding
* I want to identify the current tenant by a simple URL prefix

The solution I found was as follows:

* Add the tenantId to **UrlMappings** like this 
```groovy
class UrlMappings {
    static mappings = {
        "/$tenantId/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
        "/$tenantId/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
```
* BootStrap calls TenantValidationService.buildTenants
* Make a **TenantInterceptor** that grabs the first path element from the URL and sets it as the tenant.id.  A **Tenant** helper class manages it in a thread local and rejects non-schema values with a 404.
* When the tenant is valid, it sets up the current connection with a "USE SCHEMA"
* Use a spring bean **TenantLinkGenerator** to prefix the g:link tags with the current tenant id so that the scaffolding can work.

That's pretty much the idea.  I was happy to see it seems to work, so I wanted to share.

# How to use it

If/when I publish this to a repo these steps may change.  Meanwhile:

1. download this project
1. grails install
1. reference it in your own project build.gradle
```groovy
    compile "org.grails.plugins:multitenant-url:0.1"
```
The plugin is supposed to install a TenantLinkGenerator via the plugin's doWithSpring closure, but for some reason it isn't.  
To work around that, save this as your conf/spring/resources.groovy
```groovy
import multitenant.url.TenantLinkGenerator

// Place your Spring DSL code here
beans = {
    grailsLinkGenerator(TenantLinkGenerator) {}
}
``` 
(If you skip this step everything still works except scaffolding)

If you are using MySql, add this to your BootStrap
```groovy
    TenantValidatorService tenantValidatorService
    
    def init = { servletContext ->
        tenantValidatorService.SET_SCHEMA = 'USE'
    }
```    


Enjoy!

pinkhamj@gmail.com