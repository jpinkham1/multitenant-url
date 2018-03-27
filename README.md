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
* Make a **TenantInterceptor** that grabs the first path element from the URL and sets it as the tenant.id.  A **Tenant** helper class manages it in a thread local and rejects non-schema values with a 404.
* Use a spring bean **TenantLinkGenerator** to prefix the g:link tags with the current tenant id so that the scaffolding can work.
* Make sure all multi-tenant domain objects have
```groovy
    static mapping = {
        table name: 'tenant.your_table_name'   // my preference would have been to use schema: 'tenant' but this was simpler
    }
```  
* Install a hibernate session_factory.statement_inspector **TenantStatementInspector** which replaces "tenant." prefixes in SQL with the current tenant id.    

That's pretty much the idea.  I was happy to see it seems to work, so I wanted to share.

Enjoy!
