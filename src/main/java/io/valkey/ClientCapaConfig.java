package io.valkey;

public class ClientCapaConfig {
    private final boolean disabled;
    private final boolean redirect;

    public static final ClientCapaConfig DEFAULT = new ClientCapaConfig();
    public static final ClientCapaConfig DISABLED = new ClientCapaConfig(true, false);

    public ClientCapaConfig() {
        this(false, true);
    }

    public ClientCapaConfig(boolean disabled, boolean redirect) {
        this.disabled = disabled;
        this.redirect = redirect;
    }

    public final boolean isDisabled() {
        return disabled;
    }

    public boolean isRedirect() {
        return redirect;
    }

    public static ClientCapaConfig withRedirect() {
        return new ClientCapaConfig(false, true);
    }
}
