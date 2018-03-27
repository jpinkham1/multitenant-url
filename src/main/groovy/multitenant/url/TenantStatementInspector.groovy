package multitenant.url

import groovy.transform.CompileStatic
import org.hibernate.resource.jdbc.spi.StatementInspector

@CompileStatic
class TenantStatementInspector implements StatementInspector {
    @Override
    String inspect(String sql) {
        sql?.replaceAll(/tenant\./, "${Tenant.get()}.")
    }
}
