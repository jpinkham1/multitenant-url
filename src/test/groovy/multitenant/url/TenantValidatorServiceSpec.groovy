package multitenant.url

import spock.lang.Specification

class TenantValidatorServiceSpec extends Specification {
    def service

    void setup() {
        service = new TenantValidatorService()
        service.tenants << 'ok'
    }

    void "isValid checks against set of tenants"() {
        expect:
        service.isValid('ok')
        !service.isValid('foo')
        !service.isValid(null)
    }
}