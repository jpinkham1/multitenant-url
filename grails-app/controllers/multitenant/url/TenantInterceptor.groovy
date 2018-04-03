package multitenant.url

import groovy.transform.CompileStatic

import javax.sql.DataSource

class TenantInterceptor {

    def tenantValidatorService

    TenantInterceptor() {
        matchAll()
                .excludes(uri:'/')
                .excludes(uri:'/assets')
                .excludes(uri:'/error')
                .excludes(controller: 'auth')
    }

    boolean before() {
        String tenant = getFirstPathElement(request?.requestURI)
        if (tenantValidatorService.isValid(tenant)) {
            tenantValidatorService.setSchema(tenant)   // use tenant schema
            Tenant.set(tenant)
            return true
        }
        response.sendError 404
        return false
    }

    void afterView() {
        Tenant.remove()
    }

    @CompileStatic
    String getFirstPathElement(String uri) {
        if (!uri)
            return ''
        int start = uri.indexOf('/')==0 ? 1 : 0
        int end = uri.indexOf('/',start)
        if (end == -1) {
            end = uri.length()
        }
        uri.substring(start, end).replaceAll(/\W/,'')  // to be safe from sql injection
    }
}
