package multitenant.url

import groovy.transform.CompileStatic
import org.hibernate.EmptyInterceptor

@CompileStatic
class TenantStatementInterceptor extends EmptyInterceptor {
    @Override
    public String onPrepareStatement(String sql) {
        sql?.replaceAll(/tenant\./, "${Tenant.get()}.")
    }
}
