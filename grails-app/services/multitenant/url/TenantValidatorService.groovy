package multitenant.url

import groovy.sql.Sql
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

import javax.sql.DataSource
import java.sql.DatabaseMetaData
import java.sql.ResultSet
import java.util.concurrent.ConcurrentHashMap

@CompileStatic
class TenantValidatorService {
    static transactional = false

    final Set<String> tenants = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>())

    boolean isValid(String tenant) {
        tenant in tenants
    }

    void setSchema(DataSource dataSource, String tenant) {
        new Sql(dataSource).execute(String.format('SET SCHEMA %s',tenant))
    }

    Set BUILT_IN = ['guest', 'information_schema', 'sys', 'public'] as Set

    // BootStrap calls this at startup
    // TODO: support various dialects
    @CompileDynamic
    void buildValidTenants(DataSource dataSource, String getSchemasMethodName='getSchemas') {
        DatabaseMetaData dmd = dataSource.connection?.getMetaData()
        //ResultSet rs = dmd?.getCatalogs()  // for mysql
        ResultSet rs = dmd?."${getSchemasMethodName}"() // for H2 and others...
        tenants.clear()
        while (rs?.next()) {
            tenants << rs.getString(1).toLowerCase()
        }
        tenants.removeAll(BUILT_IN)
    }
}