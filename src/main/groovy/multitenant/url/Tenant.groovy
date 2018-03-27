package multitenant.url

import groovy.transform.CompileStatic

// See grails.gorm.multitenancy.Tenants.CurrentTenant
@CompileStatic
class Tenant {
    private static ThreadLocal<Serializable> _current = new ThreadLocal<Serializable>()

    static Serializable get() {
        _current.get() ?: ''
    }

    static void remove() {
        _current.remove()
    }

    static void set(Serializable tenant) {
        tenant ? _current.set(tenant) : _current.remove()
    }
}
