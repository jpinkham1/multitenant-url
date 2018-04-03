package multitenant.url

import groovy.sql.Sql
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

import javax.sql.DataSource
import java.sql.DatabaseMetaData
import java.sql.ResultSet

@CompileStatic
class TenantValidatorService {
    @SuppressWarnings("GroovyUnusedDeclaration")
    static transactional = false
    DataSource dataSource
    Sql sql = null
    String SET_SCHEMA = 'SET SCHEMA ?'

    final Set<String> tenants = [] as Set

    boolean isValid(String tenant) {
        tenant in tenants
    }

    // also return the sql so we can keep executing for sure on the same schema
    Sql setSchema(String tenant) {
        if (!sql) {
            sql = new Sql(dataSource)
            sql.setCacheStatements(true)
        }
        sql.execute(SET_SCHEMA, tenant)
        sql
    }

    Set BUILT_IN = ['guest', 'information_schema', 'sys', 'public'] as Set

    // BootStrap should call this at startup
    @CompileDynamic
    void buildValidTenants() {
        DatabaseMetaData dmd = dataSource.connection?.getMetaData()

        tenants.clear()
        ['getSchemas', 'getCatalogs'].each { cmd->
            ResultSet rs = dmd?."${cmd}"()
            while (rs?.next()) {
                tenants << rs.getString(1).toLowerCase()
            }
        }
        tenants.removeAll(BUILT_IN)
    }
}