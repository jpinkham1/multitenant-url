package multitenant.url

import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class TenantInterceptorSpec extends Specification implements InterceptorUnitTest<TenantInterceptor> {

    void "Test tenant interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"tenant")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }

    void "tenant from URI should pull the first path element"() {
        expect:
        interceptor.getFirstPathElement('/prefix/controller/action') == 'prefix'
        interceptor.getFirstPathElement('/prefix') == 'prefix'
        interceptor.getFirstPathElement('prefix/controller/action') == 'prefix'
        interceptor.getFirstPathElement('//prefix/controller/action') == ''
        interceptor.getFirstPathElement(null) == ''
    }
}
