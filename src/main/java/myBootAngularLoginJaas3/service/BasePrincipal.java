package myBootAngularLoginJaas3.service;

import java.io.Serializable;
import java.security.Principal;

public abstract class BasePrincipal implements Principal, Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    public BasePrincipal() {
    }

    public BasePrincipal(String name) {
        this.name = name;
    }

    /* (non-Javadoc)
     * @see java.security.Principal#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && this.getClass().isInstance(obj)) {
            BasePrincipal that = (BasePrincipal) obj;
            return this.name.equals(that.name);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() ^ this.name.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + this.name;
    }
}
