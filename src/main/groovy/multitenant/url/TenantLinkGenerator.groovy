package multitenant.url

import grails.util.Holders
import org.grails.web.mapping.CachingLinkGenerator

class TenantLinkGenerator extends CachingLinkGenerator {

    TenantLinkGenerator() {
        super(Holders.config?.grails?.serverURL as String)
    }

    @Override
    String link(Map attrs, String encoding) {
        String ret = super.link(attrs, encoding)
        String tenant = Tenant.get()
        tenant ? ret.replaceFirst('/',"/$tenant/") : ret
    }
}
