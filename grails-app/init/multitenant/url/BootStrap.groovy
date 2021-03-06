package multitenant.url

import groovy.sql.Sql

import javax.sql.DataSource

class BootStrap {

    TenantValidatorService tenantValidatorService

    def init = { servletContext ->

        // create schemas first - for development I've got them at the end of the H2 url ;INIT=CREATE SCHEMA IF NOT EXISTS prefix1\;
        tenantValidatorService.buildValidTenants()

//        tenantValidatorService.tenants.each { String tenant->
//            def sql = tenantValidatorService.setSchema(tenant)
            // from turning on logSql and setting dbCreate: create
//            sql.execute('create table Test (id bigint generated by default as identity, version bigint not null, name varchar(20) not null, primary key (id))')
//            sql.execute('alter table Test add constraint UK_ex465bpneon54qrrtfakuwi79 unique (name)')
//        }
    }
    def destroy = {
    }
}
