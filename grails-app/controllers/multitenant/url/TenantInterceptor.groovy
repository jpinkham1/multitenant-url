package multitenant.url

import groovy.transform.CompileStatic

import java.sql.DatabaseMetaData
import java.sql.ResultSet
import java.util.concurrent.ConcurrentHashMap

class TenantInterceptor {

    def sessionFactory

    TenantInterceptor() {
        matchAll()
                .excludes(uri:'/')
                .excludes(uri:'/assets')
                .excludes(uri:'/error')
                .excludes(controller: 'auth')
    }

    boolean before() {
        String tenant = getFirstPathElement(request?.requestURI)
        if (!isValid(tenant)) {
            response.sendError 404
            return false
        }
        Tenant.set(tenant)
        true
    }

    void afterView() {
        Tenant.remove()
    }

    @CompileStatic
    static String getFirstPathElement(String uri) {
        if (!uri)
            return ''
        int start = uri.indexOf('/')==0 ? 1 : 0
        int end = uri.indexOf('/',start)
        if (end == -1) {
            end = uri.length()
        }
        uri.substring(start, end).replaceAll(/\W/,'')  // to be safe from sql injection
    }

    final Set<String> tenants = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>())

    @CompileStatic
    boolean isValid(String tenant) {
        //synchronized (tenants) {   too paranoid
            if (!tenants) {
                buildValidTenants()
            }
        //}
        tenant in tenants
    }

    // TODO: support various dialects
    void buildValidTenants() {
        DatabaseMetaData dmd = sessionFactory.currentSession.connection().getMetaData()
        ResultSet rs = dmd.getCatalogs()  // for mysql  maybe dmd.getSchemas() for others...
        tenants.clear()
        while (rs.next()) {
            tenants << rs.getString(1)
        }
    }
}
