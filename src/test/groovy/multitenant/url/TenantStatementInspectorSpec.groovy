package multitenant.url

import spock.lang.Specification

class TenantStatementInspectorSpec extends Specification {
    void "inspect replaces all 'tenant' with current Tenant"() {
        given:
        def sql = "Select count(*) from tenant.Foo f join tenant.bar b on f.id = b.id"
        String answer = ''
        try {
            Tenant.set('tenant1')
            def inspector = new TenantStatementInspector()
            answer = inspector.inspect(sql)
        } finally {
            Tenant.remove()
        }
        expect:
        answer == 'Select count(*) from tenant1.Foo f join tenant1.bar b on f.id = b.id'
    }
}