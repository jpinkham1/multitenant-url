package multitenant.url

import spock.lang.Specification

class TenantStatementInterceptorSpec extends Specification {
    void "inspect replaces all 'tenant' with current Tenant"() {
        given:
        def sql = "Update tenant.foo set bar=? where id = ?"
        String answer = ''
        try {
            Tenant.set('tenant1')
            def inspector = new TenantStatementInterceptor()
            answer = inspector.onPrepareStatement(sql)
        } finally {
            Tenant.remove()
        }
        expect:
        answer == 'Update tenant1.foo set bar=? where id = ?'
    }
}